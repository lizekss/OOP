import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	class Spot implements Comparable<Spot> {
		private int x;
		private int y;
		private boolean filled;

		public Spot(int x, int y, int value) {
			if (x < 0 || x >= SIZE || y < 0 || y >= SIZE)
				throw new IllegalArgumentException("Coordinates out of bounds");
			this.x = x;
			this.y = y;
			setValue(value);
			filled = value > 0;
		}

		public Spot(int x, int y) {
			this(x, y, grid[x][y]);
		}

		public int getX() { return x; }
		public int getY() { return y; }
		public boolean isFilled() { return filled; }
		public int getValue() { return grid[x][y]; }

		public void setValue(int val) {
			if (val < 0 || val > SIZE)
				throw new IllegalArgumentException("Value needs to be between 0-" + SIZE);
			grid[x][y] = val;
		}

		public HashSet<Integer> getPossibleValues() {
			HashSet<Integer> result = new HashSet<>();
			for (int n = 1; n <= SIZE; n++) {
				if (isPossible(x, y, n))
					result.add(n);
			}
			return result;
		}

		private boolean isPossible(int x, int y, int val) {
			return notInRow(x, y, val) && notInCol(x, y, val) && notInSquare(x, y, val);
		}

		private boolean notInSquare(int x, int y, int val) {
			int x0 = x - (x % PART);
			int y0 = y - (y % PART);
			for (int i = 0; i < PART; i++) {
				for (int j = 0; j < PART; j++) {
					if (i == 0 && j == 0) continue;
					if (grid[x0 + i][y0 + j] == val)
						return false;
				}
			}
			return true;
		}

		private boolean notInCol(int x, int y, int val) {
			for (int i = 0; i < SIZE; i++) {
				if (i == x) continue;
				if (grid[i][y] == val)
					return false;
			}
			return true;
		}

		private boolean notInRow(int x, int y, int val) {
			for (int i = 0; i < SIZE; i++) {
				if (i == y) continue;
				if (grid[x][i] == val)
					return false;
			}
			return true;
		}

		@Override
		// Ordering by initial number of possible values;
		// This ordering is inconsistent with equals().
		public int compareTo(Spot o) {
			int n = getPossibleValues().size();
			int m = o.getPossibleValues().size();

			if (n == m) {
				if (getX() == o.getX())
					return getY() - o.getY();
				else return getX() - o.getX();
			} else return n - m;
		}

	};
	private int[][] grid;
	private String solution;
	private int numSoln;
	private long timeTakenToSolve;
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		if (ints.length != ints[0].length || ints.length != SIZE)
			throw new IllegalArgumentException("Grid is not " + SIZE + " by " + SIZE);

		grid = new int[SIZE][];
		for(int i = 0; i < ints.length; i++)
			grid[i] = ints[i].clone();

		solution = "";
		numSoln = 0;
		timeTakenToSolve = -1;
	}
	
	public Sudoku(String text) {
		this(textToGrid(text));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				sb.append(grid[i][j]);
				if (j < SIZE - 1)
					sb.append(" ");
			}
			if (i < SIZE - 1)
				sb.append('\n');
		}
		return sb.toString();
	}

	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		long t0 = System.currentTimeMillis();
		ArrayList<Spot> sorted = sortSpotsByNumPossible();
		solveSudokuRec(sorted, 0);
		long t1 = System.currentTimeMillis();
		timeTakenToSolve = t1 - t0;
		return numSoln;
	}

	private void solveSudokuRec(ArrayList<Spot> spots, int idx) {
		if (numSoln >= MAX_SOLUTIONS)
			return;

		if (idx >= spots.size()) {
			numSoln++;
			if (solution.isEmpty())
				solution = this.toString();
			return;
		}

		Spot cur = spots.get(idx);
		HashSet<Integer> possibleValues = cur.getPossibleValues();

		for (Integer i : possibleValues) {
			cur.setValue(i);
			solveSudokuRec(spots, idx + 1);
		}
		cur.setValue(0);
	}

	private ArrayList<Spot> sortSpotsByNumPossible() {
		ArrayList<Spot> result = new ArrayList<>();
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				Spot cur = new Spot(i, j, grid[i][j]);
				if (!cur.isFilled())
					result.add(cur);
			}
		}
		Collections.sort(result);
		return result;
	}

	public String getSolutionText() {
		return solution;
	}
	
	public long getElapsed() {
		if (timeTakenToSolve == -1)
			throw new RuntimeException("Sudoku hasn't been solved yet.");

		return timeTakenToSolve;
	}

}
