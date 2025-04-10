public class Cell {
    protected int[] coordinates;
    protected int value;
    protected boolean locked;

    public Cell(int[] coordinates, int value, boolean locked) {
        this.coordinates = coordinates.clone();
        this.value = value;
        this.locked = locked;
    }

    public int[] getCoordinates() { return coordinates; }
    public int getValue() { return value; }
    public boolean isLocked() { return locked; }
    public void setValue(int val) { value = val; }
}