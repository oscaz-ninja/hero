package ninja.oscaz.hero.transform.intercept;

import com.google.common.collect.Lists;
import java.util.List;

public class Arguments {

    private final List<Object> arguments;

    public Arguments(Object[] arguments) {
        this.arguments = Lists.newArrayList(arguments);
    }

}
