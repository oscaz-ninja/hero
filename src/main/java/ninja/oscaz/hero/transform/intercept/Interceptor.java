package ninja.oscaz.hero.transform.intercept;

@FunctionalInterface
public interface Interceptor {

    Object intercept(Arguments arguments, Pipe pipe);

}
