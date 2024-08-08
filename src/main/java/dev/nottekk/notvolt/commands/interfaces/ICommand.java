package dev.nottekk.notvolt.commands.interfaces;

import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.utils.EAccessLevel;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface ICommand {

    /**
     * Will be fired when the Command is called.
     *
     * @param commandEvent the Event, with every needed data.
     */
    void onPerform(CommandEvent commandEvent);

    /**
     * A CommandData implementation for JDAs SlashCommand Interaction Implementation.
     *
     * @return the created CommandData.
     */
    CommandData getCommandData();

    /**
     * Aliases of the current Command.
     *
     * @return the Aliases.
     */
    String[] getAlias();

    EAccessLevel getCommandLevel();


}
