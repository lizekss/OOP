// Board.java

import java.util.Arrays;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;
	private int[] widths;
	private int[] heights;
	private int maxHeight;
	private boolean[][] backupGrid;
	private int[] backupWidths;
	private int[] backupHeights;
	private int backupMaxHeight;

	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		committed = true;
		widths = new int[height];
		heights = new int[width];
		maxHeight = 0;
		backupGrid =  new boolean[width][height];
		backupHeights = new int[width];
		backupWidths = new int[height];
		backupMaxHeight = 0;

		// YOUR CODE HERE
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		return maxHeight;
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			// widths, heights, maxheight
			int[] actualWidths = new int[height];
			int[] actualHeights = new int[width];
			int actualMaxHeight = 0;
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (grid[x][y]) {
						actualWidths[y]++;
						actualHeights[x] = y + 1;
					}
				}
				actualMaxHeight = Math.max(actualMaxHeight, actualHeights[x]);
			}
			if (!Arrays.equals(widths, actualWidths))
				throw new RuntimeException("Width array is not correct");
			if (!Arrays.equals(heights, actualHeights))
				throw new RuntimeException("Height array is not correct");
			if (maxHeight != actualMaxHeight)
				throw new RuntimeException("Max height is not correct");

		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	// out of bounds?
	public int dropHeight(Piece piece, int x) {
		if (x >= width)
			throw new RuntimeException("Piece goes out of bounds");
		int maxDiff = 0;
		int collisionIdx = 0;
		int[] skirt = piece.getSkirt();
		for (int i = 0; i < skirt.length; i++) {
			if (heights[x + i] - skirt[i] > maxDiff) {
				maxDiff = heights[x + i] - skirt[i];
				collisionIdx = i;
			}
		}
		return maxDiff;
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x];
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return widths[y];
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return true;
		return grid[x][y];
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");
		committed = false;
		backUpData();
		int result = PLACE_OK;
		//if (x < 0 || x >= width || y < 0 || y >= height)
		//	return PLACE_OUT_BOUNDS;

		TPoint[] body = piece.getBody();
		for (int i = 0; i < body.length; i++) {
			int grid_x = x + body[i].x;
			int grid_y = y + body[i].y;
			if (grid_x < 0 || grid_x >= width || grid_y < 0 || grid_y >= height)
				return PLACE_OUT_BOUNDS;
			if (grid[grid_x][grid_y] == true)
				return PLACE_BAD;
			grid[grid_x][grid_y] = true;
			widths[grid_y]++;
			heights[grid_x] = Math.max(heights[grid_x], grid_y + 1);
			maxHeight = Math.max(maxHeight, heights[grid_x]);
			if (widths[grid_y] == width)
				result = PLACE_ROW_FILLED;
		}
		sanityCheck();
		return result;
	}

	private void backUpData() {
		System.arraycopy(widths, 0, backupWidths, 0, widths.length);
		System.arraycopy(heights, 0, backupHeights, 0, heights.length);
		for (int i = 0; i < grid.length; i++)
			System.arraycopy(grid[i], 0, backupGrid[i], 0, grid[i].length);
		backupMaxHeight = maxHeight;
	}


	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		if (committed)
			backUpData();
		committed = false;
		int maxHeightBefore = maxHeight;
		int to = 0;
		while (to < height && widths[to] != width)
			to++;

		int from = to + 1;
		while (to < maxHeightBefore) {
			while (from < height && widths[from] == width)
				from++;
			shiftRow(from, to);
			from++; to++;
		}
		updateHeights();
		sanityCheck();
		return (maxHeightBefore - maxHeight); // wrong
	}

	private void updateHeights() {
		int max = 0;
		for (int x = 0; x < heights.length; x++) {
			heights[x] = 0;
			for (int y = 0; y < height; y++) {
				if (grid[x][y])
					heights[x] = y + 1;
			}
			max = Math.max(heights[x], max);
		}
		maxHeight = max;
	}

	private void shiftRow(int from, int to) {
		//int maxH = 0;
		if (from >= maxHeight) {
			widths[to] = 0;
			for (int x = 0; x < width; x++)
				grid[x][to] = false;
		} else {
			widths[to] = widths[from];
			widths[from] = 0;
			for (int x = 0; x < width; x++) {
				grid[x][to] = grid[x][from];
				grid[x][from] = false;
			}
		}
	}


	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if (committed)
			return;
		swapGridsForUndo();
		swapWidthsForUndo();
		swapHeightsForUndo();
		swapMaxHeightsForUndo();
		sanityCheck();
		committed = true;
	}

	private void swapMaxHeightsForUndo() {
		int tmp = maxHeight;
		maxHeight = backupMaxHeight;
		backupMaxHeight = tmp;
	}

	private void swapHeightsForUndo() {
		int[] tmp = heights;
		heights = backupHeights;
		backupHeights = tmp;
	}

	private void swapWidthsForUndo() {
		int[] tmp = widths;
		widths = backupWidths;
		backupWidths = tmp;
	}

	private void swapGridsForUndo() {
		boolean[][] tmp = grid;
		grid = backupGrid;
		backupGrid = tmp;
	}


	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


