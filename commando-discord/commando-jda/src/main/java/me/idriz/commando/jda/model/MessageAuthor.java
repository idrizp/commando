package me.idriz.commando.jda.model;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class MessageAuthor {

    private final User user;
    private final Message message;

    public MessageAuthor(User user, Message message) {
        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public Message getMessage() {
        return message;
    }
}
