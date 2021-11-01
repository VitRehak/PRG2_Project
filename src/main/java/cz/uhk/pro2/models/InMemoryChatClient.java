package cz.uhk.pro2.models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class InMemoryChatClient implements ChatClient {
    private String loggedUser;
    private List<Message> messages;
    private List<String> loggedUsers;
    private List<ActionListener> listenersLoggedUserChanged = new ArrayList<>();
    private List<ActionListener> listenerMessageAdded = new ArrayList<>();

    public InMemoryChatClient() {
        messages = new ArrayList<>();
        loggedUsers = new ArrayList<>();
    }

    @Override
    public Boolean isAuthenticated() {
        return loggedUser != null;
    }

    @Override
    public void login(String userName) {
        loggedUser = userName;
        loggedUsers.add(userName);
        messages.add(new Message(Message.USER_LOGGED_IN, userName));
        raiseEventListenerUsersChanged();
        raiseEventListenerMessageAdded();
    }

    @Override
    public void logout() {
        loggedUsers.remove(loggedUser);
        messages.add(new Message(Message.USER_LOGGED_OUT, loggedUser));
        loggedUser = null;
        raiseEventListenerUsersChanged();
        raiseEventListenerMessageAdded();
    }

    @Override
    public void sendMessage(String text) {
        messages.add(new Message(loggedUser, text));
        raiseEventListenerMessageAdded();
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
        listenersLoggedUserChanged.forEach(l -> {
            l.actionPerformed(new ActionEvent(this, 1, "listenerLoggedUsersChanged"));
        });
    }

    private void raiseEventListenerMessageAdded() {
        listenerMessageAdded.forEach(m -> {
            m.actionPerformed(new ActionEvent(this, 1, "listenerMessageAdded"));
        });
    }
}