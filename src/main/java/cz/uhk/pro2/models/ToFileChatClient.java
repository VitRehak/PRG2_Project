package cz.uhk.pro2.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cz.uhk.pro2.models.chatFileOperations.ChatFileOperations;
import cz.uhk.pro2.models.chatFileOperations.JsonChatFileOperations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ToFileChatClient implements ChatClient {
    @Override
    public void addActionListenerMessageAdded(ActionListener toAdd) {

    }

    private static final String MESSAGES_FILE = "./messages.json";
    private Gson gson;
    private ChatFileOperations chatFileOperations;
    private String loggedUser;
    private List<Message> messages;
    private List<String> loggedUsers;
    private List<ActionListener> listenersLoggedUserChanged = new ArrayList<>();
    //private List<ActionListener> listenerMessageAdded = new ArrayList<>();

    public ToFileChatClient(ChatFileOperations chatFileOperations) {
        messages = new ArrayList<>();
        loggedUsers = new ArrayList<>();
        this.chatFileOperations = chatFileOperations;
        gson = new GsonBuilder().setPrettyPrinting().create();
        readMessagesFromFile();
    }

    @Override
    public Boolean isAuthenticated() {
        return loggedUser != null;
    }

    @Override
    public void login(String userName) {
        loggedUser = userName;
        loggedUsers.add(userName);
        addMessage(new Message(Message.USER_LOGGED_IN, userName));
        raiseEventListenerUsersChanged();
    }

    @Override
    public void logout() {
        loggedUsers.remove(loggedUser);
        addMessage(new Message(Message.USER_LOGGED_OUT, loggedUser));
        loggedUser = null;
        raiseEventListenerUsersChanged();
    }

    @Override
    public void sendMessage(String text) {
        addMessage(new Message(loggedUser, text));
        //raiseEventListenerMessageAdded();
    }

    @Override
    public List<String> getLoggedUsers() {
        return loggedUsers;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public void addActionListenerLoggedUserChanged(ActionListener toAdd) {
        listenersLoggedUserChanged.add(toAdd);
    }

    /*@Override
    public void addActionListenerMessageAdded(ActionListener toAdd) {
        listenerMessageAdded.add(toAdd);
    }*/

    private void raiseEventListenerUsersChanged() {
        listenersLoggedUserChanged.forEach(l -> {
            l.actionPerformed(new ActionEvent(this, 1, "listenerLoggedUsersChanged"));
        });
    }

    /*private void raiseEventListenerMessageAdded() {
        listenerMessageAdded.forEach(m -> {
            m.actionPerformed(new ActionEvent(this, 1, "listenerMessageAdded"));
        });
    }*/
    private void addMessage(Message message) {
        messages.add(message);
        writeMessagesToFile();
    }

    private void writeMessagesToFile() {
        String json = gson.toJson(messages);

        try {
            FileWriter writer = new FileWriter(MESSAGES_FILE);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessagesFromFile() {
        try {
            FileReader reader = new FileReader(MESSAGES_FILE);
            BufferedReader bufferedReader= new BufferedReader(reader);
            StringBuilder json = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) !=null){
                json.append(line);
            }
            Type targetType = new TypeToken<ArrayList<Message>>(){}.getType();
            messages = gson.fromJson(json.toString(),targetType);
            System.out.println("a");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
