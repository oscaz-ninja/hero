package ninja.oscaz.hero.util.except;

@FunctionalInterface
public interface CheckedRunnable {

    void run() throws Throwable;

}
