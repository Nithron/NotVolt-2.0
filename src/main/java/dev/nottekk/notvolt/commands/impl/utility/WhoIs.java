package dev.nottekk.notvolt.commands.impl.utility;

import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.language.LanguageService;
import dev.nottekk.notvolt.utils.EAccessLevel;
import dev.nottekk.notvolt.utils.data.UserUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.awt.*;
import java.time.format.DateTimeFormatter;

@Command(name = "whois", description = "command.description.whois", category = Category.INFO)
public class WhoIs implements ICommand {

    @Override
    public void onPerform(CommandEvent commandEvent) {
        User mentionedUser;
        if (commandEvent.isSlashCommand()) {
            mentionedUser = UserUtils.getUserByID(commandEvent, commandEvent.getArguments()[0]);
        } else {
            mentionedUser = commandEvent.getMessage().getMentions().getUsers().get(0);
        }

        if (mentionedUser == null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.RED);
            embedBuilder.setDescription("No user mentioned.");
            commandEvent.reply(embedBuilder.build());
        } else {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setAuthor("Information about " + mentionedUser.getGlobalName(), null, "http://i.imgur.com/880AyL6.png");
            builder.setColor(commandEvent.getGuild().getMemberById(mentionedUser.getId()).getColor());
            builder.setThumbnail(mentionedUser.getAvatarUrl());
            builder.addField(":id: User ID", mentionedUser.getId(), true);

            String nickname = "None";
            if (commandEvent.getGuild().getMemberById(mentionedUser.getId()).getNickname() != null) {
                nickname = commandEvent.getGuild().getMemberById(mentionedUser.getId()).getNickname();
            }
            builder.addField(":information_source: Nickname", nickname, true);
            //builder.addField(":computer: Status", event.getJDA().getUserById(user.getIdLong()).get, true);

            String activity = "None";
            if (!commandEvent.getGuild().getMemberById(mentionedUser.getId()).getActivities().isEmpty()) {
                activity = commandEvent.getGuild().getMemberById(mentionedUser.getId()).getActivities().get(0).getName();
            }
            builder.addField(":video_game: Activity", activity, true);

            String isOwner = "No";
            if (commandEvent.getGuild().getMemberById(mentionedUser.getId()).isOwner()) {
                isOwner = "Yes";
            }
            builder.addField(":white_check_mark: Owner", isOwner, true);

            String role = "No role";
            if (!commandEvent.getGuild().getMemberById(mentionedUser.getId()).getRoles().isEmpty()) {
                role = commandEvent.getGuild().getMemberById(mentionedUser.getId()).getRoles().get(0).getAsMention();
            }
            builder.addField(":medal: Highest role", role, true);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");
            builder.addField(":clock2: Creation date", mentionedUser.getTimeCreated().format(formatter), true);
            builder.addField(":inbox_tray:  Join date", commandEvent.getGuild().getMemberById(mentionedUser.getId()).getTimeJoined().format(formatter), true);

           commandEvent.reply(builder.build());
        }
    }

    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl("whois", LanguageService.getDefault("command.description.whois"))
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
