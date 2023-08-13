package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONPropertyName;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Manager implements Writable {

    private List<Friend> friends;
    private EventLog eventLog;

    // MODIFIES: this
    // EFFECTS: constructs new Manager, initialising friends as a new ArrayList of Friends
    public Manager() {
        friends = new ArrayList<Friend>();
        eventLog = EventLog.getInstance();
    }

    public List<Friend> getAllFriends() {
        return friends;
    }

    // EFFECTS: returns friend object with given name
    public Friend getFriend(String name) {
        for (Friend f : friends) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }

    // MODIFIES: this
    // EFFECTS: adds a new friend to list of friends. if added, logs event
    public boolean addFriend(String name) {
        if (getFriend(name) == null) {
            friends.add(new Friend(name));
            eventLog.logEvent(new Event("Added friend: " + name));
            return true;
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: removes given friend from list of friends. if removed, logs event.
    public void removeFriend(String name) {
        boolean removed = friends.remove(getFriend(name));
        if (removed) {
            eventLog.logEvent(new Event("Removed friend: " + name));
        }
    }

    // EFFECTS: return list of all friends' names
    public List<String> getAllFriendsNames() {
        List<String> friendNames = new ArrayList<String>();
        for (Friend f : friends) {
            friendNames.add(f.getName());
        }
        return friendNames;
    }

    // MODIFIES: this
    // EFFECTS: edits given debit of given friend by given amount. if added, logs event.
    public String editDebit(Friend friend, String type, String amount) {
        int intAmount;

        try {
            intAmount = Integer.parseInt(amount);
        } catch (Exception e) {
            return "Input not an integer or out of bounds, debit not edited";
        }

        boolean edited = friend.editDebit(type, intAmount);

        if (edited) {
            eventLog.logEvent(new Event(amount + "added to debit type: " + type + " of friend: "
                    + friend.getName()));
            return "Debit amount successfully edited.";
        } else {
            return "Debit type not found, amount not added.\n";
        }
    }

    // MODIFIES: this
    // EFFECTS: adds new debit type to list of debits for given friend
    //          returns string saying whether or not it was added and why
    public String addDebitType(String friendName, String typeName) {
        Friend friend = getFriend(friendName);
        if (friend != null) {
            boolean added = friend.addDebitType(typeName);
            if (added) {
                eventLog.logEvent(new Event("Created debit type: " + typeName + "for friend: "
                        + friendName));
                return "New debit successfully created.";
            } else {
                return "Debit type already exists, did not add";
            }
        } else {
            return "Friend not found, debit not created";
        }
    }

    // EFFECTS: returns this as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("friends", friendsToJson());
        return json;
    }

    // EFFECTS: returns friends in this manager as a JSON array
    private JSONArray friendsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Friend f : friends) {
            JSONObject subArray = new JSONObject();
            subArray.put("name",f.getName());
            subArray.put("debits",f.getDebits());
            jsonArray.put(subArray);
        }

        return jsonArray;
    }

    // EFFECTS: prints all event logs
    public void printEventLog() {
        for (Event event : eventLog) {
            System.out.println(event);
        }
    }
}