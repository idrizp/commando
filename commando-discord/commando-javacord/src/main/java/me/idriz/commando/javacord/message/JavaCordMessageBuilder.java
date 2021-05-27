package me.idriz.commando.javacord.message;

import me.idriz.commando.message.Message;
import org.jetbrains.annotations.NotNull;

public class JavaCordMessageBuilder implements Message.Builder {
	
	@Override
	public @NotNull Message createMessage() {
		return new JavaCordMessage();
	}
}