package me.idriz.commando.example.javacord;

import me.idriz.commando.example.javacord.command.WaveCommand;
import me.idriz.commando.javacord.JavaCordCommando;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class JavaCordExample {
	
	public static final String COMMAND_PREFIX = "-";
	
	public static void main(String[] args) {
		DiscordApi api = new DiscordApiBuilder()
				.setToken(System.getenv("BOT_TOKEN"))
				.login()
				.join();
		
		if (api == null) {
			throw new IllegalStateException("Bot not logged in");
		}
		
		JavaCordCommando commando = new JavaCordCommando(api, COMMAND_PREFIX, true);
		commando.registerCommand(new WaveCommand());
	}
	
}
