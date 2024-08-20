package dev.nottekk.notvolt.commands.impl.administrator;

import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.utils.EAccessLevel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class ChangeStatus implements ICommand {

    private void changeActivity(JDA jda, String activityType, String activity, String URL) {
        switch (activityType) {
            case "watching":
                jda.getPresence().setActivity(Activity.watching(activity));
                break;
            case "competing":
                jda.getPresence().setActivity(Activity.competing(activity));
                break;
            case "streaming":
                if (!URL.isEmpty()) {
                    jda.getPresence().setActivity(Activity.streaming(activity, URL));
                }
                break;
            case "listening":
                jda.getPresence().setActivity(Activity.listening(activity));
                break;
        }
    }

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
        return EAccessLevel.OWNER;
    }

}
