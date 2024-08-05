package dev.nottekk.notvolt.commands.impl.fun;

import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.utils.others.GuildUtil;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

/**
 * Just a simple command which gives you a thanks messages!
 */
@Command(name = "premium", description = "Premium shit YAY", category = Category.FUN)
public class PremiumCommand implements ICommand {
    @Override
    public void onPerform(CommandEvent commandEvent) {
        if (GuildUtil.isSupporter(commandEvent.getMember().getUser())) {
            commandEvent.setEphemeral(false);
            commandEvent.reply("Thank you for helping with funding Ree6 " + commandEvent.getMember().getAsMention() + "!");
        } else {
            commandEvent.reply("You need to be a Premium User to use this Command!");
            /*if (commandEvent.isSlashCommand()) {
                commandEvent.getSlashCommandInteractionEvent().replyWithPremiumRequired().queue();
            } else {
                commandEvent.reply("You need to be a Premium User to use this Command!");
            }*/
        }
    }

    @Override
    public CommandData getCommandData() {
        return null;
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }
}
