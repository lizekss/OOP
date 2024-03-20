
// Test cases for CharGrid -- a few basic tests are provided.

import junit.framework.TestCase;

public class CharGridTest extends TestCase {
	
	public void testCharArea1() {
		char[][] grid = new char[][] {
				{'a', 'y', ' '},
				{'x', 'a', 'z'},
			};
		
		
		CharGrid cg = new CharGrid(grid);
				
		assertEquals(4, cg.charArea('a'));
		assertEquals(1, cg.charArea('z'));
	}
	
	
	public void testCharArea2() {
		char[][] grid = new char[][] {
				{'c', 'a', ' '},
				{'b', ' ', 'b'},
				{' ', ' ', 'a'}
			};
		
		CharGrid cg = new CharGrid(grid);
		
		assertEquals(6, cg.charArea('a'));
		assertEquals(3, cg.charArea('b'));
		assertEquals(1, cg.charArea('c'));
	}

	public void testCharArea3() {
		char[][] grid = new char[][] {
				{'c', 'g', ' '},
				{'e', 'f', 'b'},
				{' ', 'g', 'a'}
		};

		CharGrid cg = new CharGrid(grid);
		// char not present
		assertEquals(0, cg.charArea('1'));

		// char present once
		assertEquals(1, cg.charArea('a'));

		// single column
		assertEquals(3, cg.charArea('g'));

		// entire grid
		assertEquals(9, cg.charArea(' '));

		grid = new char[][] {
				{'a', 'a', 'a'},
				{'a', 'a', 'a'},
				{'a', 'a', 'a'}
		};

		// every char the same
		assertEquals(9, cg.charArea('a'));
	}

	public void testCharArea4() {
		char[][] grid = new char[][] {};

		CharGrid cg = new CharGrid(grid);
		// empty
		assertEquals(0, cg.charArea('1'));

		grid = new char[][] {
				{' ', 'b', 'a'},
				{' ', 'a', 'b'},
				{'b', ' ', 'a'}
		};

		cg = new CharGrid(grid);
		// columns not aligned
		assertEquals(6, cg.charArea('a'));

		// rows not aligned
		assertEquals(9, cg.charArea('b'));

		assertEquals(6, cg.charArea(' '));
	}

	public void testCountPlus1() {
		char[][] grid = new char[][] {
				{' ', '*', ' '},
				{'*', '*', '*'},
				{' ', '*', ' '}
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(1, cg.countPlus());

		grid[1][0] = ' ';
		assertEquals(0, cg.countPlus());
	}

	public void testCountPlus2() {
		char[][] grid = new char[][] {
				{' ', ' ', 'p', ' ', ' ', ' ', ' ', ' ', ' '},
				{' ', ' ', 'p', ' ', ' ', ' ', ' ', 'x', ' '},
				{'p', 'p', 'p', 'p', 'p', ' ', 'x', 'x', 'x'},
				{' ', ' ', 'p', ' ', ' ', 'y', ' ', 'x', ' '},
				{' ', ' ', 'p', ' ', 'y', 'y', 'y', ' ', ' '},
				{'z', 'z', 'z', 'z', 'z', 'y', 'z', 'z', 'z'},
				{' ', ' ', 'x', 'x', ' ', 'y', ' ', ' ', ' '}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(2, cg.countPlus());

		grid[5][2] = 'p';
		assertEquals(1, cg.countPlus());

		grid[6][5] = ' ';
		assertEquals(2, cg.countPlus());

		grid[1][3] = '-';
		assertEquals(3, cg.countPlus());
	}

	// empty grid
	public void testCountPlus3() {
		char[][] grid = new char[][] {};
		CharGrid cg = new CharGrid(grid);
		assertEquals(0, cg.countPlus());
	}

	// overlapping
	public void testCountPlus4() {
		char[][] grid = new char[][] {
				{' ', '*', ' '},
				{'*', '*', '*'},
				{'*', '*', '*'},
				{' ', '*', ' '}
		};

		CharGrid cg = new CharGrid(grid);

		assertEquals(0, cg.countPlus());

	}
}
