package dev.nottekk.notvolt.events.audio;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class AudioDisconnectListener extends ListenerAdapter {
    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        if(Objects.requireNonNull(event.getGuild().getSelfMember().getVoiceState()).inAudioChannel()) {
            if(Objects.requireNonNull(event.getGuild().getSelfMember().getVoiceState().getChannel()).getMembers().size() == 1) {
                //CustomAudioPlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.player.stopTrack();
                //CustomAudioPlayerManager.getInstance().getMusicManager(event.getGuild()).scheduler.queue.clear();
                event.getGuild().getAudioManager().closeAudioConnection();
            }
        }
    }
}
