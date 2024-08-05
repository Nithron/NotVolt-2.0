package dev.nottekk.notvolt.commands.impl.hidden;

import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.language.LanguageService;
import dev.nottekk.notvolt.news.AnnouncementManager;
import dev.nottekk.notvolt.bot.BotConfig;
import dev.nottekk.notvolt.utils.others.RandomUtils;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

/**
 * A command to create an announcement.
 */
@Command(name = "announcement", description = "command.description.announcement", category = Category.HIDDEN)
public class Announcement implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {

        if (!commandEvent.getMember().getUser().getId().equalsIgnoreCase(BotConfig.getBotOwner())) {
            commandEvent.reply(commandEvent.getResource("message.default.insufficientPermission", "BE DEVELOPER"), 5);
            return;
        }

        if (!commandEvent.isSlashCommand()) {
            commandEvent.reply(commandEvent.getResource("command.perform.onlySlashSupported"));
            return;
        }

        OptionMapping title = commandEvent.getOption("title");
        OptionMapping content = commandEvent.getOption("content");
        OptionMapping toDeleteId = commandEvent.getOption("id");

        if (title != null && content != null) {
            dev.nottekk.notvolt.news.Announcement announcement =
                    new dev.nottekk.notvolt.news.Announcement(RandomUtils.randomString(16, false), title.getAsString(),
                            content.getAsString());

            AnnouncementManager.addAnnouncement(announcement);

            commandEvent.reply(commandEvent.getResource("message.announcement.added"), 5);
        } else if (toDeleteId != null) {
            AnnouncementManager.removeAnnouncement(toDeleteId.getAsString());
            commandEvent.reply(commandEvent.getResource("message.announcement.removed"), 5);
        } else {
            commandEvent.reply(commandEvent.getResource("message.announcement.list", String.join("\n",
                    AnnouncementManager.getAnnouncementList().stream().map(c -> c.id() + " -> " + c.title()).toArray(String[]::new))));
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl("announcement", LanguageService.getDefault("command.description.announcement"))
                .addOptions(new OptionData(OptionType.STRING, "title", "The title of the announcement.", false),
                        new OptionData(OptionType.STRING, "content", "The content of the announcement.", false),
                        new OptionData(OptionType.STRING, "id", "The to delete announcement id.", false));
    }

    /**
     * @inheritDoc
     */
    @Override
    public String[] getAlias() {
        return new String[0];
    }
}
