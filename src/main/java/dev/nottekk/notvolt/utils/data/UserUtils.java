package dev.nottekk.notvolt.utils.data;

import dev.nottekk.notvolt.commands.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class UserUtils {

    public static User getUserByID(CommandEvent commandEvent, String userId) {
        String refactoredUserId = refactorUserId(userId);
        final User[] user = new User[1];
        try {
            commandEvent.getGuild().retrieveMemberById(refactoredUserId).queue(member -> {
                user[0] = member.getUser();
            }, failure -> {
                System.out.println("Failed to retrieve member: " + failure.getMessage());
            });
        } catch (NumberFormatException e) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.RED);
            embedBuilder.setDescription("No user mentioned.");
            commandEvent.reply(embedBuilder.build());
        }
        return user[0];
    }

    public static String refactorUserId(String userId) {
        String res = "";
        if (userId.contains("<@") || userId.contains(">")) {
            res = userId.replace("<@", "").replace(">", "");
        } else {
            res = userId;
        }
        return res;
    }

}
