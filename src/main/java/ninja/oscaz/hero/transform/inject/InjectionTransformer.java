package ninja.oscaz.hero.transform.inject;

import java.util.IdentityHashMap;
import java.util.Map;
import ninja.oscaz.hero.Hero;
import ninja.oscaz.hero.transform.HeroTransformer;

public class InjectionTransformer implements HeroTransformer {

    private final Hero hero;
    private final Map<Class<?>, FieldProfile> profiles = new IdentityHashMap<>();

    public InjectionTransformer(Hero hero) {
        this.hero = hero;
    }

    @Override
    public <T> T transform(T value) {
        this.profiles.computeIfAbsent(value.getClass(), this::generateFieldProfile)
            .inject(this.hero, value);

        return value;
    }

    private FieldProfile generateFieldProfile(Object value) {
        return new FieldProfile(value.getClass());
    }

}
