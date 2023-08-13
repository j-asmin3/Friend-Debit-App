package persistence;

import model.Friend;
import model.Manager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            Manager manager = new Manager();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyManager() {
        try {
            Manager manager = new Manager();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyManager.json");
            writer.open();
            writer.write(manager);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyManager.json");
            manager = reader.read();
            assertEquals(0, manager.getAllFriends().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralManager() {
        try {
            Manager manager = new Manager();

            manager.addFriend("joe");
            Friend joe = manager.getFriend("joe");
            joe.addDebitType("uber");
            joe.editDebit("uber",1);
            joe.addDebitType("coffee");
            joe.editDebit("coffee",-2);

            manager.addFriend("bob");
            Friend bob = manager.getFriend("bob");
            bob.addDebitType("meals");
            bob.editDebit("meals",-3);
            bob.addDebitType("groceries");
            bob.editDebit("groceries",5);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralManager.json");
            writer.open();
            writer.write(manager);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralManager.json");
            manager = reader.read();
            assertEquals(2, manager.getAllFriends().size());

            HashMap joeDebits = new HashMap();
            joeDebits.put("uber",1);
            joeDebits.put("coffee",-2);
            HashMap bobDebits = new HashMap();
            bobDebits.put("meals",-3);
            bobDebits.put("groceries",5);

            checkFriend("joe",joeDebits,joe);
            checkFriend("bob",bobDebits,bob);

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}