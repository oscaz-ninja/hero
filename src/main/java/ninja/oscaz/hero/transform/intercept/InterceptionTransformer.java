package ninja.oscaz.hero.transform.intercept;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import ninja.oscaz.hero.Hero;
import ninja.oscaz.hero.transform.HeroTransformer;
import ninja.oscaz.hero.transform.inject.Inject;
import ninja.oscaz.hero.util.annotate.Annotations;
import ninja.oscaz.hero.util.reflect.Handles;

public class InterceptionTransformer implements HeroTransformer {

    private final Map<Class<?>, Class<?>> transformedTypes = new IdentityHashMap<>();

    @Inject private Hero hero;
    @Inject private ArgumentMappings mappings;

    @Override
    public <T> Class<? extends T> transformType(Class<T> type) {
        Class<?> transformed = this.transformedTypes.computeIfAbsent(type, this::computeTypeTransformation);

        @SuppressWarnings("unchecked")
        Class<? extends T> casted = (Class<? extends T>) transformed;

        return casted;
    }

    private <T> Class<? extends T> computeTypeTransformation(Class<T> type) {
        DynamicType.Builder<T> builder = new ByteBuddy(ClassFileVersion.JAVA_V8).subclass(type);

        boolean intercepted = false;

        for (Method method : type.getDeclaredMethods()) {
            Handles.accessibilify(method);

            if (!Annotations.findSingleSecondary(method, Intercept.class).isPresent()) {
                continue;
            }

            intercepted = true;

            List<Interceptor> interceptors = Annotations.findAllSecondary(method, Intercept.class)
                .stream()
                .map(Annotation::annotationType)
                .map(this.hero::requestDynamic)
                .map(object -> (Interceptor) object)
                .collect(Collectors.toList());

            InterceptionPipeline pipeline = new InterceptionPipeline(method, this.mappings, interceptors);

            builder = builder.method(ElementMatchers.is(method))
                .intercept(MethodDelegation.to(pipeline))
                .annotateMethod(method.getAnnotations());
        }

        if (!intercepted) {
            return type;
        }

        return builder.make().load(this.getClass().getClassLoader()).getLoaded();
    }

}
