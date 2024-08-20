package dev.nottekk.notvolt.commands.impl.administrator;

import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.utils.EAccessLevel;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class ChangeAvatar implements ICommand {

    @Override
    public void onPerform(CommandEvent commandEvent) {

    }

    @Override
    public CommandData getCommandData() {
        return null;
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public EAccessLevel getCommandLevel() {
        return null;
    }
}
