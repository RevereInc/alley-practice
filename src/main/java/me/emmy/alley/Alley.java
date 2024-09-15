package me.emmy.alley;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.api.assemble.Assemble;
import me.emmy.alley.api.assemble.AssembleStyle;
import me.emmy.alley.api.command.CommandFramework;
import me.emmy.alley.api.menu.MenuListener;
import me.emmy.alley.arena.ArenaRepository;
import me.emmy.alley.arena.command.ArenaCommand;
import me.emmy.alley.arena.listener.ArenaListener;
import me.emmy.alley.commands.AlleyCommand;
import me.emmy.alley.commands.AlleyReloadCommand;
import me.emmy.alley.commands.admin.debug.FFAStateCommand;
import me.emmy.alley.commands.admin.debug.StateCommand;
import me.emmy.alley.commands.admin.essential.EnchantCommand;
import me.emmy.alley.commands.admin.essential.RenameCommand;
import me.emmy.alley.commands.admin.management.PlaytimeCommand;
import me.emmy.alley.commands.donator.HostCommand;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.cooldown.CooldownRepository;
import me.emmy.alley.database.MongoService;
import me.emmy.alley.essential.command.InvSeeCommand;
import me.emmy.alley.ffa.FFARepository;
import me.emmy.alley.ffa.combat.CombatManager;
import me.emmy.alley.ffa.command.admin.FFACommand;
import me.emmy.alley.ffa.listener.FFAListener;
import me.emmy.alley.ffa.safezone.FFASpawnHandler;
import me.emmy.alley.ffa.safezone.task.FFASpawnTask;
import me.emmy.alley.host.impl.event.command.EventCommand;
import me.emmy.alley.host.impl.tournament.TournamentRepository;
import me.emmy.alley.host.impl.tournament.command.TournamentCommand;
import me.emmy.alley.hotbar.HotbarRepository;
import me.emmy.alley.hotbar.listener.HotbarListener;
import me.emmy.alley.kit.KitRepository;
import me.emmy.alley.kit.command.KitCommand;
import me.emmy.alley.kit.settings.KitSettingRepository;
import me.emmy.alley.leaderboard.command.LeaderboardCommand;
import me.emmy.alley.match.AbstractMatch;
import me.emmy.alley.match.MatchRepository;
import me.emmy.alley.match.command.admin.MatchCommand;
import me.emmy.alley.match.command.admin.impl.MatchInfoCommand;
import me.emmy.alley.match.command.player.CurrentMatchesCommand;
import me.emmy.alley.match.command.player.LeaveMatchCommand;
import me.emmy.alley.match.command.player.LeaveSpectatorCommand;
import me.emmy.alley.match.command.player.SpectateCommand;
import me.emmy.alley.match.listener.MatchListener;
import me.emmy.alley.match.player.visibility.PlayerVisibility;
import me.emmy.alley.match.snapshot.SnapshotRepository;
import me.emmy.alley.match.snapshot.command.InventoryCommand;
import me.emmy.alley.party.PartyRepository;
import me.emmy.alley.party.PartyRequest;
import me.emmy.alley.party.command.PartyCommand;
import me.emmy.alley.party.listener.PartyListener;
import me.emmy.alley.profile.ProfileRepository;
import me.emmy.alley.profile.command.ChallengesCommand;
import me.emmy.alley.profile.command.MatchHistoryCommand;
import me.emmy.alley.profile.command.ProfileMenuCommand;
import me.emmy.alley.profile.command.ThemesCommand;
import me.emmy.alley.profile.cosmetic.command.CosmeticCommand;
import me.emmy.alley.profile.cosmetic.repository.CosmeticRepository;
import me.emmy.alley.profile.division.DivisionRepository;
import me.emmy.alley.profile.division.command.DivisionCommand;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.profile.listener.ProfileListener;
import me.emmy.alley.profile.settings.matchsettings.command.MatchSettingsCommand;
import me.emmy.alley.profile.settings.playersettings.command.SettingsCommand;
import me.emmy.alley.profile.settings.playersettings.command.toggle.TogglePartyInvitesCommand;
import me.emmy.alley.profile.settings.playersettings.command.toggle.TogglePartyMessagesCommand;
import me.emmy.alley.profile.settings.playersettings.command.toggle.ToggleScoreboardCommand;
import me.emmy.alley.profile.settings.playersettings.command.toggle.ToggleTablistCommand;
import me.emmy.alley.profile.settings.playersettings.command.worldtime.CurrentTimeCommand;
import me.emmy.alley.profile.settings.playersettings.command.worldtime.DayCommand;
import me.emmy.alley.profile.settings.playersettings.command.worldtime.NightCommand;
import me.emmy.alley.profile.settings.playersettings.command.worldtime.SunsetCommand;
import me.emmy.alley.profile.shop.command.ShopCommand;
import me.emmy.alley.profile.shop.command.admin.SetCoinsCommand;
import me.emmy.alley.queue.QueueRepository;
import me.emmy.alley.queue.command.admin.ForceQueueCommand;
import me.emmy.alley.queue.command.admin.QueueReloadCommand;
import me.emmy.alley.queue.command.player.LeaveQueueCommand;
import me.emmy.alley.queue.command.player.QueuesCommand;
import me.emmy.alley.queue.command.player.RankedCommand;
import me.emmy.alley.queue.command.player.UnrankedCommand;
import me.emmy.alley.scoreboard.ScoreboardVisualizer;
import me.emmy.alley.scoreboard.animation.ScoreboardTitleHandler;
import me.emmy.alley.spawn.SpawnHandler;
import me.emmy.alley.spawn.command.SetSpawnCommand;
import me.emmy.alley.spawn.command.SpawnCommand;
import me.emmy.alley.spawn.command.SpawnItemsCommand;
import me.emmy.alley.spawn.listener.SpawnListener;
import me.emmy.alley.util.ServerUtil;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.util.chat.Logger;
import me.emmy.alley.world.WorldListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
public class Alley extends JavaPlugin {

    @Getter
    private static Alley instance;

    private CommandFramework commandFramework;
    private CosmeticRepository cosmeticRepository;
    private ProfileRepository profileRepository;
    private PlayerVisibility playerVisibility;
    private DivisionRepository divisionRepository;
    private FFASpawnHandler ffaSpawnHandler;
    private MongoService mongoService;
    private ArenaRepository arenaRepository;
    private QueueRepository queueRepository;
    private ConfigHandler configHandler;
    private MatchRepository matchRepository;
    private PartyRepository partyRepository;
    private CooldownRepository cooldownRepository;
    private CombatManager combatManager;
    private KitRepository kitRepository;
    private ScoreboardTitleHandler sbTitleHandler;
    private PartyRequest partyRequest;
    private KitSettingRepository kitSettingRepository;
    private TournamentRepository tournamentRepository;
    private SnapshotRepository snapshotRepository;
    private FFARepository ffaRepository;
    private SpawnHandler spawnHandler;
    private HotbarRepository hotbarRepository;

    private String prefix = "§f[§bAlley§f] §r";

    @Override
    public void onEnable() {
        instance = this;

        long start = System.currentTimeMillis();

        checkDescription();
        registerHandlers();
        registerDatabase();
        registerManagers();
        registerListeners();
        registerCommands();
        loadScoreboard();
        loadTasks();

        long end = System.currentTimeMillis();
        long timeTaken = end - start;

        CC.pluginEnabled(timeTaken);
    }

    @Override
    public void onDisable() {
        profileRepository.getProfiles().forEach((uuid, profile) -> profile.save());

        ServerUtil.disconnectPlayers();
        kitRepository.saveKits();
        ffaRepository.saveFFAMatches();
        arenaRepository.saveArenas();

        Bukkit.getWorlds().forEach(world -> world.getEntities().forEach(entity -> {
            if (entity.getType() == EntityType.DROPPED_ITEM) {
                entity.remove();
            }
        }));

        CC.pluginDisabled();
    }

    private void checkDescription() {
        List<String> authors = getDescription().getAuthors();
        List<String> expectedAuthors = Arrays.asList("Emmy", "Remi");

        Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "Expected authors: &a" + expectedAuthors + "&f, Retrieved authors: &c" + authors));

        if (!new HashSet<>(authors).containsAll(expectedAuthors)) {
            System.exit(0);
            Bukkit.shutdown();
        }
    }

    private void registerHandlers() {
        configHandler = new ConfigHandler();
        sbTitleHandler = new ScoreboardTitleHandler();
    }

    private String registerDatabase() {
        FileConfiguration config = configHandler.getConfigByName("database/database.yml");
        return config.getString("mongo.uri");
    }

    private void registerManagers() {
        Logger.logTime("CommandFramework", () -> this.commandFramework = new CommandFramework(this));
        Logger.logTime("QueueRepository", () -> this.queueRepository = new QueueRepository());
        Logger.logTime("KitSettingRepository", () -> this.kitSettingRepository = new KitSettingRepository());
        Logger.logTime("KitRepository", () -> this.kitRepository = new KitRepository());
        Logger.logTime("ArenaRepository", () -> this.arenaRepository = new ArenaRepository());
        Logger.logTime("FFARepository", () -> this.ffaRepository = new FFARepository());
        Logger.logTime("CosmeticRepository", () -> this.cosmeticRepository = new CosmeticRepository());
        Logger.logTime("ProfileRepository", () -> this.profileRepository = new ProfileRepository());
        Logger.logTime("DivisionRepository", () -> this.divisionRepository = new DivisionRepository());
        Logger.logTime("MongoService", () -> this.mongoService = new MongoService(registerDatabase()));
        Logger.logTime("HotbarRepository", () -> this.hotbarRepository = new HotbarRepository());
        Logger.logTime("Profiles", () -> this.profileRepository.loadProfiles());
        Logger.logTime("CooldownRepository", () -> this.cooldownRepository = new CooldownRepository());
        Logger.logTime("SnapshotRepository", () -> this.snapshotRepository = new SnapshotRepository());
        Logger.logTime("MatchRepository", () -> this.matchRepository = new MatchRepository());
        Logger.logTime("PartyRepository", () -> this.partyRepository = new PartyRepository());
        Logger.logTime("SpawnHandler", () -> this.spawnHandler = new SpawnHandler());
        Logger.logTime("CombatManager", () -> this.combatManager = new CombatManager());
        Logger.logTime("FFASpawnHandler", () -> this.ffaSpawnHandler = new FFASpawnHandler());
        Logger.logTime("PlayerVisibility", () -> this.playerVisibility = new PlayerVisibility());
    }

    private void registerListeners() {
        Arrays.asList(
                new ProfileListener(),
                new HotbarListener(),
                new PartyListener(),
                new MatchListener(),
                new ArenaListener(),
                new MenuListener(),
                new SpawnListener(),
                new FFAListener(),
                new WorldListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void registerCommands() {
        Logger.logTime("Admin Commands", () -> {
            new AlleyCommand();
            new AlleyReloadCommand();

            new KitCommand();
            new ArenaCommand();
            new MatchCommand();
            new FFACommand();
            new CosmeticCommand();
            new DivisionCommand();

            new ForceQueueCommand();
            new QueueReloadCommand();

            //debugging
            new StateCommand();
            new FFAStateCommand();
            new MatchInfoCommand();

            //essential
            new InvSeeCommand();
            new RenameCommand();
            new EnchantCommand();
            new PlaytimeCommand();
            new SpawnItemsCommand();
            new SetSpawnCommand();
            new SpawnCommand();
            new SetCoinsCommand();
        });

        Logger.logTime("Donator Command", () -> {
            new HostCommand();
            new EventCommand();
            new TournamentCommand();
        });

        Logger.logTime("Player Commands", () -> {

            new DayCommand();
            new NightCommand();
            new SunsetCommand();
            new CurrentTimeCommand();
            new TogglePartyInvitesCommand();
            new TogglePartyMessagesCommand();
            new ToggleScoreboardCommand();
            new ToggleTablistCommand();

            new PartyCommand();

            new UnrankedCommand();
            new RankedCommand();
            new SettingsCommand();
            new LeaderboardCommand();
            new SpectateCommand();
            new LeaveSpectatorCommand();
            new LeaveMatchCommand();
            new CurrentMatchesCommand();
            new LeaveQueueCommand();
            new QueuesCommand();
            new MatchSettingsCommand();

            new InventoryCommand();

            new ShopCommand();
            new ChallengesCommand();
            new ProfileMenuCommand();
            new MatchHistoryCommand();
            new ThemesCommand();
        });
    }

    private void loadScoreboard() {
        Assemble assemble = new Assemble(this, new ScoreboardVisualizer());
        assemble.setTicks(2);
        assemble.setAssembleStyle(AssembleStyle.MODERN);
    }

    private void loadTasks() {
        new FFASpawnTask(this.ffaSpawnHandler.getCuboid(), this).runTaskTimer(this, 0, 20);
    }

    /**
     * Get the configuration file by name
     *
     * @param fileName the name of the file
     * @return the file configuration
     */
    public FileConfiguration getConfig(String fileName) {
        File configFile = new File(getDataFolder(), fileName);
        return YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Get the exact bukkit version
     *
     * @return the exact bukkit version
     */
    public String getBukkitVersionExact() {
        String version = Bukkit.getServer().getVersion();
        version = version.split("MC: ")[1];
        version = version.split("\\)")[0];
        return version;
    }

    /**
     * Get the player count of a specific queue type (Unranked, FFA, Ranked)
     *
     * @param queue the queue
     * @return the player count
     */
    public int getPlayerCountOfGameType(String queue) {
        switch (queue) {
            case "Unranked":
                return (int) matchRepository.getMatches().stream()
                        .filter(match -> !match.isRanked())
                        .distinct()
                        .count() * 2; //* 2 because there are 2 players in a regular match, so we double the amount of matches
            case "Ranked":
                return (int) matchRepository.getMatches().stream()
                        .filter(AbstractMatch::isRanked)
                        .distinct()
                        .count() * 2; //same applies here
            case "FFA":
                return (int) profileRepository.getProfiles().values().stream()
                        .filter(profile -> profile.getState().equals(EnumProfileState.FFA))
                        .count(); //not needed, because we can just get every profile that is in the FFA state
            case "Bots":
                return 0;
        }
        return 0;
    }
}
