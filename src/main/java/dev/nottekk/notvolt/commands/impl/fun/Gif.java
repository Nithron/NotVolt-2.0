package dev.nottekk.notvolt.commands.impl.fun;

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
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

@Command(name = "gif", description = "command.description.gif", category = Category.FUN)
public class Gif implements ICommand {

    @Override
    public void onPerform(CommandEvent commandEvent) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        String url;
        JSONArray array;
        String query = "";

        for(String arg : commandEvent.getArguments()) {
            query += arg.toLowerCase() + "+";
            query = query.substring(0, query.length()-1);
        }

        OkHttpClient caller = new OkHttpClient();
        Request request = new Request.Builder().url("http://api.giphy.com/v1/gifs/search?q=" + query + "&api_key=CEleBCgisUiRAz0awAFGFcDizTnaN8qx").build();
        try {
            Random rand = new Random();
            Response response = caller.newCall(request).execute();
            JSONObject json = new JSONObject(response.body().string());
            array = json.getJSONArray("data");
            //Random GIF returned by the API
            int gifIndex = rand.nextInt(array.length());
            url = (String) array.getJSONObject(gifIndex).get("url");
            embedBuilder.setColor(new Color(83, 4, 139));
            embedBuilder.setImage(url);
            commandEvent.reply(embedBuilder.build());
        } catch (IOException | NullPointerException e) {
            embedBuilder.setColor(Color.RED);
            embedBuilder.setDescription("No GIF found :cry:");
            commandEvent.reply(embedBuilder.build());
        }
    }

    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl("gif", LanguageService.getDefault("command.description.gif"))
                .addOptions(new OptionData(OptionType.STRING, "searchterm", "Term in which the gif search is based"));
    }

    @Override
    public String[] getAlias() {
        return new String[]{
                "gf"
        };
    }

    @Override
    public EAccessLevel getCommandLevel() {
        return EAccessLevel.USER;
    }

}
