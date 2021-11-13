package me.idriz.commando.paper.completion.mapping;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import java.lang.reflect.Parameter;
import me.idriz.commando.command.Command.Text;

public class StringCompletionTypeMapping implements CompletionTypeMapping<String> {

	@Override
	public ArgumentType<String> apply(Parameter parameter) {
		return parameter.isAnnotationPresent(Text.class) ? StringArgumentType.greedyString() : StringArgumentType.string();
	}

}
