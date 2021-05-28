package me.idriz.commando.jda.sender;

import me.idriz.commando.jda.message.JDAMessage;
import me.idriz.commando.jda.model.MessageAuthor;
import me.idriz.commando.message.Message;
import me.idriz.commando.sender.CommandoSender;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

public class UserSender implements CommandoSender<MessageAuthor> {
	
	private final MessageAuthor author;
	private final MessageChannel channel;
	
	public UserSender(MessageAuthor author) {
		this.author = author;
		this.channel = author.getMessage().getChannel();
	}
	
	@Override
	public void sendMessage(@NotNull Message message) {
		if (!(message instanceof JDAMessage)) {
			throw new UnsupportedOperationException("Unknown message type. Please provide a JDAMessage");
		}
		
		channel.sendMessage(((JDAMessage) message).getMessageBuilder().build()).queue();
	}
	
	@Override
	public @NotNull MessageAuthor getRawSender() {
		return author;
	}
}
