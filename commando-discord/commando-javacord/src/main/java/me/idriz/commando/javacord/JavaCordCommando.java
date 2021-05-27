package me.idriz.commando.javacord;

import me.idriz.commando.Commando;
import me.idriz.commando.command.Command;
import me.idriz.commando.javacord.message.JavaCordMessageBuilder;
import me.idriz.commando.message.Message;
import me.idriz.commando.wrapper.CommandWrapper;
import org.javacord.api.DiscordApi;
import org.jetbrains.annotations.NotNull;

public class JavaCordCommando extends Commando {
	
	private final JavaCordMessageBuilder messageBuilder = new JavaCordMessageBuilder();
	private final DiscordApi api;
	private String commandPrefix;
	
	public JavaCordCommando(DiscordApi api, String commandPrefix) {
		this.api = api;
		this.commandPrefix = commandPrefix;
		api.addMessageCreateListener(messageEvent -> {
			if (!messageEvent.getMessageContent().startsWith(getCommandPrefix())) {
				return;
			}
		});
	}
	
	public String getCommandPrefix() {
		return commandPrefix;
	}
	
	public void setCommandPrefix(String commandPrefix) {
		this.commandPrefix = commandPrefix;
	}
	
	@Override
	public CommandWrapper registerCommand(Command command) {
		CommandWrapper wrapper = super.registerCommand(command);
		return wrapper;
	}
	
	@Override
	public @NotNull Message.Builder getMessageBuilder() {
		return messageBuilder;
	}
}
