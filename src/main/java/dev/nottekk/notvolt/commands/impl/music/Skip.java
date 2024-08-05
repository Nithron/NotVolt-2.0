package dev.nottekk.notvolt.commands.impl.music;

import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.language.LanguageService;
import dev.nottekk.notvolt.main.Main;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

/**
 * Skip the current Song.
 */
@Command(name = "skip", description = "command.description.skip", category = Category.MUSIC)
public class Skip implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {
        int skipAmount = 1;

        if (commandEvent.isSlashCommand()) {
            OptionMapping optionMapping = commandEvent.getOption("amount");
            if (optionMapping != null) {
                skipAmount = optionMapping.getAsInt();
            }
        } else if (commandEvent.getArguments().length >= 1) {
            try {
                skipAmount = Integer.parseInt(commandEvent.getArguments()[0]);
            } catch (NumberFormatException ignored) {
            }
        }

        if (!Main.getInstance().getMusicWorker().isConnected(commandEvent.getGuild())) {
            commandEvent.reply(commandEvent.getResource("message.music.notConnected"));
            return;
        }

        if (!Main.getInstance().getMusicWorker().checkInteractPermission(commandEvent)) {
            return;
        }

        Main.getInstance().getMusicWorker().skipTrack(commandEvent.getChannel(), commandEvent.getInteractionHook(), skipAmount);
    }

    /**
     * @inheritDoc
     */
    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl("skip", LanguageService.getDefault("command.description.skip")).addOptions(new OptionData(OptionType.INTEGER, "amount", "The amount of songs that should be skipped!").setRequired(false));
    }

    /**
     * @inheritDoc
     */
    @Override
    public String[] getAlias() {
        return new String[] { "sk", "next" };
    }
}
