package ninja.oscaz.hero.util.except;

import com.google.common.truth.Truth;
import ninja.oscaz.hero.util.BaseUtilTester;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class ExceptionsTest extends BaseUtilTester {

    ExceptionsTest() {
        super(Exceptions.class);
    }

    @Test
    public void testPropagation() {
        IllegalStateException exception = new IllegalStateException();
        Truth.assertThat(Exceptions.uncheck(exception)).isSameAs(exception);

        Exception checked = new Exception();
        Truth.assertThat(Exceptions.uncheck(checked)).isInstanceOf(RuntimeException.class);
    }

}
