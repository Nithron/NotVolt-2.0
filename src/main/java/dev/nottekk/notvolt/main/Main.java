package dev.nottekk.notvolt.main;

import best.azura.eventbus.core.EventBus;
import dev.nottekk.notvolt.addons.AddonLoader;
import dev.nottekk.notvolt.addons.AddonManager;
import dev.nottekk.notvolt.audio.music.MusicWorker;
import dev.nottekk.notvolt.bot.BotConfig;
import dev.nottekk.notvolt.bot.BotWorker;
import dev.nottekk.notvolt.bot.version.BotState;
import dev.nottekk.notvolt.bot.version.BotVersion;
import dev.nottekk.notvolt.commands.CommandManager;
import dev.nottekk.notvolt.events.MessageCommandListener;
import dev.nottekk.notvolt.events.ReadyStateListener;
import dev.nottekk.notvolt.events.SlashCommandListener;
import dev.nottekk.notvolt.events.audio.AudioDisconnectListener;
import dev.nottekk.notvolt.handlers.ConfigHandler;
import dev.nottekk.notvolt.language.LanguageService;
import dev.nottekk.notvolt.logger.LoggerQueue;
import dev.nottekk.notvolt.utils.apis.ChatGPTAPI;
import dev.nottekk.notvolt.utils.apis.SpotifyAPIHandler;
import dev.nottekk.notvolt.utils.data.*;
import dev.nottekk.notvolt.utils.external.RequestUtility;
import dev.nottekk.notvolt.utils.others.ThreadUtil;
import io.sentry.Sentry;
import lavalink.client.io.jda.JdaLavalink;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

/**
 * Main Application class, used to store Instances of System Relevant classes.
 */
@Slf4j
@Getter
@Setter(AccessLevel.PRIVATE)
public class Main {
    /**
     * An Instance of the class itself.
     */

    static Main instance;


    /**
     * Instance of the EventBus, used to manage the Events.
     */
    EventBus eventBus;

    /**
     * Instance of the ICommand.
     */
    CommandManager commandManager;

    /**
     * Addon Manager, used to manage the Addons.
     */
    AddonManager addonManager;

    /**
     * Instance of the LoggerQueue, used to merge Logs to prevent Rate-Limits.
     */
    LoggerQueue loggerQueue;

    /**
     * Instance of the MusicWorker used to manage the Music-Player.
     */
    MusicWorker musicWorker;

    /**
     * Instance of the ChatGPT API used for making the setup process easier and give people a better experience.
     */
    ChatGPTAPI chatGPTAPI;

    /**
     * Instance of the Config System.
     */
    Config config;

    /**
     * Instance of the Lavalink.
     */
    JdaLavalink lavalink;

    /**
     * String used to identify the last day.
     */
    String lastDay = "";

    /**
     * Main methode called when Application starts.
     *
     * @param args Start Arguments.
     */
    public static void main(String[] args) {

        ConfigHandler configHandler = new ConfigHandler("src/main/resources/.env");
        configHandler.load();

        // To allow Image creation on CPU.
        System.setProperty("java.awt.headless", "true");

        // Create the Main instance.
        instance = new Main();

        // Create an Instance of the EventBus.
        getInstance().setEventBus(new EventBus());

        // Create the LoggerQueue Instance.
        getInstance().setLoggerQueue(new LoggerQueue());

        // Create the Config System Instance.
        getInstance().setConfig(new Config());

        // Initialize the Config.
        getInstance().getConfig().init();

        ArrayUtil.temporalVoicechannel.addAll(getInstance().getConfig().getTemporal().getStringList("temporalvoice"));

        if (BotConfig.shouldUseSentry()) {
            log.info("Creating Sentry Instance.");

            // Create a Sentry Instance to send Exception to an external Service for bug fixing.
            Sentry.init(options -> {
                String dsn = getInstance().getConfig().getConfiguration().getString("sentry.dsn");
                options.setDsn((dsn == null || dsn.equalsIgnoreCase("yourSentryDSNHere")) ? "" : dsn);

                // Set tracesSampleRate to 1.0 to capture 100% of transactions for performance monitoring.
                // We recommend adjusting this value in production.
                options.setTracesSampleRate(1.0);

                // When first trying Sentry, it's good to see what the SDK is doing:
                options.setRelease(BotWorker.getBuild());
            });

            Thread.setDefaultUncaughtExceptionHandler((t, e) -> Sentry.captureException(e));
        }

        log.info("Starting preparations of the Bot...");

        if (Arrays.stream(args).noneMatch("--skip-download"::equalsIgnoreCase)) {
            LanguageService.downloadLanguages();
            downloadMisc("storage");
        } else {
            LanguageService.initializeLanguages();
        }

        log.info("Finished preparations of the Bot!");

        log.info("Starting NotVolt!");

        /*

        DatabaseTyp databaseTyp;

        switch (getInstance().getConfig().getConfiguration().getString("hikari.misc.storage").toLowerCase()) {
            case "mariadb" -> databaseTyp = DatabaseTyp.MariaDB;

            case "h2" -> databaseTyp = DatabaseTyp.H2;

            case "h2-server", "h2_server" -> databaseTyp = DatabaseTyp.H2_Server;

            case "postgresql", "postgres" -> databaseTyp = DatabaseTyp.PostgreSQL;

            default -> databaseTyp = DatabaseTyp.SQLite;
        }

        try {
            SQLConfig sqlConfig = SQLConfig.builder()
                    .username(getInstance().getConfig().getConfiguration().getString("hikari.sql.user"))
                    .database(getInstance().getConfig().getConfiguration().getString("hikari.sql.db"))
                    .password(getInstance().getConfig().getConfiguration().getString("hikari.sql.pw"))
                    .host(getInstance().getConfig().getConfiguration().getString("hikari.sql.host"))
                    .port(getInstance().getConfig().getConfiguration().getInt("hikari.sql.port"))
                    .path(getInstance().getConfig().getConfiguration().getString("hikari.misc.storageFile"))
                    .typ(databaseTyp)
                    .poolSize(getInstance().getConfig().getConfiguration().getInt("hikari.misc.poolSize", 1))
                    .createEmbeddedServer(getInstance().getConfig().getConfiguration().getBoolean("hikari.misc.createEmbeddedServer"))
                    .debug(BotConfig.isDebug())
                    .sentry(BotConfig.shouldUseSentry()).build();

            new SQLSession(sqlConfig);
        } catch (Exception exception) {
            log.error("Shutting down, because of an critical error!", exception);
            System.exit(0);
            return;
        }
        */

        log.info("Loading ChatGPTAPI");
        getInstance().setChatGPTAPI(new ChatGPTAPI());

        try {
            // Create the ICommand-Manager instance.
            getInstance().setCommandManager(new CommandManager());

        } catch (Exception exception) {
            log.error("Shutting down, because of an critical error!", exception);
            System.exit(0);
            return;
        }

        log.info("Creating JDA Instance.");

        // Create a new Instance of the Bot, as well as add the Events.
        try {
            List<String> argList = Arrays.stream(args).map(String::toLowerCase).toList();

            int shards = getInstance().getConfig().getConfiguration().getInt("bot.misc.shards", 1);

            BotVersion version = BotVersion.RELEASE;

            if (argList.contains("--dev")) {
                version = BotVersion.DEVELOPMENT;
            } else if (argList.contains("--beta")) {
                version = BotVersion.BETA;
            }

            if (BotConfig.shouldUseLavaLink()) {
                getInstance().lavalink = new JdaLavalink(shards, shard -> BotWorker.getShardManager().getShardById(shard));
            }

            BotWorker.createBot(version, shards);
            getInstance().addEvents(getInstance().getCommandManager());
        } catch (Exception ex) {
            log.error("[Main] Error while init: " + ex.getMessage());
            Sentry.captureException(ex);
            System.exit(0);
            return;
        }

        try {
            getInstance().setMusicWorker(new MusicWorker());

            if (BotConfig.shouldUseLavaLink()) {

                List<HashMap<String, Object>> nodes = (List<HashMap<String, Object>>) getInstance().getConfig()
                        .getConfiguration().getList("lavalink.nodes");

                for (Config.LavaLinkNodeConfig node : nodes.stream().map(map ->
                        new Config.LavaLinkNodeConfig((String) map.get("name"), (String) map.get("host"),
                                (Integer) map.get("port"), (boolean) map.get("secure"),
                                (String) map.get("password"))).toList()) {
                    getInstance().getLavalink().addNode(node.getName(),
                            URI.create(node.buildAddress()), node.getPassword());
                }
            }
        } catch (Exception ex) {
            log.error("Failed to load Music Module: " + ex.getMessage());
        }

        if (BotConfig.isModuleActive("music")) {
            log.info("Loading SpotifyAPI");
            new SpotifyAPIHandler();
        }

        // Add the Runtime-hooks.
        getInstance().addHooks();

        // Set the start Time for stats.
        BotWorker.setStartTime(System.currentTimeMillis());

        log.info("Loading AddonManager");
        // Initialize the Addon-Manager.
        getInstance().setAddonManager(new AddonManager());

        if (BotConfig.isModuleActive("addons")) {
            // Initialize the Addon-Loader.
            AddonLoader.loadAllAddons();

            // Start all Addons.
            getInstance().getAddonManager().startAddons();
        }

        // Create checker Thread.
        getInstance().createCheckerThread();

        // Create Heartbeat Thread.
        getInstance().createHeartbeatThread();

        log.info("Any previous messages about \"Error executing DDL\" can be most likely ignored.");
        log.info("Initialization finished.");
        log.info("Bot is ready to use.");
        log.info("You are running on: v" + BotWorker.getBuild());
        log.info("You are running on: " + BotWorker.getShardManager().getShardsTotal() + " Shards.");
        log.info("You are running on: " + BotWorker.getShardManager().getGuilds().size() + " Guilds.");
        log.info("You are running on: " + BotWorker.getShardManager().getUsers().size() + " Users.");
        log.info("Have fun!");
    }

    /**
     * Called to add all Runtime-hooks.
     */
    private void addHooks() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    /**
     * Called when the application shutdowns.
     */
    private void shutdown() {
        // Current time for later stats.
        long start = System.currentTimeMillis();
        log.info("[Main] Shutdown init. !");
        BotWorker.setState(BotState.STOPPED);

        if (BotConfig.isModuleActive("temporalvoice")) {
            // Save it all.
            getConfig().getTemporal().set("temporalvoice", ArrayUtil.temporalVoicechannel);
        }

        if (BotConfig.isModuleActive("addons")) {
            // Shutdown every Addon.
            log.info("[Main] Disabling every Addon!");
            getAddonManager().stopAddons();
            log.info("[Main] Every Addon has been disabled!");
        }

        // Shutdown the Bot instance.
        log.info("[Main] JDA Instance shutdown init. !");
        BotWorker.shutdown();
        log.info("[Main] JDA Instance has been shut down!");

        // Inform of how long it took.
        log.info("[Main] Everything has been shut down in {}ms!", System.currentTimeMillis() - start);
        log.info("[Main] Good bye!");
    }

    /**
     * Method used to create all the misc.
     *
     * @param parentPath the path that should be used.
     */
    private static void downloadMisc(String parentPath) {
        try {
            RequestUtility.requestJson(RequestUtility.Request.builder().url("https://api.github.com/repos/NotVolt-Applications/NotVolt/contents/" + parentPath).build()).getAsJsonArray().forEach(jsonElement -> {
                String name = jsonElement.getAsJsonObject().getAsJsonPrimitive("name").getAsString();
                String path = jsonElement.getAsJsonObject().getAsJsonPrimitive("path").getAsString();
                String download = jsonElement.getAsJsonObject().get("download_url").isJsonNull() ? null : jsonElement.getAsJsonObject().getAsJsonPrimitive("download_url").getAsString();

                boolean isDirectory = download == null;

                if (isDirectory) {
                    downloadMisc(path);
                    return;
                }

                Path parentFilePath = Path.of(parentPath);
                if (!Files.exists(parentFilePath)) {
                    try {
                        Files.createDirectories(parentFilePath);
                    } catch (IOException e) {
                        log.error("Failed to create directory: {}", parentFilePath);
                    }
                }

                Path filePath = Path.of(path);
                if (Files.exists(filePath)) {
                    return;
                }

                log.info("Downloading file {}!", name);

                try (InputStream inputStream = RequestUtility.request(RequestUtility.Request.builder().url(download).build())) {
                    if (inputStream == null) return;

                    Files.copy(inputStream, filePath);
                } catch (IOException exception) {
                    log.error("An error occurred while downloading the file!", exception);
                }
            });
        } catch (Exception exception) {
            log.error("An error occurred while downloading the files!", exception);
        }
    }

    /**
     * Method creates a Thread used to create a Checker Thread.
     */
    public void createCheckerThread() {
        ThreadUtil.createThread(x -> {

            if (!lastDay.equalsIgnoreCase(new SimpleDateFormat("dd").format(new Date()))) {

                // region Update the statistics.
                try {
                    ArrayUtil.messageIDwithMessage.clear();
                    ArrayUtil.messageIDwithUser.clear();

                    BotWorker.getShardManager().getShards().forEach(jda ->
                            BotWorker.setActivity(jda, BotConfig.getStatus(), Activity.ActivityType.CUSTOM_STATUS));

                    log.info("[Stats] ");
                    log.info("[Stats] Today's Stats:");
                    int guildSize = BotWorker.getShardManager().getGuilds().size(), userSize = BotWorker.getShardManager().getGuilds().stream().mapToInt(Guild::getMemberCount).sum();
                    log.info("[Stats] Guilds: {}", guildSize);
                    log.info("[Stats] Overall Users: {}", userSize);
                    log.info("[Stats] ");

                    LocalDate yesterday = LocalDate.now().minusDays(1);
                    //Statistics statistics = SQLSession.getSqlConnector().getSqlWorker().getStatistics(yesterday.getDayOfMonth(), yesterday.getMonthValue(), yesterday.getYear());
                    //JsonObject jsonObject = statistics != null ? statistics.getStatsObject() : new JsonObject();
                    //JsonObject guildStats = statistics != null && jsonObject.has("guild") ? jsonObject.getAsJsonObject("guild") : new JsonObject();

                    //guildStats.addProperty("amount", guildSize);
                    //guildStats.addProperty("users", userSize);

                    //jsonObject.add("guild", guildStats);

                    //SQLSession.getSqlConnector().getSqlWorker().updateStatistic(jsonObject);

                    Calendar currentCalendar = Calendar.getInstance();

                    //SQLSession.getSqlConnector().getSqlWorker()
                    //        .getBirthdays().stream().filter(birthday -> {
                    //            Calendar calendar = Calendar.getInstance();
                    //            calendar.setTime(birthday.getBirthdate());
                    //            return calendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
                    //                    calendar.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH);
                    //        }).forEach(birthday -> {
                    //            TextChannel textChannel = BotWorker.getShardManager().getTextChannelById(birthday.getChannelId());

                    //            if (textChannel != null && textChannel.canTalk())
                    //                textChannel.sendMessage(LanguageService.getByGuild(textChannel.getGuild(), "message.birthday.wish", birthday.getUserId())).queue();
                    //        });

                    lastDay = new SimpleDateFormat("dd").format(new Date());
                } catch (Exception exception) {
                    log.error("Failed to update statistics!", exception);
                    Sentry.captureException(exception);
                }
                //endregion
            }

            //region Cleanup
            try {
                File storageTemp = new File("storage/tmp/");
                File[] files = storageTemp.listFiles();
                if (files != null) {
                    Arrays.stream(files).forEach(f -> {
                        try {
                            Files.deleteIfExists(f.toPath());
                        } catch (IOException e) {
                            log.error("Couldn't delete file " + f.getName(), e);
                        }
                    });
                }
            } catch (Exception exception) {
                log.error("Failed to clear temporal files!", exception);
                Sentry.captureException(exception);
            }
            //endregion

            //region Fallback Temporal Voice check.
            ArrayUtil.temporalVoicechannel.forEach(vc -> {
                VoiceChannel voiceChannel = BotWorker.getShardManager().getVoiceChannelById(vc);
                if (voiceChannel == null) {
                    ArrayUtil.temporalVoicechannel.remove(vc);
                } else {
                    if (voiceChannel.getMembers().isEmpty()) {
                        voiceChannel.delete().queue();
                        ArrayUtil.temporalVoicechannel.remove(vc);
                    } else {
                        if (voiceChannel.getMembers().size() == 1) {
                            if (voiceChannel.getMembers().get(0).getUser().isBot()) {
                                voiceChannel.delete().queue();
                                ArrayUtil.temporalVoicechannel.remove(vc);
                            }
                        }
                    }
                }
            });
            //endregion

        }, null, Duration.ofMinutes(1), true, false);
    }

    /**
     * Called to add all Events.
     */
    private void addEvents(CommandManager commandManager) {
        BotWorker.addEvent(new ReadyStateListener(commandManager), new MessageCommandListener(commandManager), new SlashCommandListener(commandManager), new AudioDisconnectListener());
    }


    /**
     * Method creates a Thread which sends a heartbeat to a URL in an x seconds interval.
     */
    public void createHeartbeatThread() {
        String heartbeatUrl = getInstance().getConfig().getConfiguration().getString("heartbeat.url", null);

        if (heartbeatUrl == null || heartbeatUrl.isBlank() || heartbeatUrl.equalsIgnoreCase("none"))
            return;

        ThreadUtil.createThread(x -> {
                    String formattedUrl = heartbeatUrl.replace("%ping%", String.valueOf(BotWorker.getShardManager().getAverageGatewayPing()));
                    try (InputStream ignored = RequestUtility.request(RequestUtility.Request.builder().url(formattedUrl).GET().build())) {
                        Main.getInstance().logAnalytic("Heartbeat sent!");
                    } catch (Exception exception) {
                        log.warn("Heartbeat failed! Reporting to Sentry...");
                        Sentry.captureException(exception);
                    }
                }, Sentry::captureException,
                Duration.ofSeconds(getInstance().getConfig().getConfiguration().getInt("heartbeat.interval", 60)), true, true);
    }

    /**
     * Method used to log analytics.
     *
     * @param message the message that should be logged.
     * @param args    the arguments for the message that should be logged.
     */
    public void logAnalytic(String message, Object... args) {
        if (!BotConfig.isDebug()) return;
        getAnalyticsLogger().debug(message, args);
    }

    /**
     * Retrieve the Instance of the Analytics Logger.
     *
     * @return {@link Logger} Instance of the Analytics Logger.
     */
    public Logger getAnalyticsLogger() {
        return LoggerFactory.getLogger("analytics");
    }

    /**
     * Retrieve the Instance of the Main class.
     *
     * @return {@link Main} Instance of the Main class.
     */
    public static Main getInstance() {
        if (instance == null) {
            instance = new Main();
        }

        return instance;
    }
}
