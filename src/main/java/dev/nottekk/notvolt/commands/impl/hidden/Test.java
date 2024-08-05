package dev.nottekk.notvolt.commands.impl.hidden;

import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.utils.data.ArrayUtil;
import dev.nottekk.notvolt.bot.BotConfig;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

/**
 * A command to test stuff.
 */
@Command(name = "test", description = "command.description.test", category = Category.HIDDEN)
public class Test implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {
        if (!commandEvent.getMember().getUser().getId().equalsIgnoreCase(BotConfig.getBotOwner())) {
            commandEvent.reply(commandEvent.getResource("message.default.insufficientPermission", "BE DEVELOPER"), 5);
            return;
        }

        commandEvent.reply("Contains: " + ArrayUtil.voiceJoined.containsKey(commandEvent.getMember()) + " - " + ArrayUtil.voiceJoined.size() + " - " + ArrayUtil.voiceJoined);
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
