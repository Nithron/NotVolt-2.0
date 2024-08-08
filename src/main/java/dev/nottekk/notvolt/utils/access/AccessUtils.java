package dev.nottekk.notvolt.utils.access;

import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.handlers.ConfigHandler;
import dev.nottekk.notvolt.utils.EAccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class AccessUtils {

    public static int getLevelCode(dev.nottekk.notvolt.utils.EAccessLevel level) {
        int res;
        if (level.equals(EAccessLevel.OWNER)) {
            res = 0;
        } else if (level.equals(EAccessLevel.ADMIN)) {
            res = 1;
        } else {
            res = 2;
        }

        return res;
    }

    public static EAccessLevel getUserAccessLevel(User user) {
        final List<String> adminsIds = Arrays.stream(ConfigHandler.get("ADMIN_IDS").split(";")).toList();
        final String ownerId = ConfigHandler.get("OWNER_ID");
        final String userId = user.getId();
        EAccessLevel res;

        if (ownerId.equals(userId)) {
            res = EAccessLevel.OWNER;
        } else if (adminsIds.contains(userId)) {
            res = EAccessLevel.ADMIN;
        } else {
            res = EAccessLevel.USER;
        }

        return res;
    }

    public static boolean userHasAccess(User user, ICommand command) {
        int commandLevel = getLevelCode(command.getCommandLevel());
        int userLevel = getLevelCode(getUserAccessLevel(user));
        boolean res = false;

        if (userLevel <= commandLevel) {
            res = true;
        }

        return res;
    }

    public static EmbedBuilder buildAccessResultEmbed(boolean hasAccess) {
        EmbedBuilder embed = new EmbedBuilder();
        if (hasAccess) {
            embed.setColor(Color.GREEN);
            embed.setDescription("Access Granted!");
        } else {
            embed.setColor(Color.RED);
            embed.setDescription("ICommand for Owner / Admin only!");
            embed.setImage("https://i.imgflip.com/5my304.jpg");
        }

        return embed;
    }

}
