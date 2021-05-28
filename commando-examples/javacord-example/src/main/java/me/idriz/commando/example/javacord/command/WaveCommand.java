package me.idriz.commando.example.javacord.command;

import me.idriz.commando.command.Command;
import me.idriz.commando.command.Command.Info;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.MessageBuilder;

@Info("wave")
public class WaveCommand implements Command {
	
	@Default
	public void onExecute(MessageAuthor author, @Text String message, @Flag('e') boolean evil) {
		new MessageBuilder()
				.append(":wave:, " + (evil ? "fuck you" : "thanks") + " for saying " + message + ", " + author.getDisplayName())
				.send(author.getMessage().getChannel());
	}
	
	@Info("test")
	public void onTest(MessageAuthor author, int number) {
		new MessageBuilder()
				.append(":middle_finger: Don't test me. I've told you " + number + " time(s).")
				.send(author.getMessage().getChannel());
	}
	
}
