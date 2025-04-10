/*
file name:      SudokuTests.java
Authors:        Max Bender and Ike Lage
last modified:  04/01/2025

How to run:     java -ea SudokuTests
*/

public class SudokuTests {

    /**
    * Stack Tests
    */
    public static double sudokuTests() throws InterruptedException{
        double testScore = 0.0;

        /**
         * Test 1: Solve Blank Board
         */
        Sudoku blankBoardTest = new Sudoku(0, 0);
        if (blankBoardTest.solve()) {
            testScore += 2;
        } else {
            System.out.println("Blank board not solving");
        }

        /**
         * Test 2: Solve a Board with a small number of elements
         */
        Sudoku fiveElementTest = new Sudoku(5, 0);
        if (fiveElementTest.solve()) {
            testScore += 2;
        } else {
            System.out.println("5-element board not solving");
        }

        /**
         * Test 3: Solve a Board with a moderate number of elements
         */
        Sudoku fortyElementTest = new Sudoku(40, 0);
        if (fortyElementTest.solve()) {
            testScore += 2;
        } else {
            System.out.println("40-element board not solving");
        }
        return testScore;
    }

    public static void main(String[] args) throws InterruptedException{
        System.out.println(sudokuTests() + "/6");
    }
}