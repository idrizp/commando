package me.idriz.commando.adapter;

import java.lang.reflect.Parameter;
import me.idriz.commando.sender.CommandoSender;
import org.jetbrains.annotations.Nullable;

public interface TypeAdapter<T, R extends CommandoSender<?>> {
	
	@Nullable
	T adapt(R sender, Parameter parameter, String argument, String[] originalArgs, String[] args);
	
	void onError(R sender, Parameter parameter, String argument, String[] originalArgs, String[] args);
	
}
