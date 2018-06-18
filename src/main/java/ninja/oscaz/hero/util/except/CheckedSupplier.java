package ninja.oscaz.hero.util.except;

@FunctionalInterface
public interface CheckedSupplier<T> {

    T get() throws Throwable;

}
