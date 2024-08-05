package dev.nottekk.notvolt.commands.impl.mod;

import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.main.Main;
import dev.nottekk.notvolt.module.invite.InviteContainerManager;
import de.presti.ree6.sql.SQLSession;
import dev.nottekk.notvolt.utils.others.ThreadUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.time.Duration;
import java.util.ArrayList;

/**
 * A command to clear the Invite-Data stored.
 */
@Command(name = "cleardata", category = Category.MOD, description = "command.description.clearData")
public class ClearData implements ICommand {

    /**
     * A list of all timeout Ids, since it is not good to allow them to clear the Invite data every second.
     */
    ArrayList<String> timeout = new ArrayList<>();

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {
        if (commandEvent.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            if (!timeout.contains(commandEvent.getGuild().getId())) {
                SQLSession.getSqlConnector().getSqlWorker().clearInvites(commandEvent.getGuild().getIdLong());
                if (commandEvent.getGuild().getSelfMember().hasPermission(Permission.MANAGE_SERVER))
                    commandEvent.getGuild().retrieveInvites().queue(invites -> invites.stream().filter(invite -> invite.getInviter() != null).forEach(invite -> SQLSession.getSqlConnector().getSqlWorker().setInvite(commandEvent.getGuild().getIdLong(), invite.getInviter().getIdLong(), invite.getCode(), invite.getUses())));

                Invite vanityInvite = InviteContainerManager.convertVanityInvite(commandEvent.getGuild());

                if (vanityInvite != null) {
                    SQLSession.getSqlConnector().getSqlWorker().setInvite(commandEvent.getGuild().getIdLong(), commandEvent.getGuild().getOwnerIdLong(), vanityInvite.getCode(), vanityInvite.getUses());
                }

                commandEvent.reply(commandEvent.getResource("message.clearData.success"), 5);
                Main.getInstance().getInviteContainerManager().refreshGuild(commandEvent.getGuild().getIdLong());
                ThreadUtil.createThread(x -> timeout.remove(commandEvent.getGuild().getId()), null, Duration.ofMinutes(10), false, false);
            } else {
                commandEvent.reply(commandEvent.getResource("message.clearData.cooldown"), 5);
            }
        } else {
            commandEvent.reply(commandEvent.getResource("message.default.insufficientPermission", Permission.ADMINISTRATOR.name()), 5);
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
