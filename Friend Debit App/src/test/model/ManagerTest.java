package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerTest {

    private Manager manager = new Manager();
    private List<Friend> friends = new ArrayList<Friend>();
    private Friend bob;
    private Friend hannah;
    private Friend joe;
    private Friend elise;

    @BeforeEach
    public void setup() {
        bob = new Friend("bob");
        hannah = new Friend("hannah");
        joe = new Friend("joe");
        elise = new Friend("elise");

        manager.getAllFriends().add(bob);
        manager.getAllFriends().add(hannah);
        manager.getAllFriends().add(joe);
        manager.getAllFriends().add(elise);
    }

    @Test
    public void getFriendExistTest() {
        assertEquals(bob,manager.getFriend("bob"));
        assertEquals(hannah,manager.getFriend("hannah"));
    }

    @Test
    public void addFriendDoesntExistTest() {
        assertTrue(manager.addFriend("jason"));
        assertTrue(manager.addFriend("elle"));
    }

    @Test
    public void addFriendAlreadyExistsTest() {
        assertFalse(manager.addFriend("bob"));
        assertFalse(manager.addFriend("elise"));
    }

    @Test
    public void getFriendDoesntExistTest() {
        assertNull(manager.getFriend("joanna"));
        assertNull(manager.getFriend("william"));
    }

    @Test
    public void removeFriendExistsTest() {
        assertEquals(bob,manager.getFriend("bob"));
        manager.removeFriend("bob");
        assertNull(manager.getFriend("bob"));
    }

    @Test
    public void removeFriendDoesntExistTest() {
        assertNull(manager.getFriend("joanna"));
        manager.removeFriend("joanna");
        assertNull(manager.getFriend("joanna"));
    }

    @Test
    public void getAllFriendsNames() {
        Manager manager1 = new Manager();
        List<String> friendsNames = new ArrayList<String>();
        assertEquals(friendsNames,manager1.getAllFriendsNames());

        friendsNames.add("bob");
        friendsNames.add("hannah");
        friendsNames.add("joe");
        friendsNames.add("elise");
        assertEquals(friendsNames,manager.getAllFriendsNames());
    }

    @Test
    public void editDebitSuccessTest() {
        manager.addDebitType("bob","bubble tea");
        assertEquals("Debit amount successfully edited.",
                manager.editDebit(bob,"bubble tea","-1"));
        assertEquals(-1,bob.getDebits().get("bubble tea"));
        assertEquals("Debit amount successfully edited.",
                manager.editDebit(bob,"bubble tea","2"));
        assertEquals(1,bob.getDebits().get("bubble tea"));
    }

    @Test
    public void editDebitTypeNotFoundTest() {
        assertEquals("Debit type not found, amount not added.\n",
                manager.editDebit(bob,"bubble tea","-1"));
    }

    @Test
    public void editDebitInputNotNumberTest() {
        manager.addDebitType("bob","bubble tea");
        assertEquals("Input not an integer or out of bounds, debit not edited",
                manager.editDebit(bob,"bubble tea","0.00"));
        assertEquals("Input not an integer or out of bounds, debit not edited",
                manager.editDebit(bob,"bubble tea","haha"));
    }

    @Test
    public void addDebitTypeDoesntExistAlreadyTest() {
        assertEquals("New debit successfully created.",
                manager.addDebitType("bob","coffee"));
        assertEquals(1, bob.getDebits().size());

        assertEquals("New debit successfully created.",
                manager.addDebitType("elise","meal"));
        assertEquals(1, elise.getDebits().size());
    }

    @Test
    public void addDebitTypeAlreadyExistsTest() {
        manager.addDebitType("bob","coffee");
        assertEquals(1, bob.getDebits().size());
        assertEquals("Debit type already exists, did not add",
                manager.addDebitType("bob","coffee"));
        assertEquals(1, bob.getDebits().size());

        manager.addDebitType("elise","meal");
        assertEquals(1, elise.getDebits().size());
        assertEquals("Debit type already exists, did not add",
                manager.addDebitType("elise","meal"));
        assertEquals(1, elise.getDebits().size());
    }

    @Test
    public void addDebitTypeFriendNotFoundTest() {
        assertEquals("Friend not found, debit not created",
                manager.addDebitType("jason","coffee"));
        assertEquals("Friend not found, debit not created",
                manager.addDebitType("elle","meal"));
    }

}
