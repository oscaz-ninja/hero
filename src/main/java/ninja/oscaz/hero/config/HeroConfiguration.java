package ninja.oscaz.hero.config;

import ninja.oscaz.hero.Hero;

public interface HeroConfiguration {

    static HeroConfigurationBuilder make() {
        return new HeroConfigurationBuilder();
    }

    void defaultBindings(Hero hero);
    void defaultTransformers(Hero hero);

}
