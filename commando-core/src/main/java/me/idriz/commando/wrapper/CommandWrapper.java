package me.idriz.commando.wrapper;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import me.idriz.commando.Commando;
import me.idriz.commando.adapter.TypeAdapter;
import me.idriz.commando.command.Command;
import me.idriz.commando.command.Command.Flag;
import me.idriz.commando.command.Command.Info;
import me.idriz.commando.command.Command.MiddlewareData;
import me.idriz.commando.command.Command.OptionalArgument;
import me.idriz.commando.command.Command.UseAdapter;
import me.idriz.commando.command.Command.UseMiddleware;
import me.idriz.commando.message.Message;
import me.idriz.commando.middleware.CommandMiddleware;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandWrapper {
	
	private final @NotNull Commando commando;
	private final @NotNull Command command;
	
	private final @NotNull String name;
	private final @NotNull String[] aliases;
	
	private final @NotNull Message usage;
	private final @NotNull Message description;
	
	private final @Nullable Method method;
	private final @NotNull Map<String, CommandWrapper> subCommands;
	
	private final Map<Character, Integer> flags;
	
	private final List<CommandMiddleware> middleware;
	private final List<CommandMiddleware> inheritedMiddleware;
	
	private final boolean subCommand;
	
	private final List<Parameter> arguments = new ArrayList<>();
	private int requiredParameterCount = -1;
	
	public CommandWrapper(
			@NotNull Commando commando,
			@NotNull Command command,
			@NotNull String name,
			@NotNull String[] aliases,
			@NotNull Message usage,
			@NotNull Message description,
			@Nullable Method method,
			@NotNull Map<String, CommandWrapper> subCommands,
			@NotNull Map<Character, Integer> flags,
			@NotNull List<CommandMiddleware> middleware,
			@NotNull List<CommandMiddleware> inheritedMiddleware,
			boolean subCommand
	) {
		this.commando = commando;
		this.command = command;
		this.name = name;
		this.aliases = aliases;
		this.usage = usage;
		this.description = description;
		this.method = method;
		this.subCommands = subCommands;
		this.flags = flags;
		this.middleware = middleware;
		this.inheritedMiddleware = inheritedMiddleware;
		this.subCommand = subCommand;
	}
	
	public CommandWrapper(
			@NotNull Commando commando,
			@Nullable Command parentCommand,
			@NotNull Command command,
			boolean subCommand
	) {
		Info annotation = command.getClass().getAnnotation(Info.class);
		String[] aliases = command.getAliases();
		if (aliases == null) {
			if (annotation == null) {
				aliases = new String[0];
			} else {
				aliases = Arrays.copyOfRange(annotation.value(), 1, annotation.value().length);
			}
		}
		
		String name = command.getName() != null ? command.getName()
				: Objects.requireNonNull(annotation, "Didn't find info annotation on class.").value()[0];
		
		Message description = command.getDescription() != null ? command.getDescription()
				: commando.getMessageBuilder()
						.createMessage(
								Objects.requireNonNull(annotation, "Didn't find info annotation on class.").description(),
								255, 0, 0
						);
		
		Message usage = command.getUsage() != null ? command.getUsage() : commando.getMessageBuilder()
				.createMessage(Objects.requireNonNull(annotation,
						"Didn't find info annotation on class.").usage(), 255, 0, 0);
		
		Method executionMethod = Arrays.stream(command.getClass().getDeclaredMethods())
				.filter(found -> found.isAnnotationPresent(Command.Default.class))
				.findFirst().orElse(null);
		
		this.subCommands = new HashMap<>();
		
		command.getSubCommands()
				.forEach(found -> registerSubCommand(new CommandWrapper(commando, command, found, true)));
		Arrays
				.stream(command.getClass().getDeclaredMethods())
				.filter(found -> found.isAnnotationPresent(Info.class))
				.forEach(found -> registerSubCommand(
						new CommandWrapper(commando, command, found.getAnnotation(Info.class), found))
				);
		this.commando = commando;
		this.command = command;
		this.aliases = aliases;
		this.name = name;
		this.description = description;
		this.usage = usage;
		this.method = executionMethod;
		
		this.inheritedMiddleware = new ArrayList<>();
		if (parentCommand != null) {
			inheritedMiddleware.addAll(getMiddleware(commando, parentCommand));
		}
		
		this.middleware = new ArrayList<>();
		middleware.addAll(getMiddleware(commando, command));
		
		this.subCommand = subCommand;
		if (method != null) {
			this.flags = getFlags(method);
		} else {
			this.flags = new HashMap<>();
		}
		
	}
	
	private CommandWrapper(
			@NotNull Commando commando,
			@NotNull Command parentCommand,
			@NotNull Info info,
			@NotNull Method subCommandMethod
	) {
		this(
				commando,
				parentCommand,
				info.value()[0],
				Arrays.copyOfRange(info.value(), 1, info.value().length),
				commando.getMessageBuilder().createMessage(info.usage(), 255, 0, 0),
				commando.getMessageBuilder().createMessage(info.description(), 255, 0, 0),
				subCommandMethod,
				// TODO: Implement nested subcommands
				new HashMap<>(),
				getFlags(subCommandMethod),
				getMiddleware(commando, parentCommand),
				getMiddleware(commando, subCommandMethod),
				true
		);
	}
	
	@NotNull
	public static Map<Character, Integer> getFlags(@NotNull Method method) {
		Map<Character, Integer> map = new HashMap<>();
		
		Parameter[] parameters = method.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			if ((parameter.getType().equals(boolean.class) || parameter.getType().equals(Boolean.class))
					&& parameter.isAnnotationPresent(Flag.class)) {
				map.put(parameter.getAnnotation(Flag.class).value(), i);
			}
		}
		return map;
	}
	
	@NotNull
	public static List<CommandMiddleware> getMiddleware(@NotNull Commando commando,
			@NotNull Method method) {
		if (!method.isAnnotationPresent(UseMiddleware.class)) {
			return new ArrayList<>();
		}
		return Arrays.stream(method.getAnnotation(UseMiddleware.class).value())
				.map(commando::getMiddleware)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
	
	@NotNull
	public static List<CommandMiddleware> getMiddleware(@NotNull Commando commando, @NotNull Command command) {
		List<CommandMiddleware> middleware = new ArrayList<>(command.getMiddleware());
		if (command.getClass().isAnnotationPresent(UseMiddleware.class)) {
			middleware.addAll(
					Arrays.stream(command.getClass().getAnnotation(UseMiddleware.class).value())
							.map(commando::getMiddleware)
							.filter(Objects::nonNull)
							.collect(Collectors.toList())
			);
		}
		return middleware;
	}
	
	private void registerSubCommand(CommandWrapper wrapper) {
		subCommands.put(wrapper.getName().toLowerCase(), wrapper);
		Arrays.stream(wrapper.getAliases()).forEach(alias -> subCommands.put(alias.toLowerCase(), wrapper));
	}
	
	public Map<Character, Integer> getFlags() {
		return flags;
	}
	
	public Command getCommand() {
		return command;
	}
	
	public Map<String, CommandWrapper> getSubCommands() {
		return subCommands;
	}
	
	public Commando getCommando() {
		return commando;
	}
	
	public String getName() {
		return name;
	}
	
	public String[] getAliases() {
		return aliases;
	}
	
	public Message getUsage() {
		return usage;
	}
	
	public Message getDescription() {
		return description;
	}
	
	public Method getMethod() {
		return method;
	}
	
	public boolean isSubCommand() {
		return subCommand;
	}
	
	public List<CommandMiddleware> getInheritedMiddleware() {
		return inheritedMiddleware;
	}
	
	public List<CommandMiddleware> getMiddleware() {
		return middleware;
	}
	
	public List<CommandMiddleware> getActingMiddleware() {
		List<CommandMiddleware> actingMiddleware = new ArrayList<>(commando.getDefaultMiddlewareChain());
		actingMiddleware.addAll(inheritedMiddleware);
		actingMiddleware.addAll(middleware);
		return actingMiddleware;
	}
	
	public List<String> getAllNames() {
		List<String> names = new ArrayList<>();
		names.add(getName());
		names.addAll(Arrays.asList(getAliases()));
		return names;
	}
	
	public int getRequiredParameterCount() {
		if (requiredParameterCount == -1 && method != null) {
			requiredParameterCount = (int) Arrays.stream(method.getParameters())
					.filter(method -> !method.isAnnotationPresent(Flag.class) && !method
							.isAnnotationPresent(OptionalArgument.class) && !method
							.isAnnotationPresent(MiddlewareData.class))
					.count();
		}
		return requiredParameterCount;
	}
	
	@Nullable
	public TypeAdapter<?, ?> getAdapter(Parameter parameter) {
		if (parameter.isAnnotationPresent(UseAdapter.class)) {
			UseAdapter annotation = parameter.getAnnotation(UseAdapter.class);
			return commando.getCustomAdapter(annotation.value());
		}
		return commando.getTypeAdapter(parameter.getType());
	}
	
	public List<Parameter> getArguments() {
		return arguments;
	}
}
