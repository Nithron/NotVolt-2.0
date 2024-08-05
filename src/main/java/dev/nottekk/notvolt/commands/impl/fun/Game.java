package dev.nottekk.notvolt.commands.impl.fun;

import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.game.core.GameManager;
import dev.nottekk.notvolt.game.core.GameSession;
import dev.nottekk.notvolt.game.core.base.GameInfo;
import dev.nottekk.notvolt.game.core.base.GamePlayer;
import dev.nottekk.notvolt.game.core.base.GameState;
import dev.nottekk.notvolt.language.LanguageService;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.ArrayList;

/**
 * Command used to access the Game System.
 */
@Command(name = "game", description = "command.description.game", category = Category.FUN)
public class Game implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {
        if (!commandEvent.isSlashCommand()) {
            commandEvent.reply(commandEvent.getResource("command.perform.onlySlashSupported"));
            return;
        }

        OptionMapping nameMapping = commandEvent.getOption("name");
        OptionMapping inviteMapping = commandEvent.getOption("invite");

        String subcommand = commandEvent.getSubcommand();

        switch (subcommand) {
            case "create" -> {

                if (nameMapping == null) {
                    commandEvent.reply(commandEvent.getResource("message.game.valueNeeded"));
                    return;
                }

                if (GameManager.getGameNames().stream().noneMatch(c -> c.equalsIgnoreCase(nameMapping.getAsString().trim()))) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(commandEvent.getResource("message.game.availableGames")).append("```");
                    GameManager.getGameCache().forEach((entry, entryValue) -> stringBuilder.append("\n").append(entry).append("- ").append(LanguageService.getByEvent(commandEvent,entryValue.getAnnotation(GameInfo.class).description())));
                    stringBuilder.append("```");
                    commandEvent.reply(stringBuilder.toString());
                    return;
                }

                ArrayList<User> participants = new ArrayList<>();
                participants.add(commandEvent.getUser());

                GamePlayer gamePlayer = new GamePlayer(commandEvent.getMember().getUser());
                gamePlayer.setInteractionHook(commandEvent.getInteractionHook());

                GameManager.createGameSession(GameManager.generateInvite(), nameMapping.getAsString(), commandEvent.getMember(),
                        commandEvent.getChannel(), participants).getGame().joinGame(gamePlayer);
            }
            case "join" -> {

                if (inviteMapping == null) {
                    commandEvent.reply(commandEvent.getResource("message.game.valueNeeded"));
                    return;
                }

                GameSession gameSession = GameManager.getGameSession(inviteMapping.getAsString());
                if (gameSession == null) {
                    commandEvent.reply(commandEvent.getResource("message.game.invalidInvite"));
                    return;
                }

                if (gameSession.getGameState() != GameState.WAITING) {
                    commandEvent.reply(commandEvent.getResource("message.game.gameAlreadyStarted"));
                    return;
                }

                gameSession.getParticipants().add(commandEvent.getMember().getUser());
                GamePlayer gamePlayer = new GamePlayer(commandEvent.getMember().getUser());
                gamePlayer.setInteractionHook(commandEvent.getInteractionHook());
                gameSession.getGame().joinGame(gamePlayer);
            }

            case "list" -> {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(commandEvent.getResource("message.game.availableGames")).append("```");
                GameManager.getGameCache().forEach((entry, entryValue) -> stringBuilder.append("\n").append(entry).append(" ").append("-").append(" ").append(LanguageService.getByEvent(commandEvent,entryValue.getAnnotation(GameInfo.class).description())));
                stringBuilder.append("```");
                commandEvent.reply(stringBuilder.toString());
            }

            default -> commandEvent.reply(commandEvent.getResource("message.game.invalidAction"));
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl("game", LanguageService.getDefault("command.description.game"))
                .addSubcommands(new SubcommandData("create", "Create a new Game match.")
                        .addOptions(new OptionData(OptionType.STRING, "name", "The Game name.", true).addChoice("Blackjack", "blackjack").addChoice("Music Quiz", "musicquiz")),
                        new SubcommandData("join", "Join a Game match.")
                                .addOption(OptionType.STRING, "invite", "The Game invite code.", true),
                        new SubcommandData("list", "List all available Games."));
    }

    /**
     * @inheritDoc
     */
    @Override
    public String[] getAlias() {
        return new String[0];
    }
}