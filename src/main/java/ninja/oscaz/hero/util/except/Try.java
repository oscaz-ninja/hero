package ninja.oscaz.hero.util.except;

public final class Try {

    public static void to(CheckedRunnable runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            throw Exceptions.uncheck(throwable);
        }
    }

    public static void toIgnore(CheckedRunnable runnable) {
        try {
            runnable.run();
        } catch (Throwable ignored) {

        }
    }

    public static <T> T toGet(CheckedSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Throwable throwable) {
            throw Exceptions.uncheck(throwable);
        }
    }

    public static <T> T toGetOrNull(CheckedSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Throwable throwable) {
            return null;
        }
    }

    public static <T, R> R toApply(CheckedFunction<T, R> function, CheckedSupplier<T> supplier) {
        try {
            T argument = supplier.get();
            return function.apply(argument);
        } catch (Throwable throwable) {
            throw Exceptions.uncheck(throwable);
        }
    }

    public static <T, R> R toApplyOrNull(CheckedFunction<T, R> function, CheckedSupplier<T> supplier) {
        try {
            T argument = supplier.get();
            return function.apply(argument);
        } catch (Throwable throwable) {
            return null;
        }
    }

    private Try() {
        throw new UtilityInstantiationException();
    }

}
