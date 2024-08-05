package dev.nottekk.notvolt.commands.impl.fun;

import com.google.gson.JsonObject;
import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.main.Main;
import dev.nottekk.notvolt.bot.BotConfig;
import dev.nottekk.notvolt.utils.external.RequestUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;

/**
 * A command to give you a random pickup line
 * */

@Command(name = "pickupline", description = "command.description.pickupline", category = Category.FUN)
public class PickUpLine implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {
        JsonObject jsonObject = RequestUtility.requestJson(RequestUtility.Request.builder().url("https://api.dagpi.xyz/data/pickupline")
                .bearerAuth(Main.getInstance().getConfig().getConfiguration().getString("dagpi.apitoken")).build()).getAsJsonObject();

        EmbedBuilder em = new EmbedBuilder();

        // Check if there is no "category" in the JSON. If so, output the error.
        if (!jsonObject.has("category")) {
            em.setTitle("Error!");
            em.setColor(Color.RED);
            em.setDescription(commandEvent.getResource("message.default.retrievalError"));
            em.setFooter(commandEvent.getMember().getEffectiveName() + " - " + BotConfig.getAdvertisement(), commandEvent.getMember().getEffectiveAvatarUrl());
            commandEvent.reply(em.build());
            return;
        }

        if(jsonObject.has("nsfw") && jsonObject.get("nsfw").getAsBoolean()) {
            commandEvent.reply(commandEvent.getResource("message.pickupline.responseNsfw", jsonObject.get("joke").getAsString()));
        } else {
            commandEvent.reply(commandEvent.getResource("message.pickupline.response", jsonObject.get("joke").getAsString()));
        }

    }

    /**
     * @inheritDoc
     */
    @Override
    public CommandData getCommandData() {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String[] getAlias() {
        return new String[0];
    }
}
