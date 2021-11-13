package me.idriz.commando.paper.completion.mapping;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import java.lang.reflect.Parameter;
import me.idriz.commando.command.Command.Range;

public class IntegerCompletionTypeMapping implements CompletionTypeMapping<Integer> {

	@Override
	public ArgumentType<Integer> apply(Parameter parameter) {
		Range range = parameter.getAnnotation(Range.class);
		if (range == null) {
			return IntegerArgumentType.integer();
		}
		return IntegerArgumentType.integer(range.min(), range.max());
	}

}
