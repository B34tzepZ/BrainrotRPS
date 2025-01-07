public interface GameListener {
    void onAdded(Tool tool);

    boolean hasCapacity(Tool tool);

    boolean invertedControls();
    int gameSpeed();
}
