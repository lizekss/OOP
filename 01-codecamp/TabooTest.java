// TabooTest.java
// Taboo class tests -- nothing provided.

import java.util.*;

import junit.framework.TestCase;

public class TabooTest extends TestCase {
    public void testNoFollow1() {
        List<Character> list = Arrays.asList('a', 'c', 'a', 'b');
        Taboo<Character> taboo = new Taboo<>(list);

        HashSet<Character> s = new HashSet<Character>();
        s.addAll(Arrays.asList('c', 'b'));
        assertEquals(s, taboo.noFollow('a'));
        s.clear();
        assertEquals(s, taboo.noFollow('x'));
        assertEquals(s, taboo.noFollow('b'));
        s.add('a');
        assertEquals(s, taboo.noFollow('c'));
    }

    public void testNoFollow2() {
        List<Character> list = Arrays.asList(null, null, null);
        Taboo<Character> taboo = new Taboo<>(list);
        assertEquals(Collections.emptySet(), taboo.noFollow('a'));

        taboo = new Taboo<>(Collections.emptyList());
        assertEquals(Collections.emptySet(), taboo.noFollow('a'));
    }

    public void testNoFollow3() {
        List<Integer> list = Arrays.asList(1, 2, null, 1, 3, null, 2, 1, 4);
        Taboo<Integer> taboo = new Taboo<>(list);

        HashSet<Integer> s = new HashSet<>();
        s.add(2);
        s.add(3);
        s.add(4);
        assertEquals(taboo.noFollow(1), s);
        s.clear();
        assertEquals(taboo.noFollow(3), s);
    }

    // character list
    public void testReduce1() {
        List<Character> rules = Arrays.asList('a', 'c', 'a', 'b');
        Taboo<Character> taboo = new Taboo<>(rules);
        // Construct ArrayList so that remove() is supported
        List<Character> before = new ArrayList<>(Arrays.asList('a', 'c', 'b', 'x', 'c', 'a'));
        List<Character> after = Arrays.asList('a', 'x', 'c');
        taboo.reduce(before);
        assertEquals(after, before);
    }

    // string list
    public void testReduce2() {
        List<String> rules = Arrays.asList("hi", "bye", "no", "yes");
        Taboo<String> taboo = new Taboo<>(rules);

        List<String> before = new ArrayList<>(Arrays.asList("maybe", "bye", "no", "yes", "hi", "bye"));
        List<String> after = Arrays.asList("maybe", "bye", "yes", "hi");
        taboo.reduce(before);
        assertEquals(after, before);

        List<String> empty = new ArrayList<>();
        taboo.reduce(empty);
        assertEquals(empty, empty);

        List<String> fine = new ArrayList<>(Arrays.asList("yes", "no", "bye", "hi"));
        taboo.reduce(fine);
        assertEquals(fine, fine);
    }

    // integer list
    public void testReduce3() {
        List<Integer> list = Arrays.asList(1, 2, null, 1, 3, null, 2, 1, 4);
        Taboo<Integer> taboo = new Taboo<>(list);

        List<Integer> l = new ArrayList<>(Arrays.asList(3, 1, 2, 1, 4, 3));
        taboo.reduce(l);
        assertEquals(Arrays.asList(3, 1, 1), l);

        List<Integer> l2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 2, 1, 1));
        taboo.reduce(l2);
        assertEquals(Arrays.asList(1, 1, 1), l2);

        List<Integer> l3 = new ArrayList<>(Arrays.asList(1, 2, 4, 3, 2, 4, 2, 3, 7, 9, 10));
        taboo.reduce(l3);
        assertEquals(Arrays.asList(1, 7, 9, 10), l3);

    }
}
