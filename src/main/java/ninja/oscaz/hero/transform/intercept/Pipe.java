package ninja.oscaz.hero.transform.intercept;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import ninja.oscaz.hero.util.except.Try;

public class Pipe {

    private final Method base;
    private final Callable<?> callable;

    public Pipe(Method base, Callable<?> callable) {
        this.base = base;
        this.callable = callable;
    }

    public Object proceed() {
        return Try.toGet(this.callable::call);
    }

    public Method getBase() {
        return this.base;
    }

}
