package ninja.oscaz.hero.bind.binder;

import ninja.oscaz.hero.Hero;

public class SingletonBinder extends DefaultBinder {

    private Object value;

    public SingletonBinder(Hero hero, Class<?> type) {
        super(hero, type);
    }

    @Override
    public Object create() {
        if (this.value == null) {
            this.value = super.create();
        }

        return this.value;
    }

}
