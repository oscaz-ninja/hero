package ninja.oscaz.hero.transform;

public interface HeroTransformer {

    default <T> Class<? extends T> transformType(Class<T> type) {
        return type;
    }

    default <T> T transform(T value) {
        return value;
    }

}
