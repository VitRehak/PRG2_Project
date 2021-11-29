package cz.uhk.pro2.models.database;

import cz.uhk.pro2.models.Message;

import java.util.List;

public interface DatabaseOperations {

    void addMessage(Message message);

    List<Message> getMessages();
}
