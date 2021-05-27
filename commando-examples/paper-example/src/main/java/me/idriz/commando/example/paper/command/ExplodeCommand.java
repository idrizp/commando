package me.idriz.commando.example.paper.command;

import me.idriz.commando.command.Command;
import me.idriz.commando.command.Command.Info;
import me.idriz.commando.paper.command.PaperCommand.Permission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Info("explode")
@Permission("explode.command")
public class ExplodeCommand implements Command {
	
	@Default
	public void onExplode(Player sender, int power) {
		sender.sendMessage("Exploding " + power);
		sender.getLocation().createExplosion(power);
	}
	
	@Info("test")
	public void onBig(Player sender, @Text String message, @Flag('b') boolean big, @Flag('s') boolean silent) {
		sender.sendMessage("Exploding " + (big ? "big" : "small") + ", also " + message);
		if (!silent) {
			sender.sendMessage(Component.text("An explosion happened!", NamedTextColor.RED));
		}
		sender.getLocation().createExplosion(big ? 10 : 1);
	}
	
}
