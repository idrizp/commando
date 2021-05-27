package me.idriz.commando.paper.message;

import me.idriz.commando.message.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

public class PaperMessage implements Message {
	
	private TextComponent component = Component.text("");
	
	@Override
	public void setContent(String content) {
		this.component = component.content(content);
	}
	
	@Override
	public void append(Message appended) {
		if (!(appended instanceof PaperMessage)) {
			throw new UnsupportedOperationException("The message must be a Kyori TextComponent.");
		}
		this.component = component.append(((PaperMessage) appended).component);
	}
	
	@Override
	public void setColor(int red, int green, int blue) {
		this.component = component.color(TextColor.color(red, green, blue));
	}
	
	public TextComponent getComponent() {
		return component;
	}
}
