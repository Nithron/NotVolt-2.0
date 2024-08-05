package dev.nottekk.notvolt.commands.impl.level;

import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.bot.BotConfig;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

/**
 * A command to get the Leaderboard.
 */
@Command(name = "leaderboard", description = "command.description.leaderboard", category = Category.LEVEL)
public class Leaderboards implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {
        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder.setContent(commandEvent.getResource("message.leaderboards"));
        messageCreateBuilder.addActionRow(
                Button.link("https://support-dev.discord.com/hc/de/articles/360043053492-Statistics-Bot-Policy", commandEvent.getResource("label.discordGuidelines")),
                Button.link(BotConfig.getWebinterface() + "/dash/" + commandEvent.getGuild().getId() + "/leaderboards", commandEvent.getResource("label.chatLeaderboard")),
                Button.link(BotConfig.getWebinterface() + "/dash/" + commandEvent.getGuild().getId() + "/leaderboards", commandEvent.getResource("label.voiceLeaderboard"))
        );
        commandEvent.reply(messageCreateBuilder.build());
    }

    /**
     * @inheritDoc
     */
    @Override
    public String[] getAlias() {
        return new String[]{ "lb" };
    }

    /**
     * @inheritDoc
     */
    @Override
    public CommandData getCommandData() {
        return null;
    }
}
