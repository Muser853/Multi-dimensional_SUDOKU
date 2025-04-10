import java.awt.Graphics;
 import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Board {
    private static Cell[][][] cells;
    public static boolean finished;
    public int sideLength;

    public Board(int blockLength) {
        sideLength = blockLength * blockLength;
        cells = new Cell[sideLength][sideLength][sideLength];
        for (int i=0; i<9; i++)
            for (int j=0; j<9; j++)
                for (int k=0; k<9; k++)
                    cells[i][j][k] = new Cell(i,j,k,0);
    }

    public Board(int numFixed, int blockLength) {
        this (blockLength);
        Random rand = new Random();
        int count = 0;
        while (count < numFixed) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);
            int depth = rand.nextInt(9);
            int value = rand.nextInt(9) + 1;
            if (cells[row][col][depth].getValue() == 0 && validValue(row, col, depth, value)) {
                set(row, col, depth, value);
                set(row, col, depth, true); // Lock the cell
                count++;
            }
        }
    }

    public  static void setFinished(boolean finish){
        finished = finish;
    }

    public boolean validValue(int r, int c, int d, int val) {
        for (int i=0; i<9; i++) { // all columns for certain line and depth
            if (i != c && cells[r][i][d].getValue() == val) return false;
        }
        for (int i=0; i<9; i++) { // all lines for certain column and depth
            if (i != r && cells[i][c][d].getValue() == val) return false;
        }
        for (int i=0; i<9; i++) { // all depths for certain line and column
            if (i != d && cells[r][c][i].getValue() == val) return false;
        }

        // check
        int br = r/3 *3, bc = c/3 *3, bd = d/3 *3;
        for (int i=br; i<br+3; i++)
            for (int j=bc; j<bc+3; j++)
                for (int k=bd; k<bd+3; k++) {
                    if (i==r && j==c && k==d) continue;
                    if (cells[i][j][k].getValue() == val) return false;
                }
        return true;
    }

    public boolean validSolution() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = 0; k < 9; k++) {
                    if (cells[i][j][k].getValue() == 0 || !validValue(i, j, cells[i][j][k].getValue(), k)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int getCols() {
        return 9;
    }

    public int getRows() {
        return 9;
    }

    public int value(int row, int col, int depth) {
        return cells[row][col][depth].getValue();
    }

    public boolean isLocked(int row, int col, int depth) {
        return cells[row][col][depth].isLocked();
    }

    public int numLocked() {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = 0; k < 9; k++) {
                    if (isLocked(i, j, k)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public Board(String filename) throws IOException {
        this(3);
        read(filename);
    }

    public Cell get(int row, int col, int depth) {
        return cells[row][col][depth];
    }

/**
 * Sets the value of the cell at the specified row, column, and depth.
 * If the cell is locked, the value is not changed.
 *
 * @param row the row index of the cell
 * @param col the column index of the cell
 * @param depth the depth index of the cell
 * @param value the new value to set
 */

    public void set(int row, int col, int depth, int value) {
        if (isLocked(row, col, depth)) return;
        cells[row][col][depth].setValue(value);
    }

    public void set(int row, int col, int depth, boolean lock) {
        cells[row][col][depth].setLocked(lock);
    }

    public boolean read(String filename) throws IOException {
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            int row = 0;
            while (line != null) {
                String[] arr = line.split("[ ]+");
                for (int i = 0; i < arr.length; i++) {
                    int value = Integer.parseInt(arr[i]);
                    boolean locked = (value != 0);
                    set(row, i, 0, value);
                    set(row, i, 0, locked);
                }
                row++;
                line = br.readLine();
            }
            br.close();
            return true;
        } catch (FileNotFoundException ex) {
            System.out.println("Board.read():: unable to open file " + filename);
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < 9; k++) { // Use hardcoded size 9
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    sb.append(cells[k][i][j].getValue());
                    sb.append(" ");
                }
                sb.append("\n");
            }
            sb.append("\n\n");
        }
        return sb.toString();
    }

    public void draw(Graphics g, int scale){
        for(int i = 0; i<getRows(); i++){
            for(int j = 0; j<getCols(); j++){
                get(i, j, 0).draw(g, j*scale+5, i*scale+10, scale);
            }
        } 
        if (finished){
            if(validSolution()){
                g.setColor(new Color(0, 127, 0));
                g.drawChars("Hurray!".toCharArray(), 0, "Hurray!".length(), scale*3+5, scale*10+10);
            } else {
                g.setColor(new Color(127, 0, 0));
                g.drawChars("No solution!".toCharArray(), 0, "No solution!".length(), scale*3+5, scale*10+10); // Fix length mismatch
            }
        }
    }
    public static void main(String[] args) {
        try {
            Board board = new Board("board1.txt");
            System.out.println(board.toString());
        } catch (IOException ex) {
            System.out.println("Board.main():: unable to read file");
        }
    }
}