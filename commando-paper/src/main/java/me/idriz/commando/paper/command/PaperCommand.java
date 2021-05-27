package me.idriz.commando.paper.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import me.idriz.commando.command.Command;
import org.jetbrains.annotations.Nullable;

public interface PaperCommand extends Command {
	
	@Nullable
	default String getPermission() {
		return null;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@interface Permission {
		String value();
	}
	
}
