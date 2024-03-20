// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

import java.util.HashMap;

public class CharGrid {
	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		int r0 = Integer.MAX_VALUE, r1 = -1, c0 = Integer.MAX_VALUE, c1 = -1;

		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[0].length; c++) {
				if (grid[r][c] == ch) {
					r0 = Math.min(r, r0);
					r1 = Math.max(r, r1);
					c0 = Math.min(c, c0);
					c1 = Math.max(c, c1);
				}
			}
		}

		int result = 0;
		if (r1 >= 0 && c1 >= 0) {
			result = (c1 - c0 + 1) * (r1 - r0 + 1);
		}
		return result;
	}

	enum Direction {TOP, BOTTOM, LEFT, RIGHT};
	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
		int result = 0;
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[0].length; c++) {
				if (isPlusCenter(r, c))
					result++;
			}
		}
		return result;
	}

	private boolean isPlusCenter(int r, int c) {
		char ch = grid[r][c];
		int legLen = countLeg(Direction.TOP, r, c, ch);
		if (legLen >= 2 && countLeg(Direction.BOTTOM, r, c, ch) == legLen &&
				countLeg(Direction.LEFT, r, c, ch) == legLen &&
				countLeg(Direction.RIGHT, r, c, ch) == legLen) {
			return true;
		}
		return false;
	}

	private int countLeg(Direction d, int r, int c, char ch) {
		int dr = 0, dc = 0;
		switch (d) {
			case TOP: {
				dr = -1;
				break;
			}
			case BOTTOM: {
				dr = 1;
				break;
			}
			case LEFT: {
				dc = -1;
				break;
			}
			case RIGHT: {
				dc = 1;
				break;
			}
		}

		int result = 0;
		while (r >= 0 && r < grid.length && c >= 0 && c < grid[0].length && grid[r][c] == ch) {
			result++;
			r += dr;
			c += dc;
		}
		return result;
	}
	
}
