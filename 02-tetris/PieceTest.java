import junit.framework.TestCase;

import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;
	private Piece s2, s2Rotated;
	private Piece stick1, stick2;
	private Piece square;
	private Piece l1, l12, l13, l14;
	private Piece l2, l22, l23, l24;
	private Piece[] pieces;

	protected void setUp() throws Exception {
		super.setUp();
		
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
	}
	
	// Here are some sample tests to get you started
	
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		try {
			Piece check = new Piece("01 2 10");
			fail();
		} catch (RuntimeException couldNotParse) {}

		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	}
	
	
	// Test the skirt returned by a few pieces
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
	}

	public void testEquals() {
		assertTrue(s2.equals(new Piece(Piece.S2_STR)));
		assertFalse(s2.equals(null));
		assertFalse(s2.equals(pyr4));
		assertTrue(pyr1.equals(new Piece(Piece.PYRAMID_STR)));
		assertFalse(pyr1.equals(pyr3));
		assertTrue(s.equals(new Piece(Piece.S1_STR)));
		assertTrue(l1.equals(new Piece(Piece.L1_STR)));
		assertFalse(l1.equals(null));
		assertTrue(l2.equals(new Piece(Piece.L2_STR)));
		assertTrue(square.equals(new Piece(Piece.SQUARE_STR)));
		assertTrue(stick1.equals(new Piece(Piece.STICK_STR)));
	}

	public void testStickRotations() {
		assertEquals(7, pieces.length);

		// STICK, L1, L2, S1, S2, SQUARE, PYRAMID
		Piece cur = pieces[0];
		assertEquals(cur, stick1);
		cur = cur.fastRotation();
		assertEquals(cur, new Piece("0 0  1 0  2 0  3 0"));
		assertEquals(cur, stick2);
		cur = cur.fastRotation();
		assertEquals(cur, stick1);
	}

	public void testL1Rotations() {
		Piece cur = pieces[1];
		assertEquals(cur, l1);
		cur = cur.fastRotation();
		assertEquals(cur, new Piece("0 0  1 0  2 0  2 1"));
		assertEquals(cur, l12);
		cur = cur.fastRotation();
		assertEquals(cur, new Piece("0 2  1 0  1 1  1 2"));
		assertEquals(cur, l13);
		cur = cur.fastRotation();
		assertEquals(cur, new Piece("0 0  0 1  1 1  2 1"));
		assertEquals(cur, l14);
		cur = cur.fastRotation();
		assertEquals(cur, l1);
	}

	public void testS2Rotations() {
		Piece cur = pieces[4];
		assertEquals(cur, s2);
		cur = cur.fastRotation();
		assertEquals(cur, new Piece("0 0  0 1  1 1  1 2"));
		assertEquals(cur, s2Rotated);
		cur = cur.fastRotation();
		assertEquals(cur, s2);
	}

	public void testPyramidRotations() {
		Piece cur = pieces[6];
		assertEquals(cur, pyr1);
		cur = cur.fastRotation();
		assertEquals(cur, new Piece("0 1  1 0  1 1  1 2"));
		assertEquals(cur, pyr2);
		cur = cur.fastRotation();
		assertEquals(cur, new Piece("0 1  1 0  1 1  2 1"));
		assertEquals(cur, pyr3);
		cur = cur.fastRotation();
		assertEquals(cur, new Piece("0 0  0 1  1 1  0 2"));
		assertEquals(cur, pyr4);
		cur = cur.fastRotation();
		assertEquals(cur, pyr1);
	}

	public void testS1Rotations() {
		Piece cur = pieces[3];
		assertEquals(cur, s);
		cur = cur.fastRotation();
		assertEquals(cur, new Piece("0 1  1 0  1 1  0 2"));
		assertEquals(4, cur.getBody().length);
		assertEquals(cur, sRotated);
		cur = cur.fastRotation();
		assertEquals(cur, s);
		//second round
		assertEquals(cur.fastRotation(), sRotated);
	}

	public void testSquareRotations() {
		Piece cur = pieces[5];
		assertEquals(cur, square);
		cur = cur.fastRotation();
		assertEquals(cur, new Piece(Piece.SQUARE_STR));
		// second round
		cur = cur.fastRotation();
		assertEquals(cur, square);
	}

	public void testSize() {
		assertEquals(3, s2.getWidth());
		assertEquals(2, s2.getHeight());
		assertEquals(2, s2Rotated.getWidth());
		assertEquals(3, s2Rotated.getHeight());

		assertEquals(1, stick1.getWidth());
		assertEquals(4, stick1.getHeight());
		assertEquals(4, stick2.getWidth());
		assertEquals(1, stick2.getHeight());

		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		assertEquals(3, pyr3.getWidth());
		assertEquals(2, pyr3.getHeight());
		assertEquals(2, pyr4.getWidth());
		assertEquals(3, pyr4.getHeight());

		assertEquals(2, l2.getWidth());
		assertEquals(3, l2.getHeight());
		assertEquals(3, l22.getWidth());
		assertEquals(2, l22.getHeight());
		assertEquals(2, l23.getWidth());
		assertEquals(3, l23.getHeight());
		assertEquals(3, l24.getWidth());
		assertEquals(2, l24.getHeight());

		assertEquals(2, square.getWidth());
		assertEquals(2, square.getHeight());
	}

	public void testSkirt() {
		assertTrue(Arrays.equals(new int[] {1, 0}, pyr2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 1}, pyr4.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0}, square.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0}, l1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, l12.getSkirt()));
		assertTrue(Arrays.equals(new int[] {2, 0}, l13.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0}, l2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 1, 0}, l22.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 2}, l23.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, l24.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0}, stick1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 0, 0}, stick2.getSkirt()));
	}
}
