import java.util.HashSet;
import java.util.Set;

import static java.lang.Character.toLowerCase;

// CS108 HW1 -- String static methods

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adajcent chars that are the same.
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		if (str.isEmpty())
			return 0;

		int cur = 1;
		int max = 1;
		for (int i = 1; i < str.length(); i++) {
			if (str.charAt(i) == str.charAt(i - 1)) {
				cur++;
			} else {
				if (cur > max)
					max = cur;
				cur = 1;
			}
		}

		return max;
	}

	
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */
	// a3tx2z
	public static String blowup(String str) {
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			char cur = str.charAt(i);
			if (i < str.length() - 1 && cur >= '0' && cur <= '9') {
				char next = str.charAt(i + 1);
				for (int j = 0; j < cur - '0'; j++)
					result += next;
			} else if (cur < '0' || cur > '9'){
				result += cur;
			}
		}
		return result;
	}
	
	/**
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
		HashSet<String> setA = new HashSet<>();
		HashSet<String> setB = new HashSet<>();

		for (int i = 0; i <= a.length() - len; i++)
			setA.add(a.substring(i, i + len));

		for (int i = 0; i <= b.length() - len; i++)
			setB.add(b.substring(i, i + len));

		setA.retainAll(setB);

		return !setA.isEmpty();
	}
}
