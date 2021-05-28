package me.idriz.commando.jda.middleware;

import me.idriz.commando.jda.sender.UserSender;
import me.idriz.commando.middleware.CommandMiddleware.SenderCommandMiddleware;

public interface JDACommandMiddleware extends SenderCommandMiddleware<UserSender> {
}
