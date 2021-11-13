package me.idriz.commando.paper.completion.mapping;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import java.lang.reflect.Parameter;
import me.idriz.commando.command.Command.Range;

public class LongCompletionTypeMapping implements CompletionTypeMapping<Long> {

	@Override
	public ArgumentType<Long> apply(Parameter parameter) {
		Range range = parameter.getAnnotation(Range.class);
		if (range == null) {
			return LongArgumentType.longArg();
		}
		return LongArgumentType.longArg(range.min(), range.max());
	}

}
