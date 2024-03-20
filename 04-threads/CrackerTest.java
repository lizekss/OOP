import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class CrackerTest extends TestCase {
    private final PrintStream stdout = System.out;
    private ByteArrayOutputStream stream;

    public void testGenerateMode() {
        stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stream));
        Cracker.main(new String[] {"molly"});
        assertEquals("4181eecbd7a755d19fdf73887c54837cbecf63fd", stream.toString().trim());

        stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stream));
        Cracker.main(new String[] {"a!"});
        assertEquals("34800e15707fae815d7c90d49de44aca97e2d759", stream.toString().trim());

        stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stream));
        Cracker.main(new String[] {"xyz"});
        assertEquals("66b27417d37e024c46526c2f6d358a754fc552f3", stream.toString().trim());

        stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stream));
        Cracker.main(new String[] {"fm"});
        assertEquals("adeb6f2a18fe33af368d91b09587b68e3abcb9a7", stream.toString().trim());

        System.setOut(stdout);
    }

    public void testCrackMode() {
        stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stream));
        Cracker.main(new String[] {"adeb6f2a18fe33af368d91b09587b68e3abcb9a7", "3", "5"});
        assertEquals("fm\nall done", stream.toString().trim());

        stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stream));
        Cracker.main(new String[] {"66b27417d37e024c46526c2f6d358a754fc552f3", "4", "3"});
        assertEquals("xyz\nall done", stream.toString().trim());

        stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stream));
        Cracker.main(new String[] {"34800e15707fae815d7c90d49de44aca97e2d759", "4", "4"});
        assertEquals("a!\nall done", stream.toString().trim());

        System.setOut(stdout);
    }

}
