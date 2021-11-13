package me.idriz.commando.paper.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.idriz.commando.command.Command;
import org.jetbrains.annotations.Nullable;

public interface PaperCommand extends Command {
	
	@Nullable
	default String getPermission() {
		return null;
	}

	/**
	 * Defines the permission for a command.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD, ElementType.TYPE})
	@interface Permission {
		String value();
	}
	
}
