package me.idriz.commando.example.paper.middleware;

import me.idriz.commando.middleware.CommandMiddleware.SenderCommandMiddleware;
import me.idriz.commando.paper.sender.PaperSender;
import me.idriz.commando.wrapper.CommandWrapper;
import org.jetbrains.annotations.NotNull;

public class TestMiddleware implements SenderCommandMiddleware<PaperSender> {
	
	@Override
	public void onExecute(@NotNull CommandWrapper commandWrapper, @NotNull PaperSender sender,
			@NotNull String[] args, @NotNull MiddlewareContext context, @NotNull Runnable nextFunction) {
		context.insert("hello", 5);
		nextFunction.run();
	}
}
