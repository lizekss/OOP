import junit.framework.TestCase;


public class BoardTest extends TestCase {
	Board b;
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;
	private Piece s2, s2Rotated;
	private Piece stick1, stick2;
	private Piece square;
	private Piece l1, l12, l13, l14;
	private Piece l2, l22, l23, l24;
	private Piece[] pieces;

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	
	protected void setUp() throws Exception {
		b = new Board(3, 6);

		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();

		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();

		s2 = new Piece(Piece.S2_STR);
		s2Rotated = s2.computeNextRotation();

		stick1 = new Piece(Piece.STICK_STR);
		stick2 = stick1.computeNextRotation();

		square = new Piece(Piece.SQUARE_STR);

		l1 = new Piece(Piece.L1_STR);
		l12 = l1.computeNextRotation();
		l13 = l12.computeNextRotation();
		l14 = l13.computeNextRotation();

		l2 = new Piece(Piece.L2_STR);
		l22 = l2.computeNextRotation();
		l23 = l22.computeNextRotation();
		l24 = l23.computeNextRotation();

		pieces = Piece.getPieces();
		
		b.place(pyr1, 0, 0);
	}
	
	// Check the basic width/height/max after the one placement
	public void testSample1() {
		assertEquals(3, b.getWidth());
		assertEquals(6, b.getHeight());
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}
	
	// Place sRotated into the board, then check some measures
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
		assertEquals(true, b.getGrid(-1, -1));
		assertEquals(true, b.getGrid(3, 0));
	}
	
	// Make  more tests, by putting together longer series of
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.
	public void testPlaceReturns() {
		Board b1 = new Board(3, 6);
		assertEquals(Board.PLACE_ROW_FILLED, b1.place(pyr1, 0, 0));
		b1.commit();
		assertEquals(Board.PLACE_BAD, b1.place(square, 0, 0));
		b1.undo();
		assertEquals(Board.PLACE_BAD, b1.place(stick1, 1, 0));
		b1.undo();
		assertEquals(Board.PLACE_OUT_BOUNDS, b1.place(pyr1, 1, 2));
		b1.undo();
		assertEquals(Board.PLACE_OK, b1.place(l1, 0, 2));
	}

	public void testClearRows() {
		Board b1 = new Board(3, 6);
		b1.place(l12, 0, 0);
		b1.commit();
		assertEquals(3, b1.getRowWidth(0));
		assertEquals(1, b1.getRowWidth(1));

		/*
		|   |
		|   |
		|   |
		|   |
		|  +|
		|+++|
		-----
		*/
		assertEquals(1, b1.clearRows());
		b1.commit();

		/*
		 	|   |
			|   |
			|   |
			|   |
			|   |
			|  +|
			-----
		 */
		assertEquals(0, b1.getColumnHeight(0));
		assertEquals(0, b1.getColumnHeight(1));
		assertEquals(1, b1.getColumnHeight(2));
		assertEquals(1, b1.getMaxHeight());
		assertEquals(1, b1.getRowWidth(0));
		assertEquals(0, b1.getRowWidth(1));

		b1.place(pyr4, 1, 0);
		b1.commit();

		/*
		 	|   |
			|   |
			|   |
			| + |
			| ++|
			| ++|
			-----
		 */

		assertEquals(0, b1.clearRows());
		b1.commit();
		assertEquals(0, b1.getColumnHeight(0));
		assertEquals(3, b1.getColumnHeight(1));
		assertEquals(2, b1.getColumnHeight(2));
		assertEquals(3, b1.getMaxHeight());
		assertEquals(2, b1.getRowWidth(0));
		assertEquals(2, b1.getRowWidth(1));
		assertEquals(1, b1.getRowWidth(2));

		b1.place(stick1, 0, 0);
		b1.commit();
		/*
			|   |
			|   |
			|+  |
			|++ |
			|+++|
			|+++|
			-----
	*/
		assertEquals(4, b1.getMaxHeight());
		assertEquals(3, b1.getRowWidth(0));
		assertEquals(3, b1.getRowWidth(1));
		assertEquals(2, b1.getRowWidth(2));
		assertEquals(1, b1.getRowWidth(3));

		assertEquals(2, b1.clearRows());
		assertEquals(2, b1.getColumnHeight(0));
		assertEquals(1, b1.getColumnHeight(1));
		assertEquals(0, b1.getColumnHeight(2));
		assertEquals(2, b1.getMaxHeight());
		assertEquals(2, b1.getRowWidth(0));
		assertEquals(0, b1.getRowWidth(3));

		/*
			|   |
			|   |
			|   |
			|   |
			|+  |
			|++ |
			-----
		*/
		b1.commit();
		b1.place(stick1, 2, 0);
		b1.commit();
		b1.place(square, 0, 2);
		/* System.out.println(b1);
		|   |
		|   |
		|+++|
		|+++|
		|+ +|
		|+++|
		-----
		 */
		b1.clearRows();
		System.out.println(b1);
		assertEquals(true, b1.getGrid(0, 0));
		assertEquals(true, b1.getGrid(2, 0));
		assertEquals(1, b1.getMaxHeight());
		assertEquals(0, b1.getRowWidth(1));
	}

	public void testCompletelyFull() {
		Board b1 = new Board(2, 4);
		b1.place(square, 0, 0);
		b1.commit();
		b1.place(square, 0, 2);
		/* System.out.println(b1);
		|++|
		|++|
		|++|
		|++|
		----
		 */
		b1.clearRows();
		assertEquals(0, b1.getMaxHeight());
		assertEquals(0, b1.getRowWidth(0));
	}


	public void testDropHeight() {
		Board b1 = new Board(3, 6);

		try {
			b1.dropHeight(square, 5);
			fail();
		} catch (RuntimeException outOfBounds) {}

		b1.place(square, 1, 0);
		System.out.println(b1);
		b1.commit();

		/*
		|   |
		|   |
		|   |
		|   |
		| ++|
		| ++|
		-----
		*/

		assertEquals(0, b1.dropHeight(stick1, 0));
		assertEquals(2, b1.dropHeight(square, 0));
		assertEquals(1, b1.dropHeight(l14, 0));
		assertEquals(1, b1.dropHeight(s2Rotated, 0));

		b1.place(l23, 0, 0);
		b1.commit();
		System.out.println(b1);
		/*
		|   |
		|   |
		|   |
		|++ |
		|+++|
		|+++|
		-----
		*/
		b1.clearRows();
		b1.commit();
		System.out.println(b1);
		b1.place(pyr1, 0, 1);
		b1.commit();
		System.out.println(b1);
		/*
		|   |
		|   |
		|   |
		| + |
		|+++|
		|++ |
		-----
		*/
		assertEquals(3, b1.dropHeight(pyr1, 0));
		assertEquals(2, b1.dropHeight(pyr4, 0));
		assertEquals(2, b1.dropHeight(pyr2, 1));
		assertEquals(3, b1.dropHeight(stick1, 1));
	}

	public void testCommitAndUndo() {
		Board b1 = new Board(3, 6);

		b1.place(pyr2, 1, 0);
		try {
			b1.place(stick1, 0, 0);
			fail();
		} catch (RuntimeException mustBeCommitted) {}

		b1.commit();
		/* System.out.println(b1);
		|   |
		|   |
		|   |
		|  +|
		| ++|
		|  +|
		-----
		 */
		b1.place(stick1, 0, 0);
		assertEquals(4, b1.getMaxHeight());
		assertEquals(4, b1.getColumnHeight(0));
		assertEquals(2, b1.getColumnHeight(1));

		b1.undo();
		assertEquals(3, b1.getMaxHeight());
		assertEquals(0, b1.getColumnHeight(0));
		assertEquals(1, b1.getRowWidth(0));
		assertEquals(2, b1.getRowWidth(1));

		b1.place(l23, 0, 0);
		/* System.out.println(b1);
		|   |
		|   |
		|   |
		|+++|
		|+++|
		|+ +|
		-----
		*/
		try {
			b1.place(square, 1, 1);
			fail();
		} catch (RuntimeException mustBeCommitted) {}

		// don't commit
		b1.clearRows();

		/* System.out.println(b1);
			|   |
			|   |
			|   |
			|   |
			|   |
			|+ +|
			-----
		*/

		b1.undo();
		/* System.out.println(b1);
		|   |
		|   |
		|   |
		|  +|
		| ++|
		|  +|
		-----
		 */
		assertEquals(1, b1.getRowWidth(0));
		assertEquals(0, b1.getColumnHeight(0));
		assertEquals(2, b1.getColumnHeight(1));
		assertEquals(3, b1.getColumnHeight(2));

		b1.place(l23, 0, 0);
		b1.commit();
		b1.clearRows();
		b1.undo();
		/* System.out.println(b1);
		|   |
		|   |
		|   |
		|+++|
		|+++|
		|+ +|
		-----
		*/
		assertEquals(2, b1.getRowWidth(0));
		assertEquals(3, b1.getColumnHeight(0));
		assertEquals(3, b1.getColumnHeight(1));
		assertEquals(3, b1.getColumnHeight(2));

		b1.undo(); // should do nothing
		assertEquals(2, b1.getRowWidth(0));
		assertEquals(3, b1.getColumnHeight(0));
		assertEquals(3, b1.getColumnHeight(1));
		assertEquals(3, b1.getColumnHeight(2));
	}
}
