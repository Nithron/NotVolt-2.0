package dev.nottekk.notvolt.commands.impl.fun;

import com.google.gson.JsonObject;
import dev.nottekk.notvolt.bot.BotWorker;
import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.bot.BotConfig;
import dev.nottekk.notvolt.utils.external.RequestUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

/**
 * A command to get a random dog images.
 */
@Command(name = "randomdog", description = "command.description.randomdog", category = Category.FUN)
public class DogImage implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {
        JsonObject js = RequestUtility.requestJson(RequestUtility.Request.builder().url("https://dog.ceo/api/breeds/image/random").build()).getAsJsonObject();

        EmbedBuilder em = new EmbedBuilder();

        em.setTitle(commandEvent.getResource("label.randomDogImage"));
        em.setColor(BotWorker.randomEmbedColor());
        em.setImage(js.get("message").getAsString());
        em.setFooter(commandEvent.getResource("label.footerMessage", commandEvent.getMember().getEffectiveName(), BotConfig.getAdvertisement()), commandEvent.getMember().getEffectiveAvatarUrl());

        commandEvent.reply(em.build());
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
        return new String[]{"dog", "dogimage"};
    }
}
