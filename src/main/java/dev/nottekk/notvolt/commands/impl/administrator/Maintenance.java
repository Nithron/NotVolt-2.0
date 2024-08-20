package dev.nottekk.notvolt.commands.impl.administrator;

import dev.nottekk.notvolt.bot.BotConfig;
import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.language.LanguageService;
import dev.nottekk.notvolt.main.Main;
import dev.nottekk.notvolt.utils.EAccessLevel;
import dev.nottekk.notvolt.utils.log.LogUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.awt.*;

/**
 * A command used to set bot's maintenance status!
 */
@Command(name = "maintenance", description = "command.description.maintenance", category = Category.MOD)
public class Maintenance implements ICommand {

    @Override
    public void onPerform(CommandEvent commandEvent) {
        String maintenanceFlag = getFlag(commandEvent.getArguments());
        String username = commandEvent.getUser().getName();

        if (maintenanceFlag.equals("true")) {
            if (Main.getInstance().isMaintenance()) {
                commandEvent.reply(buildEmbedMessage("Bot Maintenance","Bot is already set as in Maintenance!",Color.RED).build());
            } else {
                Main.getInstance().setMaintenanceStatus(username);
                String message = "Bot set to Under Maintenance by " + username + "!";
                commandEvent.reply(buildEmbedMessage("Bot Maintenance",message,Color.GREEN).build());
                LogUtil.logOwner(commandEvent, message);
            }
        } else if (maintenanceFlag.equals("false")) {
            if (!Main.getInstance().isMaintenance()) {
                commandEvent.reply(buildEmbedMessage("Bot Maintenance","Bot is already set to Not in Maintenance!",Color.RED).build());
            } else {
                Main.getInstance().resetMaintenanceStatus(username);
                String message = "Bot set to Not Under Maintenance by " + username + "!";
                commandEvent.reply(buildEmbedMessage("Bot Maintenance",message,Color.GREEN).build());
                LogUtil.logOwner(commandEvent, message);
            }
        } else {
            commandEvent.reply(buildEmbedMessage("Bot Maintenance","Flag not Recognised! Use 'true' or 'false'",Color.RED).build());
        }
    }

    private String getFlag(String[] arguments) {
        String res = "";
        for (String arg : arguments) {
            if (arg.contains("true") || arg.contains("false")) {
                res = arg;
            }
        }
        return res;
    }

    private EmbedBuilder buildEmbedMessage(String title, String message, Color color) {
        EmbedBuilder res = new EmbedBuilder();
        res.setColor(color);
        res.setTitle(title);
        res.setDescription(message);
        return res;
    }

    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl("maintenance", LanguageService.getDefault("command.description.maintenance"))
                .addOptions(new OptionData(OptionType.STRING, "flag", "Sets or Resets Bot's maintenance status"));
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public EAccessLevel getCommandLevel() {
        return EAccessLevel.ADMIN;
    }
}
