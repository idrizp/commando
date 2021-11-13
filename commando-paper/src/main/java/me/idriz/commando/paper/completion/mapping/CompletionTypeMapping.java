package me.idriz.commando.paper.completion.mapping;

import com.mojang.brigadier.arguments.ArgumentType;
import java.lang.reflect.Parameter;
import java.util.function.Function;

public interface CompletionTypeMapping<T> extends Function<Parameter, ArgumentType<T>> {

}
