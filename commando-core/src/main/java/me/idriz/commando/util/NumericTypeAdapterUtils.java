package me.idriz.commando.util;

import java.lang.reflect.Parameter;
import java.util.function.Function;
import me.idriz.commando.command.Command.Range;

public class NumericTypeAdapterUtils {

	private static <T extends Number> T withRangeParameter(Parameter parameter, T value, Function<Range, T> function) {
		if (value == null) {
			return null;
		}
		Range range = parameter.getAnnotation(Range.class);
		if (range == null) {
			return value;
		}
		return function.apply(range);
	}

	public static Integer withRangeValidation(Parameter parameter, Integer value) {
		return withRangeParameter(parameter, value, range -> {
			if (value < range.min()) return null;
			if (value > range.max()) return null;
			return value;
		});
	}

	public static Double withRangeValidation(Parameter parameter, Double value) {
		return withRangeParameter(parameter, value, range -> {
			if (value < range.min()) return null;
			if (value > range.max()) return null;
			return value;
		});
	}

	public static Long withRangeValidation(Parameter parameter, Long value) {
		return withRangeParameter(parameter, value, range -> {
			if (value < range.min()) return null;
			if (value > range.max()) return null;
			return value;
		});
	}

}
