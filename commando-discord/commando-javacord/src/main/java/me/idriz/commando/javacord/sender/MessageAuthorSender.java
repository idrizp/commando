package me.idriz.commando.javacord.sender;

import me.idriz.commando.javacord.message.JavaCordMessage;
import me.idriz.commando.message.Message;
import me.idriz.commando.sender.CommandoSender;
import org.javacord.api.entity.message.MessageAuthor;
import org.jetbrains.annotations.NotNull;

public class MessageAuthorSender implements CommandoSender<MessageAuthor> {
	
	private final MessageAuthor messageAuthor;
	
	public MessageAuthorSender(MessageAuthor messageAuthor) {
		this.messageAuthor = messageAuthor;
	}
	
	@Override
	public void sendMessage(@NotNull Message message) {
		if (!(message instanceof JavaCordMessage)) {
			throw new UnsupportedOperationException("Unknown message type. Please provide a JavaCordMessage.");
		}
		((JavaCordMessage) message).getMessageBuilder().send(messageAuthor.getMessage().getChannel());
	}
	
	@Override
	public @NotNull MessageAuthor getRawSender() {
		return messageAuthor;
	}
}
