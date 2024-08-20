package dev.nottekk.notvolt.listeners;

import dev.nottekk.notvolt.bot.BotConfig;
import dev.nottekk.notvolt.commands.CommandManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageCommandListener extends ListenerAdapter {

    private final CommandManager commandManager;

    public MessageCommandListener(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        String selfMention = "<@" + event.getJDA().getSelfUser().getId() + ">";
        String selfMentionWithNickname = "<@!" + event.getJDA().getSelfUser().getId() + ">";

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        String raw = event.getMessage().getContentRaw();
        String prefix = raw.startsWith(selfMention) ? selfMention : selfMentionWithNickname;

        if (raw.startsWith(prefix) || raw.startsWith(BotConfig.getDefaultPrefix())) {
            commandManager.handleCommand(event, prefix);
        }
    }
}
