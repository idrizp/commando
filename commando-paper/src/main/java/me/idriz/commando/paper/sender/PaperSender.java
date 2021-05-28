package me.idriz.commando.paper.sender;

import me.idriz.commando.message.Message;
import me.idriz.commando.paper.message.PaperMessage;
import me.idriz.commando.sender.CommandoSender;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class PaperSender implements CommandoSender<CommandSender> {
	
	private CommandSender commandSender;
	
	public PaperSender(CommandSender commandSender) {
		this.commandSender = commandSender;
	}
	
	@Override
	public void sendMessage(@NotNull Message message) {
		if (message instanceof PaperMessage) {
			commandSender.sendMessage(((PaperMessage) message).getComponent());
		}
	}
	
	@Override
	public @NotNull CommandSender getRawSender() {
		return commandSender;
	}
}
