package com.example.smartai_basedfiremonitoring.Model;

public class ChatBotModel {
    private String message;
    private boolean isBot;

    // Constructor
    public ChatBotModel(String message, boolean isBot) {
        this.message = message;
        this.isBot = isBot;
    }

    // Getter for message
    public String getMessage() {
        return message;
    }

    // Setter for message
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter for isBot
    public boolean isBot() {
        return isBot;
    }

    // Setter for isBot
    public void setBot(boolean bot) {
        isBot = bot;
    }
}
