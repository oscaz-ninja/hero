package ninja.oscaz.hero.transform.initialize;

import java.util.stream.Stream;
import ninja.oscaz.hero.transform.HeroTransformer;
import ninja.oscaz.hero.util.except.Try;

public class InitializeTransformer implements HeroTransformer {

    @Override
    public <T> T transform(T value) {
        Stream.of(value.getClass().getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(Initialize.class))
            .forEach(method -> Try.to(() -> method.invoke(value)));

        return value;
    }

}
