package me.idriz.commando.javacord;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import me.idriz.commando.Commando;
import me.idriz.commando.command.Command;
import me.idriz.commando.command.backing.ExecutorBackingCommand;
import me.idriz.commando.javacord.message.JavaCordMessageBuilder;
import me.idriz.commando.javacord.sender.MessageAuthorSender;
import me.idriz.commando.message.Message;
import me.idriz.commando.wrapper.CommandWrapper;
import org.javacord.api.DiscordApi;
import org.jetbrains.annotations.NotNull;

public class JavaCordCommando extends Commando {
	
	private final Map<Command, ExecutorBackingCommand<MessageAuthorSender>> backingCommands = new HashMap<>();
	private final JavaCordMessageBuilder messageBuilder = new JavaCordMessageBuilder();
	
	private String commandPrefix;
	
	public JavaCordCommando(DiscordApi api, String commandPrefix, boolean deleteMessageAfterCommand) {
		this.commandPrefix = commandPrefix;
		api.addMessageCreateListener(messageEvent -> {
			if (!messageEvent.getMessageContent().startsWith(getCommandPrefix())) {
				return;
			}
			String command = messageEvent.getMessageContent().substring(getCommandPrefix().length());
			
			String[] args = command.split(" ");
			
			Command found = getCommands().get(args[0].toLowerCase());
			if (found == null) {
				return;
			}
			
			ExecutorBackingCommand<MessageAuthorSender> backingCommand = backingCommands.get(found);
			if (backingCommand == null) {
				throw new IllegalStateException("Didn't find backing command registered to command " + args[0]);
			}
			
			backingCommand.onExecute(new MessageAuthorSender(messageEvent.getMessageAuthor()),
					Arrays.copyOfRange(args, 1, args.length));
			
			if (deleteMessageAfterCommand) {
				messageEvent.deleteMessage().exceptionally(throwable -> {
					throwable.printStackTrace();
					return null;
				});
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
		backingCommands.put(command, new ExecutorBackingCommand<>(this, wrapper));
		return wrapper;
	}
	
	@Override
	public @NotNull Message.Builder getMessageBuilder() {
		return messageBuilder;
	}
}
