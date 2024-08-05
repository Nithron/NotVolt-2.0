package dev.nottekk.notvolt.commands.impl.music;

import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.main.Main;
import dev.nottekk.notvolt.bot.BotConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;

/**
 * Stop the Ree6 from playing Music.
 */
@Command(name = "stop", description = "command.description.stop", category = Category.MUSIC)
public class Stop implements ICommand {

    @Override
    public void onPerform(CommandEvent commandEvent) {
        if (Main.getInstance().getMusicWorker().getGuildAudioPlayer(commandEvent.getGuild()) != null) {
            if (!Main.getInstance().getMusicWorker().checkInteractPermission(commandEvent) && Main.getInstance().getMusicWorker().isConnected(commandEvent.getGuild())) {
                return;
            }
            Main.getInstance().getMusicWorker().getGuildAudioPlayer(commandEvent.getGuild()).getScheduler().stopAll(commandEvent.getInteractionHook());
        } else {
            commandEvent.reply(new EmbedBuilder().setAuthor(commandEvent.getGuild().getJDA().getSelfUser().getName(), BotConfig.getWebsite(),
                            commandEvent.getGuild().getJDA().getSelfUser().getEffectiveAvatarUrl()).setTitle(commandEvent.getResource("label.musicPlayer"))
                    .setThumbnail(commandEvent.getGuild().getJDA().getSelfUser().getEffectiveAvatarUrl())
                    .setColor(Color.RED)
                    .setDescription(commandEvent.getResource("message.music.notPlaying")).build(), 5);
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