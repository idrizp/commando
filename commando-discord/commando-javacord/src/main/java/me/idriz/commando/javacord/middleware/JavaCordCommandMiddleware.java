package me.idriz.commando.javacord.middleware;

import me.idriz.commando.javacord.sender.MessageAuthorSender;
import me.idriz.commando.middleware.CommandMiddleware.SenderCommandMiddleware;

public interface JavaCordCommandMiddleware extends SenderCommandMiddleware<MessageAuthorSender> {

}
