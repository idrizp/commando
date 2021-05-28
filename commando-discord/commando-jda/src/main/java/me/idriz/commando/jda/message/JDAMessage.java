package me.idriz.commando.jda.message;

import me.idriz.commando.message.Message;
import net.dv8tion.jda.api.MessageBuilder;

public class JDAMessage implements Message {

    private MessageBuilder messageBuilder = new MessageBuilder();

    @Override
    public void setContent(String content) {
        messageBuilder.setContent(content);
    }

    @Override
    public void append(Message appended) {
        if (!(appended instanceof JDAMessage)) {
            throw new UnsupportedOperationException("");
        }
        messageBuilder.append(((JDAMessage) appended).messageBuilder);
    }

    @Override
    public void setColor(int red, int green, int blue) {
        // You can't set colors of messages in JDA.
    }

    public MessageBuilder getMessageBuilder() {
        return messageBuilder;
    }
}
