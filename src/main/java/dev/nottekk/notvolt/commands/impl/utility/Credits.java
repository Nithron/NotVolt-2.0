package dev.nottekk.notvolt.commands.impl.utility;

import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.utils.EAccessLevel;
import dev.nottekk.notvolt.utils.others.RandomUtils;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

/**
 * A command to give credits.
 */
@Command(name = "credits", description = "command.description.credits", category = Category.INFO)
public class Credits implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {
        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder.addActionRow(Button.link("https://notvolt.nottekk.dev/#team", (RandomUtils.secureRandom.nextInt(10000) == 1562 ?
                commandEvent.getResource("message.credits.easterEgg") : commandEvent.getResource("message.credits.default"))));
        commandEvent.reply(messageCreateBuilder.build());
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
        return new String[] { "cred" };
    }

    /**
     * @inheritDoc
     */
    @Override
    public EAccessLevel getCommandLevel() {
        return EAccessLevel.USER;
    }
}
