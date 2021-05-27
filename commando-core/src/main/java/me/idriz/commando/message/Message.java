package me.idriz.commando.message;

import org.jetbrains.annotations.NotNull;

public interface Message {
	
	void setContent(String content);
	
	void append(Message appended);
	
	void setColor(int red, int green, int blue);
	
	interface Builder {
		@NotNull
		Message createMessage();
		
		@NotNull
		default Message createMessage(String content, int red, int green, int blue) {
			Message message = createMessage();
			message.setContent(content);
			message.setColor(red, green, blue);
			return message;
		}
		
	}
	
}
