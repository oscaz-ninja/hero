package ninja.oscaz.hero;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import ninja.oscaz.hero.bind.Binding;
import ninja.oscaz.hero.bind.Bindings;
import ninja.oscaz.hero.bind.binder.DefaultBinder;
import ninja.oscaz.hero.bind.binder.SingletonBinder;
import ninja.oscaz.hero.bind.binder.SupplyingBinder;
import ninja.oscaz.hero.bind.binder.ValueBinder;
import ninja.oscaz.hero.config.DefaultHeroConfiguration;
import ninja.oscaz.hero.config.HeroConfiguration;
import ninja.oscaz.hero.except.HeroRequestException;
import ninja.oscaz.hero.transform.HeroTransformer;
import ninja.oscaz.hero.transform.inject.InjectionTransformer;

public final class Hero {

    public static Hero createDefault() {
        return Hero.create(new DefaultHeroConfiguration());
    }

    public static Hero create(HeroConfiguration config) {
        Hero hero = new Hero();

        config.defaultBindings(hero);
        config.defaultTransformers(hero);

        return hero;
    }

    private final Bindings bindings = new Bindings(this);
    private final List<HeroTransformer> transformers = Lists.newArrayList();

    private Hero() {
        this.transformers.add(new InjectionTransformer(this));
    }

    public <T> T request(Class<T> type) {
        T value = this.requestNullable(type);
        Objects.requireNonNull(value);

        return value;
    }

    public <T> T requestSpecific(Class<?> request, Class<T> cast) {
        T value = this.requestSpecificNullable(request, cast);
        Objects.requireNonNull(value);

        return value;
    }

    public Object requestDynamic(Class<?> type) {
        Object value = this.requestDynamicNullable(type);
        Objects.requireNonNull(value);

        return value;
    }

    public <T> T requestNullable(Class<T> type) {
        Object dynamic = this.requestDynamicNullable(type);

        if (dynamic == null) {
            throw new HeroRequestException("Request for type: " + type + " found null result");
        }

        if (!type.isInstance(dynamic)) {
            throw new HeroRequestException(
                "Request for type: " + type + " found unassignable result of type " + dynamic.getClass());
        }

        @SuppressWarnings("unchecked")
        T casted = (T) dynamic;

        return casted;
    }

    public <T> T requestSpecificNullable(Class<?> request, Class<T> cast) {
        Object value = this.requestNullable(request);

        @SuppressWarnings("unchecked")
        T casted = (T) value;

        return casted;
    }

    public Object requestDynamicNullable(Class<?> type) {
        if (type == null) {
            throw new HeroRequestException("Type requested was null");
        }

        Object value = this.bindings.getBinding(type).getObject();

        return this.transform(value);
    }

    public void addTransformer(Class<?> type) {
        this.transformers.add((HeroTransformer) this.requestDynamic(type));
    }

    public Class<?> transformType(Class<?> type) {
        Class<?> transformedType = type;

        for (HeroTransformer transformer : this.transformers) {
            transformedType = transformer.transformType(transformedType);
        }

        return transformedType;
    }

    public Object transform(Object value) {
        Object transformedObject = value;

        for (HeroTransformer transformer : this.transformers) {
            transformedObject = transformer.transform(transformedObject);
        }

        return transformedObject;
    }

    public void addBinding(Binding binding) {
        this.bindings.addBinding(binding);
    }

    public BindingRequest bind(Class<?> from) {
        return new BindingRequest(from);
    }

    public class BindingRequest {

        private final Class<?> binding;

        public BindingRequest(Class<?> binding) {
            this.binding = binding;
        }

        public void toNone() {
            Hero.this.bindings.removeBinding(this.binding);
        }

        public void to(Class<?> to) {
            Binding existing = Hero.this.bindings.getBinding(to);

            if (existing != null) {
                Hero.this.bindings.addBinding(
                    Binding.from(this.binding).to(to).with(existing.getBinder())
                );
            } else {
                Hero.this.bindings.addBinding(
                    Binding.from(this.binding).to(to).with(new DefaultBinder(Hero.this, to))
                );
            }
        }

        public void toSingleton() {
            this.toSingleton(this.binding);
        }

        public void toSingleton(Class<?> to) {
            Hero.this.bindings.addBinding(
                Binding.from(this.binding).to(to).with(new SingletonBinder(Hero.this, to))
            );
        }

        public void toValue(Object value) {
            Hero.this.bindings.addBinding(
                Binding.toSelf(this.binding).with(new ValueBinder(value))
            );
        }

        public void toSupplier(Supplier<Object> supplier) {
            Hero.this.bindings.addBinding(
                Binding.toSelf(this.binding).with(new SupplyingBinder(supplier))
            );
        }

    }

}
