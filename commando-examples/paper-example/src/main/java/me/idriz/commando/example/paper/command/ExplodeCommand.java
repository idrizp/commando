package me.idriz.commando.example.paper.command;

import java.util.List;
import me.idriz.commando.command.Command;
import me.idriz.commando.command.Command.Info;
import me.idriz.commando.example.paper.middleware.TestMiddleware;
import me.idriz.commando.middleware.CommandMiddleware;
import me.idriz.commando.paper.command.PaperCommand.Permission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Info("explode")
@Permission("explode.command")
public class ExplodeCommand implements Command {
	
	@Override
	public @NotNull List<CommandMiddleware> getMiddleware() {
		return List.of(new TestMiddleware());
	}
	
	@Default
	public void onExplode(Player sender, int power, @MiddlewareData("hello") int data) {
		sender.sendMessage("Exploding " + (power + data));
		sender.getLocation().createExplosion(power + data);
	}
	
	@Info("test")
	public void onBig(Player sender, @Text String message, @Flag('b') boolean big, @Flag('s') boolean silent, @MiddlewareData("hello") int hello) {
		sender.sendMessage("Exploding " + (big ? "big" : "small") + ", also " + message);
		if (!silent) {
			sender.sendMessage(Component.text("An explosion happened!" + hello, NamedTextColor.RED));
		}
		sender.getLocation().createExplosion(big ? 10 : 1);
	}
	
}
