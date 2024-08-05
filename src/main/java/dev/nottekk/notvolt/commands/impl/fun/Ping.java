package dev.nottekk.notvolt.commands.impl.fun;

import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

/**
 * A command to let the bot say Pong.
 */
@Command(name = "ping", description = "command.description.ping", category = Category.FUN)
public class Ping implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {
        commandEvent.reply(commandEvent.getResource("message.ping"));
    }

    /**
     * @inheritDoc
     */
    @Override
    public CommandData getCommandData() {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String[] getAlias() {
        return new String[0];
    }
}
