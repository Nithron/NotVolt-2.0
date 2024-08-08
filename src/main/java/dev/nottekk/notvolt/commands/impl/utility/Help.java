package dev.nottekk.notvolt.commands.impl.utility;

import dev.nottekk.notvolt.bot.BotConfig;
import dev.nottekk.notvolt.builders.CommandEmbedBuilder;
import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.formatters.formats.MessageFormat;
import dev.nottekk.notvolt.utils.EAccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

@Command(name = "help", description = "command.description.help", category = Category.INFO)
public class Help implements ICommand {

    private EmbedBuilder buildEmbed() {
        MessageFormat message1 = new MessageFormat("Bot:", true, false, false);
        MessageFormat message2 = new MessageFormat("help - Shows the available commands", false, false, false);
        MessageFormat message3 = new MessageFormat("info - Shows current bot status\n", false, false, false);
        MessageFormat message4 = new MessageFormat("Utilities:", true, false, false);
        MessageFormat message5 = new MessageFormat("gituser - Shows github user info (use github profile username)", false, false, false);
        MessageFormat message6 = new MessageFormat("ping - Checks the bot connection", false, false, false);
        MessageFormat message7 = new MessageFormat("whois - Provides information about the mentioned user\n", false, false, false);
        MessageFormat message8 = new MessageFormat("Fun:", true, false, false);
        MessageFormat message9 = new MessageFormat("ym - Makes YO MAMMA jokes with the mentioned user", false, false, false);
        MessageFormat message10 = new MessageFormat("gif - search and displays gifs based on users input\n\n", false, false, false);
        EmbedBuilder embed = CommandEmbedBuilder.buildEmbed("\uD83D\uDD37   Help\n", "\n\nCreated by NotTekk", BotConfig.getOwnerAvatar(), Color.BLUE, Arrays.asList(message1, message2, message3, message4, message5, message6, message7, message8, message9, message10));

        return embed;
    }

    @Override
    public void onPerform(CommandEvent commandEvent) {
        commandEvent.reply(buildEmbed().build());
    }

    @Override
    public CommandData getCommandData() {
        return null;
    }

    @Override
    public String[] getAlias() {
        return new String[]{"hp"};
    }

    @Override
    public EAccessLevel getCommandLevel() {
        return EAccessLevel.USER;
    }
}
