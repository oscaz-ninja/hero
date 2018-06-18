package ninja.oscaz.hero.transform.inject;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ninja.oscaz.hero.Hero;
import ninja.oscaz.hero.util.except.Try;
import ninja.oscaz.hero.util.reflect.Handles;

public class FieldProfile {

    private final List<HandledField> handles;

    public FieldProfile(Class<?> type) {
        this.handles =
            Stream.of(type.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .map(this::handle)
                .collect(Collectors.toList());
    }

    public void inject(Hero hero, Object instance) {
        this.handles.forEach(handle -> {
            Object param = hero.requestDynamic(handle.getFieldType());
            Try.to(() -> handle.getHandle().invokeExact(instance, param));
        });
    }

    private HandledField handle(Field field) {
        return new HandledField(field, field.getType());
    }

    private static final class HandledField {

        private final MethodHandle handle;
        private final Class<?> fieldType;

        private HandledField(Field field, Class<?> fieldType) {
            this.handle = Handles.createSetter(field);
            this.fieldType = fieldType;
        }

        public MethodHandle getHandle() {
            return this.handle;
        }

        public Class<?> getFieldType() {
            return this.fieldType;
        }

    }

}
