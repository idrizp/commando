package me.idriz.commando;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.idriz.commando.adapter.BooleanTypeAdapter;
import me.idriz.commando.adapter.DoubleTypeAdapter;
import me.idriz.commando.adapter.IntegerTypeAdapter;
import me.idriz.commando.adapter.LongTypeAdapter;
import me.idriz.commando.adapter.StringTypeAdapter;
import me.idriz.commando.adapter.TypeAdapter;
import me.idriz.commando.command.Command;
import me.idriz.commando.message.Message;
import me.idriz.commando.middleware.CommandMiddleware;
import me.idriz.commando.wrapper.CommandWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Commando {
	
	private final Map<String, Command> commands = new HashMap<>();
	private final Map<String, CommandWrapper> commandWrappers = new HashMap<>();
	
	private final Map<Class<?>, TypeAdapter<?, ?>> adapters = new HashMap<>();
	private final Map<Class<? extends TypeAdapter<?, ?>>, TypeAdapter<?, ?>> customAdapters = new HashMap<>();
	
	private final Map<Class<? extends CommandMiddleware>, CommandMiddleware> middlewareMap = new HashMap<>();
	private final List<CommandMiddleware> defaultMiddlewareChain = new ArrayList<>();
	
	public Commando() {
		registerTypeAdapter(Integer.class, new IntegerTypeAdapter<>(this));
		registerTypeAdapter(int.class, new IntegerTypeAdapter<>(this));
		
		registerTypeAdapter(Double.class, new DoubleTypeAdapter<>(this));
		registerTypeAdapter(double.class, new DoubleTypeAdapter<>(this));
		
		registerTypeAdapter(Long.class, new LongTypeAdapter<>(this));
		registerTypeAdapter(long.class, new LongTypeAdapter<>(this));
		
		registerTypeAdapter(String.class, new StringTypeAdapter<>(this));
		
		registerTypeAdapter(Boolean.class, new BooleanTypeAdapter<>(this));
		registerTypeAdapter(boolean.class, new BooleanTypeAdapter<>(this));
	}
	
	public Map<Class<? extends CommandMiddleware>, CommandMiddleware> getMiddlewareMap() {
		return Map.copyOf(middlewareMap);
	}
	
	public List<CommandMiddleware> getDefaultMiddlewareChain() {
		return List.copyOf(defaultMiddlewareChain);
	}
	
	@Nullable
	public <T extends CommandMiddleware> T getMiddleware(Class<T> clazz) {
		try {
			CommandMiddleware commandMiddleware = middlewareMap.get(clazz);
			return clazz.cast(commandMiddleware);
		} catch (ClassCastException exception) {
			exception.printStackTrace();
			return null;
		}
	}
	
	public <T extends TypeAdapter<?, ?>> void registerTypeAdapter(Class<?> type, T adapter) {
		adapters.put(type, adapter);
	}
	
	public <T extends TypeAdapter<?, ?>> void registerCustomAdapter(T adapter) {
		customAdapters.put((Class<T>) adapter.getClass(), adapter);
	}
	
	public <T extends TypeAdapter<?, ?>> T getTypeAdapter(Class<?> type) {
		return (T) adapters.get(type);
	}
	
	public <T extends TypeAdapter<?, ?>> T getCustomAdapter(Class<T> adapterClass) {
		return (T) customAdapters.get(adapterClass);
	}
	
	public <T extends CommandMiddleware> Commando registerMiddleware(T middleware) {
		middlewareMap.put(middleware.getClass(), middleware);
		return this;
	}
	
	public <T extends CommandMiddleware> Commando use(Class<T> clazz) {
		T middleware = getMiddleware(clazz);
		if (middleware == null) {
			throw new IllegalStateException(
					"Couldn't find middleware of class " + clazz + ". Are you sure it's registered?");
		}
		defaultMiddlewareChain.add(middleware);
		return this;
	}
	
	public <T extends CommandMiddleware> Commando use(@NotNull T middleware, boolean register) {
		if (register) {
			registerMiddleware(middleware);
		}
		defaultMiddlewareChain.add(middleware);
		return this;
	}
	
	public <T extends CommandMiddleware> Commando use(@NotNull T middleware) {
		use(middleware, true);
		return this;
	}
	
	public Commando use(boolean register, @NotNull CommandMiddleware... middlewares) {
		for (CommandMiddleware middleware : middlewares) {
			use(middleware, register);
		}
		return this;
	}
	
	public Commando use(@NotNull CommandMiddleware... middlewares) {
		use(true, middlewares);
		return this;
	}
	
	public CommandWrapper registerCommand(Command command) {
		CommandWrapper wrapper = new CommandWrapper(this, null, command, false);
		for (String name : wrapper.getAllNames()) {
			commandWrappers.put(name, wrapper);
			commands.put(name, command);
		}
		return wrapper;
	}
	
	public Map<String, Command> getCommands() {
		return Map.copyOf(commands);
	}
	
	public Map<String, CommandWrapper> getCommandWrappers() {
		return Map.copyOf(commandWrappers);
	}
	
	@NotNull
	public abstract Message.Builder getMessageBuilder();
}
