import junit.framework.TestCase;

import java.util.*;

public class AppearancesTest extends TestCase {
	// utility -- converts a string to a list with one
	// elem for each char.
	private List<String> stringToList(String s) {
		List<String> list = new ArrayList<String>();
		for (int i=0; i<s.length(); i++) {
			list.add(String.valueOf(s.charAt(i)));
			// note: String.valueOf() converts lots of things to string form
		}
		return list;
	}
	
	public void testSameCount1() {
		List<String> a = stringToList("abbccc");
		List<String> b = stringToList("cccbba");
		assertEquals(3, Appearances.sameCount(a, b));
	}
	
	public void testSameCount2() {
		// basic List<Integer> cases
		List<Integer> a = Arrays.asList(1, 2, 3, 1, 2, 3, 5);
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 9, 9, 1)));
		assertEquals(2, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1)));
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1, 1)));
	}
	
	// Add more tests
	public void testSameCount3() {
		List<Integer> a = Arrays.asList(1, 2, 3, 4, 5);

		assertEquals(0, Appearances.sameCount(a, Arrays.asList(6, 7, 8)));
		assertEquals(0, Appearances.sameCount(a, Arrays.asList()));
		assertEquals(0, Appearances.sameCount(a, Arrays.asList(1, 1, 2, 2)));
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 2, 2)));
		assertEquals(5, Appearances.sameCount(a, Arrays.asList(5, 4, 3, 2, 1)));
	}

	public void testSameCount4() {
		List<String> a = stringToList("aabbccddeeffgghhiijjkkllmmnnooppqqrrssttuuvvwwxxyyzz");

		assertEquals(26, Appearances.sameCount(a, stringToList("hhiijjkkllmmnnoowwxxyyzzppqqrrssttuuvvaabbccddeeffgg")));
		assertEquals(0, Appearances.sameCount(a, stringToList("abcdefghijklmnopqrstuvwxyz")));
		assertEquals(0, Appearances.sameCount(a, stringToList("aaabbbcccdddeeefffggghhhiiijjjkkklllmmmnnnooopppqqqrrrssstttuuuvvvwwwxxxyyyzzz")));
		assertEquals(0, Appearances.sameCount(a, Arrays.asList()));
		assertEquals(0, Appearances.sameCount(Arrays.asList(), a));
		assertEquals(0, Appearances.sameCount(Arrays.asList(), Arrays.asList()));
	}
}
