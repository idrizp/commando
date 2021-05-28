package me.idriz.commando.paper.middleware;

import java.lang.reflect.Method;
import me.idriz.commando.paper.command.PaperCommand;
import me.idriz.commando.paper.command.PaperCommand.Permission;
import me.idriz.commando.paper.sender.PaperSender;
import me.idriz.commando.wrapper.CommandWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

public class PaperPermissionMiddleware implements PaperCommandMiddleware {
	
	private TextComponent noPermissionMessage = Component.text("No permission.", NamedTextColor.RED);
	
	public TextComponent getNoPermissionMessage() {
		return noPermissionMessage;
	}
	
	public void setNoPermissionMessage(TextComponent noPermissionMessage) {
		this.noPermissionMessage = noPermissionMessage;
	}
	
	@Override
	public void onExecute(
			@NotNull CommandWrapper commandWrapper,
			@NotNull PaperSender sender,
			@NotNull String[] args,
			@NotNull MiddlewareContext context,
			@NotNull Runnable nextFunction
	) {
		String permission = null;
		if (commandWrapper.getCommand() instanceof PaperCommand) {
			PaperCommand paperCommand = (PaperCommand)commandWrapper.getCommand();
			if (paperCommand.getPermission() != null) {
				permission = paperCommand.getPermission();
			}
		}
		
		PaperCommand.Permission annotation = null;
		if (commandWrapper.getCommand().getClass().isAnnotationPresent(Permission.class)) {
			annotation = commandWrapper.getCommand().getClass().getAnnotation(Permission.class);
		}
		Method method = commandWrapper.getMethod();
		if (method != null && method.isAnnotationPresent(Permission.class)) {
			annotation = method.getAnnotation(PaperCommand.Permission.class);
		}
		
		if (permission == null && annotation != null) {
			permission = annotation.value();
		}
		
		if (permission != null && !sender.getRawSender().hasPermission(permission)) {
			sender.getRawSender().sendMessage(noPermissionMessage);
			return;
		}
		nextFunction.run();
	}
}
