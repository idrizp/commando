package me.idriz.commando.paper.completion.mapping;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import java.lang.reflect.Parameter;
import me.idriz.commando.command.Command.Range;

public class DoubleCompletionTypeMapping implements CompletionTypeMapping<Double> {

	@Override
	public ArgumentType<Double> apply(Parameter parameter) {
		Range range = parameter.getAnnotation(Range.class);
		if (range == null) {
			return DoubleArgumentType.doubleArg();
		}
		return DoubleArgumentType.doubleArg(range.min(), range.max());
	}

}
