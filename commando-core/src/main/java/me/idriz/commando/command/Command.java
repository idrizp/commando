package me.idriz.commando.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
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

	/**
	 * Defines command information
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD, ElementType.TYPE})
	@interface Info {
		String[] value();
		
		String usage() default "Contact an administrator for help with usage.";
		
		String description() default "Contact an administrator for the description of this command.";
	}

	/**
	 * Optional, gives a display name to the argument.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.PARAMETER})
	@interface Argument {
		String value();
	}

	/**
	 * A command flag.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	@interface Flag {
		char value();
	}

	/**
	 * Highlights the method that is the base executor of the command when no subcommand is executed.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface Default {
	
	}

	/**
	 * Uses a set of middleware providers by their class.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE, ElementType.METHOD})
	@interface UseMiddleware {
		Class<? extends CommandMiddleware>[] value();
	}

	/**
	 * Provides middleware data to the parameter.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	@interface MiddlewareData {
		String value();
	}

	/**
	 * Makes an argument optional.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	@interface OptionalArgument {
		String value() default "";
	}

	/**
	 * Uses a specific type adapter.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	@interface UseAdapter {
		Class<? extends TypeAdapter<?, ?>> value();
	}

	/**
	 * Makes a string greedy.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	@interface Text { }


	/**
	 * For numeric values such as integers, doubles, and longs.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	@interface Range {
		int min();
		int max() default Integer.MAX_VALUE;
	}


}
