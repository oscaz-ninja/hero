package ninja.oscaz.hero.util.except;

public final class Exceptions {

    public static RuntimeException uncheck(Throwable throwable) {
        return throwable instanceof RuntimeException ?
            (RuntimeException) throwable :
            new RuntimeException(throwable);
    }

    private Exceptions() {
        throw new UtilityInstantiationException();
    }

}
