package ninja.oscaz.hero;

import com.google.common.truth.Truth;
import java.util.function.Supplier;
import ninja.oscaz.hero.except.HeroRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class HeroTest extends HeroTestHelper {

    @Test
    public void testRequest() {
        Truth.assertThat(this.hero.request(BasicType.class)).isInstanceOf(BasicType.class);
    }

    @Test
    public void testNullRequest() {
        Assertions.assertThrows(HeroRequestException.class, () -> this.hero.request(null));
    }

    @Test
    public void testMismatchedRequest() {
        this.hero.bind(BasicType.class).to(BasicType2.class);
        Assertions.assertThrows(HeroRequestException.class, () -> this.hero.request(BasicType.class));
    }

    @Test
    public void testBindings() {
        this.hero.bind(BasicType.class).to(BasicType2.class);
        this.hero.bind(BasicType.class).toNone();
        Truth.assertThat(this.hero.request(BasicType.class)).isInstanceOf(BasicType.class);

        this.hero.bind(BasicType.class).to(BasicType2.class);
        Truth.assertThat(this.hero.requestDynamic(BasicType.class)).isInstanceOf(BasicType2.class);

        this.hero.bind(BasicType.class).toValue("Hello!");
        Truth.assertThat(this.hero.requestDynamic(BasicType.class)).isSameAs("Hello!");

        this.hero.bind(BasicType.class).toSingleton();
        Truth.assertThat(this.hero.request(BasicType.class)).isSameAs(this.hero.request(BasicType.class));

        this.hero.bind(BasicType.class).toSupplier(new StateChangingSupplier());
        Truth.assertThat(this.hero.requestDynamic(BasicType.class)).isSameAs(1);
        Truth.assertThat(this.hero.requestDynamic(BasicType.class)).isSameAs(2);
    }

    public static class BasicType {

    }

    public static class BasicType2 {

    }

    public static class StateChangingSupplier implements Supplier<Object> {

        private int count = 0;

        @Override
        public Object get() {
            this.count++;

            return this.count;
        }

    }

}
