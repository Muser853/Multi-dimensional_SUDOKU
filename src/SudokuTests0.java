/*
file name:      SudokuTests.java
Authors:        Max Bender and Ike Lage
last modified:  04/01/2025

How to run:     java -ea SudokuTests
*/

public class SudokuTests0 {

    /**
    * Stack Tests
    */
    public static double sudokuTests() {

        double testScore = 0. ;
        /**
            * Solve Blank Board
        */
        try {
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    Sudoku game = new Sudoku( 0 , 0 ) ;
                    try {
                        game.solve();
                    } catch (InterruptedException e) {e.printStackTrace();}
                }
            });
            thread.start();
            boolean result = thread.join(java.time.Duration.ofMillis(10000));
            if (result) testScore += 2;
        } catch (Exception E) {
            System.out.println( "Exception when solving blank board" );
        }

        /**
        * Solve a Board with a small number of elements
        */
        try {
            Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Sudoku game = new Sudoku( 5 , 0 ) ;
                try {
                    game.solve();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                }
            });
        thread.start();

        boolean result = thread.join(java.time.Duration.ofMillis(10000));

        if (result)
            testScore += 2;
        } catch (Exception E) {
            System.out.println( "Exception when solving blank board" );
        }

        /**
        * Solve a Board with a more elements
        */
        try {
            Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Sudoku game = new Sudoku( 40 , 0 ) ;
                try {
                    game.solve();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                }

            });
        thread.start();

        boolean result = thread.join(java.time.Duration.ofMillis(10000));

        if (result)
            testScore += 2;
        } catch (Exception E) {
            System.out.println( "Exception when solving blank board" );
        }

        return testScore ;
    }

    public static void main(String[] args) {
        System.out.println( sudokuTests() + "/6" );
    }

}