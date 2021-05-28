package me.idriz.commando.command.backing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.idriz.commando.Commando;
import me.idriz.commando.adapter.TypeAdapter;
import me.idriz.commando.command.Command.Flag;
import me.idriz.commando.command.Command.MiddlewareData;
import me.idriz.commando.command.Command.OptionalArgument;
import me.idriz.commando.command.Command.UseAdapter;
import me.idriz.commando.middleware.CommandMiddleware;
import me.idriz.commando.middleware.CommandMiddleware.MiddlewareContext;
import me.idriz.commando.middleware.CommandMiddleware.SenderCommandMiddleware;
import me.idriz.commando.sender.CommandoSender;
import me.idriz.commando.wrapper.CommandWrapper;
import org.jetbrains.annotations.NotNull;

public class ExecutorBackingCommand<T extends CommandoSender<?>> {
	
	private static final String FLAG_PREFIX = "-";
	
	private final @NotNull Commando commando;
	private final @NotNull CommandWrapper commandWrapper;
	
	private final @NotNull Map<CommandWrapper, ExecutorBackingCommand<T>> subCommandExecutors;
	
	public ExecutorBackingCommand(
			@NotNull Commando commando,
			@NotNull CommandWrapper command
	) {
		this.commandWrapper = command;
		this.commando = commando;
		this.subCommandExecutors = new HashMap<>();
		commandWrapper.getSubCommands().forEach((name, wrapper) -> subCommandExecutors
				.put(wrapper, new ExecutorBackingCommand<>(commando, wrapper)));
	}
	
	public void onExecute(T sender, String[] args) {
		traverseMiddleware(sender, args, null, 0, commandWrapper.getActingMiddleware());
	}
	
	private void executeCommand(T sender, String[] args, MiddlewareContext context) {
		Method method = commandWrapper.getMethod();
		if (method == null) {
			handleEmptyMethod(sender, args, context);
			return;
		}
		
		Parameter[] parameters = commandWrapper.getMethod().getParameters();
		List<Object> objects = new ArrayList<>(method.getParameters().length);
		for (int i = 0; i < method.getParameters().length; i++) {
			objects.add(null);
		}
		
		boolean hasSender =
				parameters.length != 0 && parameters[0].getType().isAssignableFrom(sender.getRawSender().getClass());
		
		int requiredParameterCount = commandWrapper.getRequiredParameterCount();
		if (hasSender) {
			objects.set(0, sender.getRawSender());
		}
		
		String[] pureArgs = fillFlags(objects, sender, args);
		if (pureArgs.length < (hasSender ? requiredParameterCount - 1 : requiredParameterCount)) {
			sender.sendMessage(commandWrapper.getUsage());
			return;
		}
		
		fillMiddlewareData(objects, method, context);
		for (int i = 0; i < pureArgs.length; i++) {
			String arg = pureArgs[i];
			CommandWrapper subCommand = this.commandWrapper.getSubCommands().get(arg);
			if (subCommand != null) {
				handleSubCommand(sender, pureArgs, context, subCommand);
				return;
			}
			int parameterIndex = hasSender ? i + 1 : i;
			
			while (parameterIndex < parameters.length && parameters[parameterIndex].isAnnotationPresent(Flag.class)) {
				parameterIndex++;
			}
			
			if (parameterIndex >= parameters.length) {
				continue;
			}
			
			if (objects.get(parameterIndex) != null) {
				continue;
			}
			
			Parameter parameter = parameters[parameterIndex];
			
			if (!fillParameter(objects, sender, pureArgs, pureArgs[i], i, parameterIndex, parameter)) {
				return;
			}
		}
		try {
			method.invoke(commandWrapper.getCommand(), objects.toArray(new Object[0]));
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	private void handleSubCommand(T sender, String[] args, MiddlewareContext context, CommandWrapper subCommand) {
		ExecutorBackingCommand<T> subCommandExecutor = subCommandExecutors.get(subCommand);
		if (subCommandExecutor == null) {
			throw new NullPointerException("Couldn't find executor for subcommand " + args[0]);
		}
		subCommandExecutor.onExecute(sender, Arrays.copyOfRange(args, 1, args.length));
	}
	
	private boolean detectAndFillFlags(List<Object> objects, T sender, String[] args, String currentArgument) {
		char[] characters = currentArgument.toCharArray();
		for (char character : characters) {
			Integer index = commandWrapper.getFlags().get(character);
			if (index == null) {
				return false;
			}
			objects.set(index, true);
		}
		return true;
	}
	
	private String[] fillFlags(List<Object> objects, T sender, String[] args) {
		List<String> list = new ArrayList<>();
		for (String arg : args) {
			if (arg.startsWith(FLAG_PREFIX) && detectAndFillFlags(objects, sender, args, arg.substring(1))) {
				continue;
			}
			list.add(arg);
		}
		commandWrapper.getFlags().forEach((character, index) -> {
			if (objects.get(index) == null) {
				objects.set(index, false);
			}
		});
		return list.toArray(new String[0]);
	}
	
	private boolean fillParameter(List<Object> objects, T sender, String[] args, String currentArgument,
			int currentArgumentIndex, int parameterIndex,
			Parameter parameter) {
		TypeAdapter<?, T> adapter;
		String typeName = parameter.getType().getSimpleName();
		if (parameter.isAnnotationPresent(UseAdapter.class)) {
			UseAdapter adapterAnnotation = parameter.getAnnotation(UseAdapter.class);
			adapter = (TypeAdapter<?, T>) commando.getCustomAdapter(adapterAnnotation.value());
			typeName = adapterAnnotation.value().getSimpleName();
		} else {
			adapter = commando.getTypeAdapter(parameter.getType());
		}
		if (adapter == null) {
			throw new NullPointerException("Couldn't find adapter for parameter " + parameter.getName() + ": " + typeName);
		}
		String[] copy = Arrays.copyOfRange(args, currentArgumentIndex, args.length);
		Object value = adapter.adapt(sender, parameter, currentArgument, args, copy);
		
		OptionalArgument optionalArgument = parameter.getAnnotation(OptionalArgument.class);
		if (value == null && optionalArgument == null) {
			adapter.onError(sender, parameter, currentArgument, args, copy);
			return false;
		}
		
		if (optionalArgument != null && !optionalArgument.value().isEmpty()) {
			value = adapter.adapt(sender, parameter, optionalArgument.value(), args, copy);
		}
		objects.set(parameterIndex, value);
		return true;
	}
	
	private void handleEmptyMethod(T sender, String[] args, MiddlewareContext context) {
		if (args.length == 0) {
			sender.sendMessage(commandWrapper.getUsage());
			return;
		}
		CommandWrapper subCommand = this.commandWrapper.getSubCommands().get(args[0].toLowerCase());
		if (subCommand == null) {
			sender.sendMessage(commandWrapper.getUsage());
			return;
		}
		ExecutorBackingCommand<T> subCommandExecutor = subCommandExecutors.get(subCommand);
		if (subCommandExecutor == null) {
			throw new NullPointerException("Couldn't find executor for subcommand " + args[0]);
		}
		subCommandExecutor.onExecute(sender, Arrays.copyOfRange(args, 1, args.length));
	}
	
	private void fillMiddlewareData(List<Object> objects, Method method, MiddlewareContext context) {
		for (int i = 0; i < method.getParameters().length; i++) {
			Parameter parameter = method.getParameters()[i];
			if (!parameter.isAnnotationPresent(MiddlewareData.class)) {
				continue;
			}
			
			MiddlewareData data = parameter.getAnnotation(MiddlewareData.class);
			Object value = context.get(data.value());
			if (value != null && !parameter.getType().isAssignableFrom(value.getClass())) {
				throw new IllegalStateException(
						"Mismatch between MiddlewareData parameter type " + parameter.getType().getSimpleName());
			}
			
			objects.set(i, value);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void traverseMiddleware(
			T sender,
			String[] args,
			MiddlewareContext context,
			int index,
			List<CommandMiddleware> list
	) {
		if (context == null) {
			context = new MiddlewareContext();
		}
		if (list.isEmpty()) {
			executeCommand(sender, args, context);
			return;
		}
		try {
			SenderCommandMiddleware<T> middleware = (SenderCommandMiddleware<T>) list.get(index);
			
			MiddlewareContext finalContext = context;
			middleware.onExecute(commandWrapper, sender, args, context, () -> {
				if (index == list.size() - 1) {
					executeCommand(sender, args, finalContext);
					return;
				}
				traverseMiddleware(sender, args, finalContext, index + 1, list);
			});
		} catch (ClassCastException exception) {
			throw new IllegalStateException(
					list.get(index).getClass().getSimpleName() + " must be an implementation of SenderCommandMiddleware"
			);
		}
	}
	
}
