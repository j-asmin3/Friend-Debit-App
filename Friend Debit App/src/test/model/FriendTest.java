package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FriendTest {

    private Friend friend;

    @BeforeEach
    public void setup() {
        friend = new Friend("friendName");
    }

    @Test
    public void addDebitTypeDoesntExistAlreadyTest() {
        assertTrue(friend.addDebitType("coffee"));
        assertEquals(0,friend.getDebits().get("coffee"));

        assertTrue(friend.addDebitType("meal"));
        assertEquals(0,friend.getDebits().get("meal"));

    }

    @Test
    public void addDebitTypeAlreadyExistsTest() {
        assertTrue(friend.addDebitType("coffee"));
        assertFalse(friend.addDebitType("coffee"));

        assertTrue(friend.addDebitType("meal"));
        assertFalse(friend.addDebitType("meal"));
    }

    @Test
    public void editDebitSubtractTest() {
        friend.addDebitType("bubble tea");
        assertTrue(friend.editDebit("bubble tea",-1));
        assertEquals(-1,friend.getDebits().get("bubble tea"));
        assertTrue(friend.editDebit("bubble tea",-3));
        assertEquals(-4,friend.getDebits().get("bubble tea"));
    }

    @Test
    public void editDebitAddTest() {
        friend.addDebitType("uber");
        assertTrue(friend.editDebit("uber",5));
        assertEquals(5,friend.getDebits().get("uber"));
        assertTrue(friend.editDebit("uber",1));
        assertEquals(6,friend.getDebits().get("uber"));

    }

    @Test
    public void editDebitNotFoundTest() {
        assertFalse(friend.editDebit("uber",5));
        assertFalse(friend.editDebit("bubble tea",-1));
    }

    @Test
    public void toJsonTest() {
        //insert
        // check piazza @872 post for how to test
    }
}
