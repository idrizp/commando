package me.idriz.commando.paper.completion;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import me.idriz.commando.command.Command.Argument;
import me.idriz.commando.command.Command.Flag;
import me.idriz.commando.command.Command.MiddlewareData;
import me.idriz.commando.paper.completion.mapping.CompletionTypeMapping;
import me.idriz.commando.paper.completion.mapping.DoubleCompletionTypeMapping;
import me.idriz.commando.paper.completion.mapping.IntegerCompletionTypeMapping;
import me.idriz.commando.paper.completion.mapping.LongCompletionTypeMapping;
import me.idriz.commando.paper.completion.mapping.PlayerCompletionTypeMapping;
import me.idriz.commando.paper.completion.mapping.StringCompletionTypeMapping;
import me.idriz.commando.util.PrimitiveUtils;
import me.idriz.commando.wrapper.CommandWrapper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BrigadierCommandCompletion {

	private static final Map<Class<?>, CompletionTypeMapping<?>> BRIGADIER_ARGUMENT_TYPE_MAPPINGS = new HashMap<>();

	static {
		registerArgumentTypeMapping(String.class, new StringCompletionTypeMapping());
		registerArgumentTypeMapping(Integer.class, new IntegerCompletionTypeMapping());
		registerArgumentTypeMapping(Double.class, new DoubleCompletionTypeMapping());
		registerArgumentTypeMapping(Long.class, new LongCompletionTypeMapping());
		registerArgumentTypeMapping(Player.class, new PlayerCompletionTypeMapping());
	}

	private final @NotNull CommandWrapper commandWrapper;

	public BrigadierCommandCompletion(@NotNull CommandWrapper commandWrapper) {
		this.commandWrapper = commandWrapper;
	}

	public static void registerArgumentTypeMapping(Class<?> clazz, CompletionTypeMapping<?> argumentType) {
		BRIGADIER_ARGUMENT_TYPE_MAPPINGS.put(clazz, argumentType);
		if (PrimitiveUtils.BOXED_TO_PRIMITIVE.get(clazz) != null) {
			registerArgumentTypeMapping(PrimitiveUtils.BOXED_TO_PRIMITIVE.get(clazz), argumentType);
		}
	}

	public LiteralCommandNode<?> buildCommandNode() {
		return buildNode().build();
	}

	public LiteralArgumentBuilder<?> buildNode() {
		LiteralArgumentBuilder<?> literal = LiteralArgumentBuilder.literal(commandWrapper.getName());
		// Add subcommands
		for (CommandWrapper subCommand : commandWrapper.getSubCommands().values()) {
			// weird generic stuff, thanks brigadier
			LiteralArgumentBuilder literalArgumentBuilder = new BrigadierCommandCompletion(subCommand).buildNode();
			literal.then(literalArgumentBuilder);
		}
		Method method = commandWrapper.getMethod();
		if (method == null) {
			return literal;
		}
		Parameter[] parameters = method.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			if (i == 0 && CommandSender.class.isAssignableFrom(parameter.getType())) {
				continue;
			}
			if (parameter.isAnnotationPresent(MiddlewareData.class)) {
				continue;
			}
			if (parameter.isAnnotationPresent(Flag.class)) {
				Flag flag = parameter.getAnnotation(Flag.class);
				literal.then(LiteralArgumentBuilder.literal("-" + flag.value()));
				continue;
			}
			String literalName = parameter.getName();
			Argument argumentInfo = parameter.getAnnotation(Argument.class);
			if (argumentInfo != null) {
				literalName = argumentInfo.value();
			}
			ArgumentType<?> argumentType = BRIGADIER_ARGUMENT_TYPE_MAPPINGS.get(parameter.getType()).apply(parameter);
			if (argumentType == null) {
				literal.then(LiteralArgumentBuilder.literal(literalName));
				continue;
			}
			literal.then(RequiredArgumentBuilder.argument(literalName, argumentType));
		}
		return literal;
	}


}

