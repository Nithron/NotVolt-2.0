package dev.nottekk.notvolt.commands.impl.fun;

import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.language.LanguageService;
import dev.nottekk.notvolt.main.Main;
import dev.nottekk.notvolt.utils.apis.Neko4JsAPI;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import pw.aru.api.nekos4j.image.Image;
import pw.aru.api.nekos4j.image.ImageProvider;

/**
 * A command to send someone a hug.
 */
@Command(name = "hug", description = "command.description.hug", category = Category.FUN)
public class Hug implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {
        if (commandEvent.isSlashCommand()) {
            OptionMapping targetOption = commandEvent.getOption("target");

            if (targetOption != null && targetOption.getAsMember() != null) {
                sendHug(targetOption.getAsMember(), commandEvent);
            } else {
                commandEvent.reply(commandEvent.getResource("message.default.noMention.user"),5);
            }
        } else {
            if (commandEvent.getArguments().length == 1) {
                if (commandEvent.getMessage().getMentions().getMembers().isEmpty()) {
                    commandEvent.reply(commandEvent.getResource("message.default.noMention.user"),5);
                    commandEvent.reply(commandEvent.getResource("message.default.usage","hug @user"), 5);
                } else {
                    sendHug(commandEvent.getMessage().getMentions().getMembers().get(0), commandEvent);
                }
            } else {
                commandEvent.reply(commandEvent.getResource("message.default.usage","hug @user"), 5);
            }
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl("hug", LanguageService.getDefault("command.description.hug")).addOptions(new OptionData(OptionType.USER, "target", "The User that should be hugged!").setRequired(true));
    }

    /**
     * @inheritDoc
     */
    @Override
    public String[] getAlias() {
        return new String[0];
    }

    /**
     * Sends a Hug to the given User.
     * @param member The User that should be hugged.
     * @param commandEvent The CommandEvent.
     */
    public void sendHug(Member member, CommandEvent commandEvent) {
        Main.getInstance().getCommandManager().sendMessage(commandEvent.getResource("message.hug", member.getAsMention(), commandEvent.getMember().getAsMention()), commandEvent.getChannel(), null);

        ImageProvider ip = Neko4JsAPI.imageAPI.getImageProvider();

        Image im = null;
        try {
            im = ip.getRandomImage("hug").execute();
        } catch (Exception ignored) {
        }

        Main.getInstance().getCommandManager().sendMessage((im != null ? im.getUrl() : "https://images.ree6.de/notfound.png"), commandEvent.getChannel(), null);
        if (commandEvent.isSlashCommand()) commandEvent.getInteractionHook().sendMessage(commandEvent.getResource("message.default.checkBelow")).queue();
    }

}
