package ninja.oscaz.hero.transform.intercept.aspects;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import ninja.oscaz.hero.transform.intercept.Arguments;
import ninja.oscaz.hero.transform.intercept.Interceptor;
import ninja.oscaz.hero.transform.intercept.Pipe;
import ninja.oscaz.hero.util.except.Try;

public class AsyncInterceptor implements Interceptor {

    @Override
    public CompletableFuture<Object> intercept(Arguments arguments, Pipe pipe) {
        return CompletableFuture.supplyAsync(this.supplyAsync(pipe));
    }

    public Supplier<Object> supplyAsync(Pipe pipe) {
        return () -> {
            Object value = Try.toGetOrNull(pipe::proceed);

            if (value == null) {
                return null;
            }

            if (value instanceof Future) {
                return Try.toGetOrNull(((Future<?>) value)::get);
            }

            return value;
        };
    }

}
