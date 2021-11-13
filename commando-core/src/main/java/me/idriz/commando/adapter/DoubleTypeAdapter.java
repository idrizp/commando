package me.idriz.commando.adapter;

import java.lang.reflect.Parameter;
import me.idriz.commando.Commando;
import me.idriz.commando.sender.CommandoSender;
import me.idriz.commando.util.NumericTypeAdapterUtils;

public class DoubleTypeAdapter<T extends CommandoSender<?>> implements TypeAdapter<Double, T> {
	
	private final Commando commando;
	
	public DoubleTypeAdapter(Commando commando) {
		this.commando = commando;
	}
	
	@Override
	public Double adapt(T sender, Parameter parameter, String argument, String[] originalArgs, String[] args) {
		try {
			return NumericTypeAdapterUtils.withRangeValidation(parameter, Double.parseDouble(argument));
		} catch(NumberFormatException exception) {
			return null;
		}
	}
	
	@Override
	public void onError(T sender, Parameter parameter, String argument, String[] originalArgs, String[] args) {
		sender.sendMessage(commando.getMessageBuilder().createMessage("Invalid number provided.", 255, 0, 0));
	}
}
