package cz.uhk.pro2.models.api;

import cz.uhk.pro2.models.Message;

public class MessageRequest {
    private String token;
    private String text;

    public MessageRequest(String token, Message message) {
        this.token = token;
        this.text = message.toString();
    }
}
