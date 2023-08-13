package persistence;

import model.Event;
import model.EventLog;
import model.Friend;
import model.Manager;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Represents a writer that writes JSON representation of manager to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;
    private EventLog eventLog;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
        this.eventLog = EventLog.getInstance();
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    //          be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of manager to file. logs write event.
    public void write(Manager manager) {
        JSONObject json = manager.toJson();
        saveToFile(json.toString(TAB));
        eventLog.logEvent(new Event("Saved data to \"" + this.destination));
    }

    // MODIFIES: this
    // EFFECTS: closes writer.
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
