package me.idriz.commando.example.jda;

import me.idriz.commando.example.jda.command.WaveCommand;
import me.idriz.commando.jda.JDACommando;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class JDAExample {
	
	public static final String COMMAND_PREFIX = "-";
	
	public static void main(String[] args) throws LoginException, InterruptedException {
		JDA jda = JDABuilder
				.createDefault(System.getenv("BOT_TOKEN"))
				.build()
				.awaitReady();
		
		JDACommando commando = new JDACommando(jda, COMMAND_PREFIX, true);
		commando.registerCommand(new WaveCommand());
	}
}
