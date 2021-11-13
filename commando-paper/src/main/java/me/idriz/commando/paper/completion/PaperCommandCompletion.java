package me.idriz.commando.paper.completion;

import me.idriz.commando.command.Command;
import me.idriz.commando.wrapper.CommandWrapper;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.plugin.Plugin;

public class PaperCommandCompletion {

	private final CommandWrapper commandWrapper;
	private final Command command;
	private final org.bukkit.command.Command bukkitCommand;
	private final Plugin plugin;

	public PaperCommandCompletion(CommandWrapper commandWrapper, Command command, org.bukkit.command.Command bukkitCommand,
			Plugin plugin) {
		this.commandWrapper = commandWrapper;
		this.command = command;
		this.bukkitCommand = bukkitCommand;
		this.plugin = plugin;
	}

	public void legacySetup() {
		// TODO: Implement this
	}

	public void setup() {
		if (!CommodoreProvider.isSupported()) {
			plugin.getLogger()
					.info("Commodore is not supported, therefore brigadier-based completions for the command "
							+ commandWrapper.getName()
							+ " have not been registered.");
			legacySetup();
			return;
		}
		CommodoreProvider
				.getCommodore(plugin)
				.register(bukkitCommand, new BrigadierCommandCompletion(commandWrapper).buildCommandNode());
	}

}
