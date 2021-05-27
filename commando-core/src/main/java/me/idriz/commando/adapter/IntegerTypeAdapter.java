package me.idriz.commando.adapter;

import java.lang.reflect.Parameter;
import me.idriz.commando.Commando;
import me.idriz.commando.sender.CommandoSender;

public class IntegerTypeAdapter<T extends CommandoSender<?>> implements TypeAdapter<Integer, T> {
	
	private final Commando commando;
	
	public IntegerTypeAdapter(Commando commando) {
		this.commando = commando;
	}
	
	@Override
	public Integer adapt(T sender, Parameter parameter, String argument, String[] originalArgs, String[] args) {
		try {
			return Integer.parseInt(argument);
		} catch(NumberFormatException exception) {
			return null;
		}
	}
	
	@Override
	public void onError(T sender, Parameter parameter, String argument, String[] originalArgs, String[] args) {
		sender.sendMessage(commando.getMessageBuilder().createMessage("Invalid number provided.", 255, 0, 0));
	}
}
