import java.util.*;

public class Appearances {
	
	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		int result = 0;
		HashMap<T, Integer> map = new HashMap<>();
		for (T t : a) {
			if (map.containsKey(t))
				map.replace(t, map.get(t) + 1);
			else map.put(t, 1);
		}

		for (T t : b) {
			if (map.containsKey(t))
				map.replace(t, map.get(t) - 1);
		}

		for (T t : map.keySet()) {
			if (map.get(t) == 0)
				result++;
		}
		return result; // YOUR CODE HERE
	}
	
}
