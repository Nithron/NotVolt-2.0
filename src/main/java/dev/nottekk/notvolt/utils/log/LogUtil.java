package dev.nottekk.notvolt.utils.log;

import dev.nottekk.notvolt.bot.BotConfig;
import dev.nottekk.notvolt.commands.CommandEvent;
import net.dv8tion.jda.api.entities.User;

public class LogUtil {

    public static void logOwner(CommandEvent commandEvent, String logMessage) {
        User user = commandEvent.getJda().retrieveUserById(BotConfig.getBotOwner()).complete();
        user.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(logMessage).queue();
        });
    }

}
