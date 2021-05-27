package me.idriz.commando.example.paper;

import me.idriz.commando.example.paper.command.ExampleCommand;
import me.idriz.commando.example.paper.command.ExplodeCommand;
import me.idriz.commando.paper.PaperCommando;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperCommandoExample extends JavaPlugin {
	
	@Override
	public void onEnable() {
		PaperCommando paperCommando = new PaperCommando(this);
		paperCommando.registerCommand(new ExampleCommand());
		paperCommando.registerCommand(new ExplodeCommand());
	}
}
