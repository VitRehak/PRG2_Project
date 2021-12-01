package cz.uhk.pro2.models;

import java.time.LocalDateTime;

public class Message {
    private final String author;
    private String text;
    private final LocalDateTime created;

    public static final int USER_LOGGED_IN = 1;
    public static final int USER_LOGGED_OUT = 2;

    private final String AUTHOR_SYSTEM = "System";

    public Message(String author, String text) {
        this.author = author;
        this.text = text;
        created = LocalDateTime.now();
    }

    public Message(int type, String userName) {
        author = AUTHOR_SYSTEM;
        created = LocalDateTime.now();
        if (type == USER_LOGGED_IN) {
            text = userName + " " + "has joined the chat";
        } else if (type == USER_LOGGED_OUT) {
            text = userName + " " + "has left the chat";
        }
    }

    public Message(String author, String text, LocalDateTime created) {
        this.author = author;
        this.text = text;
        this.created = created;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    @Override
    public String toString() {
        String msg = author + " [" + created + "]\n";
        msg += text + "\n";
        return msg;
    }
}
