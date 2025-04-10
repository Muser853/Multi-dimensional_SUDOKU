import java.awt.Color;
import java.awt.Graphics;

public class Cell {
    private int row, col, depth, value;
    boolean locked;

    public Cell(int row, int col, int depth, int value) {
        this.row = row; this.col = col; this.depth = depth;
        this.value = value; this.locked = false;
    }

    public Cell(int row, int col, int depth, int value, boolean locked) {
        this.row = row; this.col = col; this.depth = depth;
        this.value = value; this.locked = locked;
    }

    public int getDepth() { return depth; }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int newval) {
        this.value = newval;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean lock) {
        this.locked = lock;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
    public void draw(Graphics g, int x, int y, int scale){
        char toDraw = (char) ((int) '0' + getValue());
        g.setColor(isLocked()? Color.BLUE : Color.RED);
        g.drawChars(new char[] {toDraw}, 0, 1, x, y);
    }
    // test cases
    public static void main(String[] args) {
        Cell c1 = new Cell(1, 2, 0, 3);
        Cell c2 = new Cell(4, 5, 0, 6, true);
        System.out.println(c1); // 3
        System.out.println(c2); // 6
        c1.setValue(4);
        System.out.println(c1); // 4
    }
}