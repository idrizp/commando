package me.idriz.commando.javacord.message;

import me.idriz.commando.message.Message;
import org.javacord.api.entity.message.MessageBuilder;

public class JavaCordMessage implements Message {
	
	private MessageBuilder messageBuilder = new MessageBuilder();
	
	@Override
	public void setContent(String content) {
		messageBuilder.setContent(content);
	}
	
	@Override
	public void append(Message appended) {
		if (!(appended instanceof JavaCordMessage)) {
			throw new UnsupportedOperationException("");
		}
		messageBuilder.append(((JavaCordMessage) appended).messageBuilder);
	}
	
	@Override
	public void setColor(int red, int green, int blue) {
		// You can't set colors of messages in JavaCord.
	}
}
