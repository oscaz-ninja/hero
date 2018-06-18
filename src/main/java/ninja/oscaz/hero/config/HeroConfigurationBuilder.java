package ninja.oscaz.hero.config;

import java.util.function.Consumer;
import ninja.oscaz.hero.Hero;

public class HeroConfigurationBuilder {

    HeroConfigurationBuilder() {

    }

    public ConfigBinder withDefaults() {
        return new ConfigBinder(true);
    }

    public ConfigBinder withoutDefaults() {
        return new ConfigBinder(false);
    }

    public static final class ConfigBinder {

        private final boolean defaults;

        private ConfigBinder(boolean defaults) {
            this.defaults = defaults;
        }

        public ConfigTransformer withoutBindings() {
            return this.withBindings(hero -> {});
        }

        public ConfigTransformer withBindings(Consumer<Hero> bindings) {
            return new ConfigTransformer(this.defaults, bindings);
        }

    }

    public static final class ConfigTransformer {

        private final boolean defaults;
        private final Consumer<Hero> bindings;

        private ConfigTransformer(boolean defaults, Consumer<Hero> bindings) {
            this.defaults = defaults;
            this.bindings = bindings;
        }

        public HeroConfiguration withoutTransformers() {
            return this.withTransformers(hero -> {});
        }

        public HeroConfiguration withTransformers(Consumer<Hero> transformers) {

            DefaultHeroConfiguration defaultConfig = new DefaultHeroConfiguration();

            return new HeroConfiguration() {
                @Override
                public void defaultBindings(Hero hero) {
                    if (ConfigTransformer.this.defaults) {
                        defaultConfig.defaultBindings(hero);
                    }

                    ConfigTransformer.this.bindings.accept(hero);
                }

                @Override
                public void defaultTransformers(Hero hero) {
                    if (ConfigTransformer.this.defaults) {
                        defaultConfig.defaultTransformers(hero);
                    }

                    transformers.accept(hero);
                }
            };
        }

    }

}
