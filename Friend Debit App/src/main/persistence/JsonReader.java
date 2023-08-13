package persistence;

import model.Event;
import model.EventLog;
import model.Friend;
import model.Manager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads manager from JSON data stored in file
public class JsonReader {
    private String source;
    private EventLog eventLog;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
        this.eventLog = EventLog.getInstance();
    }

    // EFFECTS: reads manager from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Manager read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseManager(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses manager from JSON object and returns it
    private Manager parseManager(JSONObject jsonObject) {
        Manager manager = new Manager();
        addListOfFriends(manager, jsonObject);
        return manager;
    }

    // MODIFIES: manager
    // EFFECTS: parses thingies from JSON object and adds them to manager. logs read event.
    private void addListOfFriends(Manager manager, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("friends");

        JSONArray friends = jsonObject.getJSONArray("friends");

//        for (int i = 0; i < friends.length(); i++) {
//            JSONObject friend = friends.getJSONObject(i);
//            String name = friend.getString("name");
//            JSONObject debits = friend.getJSONObject("debits");
//            System.out.println(name);
//            System.out.println(debits);
//        }

        for (Object json : jsonArray) {
            JSONObject nextFriend = (JSONObject) json;
            addFriend(manager, nextFriend);
        }

        eventLog.logEvent(new Event("Loaded data from " + this.source));
    }

    // MODIFIES: manager
    // EFFECTS: parses friend and their debits from JSON object and adds it to manager
    private void addFriend(Manager manager, JSONObject friend) {
        String friendName = friend.getString("name");
        JSONObject debits = friend.getJSONObject("debits");
        manager.addFriend(friendName);
        Iterator<String> keys = debits.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            manager.addDebitType(friendName, key);
            manager.editDebit(manager.getFriend(friendName), key, String.valueOf(debits.get(key)));
        }
    }
}
