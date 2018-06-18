package ninja.oscaz.hero.util.reflect;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import ninja.oscaz.hero.util.except.Try;

public class Handles {

    public static MethodHandle createSetter(Field field) {
        return Try.toGet(() ->
            Handles.eraseTypes(MethodHandles.lookup().unreflectSetter(Handles.accessibilify(field)))
        );
    }

    public static <T extends AccessibleObject> T accessibilify(T object) {
        Try.to(() -> object.setAccessible(true));

        return object;
    }

    public static MethodHandle eraseTypes(MethodHandle handle) {
        MethodType type = handle.type();
        MethodType generic = type.generic();

        if (type.returnType() == void.class) {
            generic = generic.changeReturnType(void.class);
        }

        return handle.asType(generic);
    }

}
