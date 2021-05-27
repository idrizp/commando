package me.idriz.commando.paper.message;

import me.idriz.commando.message.Message;
import org.jetbrains.annotations.NotNull;

public class PaperMessageBuilder implements Message.Builder {
	
	@Override
	@NotNull
	public Message createMessage() {
		return new PaperMessage();
	}
}
