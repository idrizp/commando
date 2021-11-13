package me.idriz.commando.paper.completion.type;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerArgumentType implements ArgumentType<Player> {

	private static final List<String> EXAMPLES = List.of("player");

	@Override
	public Player parse(StringReader reader) throws CommandSyntaxException {
		String name = reader.readString();
		return Bukkit.getPlayer(name);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		Bukkit.getOnlinePlayers().forEach(player -> {
			builder.suggest(player.getName());
		});
		return builder.buildFuture();
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}

}
