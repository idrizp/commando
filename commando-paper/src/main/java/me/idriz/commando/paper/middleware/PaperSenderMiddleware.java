package me.idriz.commando.paper.middleware;

import java.lang.reflect.Method;
import me.idriz.commando.paper.sender.PaperSender;
import me.idriz.commando.wrapper.CommandWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PaperSenderMiddleware implements PaperCommandMiddleware {
	
	private TextComponent nonConsoleSenderOnly = Component.text("This command can only be run by a non-console command sender.", NamedTextColor.RED);
	private TextComponent consoleSenderOnly = Component.text("This command can only be run by console.", NamedTextColor.RED);
	
	public void setConsoleSenderOnly(TextComponent consoleSenderOnly) {
		this.consoleSenderOnly = consoleSenderOnly;
	}
	
	public void setNonConsoleSenderOnly(TextComponent nonConsoleSenderOnly) {
		this.nonConsoleSenderOnly = nonConsoleSenderOnly;
	}
	
	public TextComponent getConsoleSenderOnly() {
		return consoleSenderOnly;
	}
	
	public TextComponent getNonConsoleSenderOnly() {
		return nonConsoleSenderOnly;
	}
	
	@Override
	public void onExecute(
			@NotNull CommandWrapper commandWrapper,
			@NotNull PaperSender sender,
			@NotNull String[] args,
			@NotNull MiddlewareContext context,
			@NotNull Runnable nextFunction
	) {
		Method method = commandWrapper.getMethod();
		if (method == null) {
			nextFunction.run();
			return;
		}
		Class<?> parameterType = method.getParameterTypes()[0];
		if (!CommandSender.class.isAssignableFrom(parameterType) || parameterType.equals(CommandSender.class)) {
			nextFunction.run();
			return;
		}
		if (sender.getRawSender() instanceof ConsoleCommandSender && !parameterType.equals(ConsoleCommandSender.class)) {
			sender.getRawSender().sendMessage(nonConsoleSenderOnly);
			return;
		}
		
		if (sender.getRawSender() instanceof Player && !parameterType.equals(Player.class)) {
			sender.getRawSender().sendMessage(consoleSenderOnly);
			return;
		}
		nextFunction.run();
	}
}
