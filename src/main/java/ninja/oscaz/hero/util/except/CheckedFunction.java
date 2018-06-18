package ninja.oscaz.hero.util.except;

@FunctionalInterface
public interface CheckedFunction<T, R> {

    R apply(T t) throws Throwable;

}
