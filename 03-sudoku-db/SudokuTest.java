import junit.framework.TestCase;
import java.util.*;
public class SudokuTest extends TestCase {

    public void testToString() {
        try {
            new Sudoku("");
            fail();
        } catch (RuntimeException e) {}

        try {
            new Sudoku(new int[2][2]);
            fail();
        } catch (IllegalArgumentException e) {}

        Sudoku m = new Sudoku(Sudoku.mediumGrid);
        String expected = "5 3 0 0 7 0 0 0 0" +
                        "\n6 0 0 1 9 5 0 0 0" +
                        "\n0 9 8 0 0 0 0 6 0" +
                        "\n8 0 0 0 6 0 0 0 3" +
                        "\n4 0 0 8 0 3 0 0 1" +
                        "\n7 0 0 0 2 0 0 0 6" +
                        "\n0 6 0 0 0 0 2 8 0" +
                        "\n0 0 0 4 1 9 0 0 5" +
                        "\n0 0 0 0 8 0 0 7 9";
        assertEquals(expected, m.toString());

        Sudoku h = new Sudoku(Sudoku.hardGrid);
        String expected1 = "3 7 0 0 0 0 0 8 0\n" +
                "0 0 1 0 9 3 0 0 0\n" +
                "0 4 0 7 8 0 0 0 3\n" +
                "0 9 3 8 0 0 0 1 2\n" +
                "0 0 0 0 4 0 0 0 0\n" +
                "5 2 0 0 0 6 7 9 0\n" +
                "6 0 0 0 2 1 0 4 0\n" +
                "0 0 0 5 3 0 9 0 0\n" +
                "0 3 0 0 0 0 0 5 1";
        assertEquals(expected1, h.toString());

    }

    public void testSolve() {
        Sudoku e = new Sudoku(Sudoku.easyGrid);
        assertEquals(1, e.solve());
        String soln1 = "1 6 4 7 9 5 3 8 2\n" +
                "2 8 7 4 6 3 9 1 5\n" +
                "9 3 5 2 8 1 4 6 7\n" +
                "3 9 1 8 7 6 5 2 4\n" +
                "5 4 6 1 3 2 7 9 8\n" +
                "7 2 8 9 5 4 1 3 6\n" +
                "8 1 9 6 4 7 2 5 3\n" +
                "6 7 3 5 2 9 8 4 1\n" +
                "4 5 2 3 1 8 6 7 9";
        assertEquals(soln1, e.getSolutionText());
        assertEquals(new Sudoku(Sudoku.easyGrid).toString(), e.toString()); // check that solving didnt change original grid

        Sudoku m = new Sudoku(Sudoku.mediumGrid);
        assertEquals(1, m.solve());
        String soln2 = "5 3 4 6 7 8 9 1 2\n" +
                "6 7 2 1 9 5 3 4 8\n" +
                "1 9 8 3 4 2 5 6 7\n" +
                "8 5 9 7 6 1 4 2 3\n" +
                "4 2 6 8 5 3 7 9 1\n" +
                "7 1 3 9 2 4 8 5 6\n" +
                "9 6 1 5 3 7 2 8 4\n" +
                "2 8 7 4 1 9 6 3 5\n" +
                "3 4 5 2 8 6 1 7 9";
        assertEquals(soln2, m.getSolutionText());

        Sudoku h = new Sudoku(Sudoku.hardGrid);
        assertEquals(1, h.solve());
        String soln3 = "3 7 5 1 6 2 4 8 9\n" +
                "8 6 1 4 9 3 5 2 7\n" +
                "2 4 9 7 8 5 1 6 3\n" +
                "4 9 3 8 5 7 6 1 2\n" +
                "7 1 6 2 4 9 8 3 5\n" +
                "5 2 8 3 1 6 7 9 4\n" +
                "6 5 7 9 2 1 3 4 8\n" +
                "1 8 2 5 3 4 9 7 6\n" +
                "9 3 4 6 7 8 2 5 1";

        assertEquals(soln3, h.getSolutionText());
        assertEquals(new Sudoku(Sudoku.hardGrid).toString(), h.toString());

        String extremeSudoku = "";
        for (int i = 0; i < 81; i++) {
            extremeSudoku += "0";
            if (i < 80) extremeSudoku += " ";
        }

        Sudoku extreme = new Sudoku(extremeSudoku);
        assertEquals(100, extreme.solve());
    }

    public void testSpotGetPossibleValues() {
        Sudoku m = new Sudoku(Sudoku.mediumGrid);
        Sudoku.Spot s = m.new Spot(0, 2, 0);
        assertEquals(s.getPossibleValues(), new HashSet<>(Arrays.asList(1, 2, 4)));

        Sudoku.Spot s1 = m.new Spot(4, 6, 0);
        assertEquals(s1.getPossibleValues(), new HashSet<>(Arrays.asList(5, 7, 9)));

        try {
            Sudoku.Spot s3 = m.new Spot(-1, 2, 0);
            fail();
        } catch (IllegalArgumentException e) {}

        Sudoku.Spot s4 = m.new Spot(1, 7, 0);
        assertEquals(s4.getPossibleValues(), new HashSet<>(Arrays.asList(2, 3, 4)));

    }

    public void testTimeElapsed() {
        Sudoku h = new Sudoku(Sudoku.hardGrid);
        try {
            h.getElapsed();
            fail();
        } catch (RuntimeException e) {};

        // doesn't make sense to test time elapsed any other way
        // not sure what the deviation should be either
        long deviation = 10;
        for (int i = 0; i < 1000; i++) {
            long t0 = System.currentTimeMillis();
            h.solve();
            long t1 = System.currentTimeMillis();
            assertTrue(Math.abs(t1 - t0 - h.getElapsed()) <= deviation);
        }
    }

    public void testSpotGetSet() {
        Sudoku m = new Sudoku(Sudoku.mediumGrid);
        Sudoku.Spot s = m.new Spot(0, 0);
        try {
            s.setValue(10);
            fail();
        } catch (IllegalArgumentException e) {}

        assertEquals(5, s.getValue());
        s.setValue(1);
        assertEquals(1, s.getValue());
        assertEquals(9, m.new Spot(2, 1).getValue());
        assertEquals(0,  m.new Spot(3, 1).getValue());
    }
}