package ninja.oscaz.hero.transform.intercept;

public class Arguments {

    private final ArgumentMappings mappings;
    private final Object[] arguments;

    public Arguments(ArgumentMappings mappings, Object[] arguments) {
        this.mappings = mappings;
        this.arguments = arguments;
    }

    public <R> R get(Class<R> type) {
        return this.get(type, 0);
    }

    public <R> R get(Class<R> type, int skip) {
        return this.mappings.attemptToFind(type, this.arguments, skip);
    }

}
