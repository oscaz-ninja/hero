package ninja.oscaz.hero.bind;

import ninja.oscaz.hero.bind.binder.Binder;

public class Binding {

    public static IncompleteBinding from(Class<?> from) {
        return new IncompleteBinding(from);
    }

    public static UnscopedBinding toSelf(Class<?> from) {
        return Binding.from(from).to(from);
    }

    private final Class<?> from;
    private final Class<?> to;
    private final Binder binder;

    public Binding(Class<?> from, Class<?> to, Binder binder) {
        this.from = from;
        this.to = to;
        this.binder = binder;
    }

    public Class<?> from() {
        return this.from;
    }

    public Class<?> to() {
        return this.to;
    }

    public Binder getBinder() {
        return this.binder;
    }

    public Object getObject() {
        return this.binder.create();
    }

    public static final class IncompleteBinding {

        private final Class<?> from;

        private IncompleteBinding(Class<?> from) {
            this.from = from;
        }

        public UnscopedBinding to(Class<?> to) {
            return new UnscopedBinding(this.from, to);
        }

    }

    public static final class UnscopedBinding {

        private final Class<?> from;
        private final Class<?> to;

        private UnscopedBinding(Class<?> from, Class<?> to) {
            this.from = from;
            this.to = to;
        }

        public Binding with(Binder binder) {
            return new Binding(this.from, this.to, binder);
        }
    }

}
