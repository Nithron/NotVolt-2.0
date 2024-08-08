package dev.nottekk.notvolt.commands.impl.utility;


import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.main.Main;
import dev.nottekk.notvolt.utils.EAccessLevel;
import dev.nottekk.notvolt.handlers.ConfigHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Command(name = "info", description = "command.description.info", category = Category.INFO)
public class Info implements ICommand {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");

    @Override
    public void onPerform(CommandEvent commandEvent) {
        commandEvent.reply(buildEmbed(commandEvent.getJda().getSelfUser().getAvatarUrl(), commandEvent.getJda().getSelfUser().getId(), commandEvent.getJda().getGuilds().size(), commandEvent.getJda().getSelfUser().getTimeCreated().format(formatter)).build());
    }

    @Override
    public CommandData getCommandData() {
        return null;
    }

    @Override
    public String[] getAlias() {
        return new String[] {
                "if"
        };
    }

    private EmbedBuilder buildEmbed(String avatar, String id, int serverCount, String creationDate) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.BLUE);
        embed.setTitle("\uD83E\uDD16   Bot Info\n");
        embed.setThumbnail(avatar);
        embed.addField(":id: Bot ID", id, true);
        embed.addField(":id: Version", ConfigHandler.get("VERSION"), true);
        embed.addField("\uD83C\uDF10 Server Count", "In " + serverCount + " servers",true);
        embed.addField(":clock2: Creation date", creationDate, true);
        embed.setFooter("\n\nCreated by NotTekk", ConfigHandler.get("OWNER_AVATAR"));
        return embed;
    }

    @Override
    public EAccessLevel getCommandLevel() {
        return EAccessLevel.USER;
    }

}
