package dev.nottekk.notvolt.commands.impl.utility;

import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.utils.EAccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.Color;
import java.util.List;

@Command(name = "ping", description = "command.description.ping", category = Category.INFO)
public class Ping implements ICommand {

    @Override
    public void onPerform(CommandEvent commandEvent) {
        long ping = commandEvent.getJda().getGatewayPing();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(getColorByPing(ping));
        builder.setTitle("\uD83C\uDFD3  Pong!");
        builder.setDescription("Ping : " + commandEvent.getJda().getGatewayPing() + " ms");

        commandEvent.reply(builder.build());
    }

    private Color getColorByPing(long ping) {
        if (ping < 100)
            return Color.cyan;
        if (ping < 400)
            return Color.green;
        if (ping < 700)
            return Color.yellow;
        if (ping < 1000)
            return Color.orange;
        return Color.red;
    }

    public String getDescription() {
        return "Return NV's ping";
    }

    public String getName() {
        return "ping";
    }

    @Override
    public CommandData getCommandData() {
        return null;
    }

    @Override
    public String[] getAlias() {
        return new String[] {
             "pg"
        };
    }

    @Override
    public EAccessLevel getCommandLevel() {
        return EAccessLevel.USER;
    }
}
