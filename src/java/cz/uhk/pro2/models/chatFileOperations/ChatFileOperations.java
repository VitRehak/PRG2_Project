package cz.uhk.pro2.models.chatFileOperations;

import cz.uhk.pro2.models.Message;

import java.util.List;

public interface ChatFileOperations {

    List<Message>loadMessages();
    void writeMessagesToFile (List<Message> messages);

    List<String>loadLoggedUsers();
    void writeLoggedUsers (List<String> users);
}
