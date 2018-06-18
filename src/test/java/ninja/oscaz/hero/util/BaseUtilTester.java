package ninja.oscaz.hero.util;

import com.google.common.truth.Truth;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import ninja.oscaz.hero.util.except.Try;
import ninja.oscaz.hero.util.except.UtilityInstantiationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public abstract class BaseUtilTester {

    private final Class<?> util;

    protected BaseUtilTester(Class<?> util) {
        this.util = util;
    }

    @Test
    void testConstructor() {
        Constructor<?> constructor = Try.toGet(this.util::getDeclaredConstructor);
        constructor.setAccessible(true);
        Assertions.assertThrows(InvocationTargetException.class, constructor::newInstance);
        try {
            constructor.newInstance();
            Assertions.fail("Should not create instance of util");
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            Truth.assertThat(e.getCause()).isInstanceOf(UtilityInstantiationException.class);
        }
    }

}
