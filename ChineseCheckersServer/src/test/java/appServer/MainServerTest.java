package appServer;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

public class MainServerTest {

    @Test
    public void testVerifyGoodArguments() {
        String[][] argsToCheck = new String[][]{{"2"},{"3"},{"4"},{"6"}};

        for (String[] args : argsToCheck) {
            try {
                MainServer.verifyArguments(args);
            } catch (IllegalArgumentException e) {
                fail("Exception thrown when arguments were correct");
            }
        }
    }

    @Test
    public void testVerifyWrongArguments() {
        String[][] argsToCheck = new String[][]{{"1"},{"5"},{"0"},{"-1"},{"7"},{"1.3"},{"aaa"},{"12","3"}};

        for (int i = 0; i < argsToCheck.length; i++) {
            try {
                MainServer.verifyArguments(argsToCheck[i]);
                fail("Exception not thrown when arguments were incorrect");
            } catch (IllegalArgumentException e) {
                if (i < 5) { Assert.assertEquals("Incorrect number of players", e.getMessage()); }
                else if (i == 5 || i == 6) { Assert.assertEquals("Incorrect argument format", e.getMessage()); }
                else { Assert.assertEquals("Incorrect number of arguments", e.getMessage()); }
            }
        }
    }
}