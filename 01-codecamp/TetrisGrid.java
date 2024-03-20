//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.

import java.util.Stack;

public class TetrisGrid {
	private boolean[][] grid;
	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid = grid;
	}
	
	
	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		if (grid.length == 0) return;

		int[] shiftDown = new int[grid[0].length];

		for (int y = 0; y < grid[0].length; y++) {
			boolean full = true;
			for (int x = 0; x < grid.length; x++)
				full = full && grid[x][y];
			if (full) {
				for (int x = 0; x < grid.length; x++)
					grid[x][y] = false;
				for (int y1 = y + 1; y1 < shiftDown.length; y1++)
					shiftDown[y1]++;
			}
		}

		for (int y = 1; y < shiftDown.length; y++) {
			if (shiftDown[y] == 0)
				continue;
			for (int x = 0; x < grid.length; x++) {
				grid[x][y - shiftDown[y]] = grid[x][y];
				grid[x][y] = false;
			}
		}
	}
	
	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return grid;
	}
}
