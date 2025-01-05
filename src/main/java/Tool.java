public enum Tool {
    ROCK("Rock"),
    PAPER("Paper"),
    SCISSORS("Scissors");

    private final String name;

    Tool(String name) {
        this.name = name;
    }

    String asLabelContent(int currentCapacity, int maxCapacity) {
        return this.name + ": " + currentCapacity + "/" + maxCapacity;
    }
}
