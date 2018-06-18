package ninja.oscaz.hero.transform.intercept.aspects;

import com.google.common.base.Stopwatch;
import ninja.oscaz.hero.transform.intercept.Arguments;
import ninja.oscaz.hero.transform.intercept.Interceptor;
import ninja.oscaz.hero.transform.intercept.Pipe;

public class AuditInterceptor implements Interceptor {

    @Override
    public Object intercept(Arguments arguments, Pipe pipe) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        Object value = pipe.proceed();

        stopwatch.stop();
        System.out.println(pipe.getBase().getName() + " took " + stopwatch.toString() + " to execute");

        return value;
    }

}
