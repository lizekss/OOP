
/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/

import java.util.*;

public class Taboo<T> {
	HashMap<T, HashSet<T>> noFollows;
	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
		noFollows = new HashMap<>();

		for (int i = 0; i < rules.size() - 1; i++) {
			T elem = rules.get(i);
			if (elem != null && rules.get(i + 1) != null) {
				if (!noFollows.containsKey(elem))
					noFollows.put(elem, new HashSet<T>());

				noFollows.get(elem).add(rules.get(i + 1));
			}
		}
	}
	
	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		if (!noFollows.containsKey(elem))
			return Collections.emptySet();

		return noFollows.get(elem);
	}
	
	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
		for (int i = 1; i < list.size(); i++) {
			T cur = list.get(i);
			T prev = list.get(i - 1);
			if (noFollows.containsKey(prev) && noFollows.get(prev).contains(cur)) {
				list.remove(i);
				i--;
			}
		}
	}
}
