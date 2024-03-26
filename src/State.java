public enum State {
    OPEN("Open"),
    PARTIALLY_FILLED("Partially Filled"),
    FULLY_FILLED("Fully Filled");
    public final String description;

    private State(String description) {
        this.description = description;
    }

    public static State findByName(String name) {
        State result = null;
        for (State state : values()) {
            if (state.name().equalsIgnoreCase(name)) {
                result = state;
                break;
            }
        }
        return result;
    }
}
