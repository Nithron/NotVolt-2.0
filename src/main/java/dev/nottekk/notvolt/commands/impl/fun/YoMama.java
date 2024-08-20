package dev.nottekk.notvolt.commands.impl.fun;

import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.language.LanguageService;
import dev.nottekk.notvolt.utils.EAccessLevel;
import dev.nottekk.notvolt.utils.data.UserUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Command(name = "yomama", description = "command.description.yomama", category = Category.FUN)
public class YoMama implements ICommand {

    @Override
    public void onPerform(CommandEvent commandEvent) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        List<User> mentionedUsers = new ArrayList<>();

        if (commandEvent.isSlashCommand()) {
            for (String userId : commandEvent.getArguments()) {
                mentionedUsers.add(UserUtils.getUserByID(commandEvent, userId));
            }
        } else {
            if (commandEvent.getMessage() != null) {
                for (User user : commandEvent.getMessage().getMentions().getUsers()) {
                    if (!user.equals(commandEvent.getJda().getSelfUser())) {
                        mentionedUsers.add(user);
                    }
                }
            }
        }

        if (mentionedUsers.isEmpty()) {
            embedBuilder.setColor(Color.RED);
            embedBuilder.setDescription("No user mentioned.");
            commandEvent.reply(embedBuilder.build());
        } else {
            for (User user : mentionedUsers) {
                String joke = fetchJoke();
                if (joke == null) {
                    embedBuilder.setColor(Color.RED);
                    embedBuilder.setDescription("No joke found :cry:");
                    commandEvent.reply(embedBuilder.build());
                } else {
                    embedBuilder.setColor(new Color(83, 4, 139));
                    embedBuilder.setDescription(user.getAsMention() + " " + joke);
                    commandEvent.reply(embedBuilder.build());
                }
            }
        }
    }

    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl("yomama", LanguageService.getDefault("command.description.yomama"))
                .addOptions(new OptionData(OptionType.STRING, "user", "Mention an User here!"));
    }

    @Override
    public String[] getAlias() {
        return new String[]{
                "ym", "ymana", "mama"
        };
    }

    private String fetchJoke() {
        OkHttpClient caller = new OkHttpClient();
        Request request = new Request.Builder().url("https://www.yomama-jokes.com/api/v1/jokes/random/").build();
        try {
            Response response = caller.newCall(request).execute();
            JSONObject json = new JSONObject(response.body().string());
            return (String) json.get("joke");
        } catch (IOException | NullPointerException e) {
            return null;
        }
    }

    @Override
    public EAccessLevel getCommandLevel() {
        return EAccessLevel.USER;
    }

}
