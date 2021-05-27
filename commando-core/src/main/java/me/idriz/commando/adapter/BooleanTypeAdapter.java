package me.idriz.commando.adapter;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import me.idriz.commando.Commando;
import me.idriz.commando.sender.CommandoSender;

public class BooleanTypeAdapter<T extends CommandoSender<?>> implements TypeAdapter<Boolean, T>  {
	
	private final Commando commando;
	
	private static final Map<String, Boolean> STRING_TO_BOOLEAN_MAPPING = new HashMap<>();
	
	static {
		STRING_TO_BOOLEAN_MAPPING.put("yes", true);
		STRING_TO_BOOLEAN_MAPPING.put("no", false);
		STRING_TO_BOOLEAN_MAPPING.put("true", true);
		STRING_TO_BOOLEAN_MAPPING.put("false", false);
	}
	
	
	public BooleanTypeAdapter(Commando commando) {
		this.commando = commando;
	}
	
	@Override
	public Boolean adapt(T sender, Parameter parameter, String argument, String[] originalArgs, String[] args) {
		return STRING_TO_BOOLEAN_MAPPING.get(argument.toLowerCase());
	}
	
	@Override
	public void onError(T sender, Parameter parameter, String argument, String[] originalArgs, String[] args) {
		sender.sendMessage(commando.getMessageBuilder().createMessage("Invalid boolean provided.", 255, 0, 0));
	}
	
}
