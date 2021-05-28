package me.idriz.commando.jda;

import me.idriz.commando.Commando;
import me.idriz.commando.command.Command;
import me.idriz.commando.command.backing.ExecutorBackingCommand;
import me.idriz.commando.jda.message.JDAMessageBuilder;
import me.idriz.commando.jda.model.MessageAuthor;
import me.idriz.commando.jda.sender.UserSender;
import me.idriz.commando.message.Message;
import me.idriz.commando.wrapper.CommandWrapper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JDACommando extends Commando {

    private final Map<Command, ExecutorBackingCommand<UserSender>> backingCommands = new HashMap<>();
    private final JDAMessageBuilder messageBuilder = new JDAMessageBuilder();

    private String commandPrefix;

    public JDACommando(JDA jda, String commandPrefix, boolean deleteMessageAfterCommand){
        this.commandPrefix = commandPrefix;
        jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onMessageReceived(@NotNull MessageReceivedEvent event) {
                try {
                    if (!event.getMessage().getContentRaw().startsWith(getCommandPrefix())) {
                        return;
                    }
                    String command = event.getMessage().getContentRaw().substring(getCommandPrefix().length());

                    String[] args = command.split(" ");
                    Command found = getCommands().get(args[0].toLowerCase());
                    if (found == null) {
                        return;
                    }

                    ExecutorBackingCommand<UserSender> backingCommand = backingCommands.get(found);
                    if (backingCommand == null) {
                        throw new IllegalStateException("Didn't find backing command registered to command " + args[0]);
                    }

                    backingCommand.onExecute(new UserSender(new MessageAuthor(event.getAuthor(), event.getMessage())),
                            Arrays.copyOfRange(args, 1, args.length));

                    if (deleteMessageAfterCommand) {
                        event.getMessage().delete().queue();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    public String getCommandPrefix() {
        return commandPrefix;
    }

    public void setCommandPrefix(String commandPrefix) {
        this.commandPrefix = commandPrefix;
    }

    @Override
    public CommandWrapper registerCommand(Command command) {
        CommandWrapper wrapper = super.registerCommand(command);
        backingCommands.put(command, new ExecutorBackingCommand<>(this, wrapper));
        return wrapper;
    }

    @Override
    public @NotNull Message.Builder getMessageBuilder() {
        return messageBuilder;
    }
}
