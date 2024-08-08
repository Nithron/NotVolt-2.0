package dev.nottekk.notvolt.commands.impl.utility;

import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.language.LanguageService;
import dev.nottekk.notvolt.utils.EAccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Command(name = "whois", description = "command.description.whois", category = Category.INFO)
public class WhoIs implements ICommand {

    @Override
    public void onPerform(CommandEvent commandEvent) {
       if(commandEvent.getMessage().getMentions().getUsers().isEmpty()) {
            commandEvent.getChannel().sendMessage("No user mentioned.").queue();
        } else {
            User user = commandEvent.getMessage().getMentions().getUsers().get(1);
            EmbedBuilder builder = new EmbedBuilder();
            builder.setAuthor("Information about " + user.getGlobalName(), null, "http://i.imgur.com/880AyL6.png");
            builder.setColor(commandEvent.getGuild().getMemberById(user.getId()).getColor());
            builder.setThumbnail(user.getAvatarUrl());
            builder.addField(":id: User ID", user.getId(), true);

            String nickname = "None";
            if (commandEvent.getGuild().getMemberById(user.getId()).getNickname() != null) {
                nickname = commandEvent.getGuild().getMemberById(user.getId()).getNickname();
            }
            builder.addField(":information_source: Nickname", nickname, true);
            //builder.addField(":computer: Status", event.getJDA().getUserById(user.getIdLong()).get, true);

            String activity = "None";
            if (!commandEvent.getGuild().getMemberById(user.getId()).getActivities().isEmpty()) {
                activity = commandEvent.getGuild().getMemberById(user.getId()).getActivities().get(0).getName();
            }
            builder.addField(":video_game: Activity", activity, true);

            String isOwner = "No";
            if (commandEvent.getGuild().getMemberById(user.getId()).isOwner()) {
                isOwner = "Yes";
            }
            builder.addField(":white_check_mark: Owner", isOwner, true);

            String role = "No role";
            if (!commandEvent.getGuild().getMemberById(user.getId()).getRoles().isEmpty()) {
                role = commandEvent.getGuild().getMemberById(user.getId()).getRoles().get(0).getAsMention();
            }
            builder.addField(":medal: Highest role", role, true);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");
            builder.addField(":clock2: Creation date", user.getTimeCreated().format(formatter), true);
            builder.addField(":inbox_tray:  Join date", commandEvent.getGuild().getMemberById(user.getId()).getTimeJoined().format(formatter), true);

           commandEvent.reply(builder.build());
        }
    }

    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl("whois", LanguageService.getDefault("command.description.whois_slash"))
                .addOptions(new OptionData(OptionType.STRING, "user", "Tags of the User to be described"));
    }

    @Override
    public String[] getAlias() {
        return new String[]{
                "wi", "who"
        };
    }

    @Override
    public EAccessLevel getCommandLevel() {
        return EAccessLevel.USER;
    }

}
