import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankTest extends TestCase {
    public void testBank() {
        PrintStream stdout = System.out;
        ByteArrayOutputStream  stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stream));
        Bank.main(new String[] {"5k.txt", "10"});
        Pattern p = Pattern.compile("acct:[0-9]+ bal:1000 trans:[0-9][0-9]+");
        Matcher m = p.matcher(stream.toString().trim());
        for (int i = 0; i < 20; i++) {
            assertTrue(m.find());
        }

        stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stream));
        Bank.main(new String[] {"100k.txt", "10"});
        m = p.matcher(stream.toString().trim());
        for (int i = 0; i < 20; i++) {
            assertTrue(m.find());
        }

        System.setOut(stdout);
    }
}
