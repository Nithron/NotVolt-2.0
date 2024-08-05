package dev.nottekk.notvolt.commands.impl.fun;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.nottekk.notvolt.bot.BotWorker;
import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.main.Main;
import dev.nottekk.notvolt.bot.BotConfig;
import dev.nottekk.notvolt.utils.external.RequestUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

/**
 * A command that shows random Shiba inu images, from shibe.online
 */
@Command(name = "randomshiba", description = "command.description.shiba", category = Category.FUN)
public class ShibaImage implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {
        JsonElement jsonElement = RequestUtility.requestJson(RequestUtility.Request.builder().url("https://shibe.online/api/shibes?count=1&urls=true&httpsUrls=true").build());

        if (jsonElement.isJsonObject()) {
            commandEvent.reply(commandEvent.getResource("message.default.retrievalError"), 5);
            return;
        }

        JsonArray js = jsonElement.getAsJsonArray();

        EmbedBuilder em = new EmbedBuilder();

        em.setTitle(commandEvent.getResource("label.randomShibaImage"));
        em.setColor(BotWorker.randomEmbedColor());
        em.setImage(js.get(0).getAsString());
        em.setFooter(commandEvent.getResource("label.footerMessage", commandEvent.getMember().getEffectiveName(), BotConfig.getAdvertisement()), commandEvent.getMember().getEffectiveAvatarUrl());

        Main.getInstance().getCommandManager().sendMessage(em, commandEvent.getChannel(), commandEvent.getInteractionHook());
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
        return new String[]{"shiba", "shibaimage"};
    }
}
