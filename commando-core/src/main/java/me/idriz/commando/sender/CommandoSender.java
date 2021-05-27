package me.idriz.commando.sender;

import me.idriz.commando.message.Message;
import org.jetbrains.annotations.NotNull;

public interface CommandoSender<T> {
	
	void sendMessage(@NotNull Message message);
	
	default void sendMessages(@NotNull Message... messages) {
		for (Message message : messages) {
			sendMessage(message);
		}
	}
	
	@NotNull
	T getRawSender();
	
}
