# Sudoku Solver Project

## Overview
This project is a Java-based implementation of a Sudoku solver. It includes a `Board` class to represent the Sudoku puzzle, a `Sudoku` class to handle the solving logic, and a `LandscapeDisplay` class to visualize the puzzle. The solver uses a backtracking algorithm to find a valid solution to the puzzle. This project explores the problem of solving Sudoku puzzles programmatically, a real-world challenge that involves constraint satisfaction and backtracking. The goal was to improve the efficiency of Sudoku solvers by implementing and comparing different cell selection strategies, such as selecting cells with the least viable options (MIN_CANDIDATES). By simulating various scenarios, including varying numbers of initial locked cells and board sizes, we analyzed the relationship between initial constraints and solver performance. Core computer science concepts like backtracking, algorithm optimization, and complexity analysis were applied to better understand the trade-offs between speed and success rates. Key findings include the superior performance of the MIN_CANDIDATES strategy and the diminishing success rate as the number of locked cells increases.

Results
1.Baseline Experiment: Implemented a basic findNextCell method that selects the first available empty cell.
2.Improved Strategy: Developed a findNextCell method based on the MIN_CANDIDATES strategy, which selects the cell with the fewest viable options.
3.Simulation Study: Ran simulations to explore the relationship between the number of initial locked cells and:
1.Success rate of solving the board.
2.Time taken to solve the board.
4.Generalization: Extended the solver to handle boards of varying sizes (e.g., 4x4, 9x9, 16x16) and analyzed performance metrics.
Metrics Reported
Success Rate: Percentage of boards successfully solved for a given number of initial locked cells.
Timeout Rate: Percentage of boards where the solver failed to find a solution within a specified time limit.
Time Taken: Average time to solve boards for each configuration.
Key Results
Table 1: Success Rate and Timeout Analysis
Initial Locked Cells	Success Rate (%)	Timeout Rate (%)
0	100	0
5	100	0
10	100	0
15	99.5	0
20	93.6	0
25	56.8	0
30	5.5	0
35	0	0
Table 2: Performance of MIN_CANDIDATES Strategy
Num Locked	Avg Time (ms)	Success Rate (%)
0	63	100
5	72	100
10	60	100
15	63	99
20	82	99
25	58	60
30	7	6
Observations
Success Rate Decline: As the number of locked cells increases, the success rate drops sharply after a certain threshold (around 20-25 locked cells).
The MIN_CANDIDATES strategy significantly outperforms the baseline approach, especially for boards with moderate constraints.
Larger boards (e.g., 16x16) take exponentially longer to solve, highlighting the importance of efficient algorithms for generalized Sudoku solvers.

## Project Structure
- **`Board.java`**: Represents the Sudoku board and includes methods to manipulate and validate the board.
- **`Cell.java`**: Represents a single cell in the Sudoku board, containing its value, position, and lock status.
- **`Sudoku.java`**: Contains the main logic for solving the Sudoku puzzle using a backtracking algorithm.
- **`LandscapeDisplay.java`**: Provides a graphical interface to visualize the Sudoku board.
- **`LinkedList.java`**: Implements a linked list data structure used as a stack in the backtracking algorithm.
- **`Stack.java`**: Defines the interface for the stack data structure.

## How to Run
1. **Compile the project**:
   ```bash
   javac *.java
   ```

2. **Run the Sudoku solver**:
   ```bash
   java Sudoku
   ```

3. **Visualize the Sudoku board**:
   - The `LandscapeDisplay` class can be used to visualize the board. Ensure that the `LandscapeDisplay` is instantiated in the `Sudoku` class to see the graphical representation.

## Time and Space Complexity Analysis

### Time Complexity
- **Backtracking Algorithm**: The time complexity of the backtracking algorithm used in this project is **O(9^(n^2))**, where `n` is the size of the board (typically 9 for a standard Sudoku puzzle). This is because, in the worst case, the algorithm may need to try all possible values (1-9) for each cell in the board.
  
- **Valid Value Check**: The `validValue` method checks if a value can be placed in a specific cell. This method has a time complexity of **O(n^2)** because it checks the row, column, and 3x3 subgrid for conflicts.

### Space Complexity
- **Board Representation**: The space complexity for storing the Sudoku board is **O(n^2)**, where `n` is the size of the board. This is because the board is represented as a 3D array of `Cell` objects.

- **Stack Usage**: The backtracking algorithm uses a stack to keep track of the cells being processed. In the worst case, the stack can grow to **O(n^2)** in size, where `n` is the size of the board.

## Features
- **Backtracking Solver**: The solver uses a backtracking algorithm to find a valid solution to the Sudoku puzzle.
- **Graphical Visualization**: The `LandscapeDisplay` class provides a graphical interface to visualize the Sudoku board.
- **Dynamic Board Initialization**: The `Board` class allows for dynamic initialization of the board with a specified number of locked cells.

## Limitations
- **Performance**: The backtracking algorithm may take a long time to solve puzzles with a large number of empty cells.
- **Graphical Interface**: The graphical interface is basic and may not support advanced features like user input or real-time updates.

  # Sudoku Solver Extension

## Overview
This project extends the classic Sudoku solver to handle multi-dimensional Sudoku puzzles. The solver supports various strategies for selecting the next cell to fill, including **Minimum Candidates**, **Random**, and **Maximum Constraints**. The project also includes a visualization component that generates performance charts for different strategies.

## Features
- **Multi-dimensional Sudoku**: Supports Sudoku puzzles with dimensions ranging from 2D to 5D.
- **Cell Selection Strategies**:
  - **Minimum Candidates**: Selects the cell with the fewest possible valid values.
  - **Random**: Randomly selects a cell from the available candidates.
  - **Maximum Constraints**: Selects the cell that is most constrained by its neighbors.
- **Performance Visualization**: Generates line charts to compare the time and success rate of different strategies across various puzzle configurations.

## Code Structure
- **BoardConfig.java**: Defines the configuration of the Sudoku board, including dimensions and block size.
- **Board.java**: Implements the Sudoku board, including cell management, constraints, and solving logic.
- **Cell.java**: Represents a single cell in the Sudoku board, including its coordinates, value, and locked status.
- **Sudoku.java**: The main class that orchestrates the solving process and generates performance charts.
- **LineChart.java**: A utility class for generating line charts to visualize performance data.
- **LinkedList.java**: A custom implementation of a linked list that supports both stack and queue operations.

Alternative Strategies: Implemented additional cell selection strategies, such as RANDOM and MAX_CONSTRAINTS, to compare their performance against MIN_CANDIDATES.

Generalized Solver: Extended the solver to handle boards of arbitrary size (e.g., 4x4, 16x16) by parameterizing the board dimensions.

Visualization: Generated line charts to visualize the relationship between the number of locked cells and solver performance metrics.

Hypothesis
I hypothesize that the MIN_CANDIDATES should perform better than RANDOM and MAX_CONSTRAINTS in terms of both success rate and solving time. It takes too long to finish solving sudokus beyond 2D.

Generalized solvers revealed that performance degrades exponentially with increasing board size, emphasizing the need for heuristic-based optimizations.

More Cell Selection Strategies

The baseline strategy selects the first available empty cell in the board by iterating through the 1D array of cells to find the first cell with a value of 0:
protected int[] findNextCell() {
for (int i = 0; i < totalCells; i++) { 
       if (cells[i].getValue() == 0) {
            return coordinatesFromIndex(i);
        }
    }
    return null; // No empty cell found}

It’s simple but inefficient for larger boards, as it does not prioritize constrained cells.

The MIN_CANDIDATES strategy selects the cell with the fewest viable options based on the bitmask (availableValues) by iterating through all empty cells and calculates the number of viable candidates using the bitmask:

protected int[] findNextCellMinCandidates() {
int minCandidates = Integer.MAX_VALUE;
int[] bestCell = null; 
   for (int i = 0; i < totalCells; i++) {
        if (cells[i].getValue() == 0) { // Check if the cell is empty 
           int candidateCount = Integer.bitCount(availableValues[i]);
            if (candidateCount < minCandidates) {
                minCandidates = candidateCount;
                bestCell = coordinatesFromIndex(i);
            }
        }
}
return bestCell;
}
It reduces the search space significantly by prioritizing highly constrained cells, leading to faster convergence.
Alternative Cell Selection Strategies
Random Selection (selectRandom)
To randomly selects an empty cell from the board, it iterates through all cells to collect indices of empty cells (value == 0). Uses a random number generator (rand.nextInt) to select one of the empty cells. It’s imple and unbiased, but it does not prioritize constrained cells, leading to potentially inefficient solving.

Maximum Constraints Selection (selectMaxConstraints)
Selects the cell with the most constraints (fewest remaining options).
Calculates the number of constraints for each empty cell as sideLength - Integer.bitCount(availableValues[i]).

Integer.bitCount(availableValues[i]) counts the number of 1s in the bitmask, representing available values. Subtracting this from sideLength gives the number of unavailable values (constraints). Selects the cell with the highest constraint count. Prioritizes highly constrained cells, reducing the search space. It may not always lead to the fastest solution compared to MIN_CANDIDATES.

Board class uses bitmasking to efficiently track and manipulate available values for each cell. Below is a detailed discussion of the methods that utilize bitmasking and how they work:

a. Initialization of Available Values
availableValues = new int[totalCells];
Arrays.fill(availableValues, (1 << sideLength) - 1);
In order to initializes the availableValues array, where each element represents the possible values for a specific cell using a bitmask, (1 << sideLength) - 1  creates a bitmask with all bits set to 1 up to the sideLength; e.g., if sideLength = 9, the bitmask is 0b111111111 (binary representation of 511).

Each bit corresponds to a possible value for the cell. A 1 indicates the value is available, while a 0 indicates it is unavailable.

b. Updating Available Values
 In order to update the bitmask to reflect constraints when a value is assigned to a cell.
~(1 << (value - 1)) creates a bitmask with a 0 at the position corresponding to the assigned value. For example, if value = 3, the bitmask becomes ~(1 << 2) = ~0b100 = 0b111111011.
The bitwise AND operation (&=) clears the bit corresponding to the assigned value in the bitmask for the current cell, its row/column, and its block, which ensures the same value cannot be assigned to conflicting cells.

c. Validating a Value
In order to check if assigning a value to a cell violates Sudoku constraints.
availableValues[index] & (1 << (value - 1))   checks if the bit corresponding to the value is still set in the bitmask for the cell. If not, the value is invalid.
Additional checks ensure the value does not conflict with existing values in the same row, column, or block.

Bitmasking provides an efficient way to track and manipulate available values in Sudoku solvers. The MIN_CANDIDATES strategy leverages this technique to prioritize cells with fewer options, leading to faster convergence. Alternative strategies like RANDOM and MAX_CONSTRAINTS offer different trade-offs but are generally less effective than MIN_CANDIDATES. Understanding these methods and their implementations highlights the importance of heuristic-based optimizations in solving constraint satisfaction problems.

In order to get the performance Metrics, the success rate and timeout rate are calculated during simulations, and the average solving time is computed across successful trials. In order to visualizes performance metrics, I created the LineChart class with multiple line charts showing the relationship between the number of locked cells and average solving time.

For scalability and Generalization, The BoardConfig class supports arbitrary board sizes:
public BoardConfig(int dimensionCount, int blockLength) {
this.dimensionCount = dimensionCount;
    this.sideLength = (int) Math.pow(blockLength, dimensionCount);
    this.blockLength = blockLength;
    dimensions = new int[dimensionCount];
    Arrays.fill(dimensions, sideLength);}

Example Configurations:
new BoardConfig(2, 3) creates a 9x9 Sudoku board.
new BoardConfig(4, 2) creates a 16x16x16x16 board.

## Time and Space Complexity Analysis

### Time Complexity
- **Board Initialization**: 
  - **Precompute Blocks**: O(n^k), where `n` is the side length and `k` is the number of dimensions.
  - **Initialize Cells**: O(n^k), as each cell is initialized individually.
- **Solving the Puzzle**:
  - **Find Next Cell**: O(n^k) in the worst case, as it may need to scan all cells.
  - **Find Next Value**: O(n), as it checks each possible value for a cell.
  - **Update Available Values**: O(n^k), as it updates constraints for all cells in the same row, column, and block.
  - Larger boards exhibit exponential growth in complexity. For example, solving a 16x16 board requires significantly more computational resources due to the increased number of cells and constraints.

For a board with N dimensions, each of size sideLength = L, resulting in a total of L^N cells.
Component	Time Complexity	Space Complexity
Bitmask Initialization	O(L^N)	O(L^N)
Bitmask Updates	O(L * N + B)	O(1)
Cell Selection	O(L^N)	O(1) or O(E)
Solution based on stack	O( (L!)^(L^(N-1)) )	O(L^N)
This explains why we couldn’t get high-dimensional sudokus solved in PC in a reasonable amount of time even with multi-threading for parallel processing different boards (for drawing the line charts).
  - **Overall Solving Time**: The worst-case time complexity is O((n^k)!), as the solver may need to backtrack through all possible configurations.

### Space Complexity
- **Board Storage**: O(n^k), as the board stores all cells and their constraints.
- **Stack for Backtracking**: O(n^k), as the stack may need to store all cells in the worst case.
- **Performance Data Storage**: O(m * s), where `m` is the number of strategies and `s` is the number of samples.

## Usage
1. **Compile the Project**:
   ```bash
   javac *.java
   ```

2. **Run the Solver**:
   ```bash
   java Sudoku
   ```

3. **View Performance Charts**:
   - The solver generates PNG files for time and success rate charts in the project directory.

## Example Output
The solver will print example solutions for 2D to 5D Sudoku puzzles and generate performance charts comparing different strategies.

## Dependencies
- **Java Swing**: Used for generating the line charts.
- **Java AWT**: Used for rendering the charts.

## Contributing
Contributions are welcome! Please open an issue or submit a pull request for any improvements or bug fixes. If your believe there's an issue within the code, please contact me at txiao28@colby.edu or xtp20210316@163.com

## Future Improvements
- **Optimization**: Implement more advanced algorithms (e.g., constraint propagation) to improve the solver's performance.
- **User Interaction**: Enhance the graphical interface to allow users to input their own puzzles and see real-time updates as the solver works.
- **Error Handling**: Add more robust error handling and validation to ensure the solver works correctly with various input formats.
