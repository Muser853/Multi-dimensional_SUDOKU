import java.io.IOException;

public class Sudoku {
    private final Board board;
    private LandscapeDisplay ld;
    private final int delay;

    public Sudoku(int numLocked, int delay) {
        board = new Board(numLocked, 3);
        this.delay = delay;
        //ld = new LandscapeDisplay(board);
    }

    public Sudoku(String filename, int delay) throws IOException {
        board = new Board(filename);
        this.delay = delay;
    }

    public int findNextValue(Cell c) {
        int currentValue = c.getValue();
        int startValue = currentValue == 0 ? 1 : currentValue + 1;
        for (int value = startValue; value <= 9; value++) {
            if (board.validValue(c.getRow(), c.getCol(), c.getDepth(), value)) {
                return value;
            }
        }
        return 0;
    }

    public Cell findNextCell() {
        Cell bestCell = null;
        int minOptions = board.sideLength;

        for (int i=0; i<9; i++)
            for (int j=0; j<9; j++)
                for (int k=0; k<9; k++) {
                    Cell c = board.get(i,j,k);
                    if (c.getValue() !=0 || c.isLocked()) continue;
                    int opts = 0;
                    for (int v=1; v<=9; v++)
                        if (board.validValue(i,j,k,v)) opts++;
                    if (opts ==0) return null;
                    if (opts < minOptions) minOptions = opts; bestCell = c;
                }
        return bestCell;
    }

    public boolean solve() throws InterruptedException {
        Stack<Cell> stack = new LinkedList<>();
        int size = (int) Math.pow(board.sideLength, 3);
        while (stack.size() <  size - board.numLocked()) { // Stack filled cells
            Cell next = findNextCell();

            while (next == null && !stack.isEmpty()) {
//                if (delay > 0) {
//                    Thread.sleep(delay);
//                    ld.repaint();
//                }
                Cell last = stack.pop();
                int value = findNextValue(last);

                if (value != 0) {
                    board.set(last.getRow(), last.getCol(), last.getDepth(), value);
                    next = last;
                } else {
                    board.set(last.getRow(), last.getCol(), last.getDepth(), 0); // Reset ineffective value
                }
            }
            if (next == null) {
                Board.finished = true;
                return false;
            }
            int nextValue = findNextValue(next); // Avoid redundant calls
            board.set(next.getRow(), next.getCol(), next.getDepth(), nextValue);
            stack.push(next);
        }
        Board.finished = true;
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        int trials = 10, timeoutThreshold = 1000;

        for (int initialLocked = 0; initialLocked < 50; initialLocked++) {
            int solved = 0;
            int timeout = 0;
            for (int i = 0; i < trials; i++) {
                Sudoku s = new Sudoku(initialLocked, 0);
                long startTime = System.currentTimeMillis();
                if (s.solve()) solved++;

                long duration = System.currentTimeMillis() - startTime;
                if (duration > timeoutThreshold) timeout++;
            }
            System.out.printf("initialLocked: %d, success: %d, timeOut: %d%n",
                initialLocked, solved, timeout);
        }
    }
}