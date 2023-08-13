package persistence;

import model.Friend;
import model.Manager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Manager manager = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyManager() {
        //make an equivalent manager one i guess
        JsonReader reader = new JsonReader("./data/testReaderEmptyManager.json");
        try {
            Manager manager = reader.read();
            assertEquals(0, manager.getAllFriends().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralManager() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralManager.json");
        try {
            Manager manager = reader.read();

            assertEquals("joe", manager.getFriend("joe").getName());
            assertEquals(2, manager.getAllFriends().size());

            HashMap debits1 = new HashMap<>();
            debits1.put("uber",1);
            debits1.put("coffee",-2);

            HashMap debits2 = new HashMap<>();
            debits2.put("meals",-3);
            debits2.put("groceries",5);

            checkFriend("joe", debits1, manager.getFriend("joe"));
            checkFriend("bob", debits2, manager.getFriend("bob"));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}