package persistence;

import model.Friend;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkFriend(String name, HashMap debits, Friend friend) {
        assertEquals(name, friend.getName());
        assertEquals(debits, friend.getDebits());
    }
}
