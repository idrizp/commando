package me.idriz.commando.middleware;

import java.util.HashMap;
import java.util.Map;
import me.idriz.commando.sender.CommandoSender;
import me.idriz.commando.wrapper.CommandWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandMiddleware {
	
	interface CommandLineCommandMiddleware extends CommandMiddleware {
		
		void onExecute(@NotNull CommandWrapper commandWrapper, @NotNull String[] args, @NotNull MiddlewareContext context,
				@NotNull Runnable nextFunction);
	}
	
	interface SenderCommandMiddleware<T extends CommandoSender<?>> extends CommandMiddleware {
		
		void onExecute(@NotNull CommandWrapper commandWrapper, @NotNull T sender, @NotNull String[] args,
				@NotNull MiddlewareContext context, @NotNull Runnable nextFunction);
	}
	
	class MiddlewareContext {
		
		private final Map<String, Object> data = new HashMap<>();
		
		public <T> void insert(@NotNull String name, @NotNull T object) {
			data.put(name, object);
		}
		
		@Nullable
		public <T> T get(String name) {
			return (T) data.get(name);
		}
	}
	
}
