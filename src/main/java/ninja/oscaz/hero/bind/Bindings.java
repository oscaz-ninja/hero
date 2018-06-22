package ninja.oscaz.hero.bind;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import ninja.oscaz.hero.Hero;
import ninja.oscaz.hero.bind.binder.Binder;
import ninja.oscaz.hero.bind.binder.BindingAnnotation;
import ninja.oscaz.hero.bind.binder.DefaultBinder;
import ninja.oscaz.hero.util.annotate.Annotations;

public class Bindings {

    private final Hero hero;
    private final Map<Class<?>, Binding> bindings = new HashMap<>();

    public Bindings(Hero hero) {
        this.hero = hero;
    }

    public void addBinding(Binding binding) {
        this.bindings.put(binding.from(), binding);
    }

    public void removeBinding(Class<?> from) {
        this.bindings.remove(from);
    }

    public Binding getBinding(Class<?> from) {
        return this.bindings.computeIfAbsent(from, this::generateBinding);
    }

    private Binding generateBinding(Class<?> type) {
        Optional<Annotation> binderAnnotation = Annotations.findSingleSecondary(type, BindingAnnotation.class);

        if (binderAnnotation.isPresent()) {
            @SuppressWarnings("unchecked")
            Function<Class<?>, Binder> binder = (Function<Class<?>, Binder>) this.hero.requestDynamic(binderAnnotation.get().annotationType());

            return Binding.toSelf(type).with(binder.apply(type));
        }

        return Binding.toSelf(type).with(new DefaultBinder(this.hero, type));
    }

}
