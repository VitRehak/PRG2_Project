package cz.uhk.pro2.models;

import cz.uhk.pro2.models.chatFileOperations.ChatFileOperations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ToFileChatClient implements ChatClient {
    private ChatFileOperations chatFileOperations;
    private String loggedUser;
    private List<Message> messages;
    private List<String> loggedUsers;
    private List<ActionListener> listenersLoggedUserChanged = new ArrayList<>();
    private List<ActionListener> listenerMessageAdded = new ArrayList<>();

    public ToFileChatClient(ChatFileOperations chatFileOperations) {
        messages = new ArrayList<>();
        loggedUsers = new ArrayList<>();
        this.chatFileOperations = chatFileOperations;
        messages = chatFileOperations.loadMessages();
        raiseEventListenerMessageAdded();
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

    @Override
    public void addActionListenerMessageAdded(ActionListener toAdd) {
        listenerMessageAdded.add(toAdd);
    }

    private void raiseEventListenerUsersChanged() {
        listenersLoggedUserChanged.forEach(l -> l.actionPerformed(new ActionEvent(this, 1, "listenerLoggedUsersChanged")));
    }

    private void raiseEventListenerMessageAdded() {
        listenerMessageAdded.forEach(m -> m.actionPerformed(new ActionEvent(this, 1, "listenerMessageAdded")));
    }

    private void addMessage(Message message) {
        if (messages == null)
            messages = new ArrayList<>();
        messages.add(message);
        chatFileOperations.writeMessagesToFile(messages);
        raiseEventListenerMessageAdded();
    }
}
