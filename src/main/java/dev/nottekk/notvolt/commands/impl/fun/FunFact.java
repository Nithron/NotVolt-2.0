package dev.nottekk.notvolt.commands.impl.fun;

import com.google.gson.JsonObject;
import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.utils.external.RequestUtility;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

/**
 * A command to get random fun facts.
 */
@Command(name = "funfact", description = "command.description.funFact", category = Category.FUN)
public class FunFact implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {
        JsonObject js = RequestUtility.requestJson(RequestUtility.Request.builder().url("https://useless-facts.sameerkumar.website/api").build()).getAsJsonObject();

        commandEvent.reply(js.get("data").getAsString());
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
        return new String[] { "randomfact", "facts" };
    }
}
