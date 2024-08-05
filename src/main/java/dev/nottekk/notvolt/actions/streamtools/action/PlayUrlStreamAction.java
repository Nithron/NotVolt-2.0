package dev.nottekk.notvolt.actions.streamtools.action;

import dev.nottekk.notvolt.actions.ActionInfo;
import dev.nottekk.notvolt.actions.streamtools.IStreamAction;
import dev.nottekk.notvolt.actions.streamtools.StreamActionEvent;
import dev.nottekk.notvolt.main.Main;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * StreamAction used to play a URL.
 */
@NoArgsConstructor
@ActionInfo(name = "PlayUrl", command = "play-url", description = "Plays a URL.", introduced = "2.2.0")
public class PlayUrlStreamAction implements IStreamAction {

    /**
     * @param event The StreamActionEvent
     */
    @Override
    public boolean runAction(@NotNull StreamActionEvent event) {
        if (event.getArguments() == null || event.getArguments().length == 0) {
            return false;
        }

        if (!Main.getInstance().getMusicWorker().isConnectedMember(event.getGuild().getSelfMember())) return false;

        Main.getInstance().getMusicWorker().loadAndPlay(event.getGuild(), null, null, event.getArguments()[0], null, true, false);
        return true;
    }
}
