package dev.nottekk.notvolt.builders;

import dev.nottekk.notvolt.formatters.formats.MessageFormat;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.List;

public class CommandEmbedBuilder {

    public static EmbedBuilder buildEmbed(String title, String footer, String footerImg, Color color, List<MessageFormat> messages) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.setFooter(footer, footerImg);
        embedBuilder.setColor(color);

        StringBuilder descriptionBuilder = new StringBuilder();
        for (MessageFormat messageFormat : messages) {
            if (messageFormat.bold) {
                descriptionBuilder.append("**");
            }
            if (messageFormat.italic) {
                descriptionBuilder.append("*");
            }
            if (messageFormat.underline) {
                descriptionBuilder.append("__");
            }
            descriptionBuilder.append(messageFormat.message);
            if (messageFormat.underline) {
                descriptionBuilder.append("__");
            }
            if (messageFormat.italic) {
                descriptionBuilder.append("*");
            }
            if (messageFormat.bold) {
                descriptionBuilder.append("**");
            }
            descriptionBuilder.append("\n");
        }

        embedBuilder.setDescription(descriptionBuilder.toString());

        return embedBuilder;
    }

}

