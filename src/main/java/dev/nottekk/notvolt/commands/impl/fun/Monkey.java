package dev.nottekk.notvolt.commands.impl.fun;

import dev.nottekk.notvolt.bot.BotWorker;
import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.bot.BotConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

/**
 * Sends a gif of a monkey with a quote.
 */
@Command(name = "monke", description = "command.description.monke", category = Category.FUN)
public class Monkey implements ICommand {

	/**
	 * @inheritDoc
	 */
	@Override
	public void onPerform(CommandEvent commandEvent) {
		final EmbedBuilder em = new EmbedBuilder();

		em.setTitle(commandEvent.getResource("label.monkey"));
		em.setColor(BotWorker.randomEmbedColor());
		em.setImage("https://c.tenor.com/Y89PE1f7exQAAAAd/reject-modernity-return-to-monke.gif");
		em.setFooter(commandEvent.getResource("label.footerMessage", commandEvent.getMember().getEffectiveName(), BotConfig.getAdvertisement()), commandEvent.getMember().getEffectiveAvatarUrl());

		commandEvent.reply(em.build());
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
		return new String[]{"monkey", "monkegif"};
	}
}
