package me.idriz.commando.paper.completion.mapping;

import com.mojang.brigadier.arguments.ArgumentType;
import java.lang.reflect.Parameter;
import me.idriz.commando.paper.completion.type.PlayerArgumentType;
import org.bukkit.entity.Player;

public class PlayerCompletionTypeMapping implements CompletionTypeMapping<Player> {

	@Override
	public ArgumentType<Player> apply(Parameter parameter) {
		return new PlayerArgumentType();
	}

}
