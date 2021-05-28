package me.idriz.commando.jda.message;

import me.idriz.commando.message.Message;
import org.jetbrains.annotations.NotNull;

public class JDAMessageBuilder implements Message.Builder{
    @Override
    public @NotNull Message createMessage() {
        return new JDAMessage();
    }
}
