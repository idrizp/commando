package me.idriz.commando.paper.command.adapter.impl;

import java.lang.reflect.Parameter;
import java.util.UUID;
import me.idriz.commando.paper.command.adapter.PaperTypeAdapter;
import me.idriz.commando.paper.sender.PaperSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class PlayerTypeAdapter implements PaperTypeAdapter<Player> {
	
	@Override
	public @Nullable Player adapt(PaperSender sender, Parameter parameter, String argument, String[] originalArgs,
			String[] args) {
		try {
			UUID id = UUID.fromString(argument);
			return Bukkit.getPlayer(id);
		} catch(IllegalArgumentException exception) {
			return Bukkit.getPlayer(argument);
		}
	}
	
	@Override
	public void onError(PaperSender sender, Parameter parameter, String argument, String[] originalArgs, String[] args) {
		sender.getRawSender().sendMessage(Component.text("Player not found.", NamedTextColor.RED));
	}
}
