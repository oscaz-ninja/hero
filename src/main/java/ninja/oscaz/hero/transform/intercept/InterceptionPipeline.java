package ninja.oscaz.hero.transform.intercept;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

public class InterceptionPipeline {

    private final Method base;
    private final List<Interceptor> interceptors;

    public InterceptionPipeline(Method base, List<Interceptor> interceptors) {
        this.base = base;
        this.interceptors = interceptors;
    }

    @RuntimeType
    public Object intercept(@AllArguments Object[] arguments, @SuperCall Callable<?> callable) throws Exception {
        return new InterceptedCall(this.base, new Arguments(arguments), callable).call();
    }

    private final class InterceptedCall implements Callable<Object> {

        private final Method base;
        private final Iterator<Interceptor> interceptors;
        private final Arguments arguments;
        private final Callable<?> last;

        private InterceptedCall(Method base, Arguments arguments, Callable<?> last) {
            this.base = base;
            this.interceptors = InterceptionPipeline.this.interceptors.iterator();
            this.arguments = arguments;
            this.last = last;
        }

        @Override
        public Object call() throws Exception {
            if (this.interceptors.hasNext()) {
                return this.interceptors.next().intercept(this.arguments, new Pipe(this.base, this));
            }

            return this.last.call();
        }

    }

}
