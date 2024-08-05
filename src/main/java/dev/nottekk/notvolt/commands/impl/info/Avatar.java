package dev.nottekk.notvolt.commands.impl.info;

import dev.nottekk.notvolt.bot.BotConfig;
import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.language.LanguageService;
import dev.nottekk.notvolt.utils.others.GuildUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

/**
 * Allows you to get the profile picture of anyone.
 */
@Command(name = "avatar", description = "command.description.avatar", category = Category.INFO)
public class Avatar implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {
        if (commandEvent.isSlashCommand()) {
            OptionMapping targetOption = commandEvent.getOption("target");

            if (targetOption != null && targetOption.getAsMember() != null) {
                sendAvatar(targetOption.getAsUser(), commandEvent);
            } else {
                sendAvatar(commandEvent.getMember().getUser(), commandEvent);
            }

        } else {
            if (commandEvent.getArguments().length == 1) {
                if (commandEvent.getMessage().getMentions().getUsers().isEmpty()) {
                    commandEvent.reply(commandEvent.getResource("message.default.noMention.user"), 5);
                    commandEvent.reply(commandEvent.getResource("message.default.usage","avatar @user"), 5);
                } else {
                    sendAvatar(commandEvent.getMessage().getMentions().getUsers().get(0), commandEvent);
                }
            } else {
                sendAvatar(commandEvent.getMember().getUser(), commandEvent);
            }
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl("avatar", LanguageService.getDefault("command.description.avatar"))
                .addOptions(new OptionData(OptionType.USER, "target", "The User whose profile you want.").setRequired(true));
    }

    /**
     * @inheritDoc
     */
    @Override
    public String[] getAlias() {
        return new String[0];
    }

    /**
     * Sends the Profile picture of a User.
     * @param member The User to get the Profile from.
     * @param commandEvent The CommandEvent.
     */
    public void sendAvatar(User member, CommandEvent commandEvent) {
        EmbedBuilder em = new EmbedBuilder();

        em.setTitle(commandEvent.getResource("label.avatar"));
        em.setAuthor(member.getEffectiveName() + (GuildUtil.isSupporter(member) ? " <a:duckswing:1070690323459735682>" : ""), member.getEffectiveAvatarUrl(), member.getEffectiveAvatarUrl());
        em.setImage(member.getEffectiveAvatar().getUrl(1024));
        em.setFooter(commandEvent.getResource("label.footerMessage", commandEvent.getMember().getEffectiveName(), BotConfig.getAdvertisement()), commandEvent.getMember().getEffectiveAvatarUrl());

        commandEvent.reply(em.build());
    }
}
