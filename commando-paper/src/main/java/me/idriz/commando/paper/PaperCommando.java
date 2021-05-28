package me.idriz.commando.paper;

import java.util.Arrays;
import java.util.List;
import me.idriz.commando.Commando;
import me.idriz.commando.command.Command;
import me.idriz.commando.command.backing.ExecutorBackingCommand;
import me.idriz.commando.message.Message;
import me.idriz.commando.paper.command.adapter.impl.PlayerTypeAdapter;
import me.idriz.commando.paper.message.PaperMessageBuilder;
import me.idriz.commando.paper.middleware.PaperPermissionMiddleware;
import me.idriz.commando.paper.middleware.PaperSenderMiddleware;
import me.idriz.commando.paper.sender.PaperSender;
import me.idriz.commando.wrapper.CommandWrapper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class PaperCommando extends Commando {
	
	private final @NotNull PaperMessageBuilder paperMessageBuilder = new PaperMessageBuilder();
	private final @NotNull Plugin plugin;
	
	public PaperCommando(@NotNull Plugin plugin) {
		super();
		
		this.plugin = plugin;
		registerTypeAdapter(Player.class, new PlayerTypeAdapter());
		use(
				new PaperSenderMiddleware(),
				new PaperPermissionMiddleware()
		);
	}
	
	@Override
	public @NotNull Message.Builder getMessageBuilder() {
		return paperMessageBuilder;
	}
	
	@Override
	public CommandWrapper registerCommand(Command command) {
		CommandWrapper wrapper = super.registerCommand(command);
		ExecutorBackingCommand<PaperSender> executorBackingCommand = new ExecutorBackingCommand<>(this, wrapper);
		plugin.getServer().getCommandMap().register(plugin.getName(), new org.bukkit.command.Command(wrapper.getName()) {
			@Override
			public @NotNull List<String> getAliases() {
				return Arrays.asList(wrapper.getAliases());
			}
			
			@Override
			public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
				executorBackingCommand.onExecute(new PaperSender(sender), args);
				return true;
			}
		});
		return wrapper;
	}
}
