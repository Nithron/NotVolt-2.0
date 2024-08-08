package dev.nottekk.notvolt.commands.impl.utility;

import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.language.LanguageService;
import dev.nottekk.notvolt.utils.EAccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Command(name = "github", description = "command.description.github", category = Category.INFO)
public class GitHub implements ICommand {

    @Override
    public void onPerform(CommandEvent commandEvent) {
        String message = commandEvent.getArguments()[1];

        if (getGithubUserInfo(message) != null) {
            EmbedBuilder builder = getGithubUserInfo(message);
            commandEvent.reply(builder.build());
        } else {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Color.RED);
            builder.setDescription("Failed to fetch GitHub user info.");
            commandEvent.reply(builder.build());
        }
    }

    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl("github", LanguageService.getDefault("command.description.github"))
                .addOptions(new OptionData(OptionType.STRING, "user", "Tags of the User to be described"));
    }

    @Override
    public String[] getAlias() {
        return new String[]{"gtu", "gt"};
    }


    private EmbedBuilder getGithubUserInfo(String username) {
        JSONObject json = fetchGithubUserJson(username);
        if (json != null) {
            return buildGithubUserInfo(json, username);
        } else {
            return null;
        }
    }

    private JSONObject fetchGithubUserJson(String username) {
        OkHttpClient caller = new OkHttpClient();
        Request request = new Request.Builder().url("https://api.github.com/users/" + URLEncoder.encode(username, StandardCharsets.UTF_8)).build();
        try {
            Response response = caller.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (IOException | NullPointerException | JSONException e) {
            return null;
        }
    }

    private EmbedBuilder buildGithubUserInfo(JSONObject json, String username) {
        String pseudonym = json.getString("login");
        String bio = json.optString("bio", "None");
        String location = json.optString("location", "Unknown");
        String website = json.optString("blog", "None");

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.blue);
        builder.setAuthor("Information about " + pseudonym + " (" + username + ")", "https://github.com/" + username, "http://i.imgur.com/pH59eAC.png");
        builder.setThumbnail(json.getString("avatar_url"));

        builder.addField("User bio", bio, false);
        builder.addField("Location", location, true);
        builder.addField("Website", website, true);
        builder.addField("Public repositories", String.valueOf(json.getInt("public_repos")), true);
        builder.addField("Public gists", "" + String.valueOf(json.getInt("public_gists")), true);

        builder.addField("Followers", "" + String.valueOf(json.getInt("followers")), true);
        builder.addField("Following", "" + String.valueOf(json.getInt("following")), true);

        return builder;
    }

    @Override
    public EAccessLevel getCommandLevel() {
        return EAccessLevel.USER;
    }

}
