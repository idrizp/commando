package me.idriz.commando.adapter;

import java.lang.reflect.Parameter;
import me.idriz.commando.Commando;
import me.idriz.commando.sender.CommandoSender;
import me.idriz.commando.util.NumericTypeAdapterUtils;

public class LongTypeAdapter<T extends CommandoSender<?>> implements TypeAdapter<Long, T> {
	
	private final Commando commando;
	
	public LongTypeAdapter(Commando commando) {
		this.commando = commando;
	}
	
	@Override
	public Long adapt(T sender, Parameter parameter, String argument, String[] originalArgs, String[] args) {
		try {
			return NumericTypeAdapterUtils.withRangeValidation(parameter, Long.parseLong(argument));
		} catch(NumberFormatException exception) {
			return null;
		}
	}
	
	@Override
	public void onError(T sender, Parameter parameter, String argument, String[] originalArgs, String[] args) {
		sender.sendMessage(commando.getMessageBuilder().createMessage("Invalid number provided.", 255, 0, 0));
	}
	
}
