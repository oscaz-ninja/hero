package ninja.oscaz.hero.transform.intercept;

import com.google.common.collect.Lists;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import ninja.oscaz.hero.bind.binder.Singleton;

@Singleton
public class ArgumentMappings {

    private final Map<Class<?>, Mappings<?>> typeMappings = new IdentityHashMap<>();

    public <T, R> void map(Class<R> type, Class<T> mapper, Function<T, R> function) {
        @SuppressWarnings("unchecked")
        Mappings<R> mappings = (Mappings<R>) this.typeMappings.computeIfAbsent(type, Mappings::new);

        Mapping<T, R> mapping = new Mapping<>(mapper, function);

        mappings.addMapping(mapping);
    }

    public <R> R attemptToFind(Class<R> type, Object[] arguments) {
        return this.attemptToFind(type, arguments, 0);
    }

    public <R> R attemptToFind(Class<R> type, Object[] arguments, int skip) {
        Skipper skipper = new Skipper(skip);

        for (Object object : arguments) {
            if (type.isInstance(object)) {
                @SuppressWarnings("unchecked")
                R value = (R) object;

                if (skipper.shouldProceed()) {
                    return value;
                }
            }
        }

        @SuppressWarnings("unchecked")
        Mappings<R> mappings = (Mappings<R>) this.typeMappings.get(type);

        if (mappings != null) {
            R value = this.attemptToFind(mappings, arguments, skipper);

            if (value != null) {
                if (skipper.shouldProceed()) {
                    return value;
                }
            }
        }

        for (Mappings<?> typeMapping : this.typeMappings.values()) {
            if (type.isAssignableFrom(typeMapping.getMappingFor())) {
                @SuppressWarnings("unchecked")
                Mappings<R> newMapping = (Mappings<R>) typeMapping;

                R value = this.attemptToFind(newMapping, arguments, skipper);

                if (value != null) {
                    if (skipper.shouldProceed()) {
                        return value;
                    }
                }
            }
        }

        return null;
    }

    private <R> R attemptToFind(Mappings<R> mappings, Object[] arguments, Skipper skipper) {
        List<Mapping<?, R>> allMappings = mappings.getAllMappings();

        for (Object object : arguments) {
            for (Mapping<?, R> mapping : allMappings) {
                if (mapping.getParameter().isInstance(object)) {
                    @SuppressWarnings("unchecked")
                    Mapping<Object, R> casted = (Mapping<Object, R>) mapping;

                    if (skipper.shouldProceed()) {
                        return casted.apply(object);
                    }
                }
            }
        }

        return null;
    }

    private static final class Skipper {

        private int skip;

        private Skipper(int skip) {
            this.skip = skip;
        }

        public boolean shouldProceed() {
            if (this.skip == 0) {
                return true;
            }
            this.skip--;

            return false;
        }

    }

    private static final class Mappings<R> {

        private final Class<R> mappingFor;
        private final List<Mapping<?, R>> mappings;

        private Mappings(Class<R> mappingFor) {
            this.mappingFor = mappingFor;
            this.mappings = Lists.newArrayList();
        }

        public void addMapping(Mapping<?, R> mapping) {
            this.mappings.add(mapping);
        }

        public Class<R> getMappingFor() {
            return this.mappingFor;
        }

        public List<Mapping<?, R>> getAllMappings() {
            return this.mappings;
        }
    }

    private static final class Mapping<T, R> implements Function<T, R> {

        private Class<?> parameter;
        private Function<T, R> function;

        private Mapping(Class<?> parameter, Function<T, R> function) {
            this.parameter = parameter;
            this.function = function;
        }

        @Override
        public R apply(T t) {
            return this.function.apply(t);
        }

        public Class<?> getParameter() {
            return this.parameter;
        }

    }

}
