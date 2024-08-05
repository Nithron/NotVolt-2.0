package dev.nottekk.notvolt.commands.impl.mod;

import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import de.presti.ree6.sql.SQLSession;
import de.presti.ree6.sql.entities.Setting;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

/**
 * A command used to enable or disable the receiving of news on the guild.
 */
@Command(name = "news", description = "command.description.news", category = Category.MOD)
public class News implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {
        if (!commandEvent.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            commandEvent.reply(commandEvent.getResource("message.default.insufficientPermission", Permission.MANAGE_SERVER.name()), 5);
            return;
        }

        Setting setting = SQLSession.getSqlConnector().getSqlWorker().getSetting(commandEvent.getGuild().getIdLong(), "configuration_news");

        if (setting == null) {
            SQLSession.getSqlConnector().getSqlWorker().setSetting(commandEvent.getGuild().getIdLong(), "configuration_news", "Receive News", true);
            commandEvent.reply(commandEvent.getResource("message.news.enabled"), 5);
            return;
        }

        if (setting.getBooleanValue()) {
            setting.setValue(false);
            commandEvent.reply(commandEvent.getResource("message.news.disabled"), 5);
        } else {
            setting.setValue(true);
            commandEvent.reply(commandEvent.getResource("message.news.enabled"), 5);
        }

        SQLSession.getSqlConnector().getSqlWorker().updateEntity(setting);
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
