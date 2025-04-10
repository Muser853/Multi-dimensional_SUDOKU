# Sudoku Solver Project

## Overview
This project is a Java-based implementation of a Sudoku solver. It includes a `Board` class to represent the Sudoku puzzle, a `Sudoku` class to handle the solving logic, and a `LandscapeDisplay` class to visualize the puzzle. The solver uses a backtracking algorithm to find a valid solution to the puzzle.

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

## Time and Space Complexity Analysis

### Time Complexity
- **Board Initialization**: 
  - **Precompute Blocks**: O(n^k), where `n` is the side length and `k` is the number of dimensions.
  - **Initialize Cells**: O(n^k), as each cell is initialized individually.
- **Solving the Puzzle**:
  - **Find Next Cell**: O(n^k) in the worst case, as it may need to scan all cells.
  - **Find Next Value**: O(n), as it checks each possible value for a cell.
  - **Update Available Values**: O(n^k), as it updates constraints for all cells in the same row, column, and block.
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
