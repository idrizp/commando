package me.idriz.commando.adapter;

import java.lang.reflect.Parameter;
import me.idriz.commando.Commando;
import me.idriz.commando.command.Command.Text;
import me.idriz.commando.sender.CommandoSender;

public class StringTypeAdapter<T extends CommandoSender<?>> implements TypeAdapter<String, T> {
	
	private final Commando commando;
	
	public StringTypeAdapter(Commando commando) {
		this.commando = commando;
	}
	
	@Override
	public String adapt(T sender, Parameter parameter, String argument, String[] originalArgs, String[] args) {
		if (parameter.isAnnotationPresent(Text.class)) {
			return String.join(" ", args);
		}
		return argument;
	}
	
	@Override
	public void onError(T sender, Parameter parameter, String argument, String[] originalArgs, String[] args) {
		sender.sendMessage(commando.getMessageBuilder().createMessage("Invalid string provided.", 255, 0, 0));
	}
	
}
