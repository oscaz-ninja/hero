package ninja.oscaz.hero.bind.binder;

import ninja.oscaz.hero.Hero;
import ninja.oscaz.hero.util.except.Try;
import ninja.oscaz.hero.util.reflect.Handles;

public class DefaultBinder implements Binder {

    private final Class<?> type;

    public DefaultBinder(Hero hero, Class<?> type) {
        this.type = hero.transformType(type);
    }

    @Override
    public Object create() {
        return Try.toGet(Handles.accessibilify(Try.toGet(this.type::getDeclaredConstructor))::newInstance);
    }

}
