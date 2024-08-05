package dev.nottekk.notvolt.commands.impl.fun;

import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.utils.data.ArrayUtil;
import dev.nottekk.notvolt.utils.others.RandomUtils;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

/**
 * A command to give you a random response.
 */
@Command(name = "8ball", description = "command.description.8ball", category = Category.FUN)
public class RandomAnswer implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {
        commandEvent.reply(commandEvent.getResource(ArrayUtil.answers[RandomUtils.random.nextInt((ArrayUtil.answers.length - 1))]));
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
        return new String[] { "answer", "randomanswer" };
    }
}
