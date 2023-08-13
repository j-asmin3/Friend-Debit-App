package ui;

import model.Friend;
import model.Manager;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class App {

    private static final String JSON_STORE = "./data/manager.json";
    private Scanner input;
    private Manager manager = new Manager();
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: constructs new App, starts app with runApp() function
    public App() {
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runApp() {
        boolean keepGoing = true;
        String command;

        init();

        while (keepGoing) {
            displayMenu();

            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("Successfully quit");

    }

    // MODIFIES: this
    // EFFECTS: initialises scanner
    private void init() {
        input = new Scanner(System.in);
        input.useDelimiter("\n");

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS: processes user commands
    private void processCommand(String command) {
        switch (command) {
            case "add f":
                addFriend();
                break;
            case "add t":
                createNewDebit();
                break;
            case "edit d":
                addSubtractDebit();
                break;
            case "check d":
                checkDebit();
                break;
            case "save":
                save();
                break;
            case "load":
                load();
                break;
            default:
                System.out.println("Error: not an existing command. Try again.");
                break;
        }
    }

    // EFFECTS: displays commands users can choose from
    private void displayMenu() {
        System.out.println("Choose:");
        System.out.println("add f -> add new friend");
        System.out.println("add t -> create new type of debit");
        System.out.println("edit d -> add/subtract debit");
        System.out.println("check d -> check how much debit of each type each friend has");
        System.out.println("save -> SAVE list of friends at their debits");
        System.out.println("load -> LOAD list of friends at their debits");
    }

    // MODIFIES: this
    // EFFECTS: add friend to list of friends
    private void addFriend() {
        System.out.println("Type name of new friend you want to add");
        String name = input.next();
        boolean addedFriend = manager.addFriend(name);
        if (addedFriend) {
            System.out.println("Friend successfully added.");
        } else {
            System.out.println("Friend already exists, not added.");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a new type of debit to list of debits for given friend
    private void createNewDebit() {
        System.out.println("Type name of new type of debit you want to create");
        String debitName = input.next();
        System.out.println("Which friend would you like to add it to?");
        System.out.println(manager.getAllFriendsNames());
        String friendName = input.next();

        String addedStatus = manager.addDebitType(friendName,debitName);
        System.out.println(addedStatus);
    }

    // MODIFIES: this
    // EFFECTS: adds/subtracts given debit of given debit type of given friend
    private void addSubtractDebit() {
        System.out.println("Type which friend you want to add debit to");
        System.out.println(manager.getAllFriendsNames());
        String name = input.next();

        Friend friend = manager.getFriend(name);

        if (friend != null) {
            System.out.println("Type what type of debit you want to add\n" + friend.getDebits());
            String type = input.next();

            System.out.println("Type how much debit you want to add");
            System.out.println("Positive number means they owe you\nNegative means you owe them");
            String amount = input.next();

            String editedStatus = manager.editDebit(friend, type, amount);
            System.out.println(editedStatus);
        } else {
            System.out.println("Friend not found, debit not created");
        }
    }

    // EFFECTS: prints all debits for all friends for the user to view
    private void checkDebit() {
        for (Friend friend : manager.getAllFriends()) {
            System.out.println(friend.getName() + ": " + friend.getDebits());
        }
        System.out.println("\n");
    }

    // EFFECTS: saves manager with list of friends & corresponding debit types & amounts to file
    private void save() {
        try {
            jsonWriter.open();
            jsonWriter.write(manager);
            jsonWriter.close();
            System.out.println("Saved data to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads manager with list of friends & corresponding debit types & amounts from file
    private void load() {
        try {
            manager = jsonReader.read();
            System.out.println("Loaded data from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

}