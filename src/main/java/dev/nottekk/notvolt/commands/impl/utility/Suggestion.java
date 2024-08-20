package dev.nottekk.notvolt.commands.impl.utility;

import dev.nottekk.notvolt.bot.BotConfig;
import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.language.LanguageService;
import dev.nottekk.notvolt.utils.EAccessLevel;
import dev.nottekk.notvolt.utils.data.DateUtil;
import dev.nottekk.notvolt.utils.data.ProfanityUtil;
import dev.nottekk.notvolt.utils.data.SuggestionUtil;
import dev.nottekk.notvolt.utils.log.IncidentsLogUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.awt.*;

@Command(name = "suggestion", description = "command.description.suggestion", category = Category.INFO)
public class Suggestion implements ICommand {
    @Override
    public void onPerform(CommandEvent commandEvent) {
        String rawSuggestion = buildSuggestionFromArguments(commandEvent.getArguments());
        String guildName = commandEvent.getGuild().getName();
        String username = commandEvent.getUser().getName();
        String date = DateUtil.getCurrentDateTime();

        EmbedBuilder embedBuilder;

        if (!rawSuggestion.isEmpty()) {
            if (!ProfanityUtil.containsCurseWords(rawSuggestion)) {
                String suggestionToLog = SuggestionUtil.buildSuggestionLine(date, guildName, username, rawSuggestion);
                SuggestionUtil.logSuggestion(suggestionToLog);
                embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Color.GREEN);
                embedBuilder.setTitle("Suggestion");
                embedBuilder.setDescription("Thanks for your Suggestion! Our team was informed, will verify it and get back to you if needed.");
                embedBuilder.setFooter(BotConfig.getAdvertisement());
                commandEvent.reply(embedBuilder.build());
            } else {
                String suggestionWithIncident = IncidentsLogUtil.buildIncidentLine(date, guildName, username, "true", rawSuggestion);
                IncidentsLogUtil.logSuggestion(suggestionWithIncident);
                embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Color.RED);
                embedBuilder.setTitle("Suggestion - Incident");
                embedBuilder.setDescription("Curse words detected - Incident was reported");
                embedBuilder.setFooter(BotConfig.getAdvertisement());
                commandEvent.reply(embedBuilder.build());
            }
        }
    }

    private String buildSuggestionFromArguments(String[] arguments) {
        if (arguments == null || arguments.length == 0) {
            return "";
        }
        StringBuilder sentence = new StringBuilder();
        for (String word : arguments) {
            sentence.append(word).append(" ");
        }

        return sentence.toString().trim();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandDataImpl("suggestion", LanguageService.getDefault("command.description.suggestion"))
                .addOptions(new OptionData(OptionType.STRING, "suggestion", "Your suggestion for improvement :)"));
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public EAccessLevel getCommandLevel() {
        return EAccessLevel.USER;
    }
}
