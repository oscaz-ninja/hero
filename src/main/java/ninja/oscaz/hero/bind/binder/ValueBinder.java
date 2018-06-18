package ninja.oscaz.hero.bind.binder;

public class ValueBinder implements Binder {

    private final Object value;

    public ValueBinder(Object value) {
        this.value = value;
    }

    @Override
    public Object create() {
        return this.value;
    }

}
