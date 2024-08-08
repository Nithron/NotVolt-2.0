package dev.nottekk.notvolt.events;

import dev.nottekk.notvolt.commands.CommandManager;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.utils.data.CommandUtil;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReadyStateListener extends ListenerAdapter {

    private final CommandManager commandManager;

    public ReadyStateListener(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        for (ICommand command : commandManager.getCommands()) {
            if (command.getCommandData() != null) {
                commandData.add(command.getCommandData());
            } else {
                commandData.add(Commands.slash(CommandUtil.getCommandName(command), CommandUtil.getCommandDesString(command)));
            }
        }
        event.getJDA().updateCommands().addCommands(commandData).queue();
    }

}
