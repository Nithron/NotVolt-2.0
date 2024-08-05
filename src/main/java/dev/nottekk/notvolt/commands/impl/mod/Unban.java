package dev.nottekk.notvolt.commands.impl.mod;

import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.language.LanguageService;
import dev.nottekk.notvolt.main.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

/**
 * A command to unban someone.
 */
@Command(name = "unban", description = "command.description.unban", category = Category.MOD)
public class Unban implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {

        if (commandEvent.getMember().hasPermission(Permission.BAN_MEMBERS)) {

            if (commandEvent.isSlashCommand()) {

                OptionMapping targetOption = commandEvent.getOption("id");

                if (targetOption != null) {
                    try {
                        commandEvent.getGuild().unban(UserSnowflake.fromId(targetOption.getAsString())).queue();
                        commandEvent.reply(commandEvent.getResource("message.unban.success", "<@" + targetOption.getAsString() + ">"), 5);
                    } catch (Exception ignored) {
                        commandEvent.reply(commandEvent.getResource("message.unban.notFound", targetOption.getAsString()), 5);
                    }
                } else {
                    commandEvent.reply(commandEvent.getResource("message.default.noMention.user"), 5);
                }

            } else {
                if (commandEvent.getArguments().length == 1) {
                    try {
                        String userId = commandEvent.getArguments()[0];
                        commandEvent.getGuild().unban(UserSnowflake.fromId(userId)).queue();
                        commandEvent.reply(commandEvent.getResource("message.unban.success", "<@" + userId + ">"), 5);
                    } catch (Exception ignored) {
                        commandEvent.reply(commandEvent.getResource("message.unban.notFound", commandEvent.getArguments()[0]), 5);
                    }
                } else {
                    commandEvent.reply(commandEvent.getResource("message.default.invalidQuery"), 5);
                    commandEvent.reply(commandEvent.getResource("message.default.usage","unban @user"), 5);
                }
            }
        } else {
            commandEvent.reply(commandEvent.getResource("message.default.insufficientPermission", Permission.BAN_MEMBERS.name()), 5);
        }

        Main.getInstance().getCommandManager().deleteMessage(commandEvent.getMessage(), commandEvent.getInteractionHook());
    }

    /**
     * @inheritDoc
     */
    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl("unban", LanguageService.getDefault("command.description.unban"))
                .addOptions(new OptionData(OptionType.STRING, "id", "Which User should be unbanned.").setRequired(true))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS));
    }

    /**
     * @inheritDoc
     */
    @Override
    public String[] getAlias() {
        return new String[0];
    }
}