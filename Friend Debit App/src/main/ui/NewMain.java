package ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Manager;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;

public class NewMain extends JPanel
        implements ListSelectionListener {
    private Manager manager = new Manager();
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private JList list;
    private DefaultListModel listModel;

    private static final String JSON_STORE = "./data/manager.json";
    private static final String IMAGE_URL = "./data/friend debit logo.png";
    private static final String ADD_BTN_STRING = "Add";
    private static final String DELETE_BTN_STRING = "Delete";
    private static final String SAVE_BTN_STRING = "Save";
    private static final String LOAD_BTN_STRING = "Load";
    private JButton deleteButton;
    private JButton saveButton;
    private JButton loadButton;
    private JTextField friendName;


    // EFFECTS: constructs new NewMain
    public NewMain() {
        super(new BorderLayout());
        init();
        JScrollPane listScrollPane = new JScrollPane(list);

        JButton addButton = new JButton(ADD_BTN_STRING);
        AddBtnListener addBtnListener = new AddBtnListener(addButton);
        setButtonActions(addButton, ADD_BTN_STRING, addBtnListener);
        addButton.setEnabled(false);

        deleteButton = new JButton(DELETE_BTN_STRING);
        saveButton = new JButton(SAVE_BTN_STRING);
        loadButton = new JButton(LOAD_BTN_STRING);
        setButtonActions(deleteButton, DELETE_BTN_STRING, new DeleteBtnListener());
        setButtonActions(saveButton, SAVE_BTN_STRING, new SaveBtnListener());
        setButtonActions(loadButton, LOAD_BTN_STRING, new LoadBtnListener());

        friendName = new JTextField(10);
        friendName.addActionListener(addBtnListener);
        friendName.getDocument().addDocumentListener(addBtnListener);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                BoxLayout.LINE_AXIS));

        addToButtonPane(addButton, buttonPane, listScrollPane);

        printEventLogOnQuit();
    }

    // EFFECTS: prints all event logs
    private void printEventLogOnQuit() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            manager.printEventLog();
        }, "Shutdown-thread"));
    }

    // MODIFIES: this
    // EFFECTS: initialises jsonWriter,Reader, listModel and list
    public void init() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        listModel = new DefaultListModel();
        listModel.addElement("Friends:");
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
    }

    // REQUIRES: addButton, buttonPane and listScrollPane is the same buttonPane as the ones in the constructor
    // MODIFIES: this, buttonPane, listScrollPane
    // EFFECTS: adds listScrollPane and buttonPane with all the buttons inside
    private void addToButtonPane(JButton addButton, JPanel buttonPane, JScrollPane listScrollPane) {
        ImageIcon image = new ImageIcon(IMAGE_URL);
        JLabel imageHolder = new JLabel(image);
        buttonPane.add(imageHolder);
        buttonPane.add(friendName);
        buttonPane.add(addButton);
        buttonPane.add(deleteButton);
        buttonPane.add(saveButton);
        buttonPane.add(loadButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);

    }

    // MODIFIES: this, button
    // EFFECTS: adds action command and listener to button
    private void setButtonActions(JButton button, String btnString, ActionListener btnListener) {
        button.setActionCommand(btnString);
        button.addActionListener(btnListener);
    }

    // Listener for the delete button
    class DeleteBtnListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: removes friend from UI and manager
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();
            String name = listModel.getElementAt(index).toString();
            listModel.remove(index);
            manager.removeFriend(name);

            int size = listModel.getSize();

            if (size == 0) {
                deleteButton.setEnabled(false);

            } else {
                if (index == listModel.getSize()) {
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    // Listener for add button
    class AddBtnListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        // EFFECTS: constructs new AddBtnListener
        public AddBtnListener(JButton button) {
            this.button = button;
        }

        // MODIFIES: this
        // EFFECTS: adds friend to UI and manager if doesn't already exist / name = ""
        public void actionPerformed(ActionEvent e) {
            String name = friendName.getText();

            if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                friendName.requestFocusInWindow();
                friendName.selectAll();
                return;
            }

            int index = list.getSelectedIndex();
            if (index == -1) {
                index = 0;
            } else {
                index++;
            }

            boolean addedFriend = manager.addFriend(name);
            if (addedFriend) {
                listModel.insertElementAt(name, index);
            }

            friendName.requestFocusInWindow();
            friendName.setText("");

            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }

        //EFFECTS: returns true if friend with name already exists
        protected boolean alreadyInList(String name) {
            return listModel.contains(name);
        }

        // MODIFIES: this
        // EFFECTS: enables the button
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        // MODIFIES: this
        // EFFECTS: disables button if empty text field
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        // MODIFIES: this
        // EFFECTS: enables button if not already enabled
        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        // MODIFIES: this
        // EFFECTS: disables button if empty text field
        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    // Class for save button listener
    class SaveBtnListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: saves manager to json, if file not found, throw FileNotFoundException
        public void actionPerformed(ActionEvent e) {
            try {
                jsonWriter.open();
                jsonWriter.write(manager);
                jsonWriter.close();
            } catch (FileNotFoundException exception) {
                System.out.println("Unable to write to file: " + JSON_STORE);
            }
        }
    }

    // Class for load button listener
    class LoadBtnListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: loads manager from json, adds friends to listScrollPane in UI
        public void actionPerformed(ActionEvent e) {
            try {
                manager = jsonReader.read();
                listModel.clear();
                listModel.addElement("Friends:");
                for (String name : manager.getAllFriendsNames()) {
                    listModel.addElement(name);
                }
            } catch (IOException exception) {
                System.out.println("Unable to read from file: " + JSON_STORE);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: enable delete button unless nothing is selected
    public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            deleteButton.setEnabled(list.getSelectedIndex() != -1);
        }
    }

    // MODIFIES: this
    // EFFECTS: creates window and content pane
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("ListDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent newContentPane = new NewMain();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        frame.pack();
        frame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: runs function to create and show gui
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
