import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

public class Sudoku {
    public enum CellSelectionStrategy {
        MIN_CANDIDATES, RANDOM, MAX_CONSTRAINTS
    }

    private Board board;
    private CellSelectionStrategy strategy;

    public Sudoku(BoardConfig config, int numLocked, CellSelectionStrategy strategy) {
        this.strategy = strategy;
        this.board = new Board(config);
        board.initializeFixedCells(numLocked);
    }
    // bitmask for each cell indicating which values are available
    private int findNextValue(int[] coordinates) {
        int index = board.getFlatIndex(coordinates);
        int available = board.availableValues[index];
        for (int val = 1; val <= board.sideLength; val++) {
            if ((available & (1 << (val - 1))) != 0) return val;
        }
        return 0;
    }

    private Cell findNextCell() {
        return switch (strategy) {
            case MIN_CANDIDATES -> selectByMinCandidates();
            case RANDOM -> selectRandom();
            case MAX_CONSTRAINTS -> selectByMaxConstraints();
        };
    }

    private Cell selectRandom() {
        ArrayList<Cell> candidates = new ArrayList<>();
        for (Cell c : board.cells) {
            if (!c.isLocked() && c.getValue() == 0) {
                candidates.add(c);
            }
        }
        if (candidates.isEmpty()) return null;
        return candidates.get(new Random().nextInt(candidates.size()));
    }

    private Cell selectByMinCandidates() {
        Cell bestCell = null;
        int minOptions = Integer.MAX_VALUE;
        for (int i = 0; i < board.totalCells; i++) {
            Cell c = board.cells[i];
            if (c.isLocked() || c.getValue() != 0) continue;
            int options = Integer.bitCount(board.availableValues[i]);
            if (options < minOptions) {
                minOptions = options;
                bestCell = c;
            }
        }
        return bestCell;
    }

    private Cell selectByMaxConstraints() {
        int maxFilled = -1;
        Cell bestCell = null;
        for (int i = 0; i < board.totalCells; i++) {
            Cell c = board.cells[i];
            if (c.isLocked() || c.getValue() != 0) continue;
            int filled = countFilledConstraints(c.getCoordinates());
            if (filled > maxFilled) {
                maxFilled = filled;
                bestCell = c;
            }
        }
        return bestCell;
    }

    private int countFilledConstraints(int[] coordinates) {
        int count = 0;
        // Calculate filled cells in all dimensions
        for (int dim = 0; dim < board.dimensionCount; dim++) {
            int[] temp = coordinates.clone();
            for (int i = 0; i < board.sideLength; i++) {
                temp[dim] = i;
                int index = board.getFlatIndex(temp);
                if (index < board.cells.length && board.cells[index].getValue() != 0) {
                    count++;
                }
            }
        }
        // Add block constraints
        int blockIndex = board.getBlockIndex(coordinates);
        for (int cellIndex : board.blocks[blockIndex]) {
            if (board.cells[cellIndex].getValue() != 0) {
                count++;
            }
        }
        return count;
    }

    public boolean solve() {
        Stack<Cell> stack = new LinkedList<>();
        while (stack.size() < board.totalCells - board.numLocked) {
            Cell next = findNextCell();

            while (next == null && !stack.isEmpty()) {
                Cell popped = stack.pop();
                int[] coords = popped.getCoordinates();
                int nextVal = findNextValue(coords);
                if (nextVal != 0) {
                    board.set(coords, nextVal);
                    popped.setValue(nextVal);
                    next = popped;
                }
            }
            if (next == null) return false;
            stack.push(next);
        }
        return true;
    }

    private record Result(long totalTime, int successCount) {}
    record Data(List<Double> timeData, List<Double> successData) {}

    public static void saveChart(LineChart chart, String filename) throws IOException {
        BufferedImage image = new BufferedImage(chart.getWidth(), chart.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        chart.setSize(800, 600);
        chart.paint(g2d);
        g2d.dispose();
        File outputFile = new File(filename);
        ImageIO.write(image, "png", outputFile);
    }

    public static void main(String[] args) throws InterruptedException {
        Map<String, Map<CellSelectionStrategy, Data>> allResults = new ConcurrentHashMap<>();
        System.out.println("Generating example puzzles:");
        for (int dim = 2; dim <=5; dim++) {
            BoardConfig config = new BoardConfig(dim, 2);
            Sudoku example = new Sudoku(config, 0, CellSelectionStrategy.MIN_CANDIDATES);
            if (example.solve()) {
                System.out.printf("\nExample for %dD Sudoku:\n", dim);
                example.board.print(dim, example.board);
            } else {
                System.out.printf("Failed to solve %dD example\n", dim);
            }
        }
        ExecutorService executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2);

        for (int dimensionCount = 2; dimensionCount <= 5; dimensionCount++) {
            for (int blockLength = 2; blockLength <= 5; blockLength++) {
                final int finalDimensionCount = dimensionCount;
                final int finalBlockLength = blockLength;
                executor.submit(() -> {
                    BoardConfig config = new BoardConfig(finalDimensionCount, finalBlockLength);
                    int sideLength = (int) Math.pow(finalBlockLength, finalDimensionCount);
                    int maxLocked = (int) Math.pow(sideLength, finalDimensionCount);

                    Map<CellSelectionStrategy, List<Double>> timeData = new HashMap<>();
                    Map<CellSelectionStrategy, List<Double>> successData = new HashMap<>();
                    for (CellSelectionStrategy s : CellSelectionStrategy.values()) {
                        timeData.put(s, new ArrayList<>());
                        successData.put(s, new ArrayList<>());
                    }

                    // process each numLocked in parallel
                    IntStream.rangeClosed(0, maxLocked).parallel().forEach(numLocked -> {
                        Map<CellSelectionStrategy, Result> strategyResults = new HashMap<>();
                        Arrays.stream(CellSelectionStrategy.values()).parallel().forEach(strategy -> {
                            long totalTime = 0;
                            int successCount = 0;
                            for (int trial = 0; trial < 100; trial++) {
                                Sudoku solver = new Sudoku(config, numLocked, strategy);
                                long startTime = System.currentTimeMillis();
                                boolean solved = solver.solve();
                                long duration = System.currentTimeMillis() - startTime;
                                totalTime += duration;
                                if (solved) successCount++;
                            }
                            strategyResults.put(strategy, new Result(totalTime, successCount));
                        });
                        synchronized (timeData) {
                            for (CellSelectionStrategy strategy : strategyResults.keySet()) {
                                Result result = strategyResults.get(strategy);
                                timeData.get(strategy).add((double) result.totalTime);
                                successData.get(strategy).add(result.successCount / 100.0);
                            }
                        }
                    });
                    // graph of total time
                    List<List<Double>> timeDatasets = new ArrayList<>();
                    String[] timeLabels = new String[CellSelectionStrategy.values().length];
                    for (int i = 0; i < CellSelectionStrategy.values().length; i++) {
                        timeDatasets.add(timeData.get(CellSelectionStrategy.values()[i]));
                        timeLabels[i] = CellSelectionStrategy.values()[i].name();
                    }
                    String timeTitle = String.format("Sudoku Time Performance (Dim %d, Block %d)", finalDimensionCount, finalBlockLength);
                    
                    LineChart timeChart = new LineChart(timeTitle, "Number of Locked Cells", "Time (ms)", timeDatasets, timeLabels, 10);
                    try {
                        saveChart(timeChart, String.format("time_dim%d_block%d.png", finalDimensionCount, finalBlockLength));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                    List<List<Double>> successDatasets = new ArrayList<>();
                    String[] successLabels = new String[CellSelectionStrategy.values().length];
                    for (int i = 0; i < CellSelectionStrategy.values().length; i++) {
                        successDatasets.add(successData.get(CellSelectionStrategy.values()[i]));
                        successLabels[i] = CellSelectionStrategy.values()[i].name();
                    }
                    String successTitle = String.format("Sudoku Success Rate (Dim %d, Block %d)", finalDimensionCount, finalBlockLength);
                    LineChart successChart = new LineChart(successTitle, "Number of Locked Cells", "Success Rate", successDatasets, successLabels, 10);
                    try {
                        saveChart(successChart, String.format("success_dim%d_block%d.png", finalDimensionCount, finalBlockLength));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String key = String.format("dim%d_block%d", finalDimensionCount, finalBlockLength);
                    Map<CellSelectionStrategy, Data> dataMap = new HashMap<>();

                    for (CellSelectionStrategy strategy : CellSelectionStrategy.values()) {
                        Data data = new Data(
                            timeData.get(strategy),
                            successData.get(strategy)
                        );
                        dataMap.put(strategy, data);
                    }
                    allResults.put(key, dataMap);
                });
            }
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        System.out.println("\nOverall Performance Comparison Table:");
        System.out.printf("%-15s %-15s %-15s %-15s%n", "Dimension", "Strategy", "Avg Time (ms)", "Success Rate");

        for (Map.Entry<String, Map<CellSelectionStrategy, Data>> entry : allResults.entrySet()) {
            String key = entry.getKey();
            Map<CellSelectionStrategy, Data> dataMap = entry.getValue();

            for (CellSelectionStrategy strategy : CellSelectionStrategy.values()) {
                Data data = dataMap.get(strategy);
                double avgTime = data.timeData.stream().mapToDouble(d -> d).average().orElse(0);
                double successRate = data.successData.stream().mapToDouble(d -> d).average().orElse(0);
                System.out.printf("%-15s %-15s %-15.2f %-15.2f%%%n", key, strategy.name(), avgTime, successRate * 100);
            }
        }
    }
}