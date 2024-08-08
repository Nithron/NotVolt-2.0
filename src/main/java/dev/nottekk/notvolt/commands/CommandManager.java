package dev.nottekk.notvolt.commands;

import dev.nottekk.notvolt.bot.BotConfig;
import dev.nottekk.notvolt.commands.impl.music.*;
import dev.nottekk.notvolt.commands.impl.utility.*;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.utils.access.AccessUtils;
import dev.nottekk.notvolt.utils.data.ArrayUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CommandManager {

    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager() {

        //adm
        //addCommand(new ChangeAvatarCommand());
        //addCommand(new ChangeStatusCommand());
        //addCommand(new RenameCommand());

        //fun
        //addCommand(new GifCommand());
        //addCommand(new YoMamaCommand());
        //music
        addCommand(new Clearqueue());
        addCommand(new Disconnect());
        addCommand(new Loop());
        addCommand(new Lyrics());
        addCommand(new MusicPanel());
        addCommand(new Pause());
        addCommand(new Play());
        addCommand(new Stop());
        addCommand(new Skip());
        addCommand(new Resume());
        addCommand(new Seek());
        addCommand(new Shuffle());
        addCommand(new SongInfo());
        addCommand(new SongList());
        addCommand(new Volume());
        //utility
        addCommand(new Info());
        addCommand(new GitHub());
        addCommand(new Help());
        addCommand(new Ping());
        addCommand(new WhoIs());

    }

    private void addCommand(ICommand command) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> getCommandName(it).equalsIgnoreCase(getCommandName(command)));

        if (nameFound) {
            throw new IllegalArgumentException("A Command with this name is already present");
        }

        commands.add(command);
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    @Nullable
    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand command : this.commands) {
            if (getCommandName(command).equals(searchLower)) {
                return command;
            }

            for (String alias : command.getAlias()) {
                if (alias.equals(searchLower)) {
                    return command;
                }
            }
        }

        return null;
    }

    public void handleCommand(MessageReceivedEvent event, String prefix) {
        User user = event.getMessage().getAuthor();
        String[] arguments = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        ICommand command = this.getCommand(arguments[1].toLowerCase());

        if (command != null) {
            if (AccessUtils.userHasAccess(user, command)) {
                event.getChannel().sendTyping().queue();
                // Remove the second argument if it exists
                if (arguments.length > 1) {
                    arguments = Stream.concat(
                            Stream.of(arguments[0]),
                            Arrays.stream(arguments).skip(2)
                    ).toArray(String[]::new);
                }
                CommandEvent commandEvent = new CommandEvent(event.getJDA(), getCommandName(command), event.getMember(), event.getGuild(), event.getMessage(), event.getGuildChannel(), arguments, null);
                command.onPerform(commandEvent);
            } else {
               event.getChannel().sendMessageEmbeds(AccessUtils.buildAccessResultEmbed(false).build()).queue();
            }
        }
    }

    public void handleCommand(SlashCommandInteractionEvent event) {
        User user = event.getUser();
        String name = event.getName();
        Optional<ICommand> commandOptional = Optional.ofNullable(this.getCommand(name));

        commandOptional.ifPresent(command -> {
            if (AccessUtils.userHasAccess(user, command)) {
                List<OptionMapping> options = event.getOptions();
                String[] arguments = options.stream()
                        .map(OptionMapping::getAsString)
                        .toArray(String[]::new);
                CommandEvent commandEvent = new CommandEvent(event.getJDA(), getCommandName(command), event.getMember(), event.getGuild(), null, event.getGuildChannel(), arguments, event);
                command.onPerform(commandEvent);
            } else {
                event.replyEmbeds(AccessUtils.buildAccessResultEmbed(false).build()).queue();
            }
        });
    }

    public String getCommandName(ICommand command) {
        Class<?> commandClass = command.getClass();
        if (commandClass.isAnnotationPresent(Command.class)) {
            return commandClass.getAnnotation(Command.class).name();
        } else {
            return null;
        }
    }

    /**
     * Check if a User is time-outed.
     *
     * @param user the User.
     * @return true, if yes | false, if not.
     */
    public boolean isTimeout(User user) {
        return ArrayUtil.commandCooldown.contains(user.getId()) && !BotConfig.isDebug();
    }

    /**
     * Send a message to a special Message-Channel.
     *
     * @param messageCreateData the Message content.
     * @param commandEvent      the Command-Event.
     */
    public void sendMessage(MessageCreateData messageCreateData, CommandEvent commandEvent) {
        sendMessage(messageCreateData, commandEvent.getChannel(), commandEvent.getInteractionHook());
    }

    /**
     * Send a message to a special Message-Channel.
     *
     * @param messageCreateData the Message content.
     * @param deleteSecond      the delete delay
     * @param commandEvent      the Command-Event.
     */
    public void sendMessage(MessageCreateData messageCreateData, int deleteSecond, CommandEvent commandEvent) {
        sendMessage(messageCreateData, deleteSecond, commandEvent.getChannel(), commandEvent.getInteractionHook());
    }

    /**
     * Send a message to a special Message-Channel.
     *
     * @param messageCreateData the Message content.
     * @param messageChannel    the Message-Channel.
     */
    public void sendMessage(MessageCreateData messageCreateData, MessageChannel messageChannel) {
        sendMessage(messageCreateData, messageChannel, null);
    }

    /**
     * Send a message to a special Message-Channel, with a deletion delay.
     *
     * @param messageCreateData the Message content.
     * @param deleteSecond      the delete delay
     * @param messageChannel    the Message-Channel.
     */
    public void sendMessage(MessageCreateData messageCreateData, int deleteSecond, MessageChannel messageChannel) {
        sendMessage(messageCreateData, deleteSecond, messageChannel, null);
    }

    /**
     * Send a message to a special Message-Channel.
     *
     * @param messageCreateData the Message content.
     * @param messageChannel    the Message-Channel.
     * @param interactionHook   the Interaction-hook if it is a slash command.
     */
    public void sendMessage(MessageCreateData messageCreateData, MessageChannel messageChannel, InteractionHook interactionHook) {
        if (interactionHook == null) {
            if (messageChannel.canTalk()) messageChannel.sendMessage(messageCreateData).queue();
        } else interactionHook.sendMessage(messageCreateData).queue();
    }

    /**
     * Send a message to a special Message-Channel, with a deletion delay.
     *
     * @param messageCreateData the Message content.
     * @param messageChannel    the Message-Channel.
     * @param interactionHook   the Interaction-hook if it is a slash command.
     * @param deleteSecond      the delete delay
     */
    public void sendMessage(MessageCreateData messageCreateData, int deleteSecond, MessageChannel messageChannel, InteractionHook interactionHook) {
        if (interactionHook == null) {
            if (messageChannel == null) return;
            if (messageChannel.canTalk())
                messageChannel.sendMessage(messageCreateData).delay(deleteSecond, TimeUnit.SECONDS).flatMap(message -> {
                    if (message != null && message.getChannel().retrieveMessageById(message.getId()).complete() != null) {
                        return message.delete();
                    }

                    return null;
                }).queue();
        } else {
            interactionHook.sendMessage(messageCreateData).queue();
        }
    }

    /**
     * Send a message to a special Message-Channel.
     *
     * @param message        the Message content.
     * @param messageChannel the Message-Channel.
     */
    public void sendMessage(String message, MessageChannel messageChannel) {
        sendMessage(message, messageChannel, null);
    }

    /**
     * Send a message to a special Message-Channel, with a deletion delay.
     *
     * @param message        the Message content.
     * @param deleteSecond   the delete delay
     * @param messageChannel the Message-Channel.
     */
    public void sendMessage(String message, int deleteSecond, MessageChannel messageChannel) {
        sendMessage(message, deleteSecond, messageChannel, null);
    }

    /**
     * Send a message to a special Message-Channel.
     *
     * @param message         the Message content.
     * @param messageChannel  the Message-Channel.
     * @param interactionHook the Interaction-hook if it is a slash command.
     */
    public void sendMessage(String message, MessageChannel messageChannel, InteractionHook interactionHook) {
        sendMessage(new MessageCreateBuilder().setContent(message).build(), messageChannel, interactionHook);
    }

    /**
     * Send a message to a special Message-Channel, with a deletion delay.
     *
     * @param messageContent  the Message content.
     * @param messageChannel  the Message-Channel.
     * @param interactionHook the Interaction-hook if it is a slash command.
     * @param deleteSecond    the delete delay
     */
    public void sendMessage(String messageContent, int deleteSecond, MessageChannel messageChannel, InteractionHook interactionHook) {
        sendMessage(new MessageCreateBuilder().setContent(messageContent).build(), deleteSecond, messageChannel, interactionHook);
    }

    /**
     * Send an Embed to a special Message-Channel.
     *
     * @param embedBuilder   the Embed content.
     * @param messageChannel the Message-Channel.
     */
    public void sendMessage(EmbedBuilder embedBuilder, MessageChannel messageChannel) {
        sendMessage(embedBuilder, messageChannel, null);
    }

    /**
     * Send an Embed to a special Message-Channel, with a deletion delay.
     *
     * @param embedBuilder   the Embed content.
     * @param deleteSecond   the delete delay
     * @param messageChannel the Message-Channel.
     */
    public void sendMessage(EmbedBuilder embedBuilder, int deleteSecond, MessageChannel messageChannel) {
        sendMessage(embedBuilder, deleteSecond, messageChannel, null);
    }

    /**
     * Send an Embed to a special Message-Channel.
     *
     * @param embedBuilder    the Embed content.
     * @param messageChannel  the Message-Channel.
     * @param interactionHook the Interaction-hook if it is a slash command.
     */
    public void sendMessage(EmbedBuilder embedBuilder, MessageChannel messageChannel, InteractionHook interactionHook) {
        sendMessage(new MessageCreateBuilder().setEmbeds(embedBuilder.build()).build(), messageChannel, interactionHook);
    }

    /**
     * Send an Embed to a special Message-Channel, with a deletion delay.
     *
     * @param embedBuilder    the Embed content.
     * @param deleteSecond    the delete delay
     * @param messageChannel  the Message-Channel.
     * @param interactionHook the Interaction-hook if it is a slash command.
     */
    public void sendMessage(EmbedBuilder embedBuilder, int deleteSecond, MessageChannel messageChannel, InteractionHook interactionHook) {
        sendMessage(new MessageCreateBuilder().setEmbeds(embedBuilder.build()).build(), deleteSecond, messageChannel, interactionHook);
    }

    /**
     * Delete a specific message.
     *
     * @param message         the {@link Message} entity.
     * @param interactionHook the Interaction-hook, if it is a slash event.
     */
    public void deleteMessage(Message message, InteractionHook interactionHook) {
        if (message != null && message.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE) &&
                message.getChannel().retrieveMessageById(message.getIdLong()).complete() != null &&
                message.getType().canDelete() &&
                !message.isEphemeral() &&
                interactionHook == null) {
            message.delete().onErrorMap(throwable -> {
                //log.error("[CommandManager] Couldn't delete a Message!", throwable);
                return null;
            }).queue();
        }
    }
}
