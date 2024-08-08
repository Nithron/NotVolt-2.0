package dev.nottekk.notvolt.commands.impl.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.nottekk.notvolt.audio.music.GuildMusicManager;
import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.main.Main;
import dev.nottekk.notvolt.bot.BotConfig;
import dev.nottekk.notvolt.utils.EAccessLevel;
import dev.nottekk.notvolt.utils.others.FormatUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;

/**
 * Get information about the current Song.
 */
@Command(name = "songinfo", description = "command.description.songinfo", category = Category.MUSIC)
public class SongInfo implements ICommand {

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {
        if (!Main.getInstance().getMusicWorker().isConnected(commandEvent.getGuild())) {
            commandEvent.reply(commandEvent.getResource("message.music.notConnected"));
            return;
        }

        EmbedBuilder em = new EmbedBuilder();

        GuildMusicManager guildMusicManager = Main.getInstance().getMusicWorker().getGuildAudioPlayer(commandEvent.getGuild());
        AudioTrack audioTrack = guildMusicManager != null ? guildMusicManager.getPlayer().getPlayingTrack() : null;

        em.setAuthor(commandEvent.getGuild().getJDA().getSelfUser().getName(), BotConfig.getWebsite(),
                commandEvent.getGuild().getJDA().getSelfUser().getEffectiveAvatarUrl());
        em.setTitle(commandEvent.getResource("label.musicPlayer"));
        em.setThumbnail(commandEvent.getGuild().getJDA().getSelfUser().getEffectiveAvatarUrl());
        em.setColor(Color.GREEN);
        em.setDescription(audioTrack == null ? commandEvent.getResource("message.music.notPlaying") :
                commandEvent.getResource("message.music.songInfo", audioTrack.getInfo().title, audioTrack.getInfo().author,
                FormatUtil.getStatusEmoji(guildMusicManager.getPlayer()) + FormatUtil.progressBar((double)audioTrack.getPosition() / audioTrack.getDuration()),
                FormatUtil.formatTime(audioTrack.getPosition()), FormatUtil.formatTime(audioTrack.getDuration()), FormatUtil.volumeIcon(guildMusicManager.getPlayer().getVolume())));
        em.setFooter(commandEvent.getGuild().getName() + " - " + BotConfig.getAdvertisement(), commandEvent.getGuild().getIconUrl());

        commandEvent.reply(em.build(), 5);
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
        return new String[] { "trackinfo", "cq" };
    }

    /**
     * @inheritDoc
     */
    @Override
    public EAccessLevel getCommandLevel() {
        return EAccessLevel.USER;
    }
}
