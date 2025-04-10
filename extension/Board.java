import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class Board {
    public int dimensionCount, sideLength, blockLength, numLocked, totalCells;
    protected int[] dimensions, availableValues;
    protected Cell[] cells; // 1D array to store all cells
    protected List<Integer>[] blocks; // precompute the blocks
    // bitmask to store available values

    public Board(BoardConfig config) {
        this.dimensionCount = config.dimensionCount;
        this.sideLength = config.sideLength;
        this.blockLength = config.blockLength;
        this.dimensions = config.dimensions;
        totalCells = (int) Math.pow(sideLength, dimensionCount);
        cells = new Cell[totalCells];
        blocks = precomputeBlocks();
        availableValues = new int[totalCells];
        Arrays.fill(availableValues, (1 << sideLength) - 1);

        // initialize all cells to 0
        for (int i = 0; i < totalCells; i++) {
            cells[i] = new Cell(coordinatesFromIndex(i), 0, false);
        }
    }

    private int[] coordinatesFromIndex(int index) {
        int[] coords = new int[dimensionCount];
        int divisor = 1;
        for (int dim = dimensionCount - 1; dim >= 0; dim--) {
            coords[dim] = (index / divisor) % dimensions[dim];
            divisor *= dimensions[dim];
        }
        return coords;
    }

    // transform coordinates
    protected int getFlatIndex(int[] coordinates) {
        int index = 0, multiplier = 1;
        for (int dim = dimensionCount - 1; dim >= 0; dim--) {
            index += coordinates[dim] * multiplier;
            multiplier *= dimensions[dim];
        }
        return index;
    }

    private List<Integer>[] precomputeBlocks() {
        int blockCount = 1;
        for (int dim = 0; dim < dimensionCount; dim++) {
            blockCount *= (dimensions[dim] / blockLength);
        }

        @SuppressWarnings("unchecked")
        List<Integer>[] blocks = new List[blockCount];

        for (int blockIndex = 0; blockIndex < blockCount; blockIndex++) {
            blocks[blockIndex] = new ArrayList<>();
            int[] blockStart = new int[dimensionCount];

            for (int dim = 0; dim < dimensionCount; dim++) {
                int divider = 1;
                for (int d = 0; d < dim; d++) {
                    divider *= (dimensions[d] / blockLength);
                }
                int blockNumber = (blockIndex / divider) % (dimensions[dim] / blockLength);
                blockStart[dim] = blockNumber * blockLength;
            }

            List<int[]> coordsList = new ArrayList<>();
            generateBlockCoordinates(blockStart, new int[dimensionCount], 0, coordsList);
            for (int[] coords : coordsList) {
                blocks[blockIndex].add(getFlatIndex(coords));
            }
        }
        return blocks;
    }

    // recursively generate all coordinates in a block
    private void generateBlockCoordinates(int[] blockStart, int[] current, int depth, List<int[]> coordsList) {
        if (depth == dimensionCount) {
            coordsList.add(current.clone());
            return;
        }
        for (int i = blockStart[depth]; i < blockStart[depth] + blockLength; i++) {
            current[depth] = i;
            generateBlockCoordinates(blockStart, current, depth + 1, coordsList);
        }
    }

    protected int getBlockIndex(int... coordinates) {
        int blockIndex = 0;
        int multiplier = 1;
        for (int dim = 0; dim < dimensionCount; dim++) {
            int blockStart = (coordinates[dim] / blockLength) * blockLength;
            int blockNumber = blockStart / blockLength;
            blockIndex += blockNumber * multiplier;
            multiplier *= (dimensions[dim] / blockLength);
        }
        return blockIndex;
    }

    // set value and update the bitmask
    public void set(int[] coordinates, int value) {
        int index = getFlatIndex(coordinates);
        cells[index].setValue(value);
        updateAvailableValues(coordinates, value);
    }

    private void updateAvailableValues(int[] coordinates, int value) {
         int index = getFlatIndex(coordinates);
        availableValues[index] &= ~(1 << (value - 1));

        // update dimensionConstraints
        for (int dim = 0; dim < dimensionCount; dim++) {
            int[] temp = coordinates.clone();
            for (int i = 0; i < dimensions[dim]; i++) {
                temp[dim] = i;
                int cellIndex = getFlatIndex(temp);
                availableValues[cellIndex] &= ~(1 << (value - 1));
            }
        }
        // update block constraints
        int blockIndex = getBlockIndex(coordinates);
        for (int cellIndex : blocks[blockIndex]) {
            availableValues[cellIndex] &= ~(1 << (value - 1));
        }
    }

    protected void initializeFixedCells(int numFixed) {
        numLocked = numFixed;
        Random rand = new Random();
        while (numFixed-- > 0) {
            int[] coords = new int[dimensionCount];
            for (int dim = 0; dim < dimensionCount; dim++) {
                coords[dim] = rand.nextInt(dimensions[dim]);
            }
            int value = rand.nextInt(sideLength) + 1;
            if (validValue(coords, value)) {
                set(coords, value);
                cells[getFlatIndex(coords)].locked = true;
            }
        }
    }

    public boolean validValue(int[] coordinates, int value) {
        int index = getFlatIndex(coordinates);
        if ((availableValues[index] & (1 << (value-1))) == 0) return false;

        for (int dim = 0; dim < dimensionCount; dim++) {
            int[] temp = coordinates.clone();
            for (int i = 0; i < sideLength; i++) {
                temp[dim] = i;
                int cellIndex = getFlatIndex(temp);
                if (cellIndex == index) continue;
                if (cells[cellIndex].getValue() == value) return false;
            }
        }
        int blockIndex = getBlockIndex(coordinates);
        for (int cellIndex : blocks[blockIndex]) {
            if (cellIndex == index) continue;
            if (cells[cellIndex].getValue() == value) return false;
        }
        return true;
    }

    public boolean validSolution() {
        for (Cell cell : cells) {
            if (cell.getValue() == 0) return false;
            int[] coords = cell.getCoordinates();
            if (!validValue(coords, cell.getValue())) return false;
        }
        return true;
    }

    public void print(int dimensionCount, Board board) {
        System.out.printf("\nExample Solution for %dD Sudoku:\n", dimensionCount);
        int sideLength = board.sideLength;
        for (int i = 0; i < board.totalCells; i++) {
            System.out.printf("%3d", board.cells[i].getValue()); // Right-align values with padding
            if ((i + 1) % sideLength == 0) {
                System.out.println();
            }
        }
        System.out.println("-------------------------------");
    }
}