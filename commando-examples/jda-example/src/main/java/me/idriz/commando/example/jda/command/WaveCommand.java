package me.idriz.commando.example.jda.command;

import me.idriz.commando.command.Command;
import me.idriz.commando.jda.model.MessageAuthor;
import net.dv8tion.jda.api.MessageBuilder;

@Command.Info("wave")
public class WaveCommand implements Command {
	
	@Default
	public void onExecute(MessageAuthor author, @Text String message, @Flag('e') boolean evil) {
		author.getMessage().getChannel().sendMessage(new MessageBuilder()
				.append(":wave:, " + (evil ? "fuck you" : "thanks") + " for saying " + message + ", " + author.getUser()
						.getName()).build()).queue();
	}
	
	@Info("test")
	public void onTest(MessageAuthor author, int number) {
		author.getMessage().getChannel().sendMessage(new MessageBuilder()
				.append(":middle_finger: Don't test me. I've told you " + number + " time(s).").build()).queue();
	}
	
}

