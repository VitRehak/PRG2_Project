package cz.uhk.pro2.models.api;

import cz.uhk.pro2.models.Message;

public class MessageRequest {
    private final String token;
    private final String text;

    public MessageRequest(String token, Message message) {
        this.token = token;

        //1. radek navic
        this.text = message.toString();

        //spatny auto pri login a logout
        //this.text = message.getText();
    }
}
