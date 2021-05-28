package me.idriz.commando.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import me.idriz.commando.adapter.TypeAdapter;
import me.idriz.commando.message.Message;
import me.idriz.commando.middleware.CommandMiddleware;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Command {
	
	@NotNull
	default Set<Command> getSubCommands() {
		return Collections.emptySet();
	}
	
	@Nullable
	default String getName() {
		return null;
	}
	
	@Nullable
	default String[] getAliases() {
		return null;
	}
	
	@Nullable
	default Message getUsage() {
		return null;
	}
	
	@Nullable
	default Message getDescription() {
		return null;
	}
	
	@NotNull
	default List<CommandMiddleware> getMiddleware() {
		return Collections.emptyList();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@interface Info {
		
		String[] value();
		
		String usage() default "Contact an administrator for help with usage.";
		
		String description() default "Contact an administrator for the description of this command.";
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@interface Flag {
		char value();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@interface Default {
	
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@interface UseMiddleware {
		
		Class<? extends CommandMiddleware>[] value();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@interface MiddlewareData {
		String value();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@interface OptionalArgument {
		String value() default "";
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@interface UseAdapter {
		Class<? extends TypeAdapter<?, ?>> value();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@interface Text { }
	
	
	
}
