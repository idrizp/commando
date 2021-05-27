package me.idriz.commando.example.paper.command;

import me.idriz.commando.command.Command;
import me.idriz.commando.command.Command.Info;
import me.idriz.commando.paper.command.PaperCommand.Permission;
import org.bukkit.command.CommandSender;

@Info("example")
@Permission("example.command")
public class ExampleCommand implements Command {
	
	@Default
	public void onExample(CommandSender sender) {
		sender.sendMessage("Hello World");
	}
	
	@Info("test")
	public void onTest(CommandSender sender) {
		sender.sendMessage("Test");
	}
	
}
