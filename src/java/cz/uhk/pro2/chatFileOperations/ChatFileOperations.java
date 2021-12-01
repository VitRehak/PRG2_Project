package cz.uhk.pro2.chatFileOperations;

import cz.uhk.pro2.models.Message;

import java.util.List;

public interface ChatFileOperations {

    List<Message>loadMessages();
    void writeMessagesToFile (List<Message> messages);
}
