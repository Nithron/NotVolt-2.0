package dev.nottekk.notvolt.commands.impl.info;

import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import de.presti.ree6.sql.SQLSession;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

/**
 * A command to opt-out of data collection.
 */
@Command(name = "optout", description = "command.description.optout", category = Category.INFO)
public class Optout implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {
        if (SQLSession.getSqlConnector().getSqlWorker().isOptOut(commandEvent.getGuild().getIdLong(), commandEvent.getMember().getIdLong())) {
            SQLSession.getSqlConnector().getSqlWorker().optIn(commandEvent.getGuild().getIdLong(), commandEvent.getMember().getIdLong());
            commandEvent.reply(commandEvent.getResource("message.optout.optedIn"));
        } else {
            SQLSession.getSqlConnector().getSqlWorker().optOut(commandEvent.getGuild().getIdLong(), commandEvent.getMember().getIdLong());
            commandEvent.reply(commandEvent.getResource("message.optout.optedOut"));
        }
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
        return new String[] { "opt-out", "out", "opt", "privacy" };
    }
}
