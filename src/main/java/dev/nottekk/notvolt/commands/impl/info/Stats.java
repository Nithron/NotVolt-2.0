package dev.nottekk.notvolt.commands.impl.info;

import dev.nottekk.notvolt.bot.BotWorker;
import dev.nottekk.notvolt.commands.Category;
import dev.nottekk.notvolt.commands.CommandEvent;
import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;
import dev.nottekk.notvolt.main.Main;
import de.presti.ree6.sql.SQLSession;
import de.presti.ree6.sql.entities.stats.CommandStats;
import de.presti.ree6.sql.entities.stats.GuildCommandStats;
import dev.nottekk.notvolt.bot.BotConfig;
import dev.nottekk.notvolt.utils.others.TimeUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A command to show you the stats of Ree6.
 */
@Command(name = "stats", description = "command.description.stats", category = Category.INFO)
public class Stats implements ICommand {

    boolean firstBoot;

    /**
     * @inheritDoc
     */
    @Override
    public void onPerform(CommandEvent commandEvent) {

        Main.getInstance().getCommandManager().deleteMessage(commandEvent.getMessage(), commandEvent.getInteractionHook());

        long start = System.currentTimeMillis();

        Message message = null;
        if (commandEvent.isSlashCommand()) {
            message = commandEvent.getInteractionHook().sendMessage(commandEvent.getResource("label.loading")).complete();
        } else {
            message = commandEvent.getChannel().sendMessage(commandEvent.getResource("label.loading")).complete();
        }

        long ping = System.currentTimeMillis() - start;
        long computeTimeStart = System.currentTimeMillis();

        EmbedBuilder em = new EmbedBuilder();

        em.setAuthor(commandEvent.getGuild().getJDA().getSelfUser().getName(), BotConfig.getWebsite(),
                commandEvent.getGuild().getJDA().getSelfUser().getEffectiveAvatarUrl());
        em.setTitle(commandEvent.getResource("label.statistics"));
        em.setThumbnail(commandEvent.getGuild().getJDA().getSelfUser().getEffectiveAvatarUrl());
        em.setColor(BotWorker.randomEmbedColor());

        long memberCount = firstBoot ? BotWorker.getShardManager().getUsers().size() : BotWorker.getShardManager().getUserCache().size();
        firstBoot = true;

        em.addField("**" + commandEvent.getResource("label.serverStats") + ":**", "", true);
        em.addField("**" + commandEvent.getResource("label.guilds") + "**", BotWorker.getShardManager().getGuilds().size() + "", true);
        em.addField("**" + commandEvent.getResource("label.users") + "**", String.valueOf(memberCount), true);

        em.addField("**" + commandEvent.getResource("label.botStats") + ":**", "", true);
        em.addField("**" + commandEvent.getResource("label.version") + "**", BotWorker.getBuild() + "-" + BotWorker.getVersion().name().toUpperCase()
                + " [[" + BotWorker.getCommit() + "](" + BotWorker.getRepository().replace(".git", "") + "/commit/" + BotWorker.getCommit() + ")]", true);
        em.addField("**" + commandEvent.getResource("label.uptime") + "**", TimeUtil.getTime(BotWorker.getStartTime()), true);

        em.addField("**" + commandEvent.getResource("label.discordStats") + ":**", "", true);
        em.addField("**" + commandEvent.getResource("label.gatewayTime") + "**", BotWorker.getShardManager().getAverageGatewayPing() + "ms", true);
        em.addField("**" + commandEvent.getResource("label.shardAmount") + "**", BotWorker.getShardManager().getShards().size() + " "  + commandEvent.getResource("label.shards"), true);

        em.addField("**" + commandEvent.getResource("label.networkStats") + ":**", "", true);
        em.addField("**" + commandEvent.getResource("label.responseTime") + "**", (Integer.parseInt((ping) + "")) + "ms", true);
        em.addField("**" + commandEvent.getResource("label.systemDate") + "**", new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date()), true);

        StringBuilder end = new StringBuilder();

        for (GuildCommandStats values : SQLSession.getSqlConnector().getSqlWorker().getStats(commandEvent.getGuild().getIdLong())) {
            end.append(values.getCommand()).append(" - ").append(values.getUses()).append("\n");
        }

        StringBuilder end2 = new StringBuilder();

        for (CommandStats values : SQLSession.getSqlConnector().getSqlWorker().getStatsGlobal()) {
            end2.append(values.getCommand()).append(" - ").append(values.getUses()).append("\n");
        }

        em.addField("**" + commandEvent.getResource("label.commandStats") + ":**", "", true);
        em.addField("**" + commandEvent.getResource("label.topCommands") + "**", end.toString(), true);
        em.addField("**" + commandEvent.getResource("label.overallTopCommands") + "**", end2.toString(), true);

        em.setFooter(commandEvent.getGuild().getName() + " - " + BotConfig.getAdvertisement(), commandEvent.getGuild().getIconUrl());

        MessageEditBuilder messageEditBuilder = new MessageEditBuilder();

        messageEditBuilder.setContent("");

        long computeTime = System.currentTimeMillis() - computeTimeStart;

        if (BotConfig.isDebug()) {
            em.addField("**DEV ONLY**", "", true);
            em.addField("**Compute Time**", computeTime + "ms", true);
            em.addField("**DEV ONLY*", "", true);
        }

        messageEditBuilder.setEmbeds(em.build());

        commandEvent.update(message, messageEditBuilder.build());
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
