package dev.nottekk.notvolt.events;

import dev.nottekk.notvolt.audio.music.GuildMusicManager;
import dev.nottekk.notvolt.bot.BotConfig;
import dev.nottekk.notvolt.language.LanguageService;
import dev.nottekk.notvolt.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.internal.interactions.component.StringSelectMenuImpl;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * EventHandler for Menu Events.
 */
public class MenuEvents extends ListenerAdapter {

    /**
     * @inheritDoc
     */
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        super.onButtonInteraction(event);

        switch (event.getComponentId()) {
            case "nv_feedback" -> {
                Modal.Builder builder = Modal.create("nv_feedback_modal", LanguageService.getByGuild(event.getGuild(), "label.feedback"));
                builder.addActionRow(TextInput.create("nv_feedback_text", LanguageService.getByGuild(event.getGuild(), "label.feedback"), TextInputStyle.PARAGRAPH).setRequired(true).setMaxLength(2042).setMinLength(16).build());
                event.replyModal(builder.build()).queue();
            }

            case "nv_suggestion" -> {
                Modal.Builder builder = Modal.create("nv_suggestion_modal", LanguageService.getByGuild(event.getGuild(), "label.suggestion"));
                builder.addActionRow(TextInput.create("nv_suggestion_text", LanguageService.getByGuild(event.getGuild(), "label.suggestion"), TextInputStyle.PARAGRAPH).setRequired(true).setMaxLength(2042).setMinLength(16).build());
                event.replyModal(builder.build()).queue();
            }

            case "nv_music_play" -> {
                event.deferReply(true).queue();
                GuildMusicManager guildMusicManager = Main.getInstance().getMusicWorker().getGuildAudioPlayer(event.getGuild());

                if (guildMusicManager != null) {
                    if (guildMusicManager.getPlayer().isPaused()) {
                        guildMusicManager.getPlayer().setPaused(false);
                        EmbedBuilder em = new EmbedBuilder()
                                .setAuthor(event.getGuild().getJDA().getSelfUser().getName(), BotConfig.getWebsite(),
                                        event.getGuild().getJDA().getSelfUser().getEffectiveAvatarUrl())
                                .setTitle(LanguageService.getByGuild(event.getGuild(), "label.musicPlayer"))
                                .setThumbnail(event.getGuild().getJDA().getSelfUser().getEffectiveAvatarUrl())
                                .setColor(Color.GREEN)
                                .setDescription(LanguageService.getByGuild(event.getGuild(), "message.music.resume"))
                                .setFooter(event.getGuild().getName() + " - " + BotConfig.getAdvertisement(), event.getGuild().getIconUrl());
                        Main.getInstance().getCommandManager().sendMessage(em, event.getChannel(), event.getHook());
                    } else {
                        EmbedBuilder em = new EmbedBuilder()
                                .setAuthor(event.getGuild().getJDA().getSelfUser().getName(), BotConfig.getWebsite(),
                                        event.getGuild().getJDA().getSelfUser().getEffectiveAvatarUrl())
                                .setTitle(LanguageService.getByGuild(event.getGuild(), "label.musicPlayer"))
                                .setThumbnail(event.getGuild().getJDA().getSelfUser().getEffectiveAvatarUrl())
                                .setColor(Color.GREEN)
                                .setDescription(LanguageService.getByGuild(event.getGuild(), "message.music.pause"))
                                .setFooter(event.getGuild().getName() + " - " + BotConfig.getAdvertisement(), event.getGuild().getIconUrl());
                        Main.getInstance().getCommandManager().sendMessage(em, event.getChannel(), event.getHook());
                    }
                } else {
                    Main.getInstance().getCommandManager().sendMessage(LanguageService.getByGuild(event.getGuild(), "message.music.notConnected"),
                            event.getChannel(), event.getHook());
                }
            }

            case "nv_music_pause" -> {
                event.deferReply(true).queue();
                GuildMusicManager guildMusicManager = Main.getInstance().getMusicWorker().getGuildAudioPlayer(event.getGuild());

                if (guildMusicManager != null) {
                    guildMusicManager.getPlayer().setPaused(true);
                    EmbedBuilder em = new EmbedBuilder()
                            .setAuthor(event.getGuild().getJDA().getSelfUser().getName(), BotConfig.getWebsite(),
                                    event.getGuild().getJDA().getSelfUser().getEffectiveAvatarUrl())
                            .setTitle(LanguageService.getByGuild(event.getGuild(), "label.musicPlayer"))
                            .setThumbnail(event.getGuild().getJDA().getSelfUser().getEffectiveAvatarUrl())
                            .setColor(Color.GREEN)
                            .setDescription(LanguageService.getByGuild(event.getGuild(), "message.music.pause"))
                            .setFooter(event.getGuild().getName() + " - " + BotConfig.getAdvertisement(), event.getGuild().getIconUrl());
                    Main.getInstance().getCommandManager().sendMessage(em, event.getChannel(), event.getHook());
                } else {
                    Main.getInstance().getCommandManager().sendMessage(LanguageService.getByGuild(event.getGuild(), "message.music.notConnected"),
                            event.getChannel(), event.getHook());
                }
            }

            case "nv_music_skip" -> {
                event.deferReply(true).queue();
                GuildMusicManager guildMusicManager = Main.getInstance().getMusicWorker().getGuildAudioPlayer(event.getGuild());

                if (guildMusicManager != null) {
                    Main.getInstance().getMusicWorker().skipTrack(event.getGuildChannel(), event.getHook(), 1, false);
                } else {
                    Main.getInstance().getCommandManager().sendMessage(LanguageService.getByGuild(event.getGuild(), "message.music.notConnected"),
                            event.getChannel(), event.getHook());
                }
            }

            case "nv_music_loop" -> {
                event.deferReply(true).queue();
                GuildMusicManager guildMusicManager = Main.getInstance().getMusicWorker().getGuildAudioPlayer(event.getGuild());

                if (guildMusicManager != null) {
                    EmbedBuilder em = new EmbedBuilder();

                    em.setAuthor(event.getGuild().getJDA().getSelfUser().getName(), BotConfig.getWebsite(),
                            event.getGuild().getJDA().getSelfUser().getEffectiveAvatarUrl());
                    em.setTitle(LanguageService.getByGuild(event.getGuild(), "label.musicPlayer"));
                    em.setThumbnail(event.getGuild().getJDA().getSelfUser().getEffectiveAvatarUrl());
                    em.setColor(Color.GREEN);
                    em.setDescription(Main.getInstance().getMusicWorker().getGuildAudioPlayer(event.getGuild()).getScheduler().loop() ?
                            LanguageService.getByGuild(event.getGuild(), "message.music.loop.enabled") :
                            LanguageService.getByGuild(event.getGuild(), "message.music.loop.disabled"));
                    em.setFooter(event.getGuild().getName() + " - " + BotConfig.getAdvertisement(), event.getGuild().getIconUrl());

                    Main.getInstance().getCommandManager().sendMessage(em, event.getChannel(), event.getHook());
                } else {
                    Main.getInstance().getCommandManager().sendMessage(LanguageService.getByGuild(event.getGuild(), "message.music.notConnected"),
                            event.getChannel(), event.getHook());
                }
            }

            case "nv_music_shuffle" -> {
                event.deferReply(true).queue();
                GuildMusicManager guildMusicManager = Main.getInstance().getMusicWorker().getGuildAudioPlayer(event.getGuild());

                if (guildMusicManager != null) {
                    EmbedBuilder em = new EmbedBuilder();

                    guildMusicManager.getScheduler().shuffle();

                    em.setAuthor(event.getGuild().getJDA().getSelfUser().getName(), BotConfig.getWebsite(),
                            event.getGuild().getJDA().getSelfUser().getEffectiveAvatarUrl());
                    em.setTitle(LanguageService.getByGuild(event.getGuild(), "label.musicPlayer"));
                    em.setThumbnail(event.getGuild().getJDA().getSelfUser().getEffectiveAvatarUrl());
                    em.setColor(Color.GREEN);
                    em.setDescription(LanguageService.getByGuild(event.getGuild(), "message.music.shuffle"));
                    em.setFooter(event.getGuild().getName() + " - " + BotConfig.getAdvertisement(), event.getGuild().getIconUrl());

                    Main.getInstance().getCommandManager().sendMessage(em, event.getChannel(), event.getHook());
                } else {
                    Main.getInstance().getCommandManager().sendMessage(LanguageService.getByGuild(event.getGuild(), "message.music.notConnected"),
                            event.getChannel(), event.getHook());
                }
            }

            case "nv_music_add" -> {
                Modal.Builder builder = Modal.create("nv_music_add_modal", LanguageService.getByGuild(event.getGuild(), "label.queueAdd"));
                builder.addActionRow(TextInput.create("nv_music_add_modal_song", LanguageService.getByGuild(event.getGuild(), "label.song"), TextInputStyle.PARAGRAPH).setRequired(true).setMaxLength(512).setMinLength(4).build());
                event.replyModal(builder.build()).queue();
            }
        }
    }


    /**
     * @inheritDoc
     */
    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        super.onModalInteraction(event);

        switch (event.getModalId()) {
            case "nv_music_add_modal" -> {
                event.deferReply(true).queue();
                Main.getInstance().getMusicWorker().playSong(event.getValue("nv_music_add_modal_song").getAsString(),
                        event.getGuild(), event.getMember(), event.getGuildChannel(), event.getInteraction().getHook());
            }

            default -> event.deferEdit().setEmbeds(new EmbedBuilder()
                    .setTitle(LanguageService.getByGuild(event.getGuild(), "label.unknownMenu"))
                    .setFooter(event.getGuild().getName() + " - " + BotConfig.getAdvertisement(), event.getGuild().getIconUrl())
                    .setColor(Color.RED)
                    .setDescription(LanguageService.getByGuild(event.getGuild(), "message.default.unknownMenu"))
                    .build()).setComponents(new ArrayList<>()).queue();
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        super.onStringSelectInteraction(event);

        if (event.getInteraction().getComponent().getId() == null ||
                event.getGuild() == null)
            return;

        if (event.getMessage().getEmbeds().isEmpty() ||
                event.getMessage().getEmbeds().get(0) == null)
            return;

        if (event.getInteraction().getValues().isEmpty() && !event.getInteraction().getComponent().getId().equals("setupAutoRole"))
            return;

        switch (event.getInteraction().getComponent().getId()) {
            case "setupActionMenu" -> {

                if (checkPerms(event.getMember(), event.getChannel())) {
                    return;
                }

                EmbedBuilder embedBuilder = new EmbedBuilder(event.getMessage().getEmbeds().get(0));

                java.util.List<SelectOption> optionList = new ArrayList<>();

                switch (event.getInteraction().getValues().get(0)) {

                    case "rewards" -> {
                        TextInput blackJackWin = TextInput.create("nv_rewards_BlackJackWin", LanguageService.getByGuild(event.getGuild(), "label.blackJackWin"), TextInputStyle.SHORT).setRequired(true).setMinLength(1).build();
                        TextInput musicQuizWin = TextInput.create("nv_rewards_MusicQuizWin", LanguageService.getByGuild(event.getGuild(), "label.musicQuizWin"), TextInputStyle.SHORT).setRequired(true).setMinLength(1).build();
                        TextInput musicQuizFeature = TextInput.create("nv_rewards_MusicQuizFeature", LanguageService.getByGuild(event.getGuild(), "label.musicQuizFeatureGuess"), TextInputStyle.SHORT).setRequired(true).setMinLength(1).build();
                        TextInput musicQuizArtist = TextInput.create("nv_rewards_MusicQuizArtist", LanguageService.getByGuild(event.getGuild(), "label.musicQuizArtistGuess"), TextInputStyle.SHORT).setRequired(true).setMinLength(1).build();
                        TextInput musicQuizTitle = TextInput.create("nv_rewards_MusicQuizTitle", LanguageService.getByGuild(event.getGuild(), "label.musicQuizTitleGuess"), TextInputStyle.SHORT).setRequired(true).setMinLength(1).build();
                        Modal modal = Modal.create("nv_rewards_modal", LanguageService.getByGuild(event.getGuild(), "label.rewards")).addActionRow(blackJackWin).addActionRow(musicQuizWin).addActionRow(musicQuizFeature).addActionRow(musicQuizArtist).addActionRow(musicQuizTitle).build();
                        event.replyModal(modal).queue();
                    }

                    case "lang" -> {

                        for (DiscordLocale locale : LanguageService.getSupported()) {
                            optionList.add(SelectOption.of(locale.getLanguageName(), locale.getLocale()));
                        }

                        embedBuilder.setDescription(LanguageService.getByGuild(event.getGuild(), "message.setup.steps.lang"));

                        event.editMessageEmbeds(embedBuilder.build()).setActionRow(new StringSelectMenuImpl("setupLangMenu", LanguageService.getByGuild(event.getGuild(), "message.default.actionRequired"), 1, 1, false, optionList)).queue();
                    }

                    case "log" -> {
                        optionList.add(SelectOption.of(LanguageService.getByGuild(event.getGuild(), "label.setup"), "logSetup"));

                        optionList.add(SelectOption.of(LanguageService.getByGuild(event.getGuild(), "label.backToMenu"), "backToSetupMenu"));

                        embedBuilder.setDescription(LanguageService.getByGuild(event.getGuild(), "message.setup.steps.auditLog"));

                        event.editMessageEmbeds(embedBuilder.build()).setActionRow(new StringSelectMenuImpl("setupLogMenu", LanguageService.getByGuild(event.getGuild(), "message.default.actionRequired"), 1, 1, false, optionList)).queue();
                    }

                    case "welcome" -> {
                        optionList.add(SelectOption.of(LanguageService.getByGuild(event.getGuild(), "label.setup"), "welcomeSetup"));

                        optionList.add(SelectOption.of(LanguageService.getByGuild(event.getGuild(), "label.setImage"), "welcomeImage"));

                        optionList.add(SelectOption.of(LanguageService.getByGuild(event.getGuild(), "label.backToMenu"), "backToSetupMenu"));

                        embedBuilder.setDescription(LanguageService.getByGuild(event.getGuild(), "message.setup.steps.welcome"));

                        event.editMessageEmbeds(embedBuilder.build()).setActionRow(new StringSelectMenuImpl("setupWelcomeMenu", LanguageService.getByGuild(event.getGuild(), "message.default.actionRequired"), 1, 1, false, optionList)).queue();
                    }

                    case "statistics" -> {
                        optionList.add(SelectOption.of(LanguageService.getByGuild(event.getGuild(), "label.setupMemberStatistics"), "statisticsSetupMember"));
                        optionList.add(SelectOption.of(LanguageService.getByGuild(event.getGuild(), "label.setupTwitchStatistics"), "statisticsSetupTwitch"));
                        optionList.add(SelectOption.of(LanguageService.getByGuild(event.getGuild(), "label.setupYoutubeStatistics"), "statisticsSetupYouTube"));
                        optionList.add(SelectOption.of(LanguageService.getByGuild(event.getGuild(), "label.setupRedditStatistics"), "statisticsSetupReddit"));
                        optionList.add(SelectOption.of(LanguageService.getByGuild(event.getGuild(), "label.setupTwitterStatistics"), "statisticsSetupTwitter"));
                        optionList.add(SelectOption.of(LanguageService.getByGuild(event.getGuild(), "label.setupInstagramStatistics"), "statisticsSetupInstagram"));

                        optionList.add(SelectOption.of(LanguageService.getByGuild(event.getGuild(), "label.backToMenu"), "backToSetupMenu"));

                        embedBuilder.setDescription(LanguageService.getByGuild(event.getGuild(), "message.statistics.setup"));

                        event.editMessageEmbeds(embedBuilder.build()).setActionRow(new StringSelectMenuImpl("setupStatisticsMenu", LanguageService.getByGuild(event.getGuild(), "message.default.actionRequired"), 1, 1, false, optionList)).queue();
                    }

                    default -> {
                        embedBuilder.setDescription(LanguageService.getByGuild(event.getGuild(), "message.default.invalidOption"));
                        event.editMessageEmbeds(embedBuilder.build()).queue();
                    }
                }
            }

            case "setupStatisticsMenu" -> {

                if (checkPerms(event.getMember(), event.getChannel())) {
                    return;
                }

                EmbedBuilder embedBuilder = new EmbedBuilder(event.getMessage().getEmbeds().get(0));

                switch (event.getInteraction().getValues().get(0)) {

                    case "backToSetupMenu" -> sendDefaultChoice(event);

                    default -> {
                        if (event.getMessage().getEmbeds().isEmpty() || event.getMessage().getEmbeds().get(0) == null)
                            return;

                        embedBuilder.setDescription(LanguageService.getByGuild(event.getGuild(), "message.default.invalidOption"));
                        event.editMessageEmbeds(embedBuilder.build()).queue();
                    }
                }
            }

            case "setupTicketsMenu" -> {
                if (checkPerms(event.getMember(), event.getChannel())) {
                    return;
                }

                EmbedBuilder embedBuilder = new EmbedBuilder(event.getMessage().getEmbeds().get(0));

                switch (event.getInteraction().getValues().get(0)) {

                    case "backToSetupMenu" -> sendDefaultChoice(event);

                    default -> {
                        if (event.getMessage().getEmbeds().isEmpty() || event.getMessage().getEmbeds().get(0) == null)
                            return;

                        embedBuilder.setDescription(LanguageService.getByGuild(event.getGuild(), "message.default.invalidOption"));
                        event.editMessageEmbeds(embedBuilder.build()).queue();
                    }
                }
            }

            default -> {
                if (event.getMessage().getEmbeds().isEmpty() || event.getMessage().getEmbeds().get(0) == null) return;

                EmbedBuilder embedBuilder = new EmbedBuilder(event.getMessage().getEmbeds().get(0));

                embedBuilder.setDescription(LanguageService.getByGuild(event.getGuild(), "message.default.invalidOption"));
                event.editMessageEmbeds(embedBuilder.build()).queue();
            }
        }
    }

    /**
     * Called when the default choices should be sent.
     *
     * @param event The InteractionEvent of the SelectMenu.
     */
    public void sendDefaultChoice(StringSelectInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder(event.getMessage().getEmbeds().get(0));

        List<SelectOption> optionList = new ArrayList<>();
        optionList.add(SelectOption.of(LanguageService.getByGuild(event.getGuild(), "label.language"), "lang"));
        optionList.add(SelectOption.of(LanguageService.getByGuild(event.getGuild(), "label.auditLog"), "log"));
        optionList.add(SelectOption.of(LanguageService.getByGuild(event.getGuild(), "label.welcomeChannel"), "welcome"));
        optionList.add(SelectOption.of(LanguageService.getByGuild(event.getGuild(), "label.autoRole"), "autorole"));
        optionList.add(SelectOption.of(LanguageService.getByGuild(event.getGuild(), "label.temporalVoice"), "tempvoice"));
        optionList.add(SelectOption.of(LanguageService.getByGuild(event.getGuild(), "label.statistics"), "statistics"));
        optionList.add(SelectOption.of(LanguageService.getByGuild(event.getGuild(), "label.ticketSystem"), "tickets"));

        embedBuilder.setDescription(LanguageService.getByGuild(event.getGuild(), "message.setup.setupMenu"));

        event.editMessageEmbeds(embedBuilder.build()).setActionRow(new StringSelectMenuImpl("setupActionMenu", LanguageService.getByGuild(event.getGuild(), "message.setup.setupMenuPlaceholder"), 1, 1, false, optionList)).queue();
    }

    /**
     * Checks if the user has the required Permissions to use the Command.
     *
     * @param member  The Member who should be checked.
     * @param channel The Channel used.
     * @return True if the user does not have the required Permissions, false if otherwise.
     */
    private boolean checkPerms(Member member, MessageChannel channel) {
        if (member == null || !member.hasPermission(Permission.ADMINISTRATOR)) {
            channel.sendMessage(LanguageService.getByGuild((member == null ? null : member.getGuild()), "message.default.insufficientPermission", Permission.ADMINISTRATOR.name())).queue();
            return true;
        }

        if (!member.getGuild().getSelfMember().hasPermission(Permission.MANAGE_WEBHOOKS)) {
            channel.sendMessage(LanguageService.getByGuild(member.getGuild(), "message.default.needPermission", Permission.MANAGE_WEBHOOKS.name())).queue();
            return true;
        }

        return false;
    }
}

