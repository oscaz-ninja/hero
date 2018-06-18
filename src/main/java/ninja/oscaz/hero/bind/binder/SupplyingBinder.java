package ninja.oscaz.hero.bind.binder;

import java.util.function.Supplier;

public class SupplyingBinder implements Binder {

    private final Supplier<Object> supplier;

    public SupplyingBinder(Supplier<Object> supplier) {
        this.supplier = supplier;
    }

    @Override
    public Object create() {
        return this.supplier.get();
    }

}
