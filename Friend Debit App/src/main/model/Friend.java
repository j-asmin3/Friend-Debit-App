package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.HashMap;

public class Friend {
    private String name;
    private HashMap<String,Integer> debits;

    // MODIFIES: this
    // EFFECTS: constructs new Friend, setting name as given input and initialising debits as a HashMap
    public Friend(String name) {
        this.name = name;
        debits = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public HashMap<String,Integer> getDebits() {
        return debits;
    }

    // MODIFIES: this
    // EFFECTS: adds new debit type to list of debits for friend
    //          returns true if type doesn't already exist and successfully added
    //          returns false if type already exists
    public boolean addDebitType(String typeName) {
        if (!debits.containsKey(typeName)) {
            debits.put(typeName,0);
            return true;
        } else {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: adds given amount to debit of given type,
    //          returns true if there is a debit of that type and debit has been edited
    //          returns false if debit of that type not found
    public boolean editDebit(String type, int amount) {
        if (debits.containsKey(type)) {
            debits.replace(type,debits.get(type) + amount);
            return true;
        } else {
            return false;
        }

    }

}