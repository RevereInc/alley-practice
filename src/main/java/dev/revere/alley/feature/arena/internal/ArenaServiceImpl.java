package dev.revere.alley.feature.arena.internal;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.bootstrap.AlleyContext;
import dev.revere.alley.bootstrap.annotation.Service;
import dev.revere.alley.common.FileUtil;
import dev.revere.alley.common.Serializer;
import dev.revere.alley.common.VoidChunkGenerator;
import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.arena.ArenaType;
import dev.revere.alley.feature.arena.internal.types.FreeForAllArena;
import dev.revere.alley.feature.arena.internal.types.SharedArena;
import dev.revere.alley.feature.arena.internal.types.StandAloneArena;
import dev.revere.alley.feature.arena.schematic.ArenaSchematicService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @date 20/05/2024 - 16:54
 */
@Getter
@Service(provides = ArenaService.class, priority = 110)
public class ArenaServiceImpl implements ArenaService {
    private final AlleyPlugin plugin;
    private final ConfigService configService;
    private final KitService kitService;
    private final ArenaSchematicService arenaSchematicService;
    private final ExecutorService executorService;

    private final List<Arena> arenas = new ArrayList<>();
    private final List<StandAloneArena> temporaryArenas = new ArrayList<>();
    private final AtomicInteger copyIdCounter = new AtomicInteger(0);

    private final Map<String, List<Arena>> arenasByKit = new ConcurrentHashMap<>();
    private final Map<String, Arena> arenasByName = new ConcurrentHashMap<>();

    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    private World temporaryWorld;
    private Location nextCopyLocation;
    private final int arenaSpacing = 1500;

    /**
     * DI Constructor for the ArenaServiceImpl class.
     *
     * @param plugin                The Alley plugin instance.
     * @param configService         The configuration service instance.
     * @param kitService            The kit service instance.
     * @param arenaSchematicService The arena schematic service instance.
     */
    public ArenaServiceImpl(AlleyPlugin plugin, ConfigService configService, KitService kitService, ArenaSchematicService arenaSchematicService) {
        this.plugin = plugin;
        this.configService = configService;
        this.kitService = kitService;
        this.arenaSchematicService = arenaSchematicService;
        this.executorService = Executors.newFixedThreadPool(4);
    }

    @Override
    public void initialize(AlleyContext context) {
        this.loadArenas();
        this.initializeTemporaryWorld();
        this.buildCaches();
        this.arenaSchematicService.generateMissingSchematics(this.arenas);
    }

    @Override
    public void shutdown(AlleyContext context) {
        this.cleanupTemporaryArenas();

        if (this.temporaryWorld != null) {
            String worldName = this.temporaryWorld.getName();

            this.temporaryWorld.getPlayers().forEach(player ->
                    player.teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation())
            );

            if (Bukkit.unloadWorld(temporaryWorld, false)) {
                Logger.info("Successfully unloaded temporary world: " + worldName);
                File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
                if (worldFolder.exists()) {
                    FileUtil.deleteWorldFolder(worldFolder);
                    Logger.info("Deleted temporary world folder: " + worldName);
                }
            } else {
                Logger.error("Failed to unload temporary world: " + worldName);
            }
            this.temporaryWorld = null;
        }

        if (this.executorService != null && !this.executorService.isShutdown()) {
            this.executorService.shutdown();
            try {
                if (!this.executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    this.executorService.shutdownNow();
                }
            } catch (InterruptedException exception) {
                this.executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    private void buildCaches() {
        this.arenasByKit.clear();
        this.arenasByName.clear();

        for (Kit kit : this.kitService.getKits()) {
            List<Arena> kitArenas = this.arenas.stream()
                    .filter(arena -> arena.getKits().contains(kit.getName()))
                    .filter(Arena::isEnabled)
                    .collect(Collectors.toList());
            this.arenasByKit.put(kit.getName(), kitArenas);
        }

        for (Arena arena : this.arenas) {
            this.arenasByName.put(arena.getName().toLowerCase(), arena);
        }
    }

    private void initializeTemporaryWorld() {
        String worldName = "temporary_arena_world";
        this.cleanupExistingWorld(worldName);

        WorldCreator creator = new WorldCreator(worldName);
        creator.generateStructures(false).generator(new VoidChunkGenerator());

        this.temporaryWorld = creator.createWorld();
        this.nextCopyLocation = new Location(temporaryWorld, 0, 100, 0);
    }

    /**
     * Method to load all arenas from the arenas.yml file.
     */
    public void loadArenas() {
        FileConfiguration config = this.configService.getArenasConfig();
        ConfigurationSection arenasConfig = config.getConfigurationSection("arenas");

        if (arenasConfig == null) {
            return;
        }

        Set<String> arenaNames = arenasConfig.getKeys(false);

        if (arenaNames.size() <= 5) {
            for (String arenaName : arenaNames) {
                Arena arena = loadSingleArena(config, arenaName);
                if (arena != null) {
                    this.arenas.add(arena);
                }
            }
            return;
        }

        List<CompletableFuture<Arena>> futures = new ArrayList<>();

        for (String arenaName : arenaNames) {
            CompletableFuture<Arena> future = CompletableFuture.supplyAsync(() -> loadSingleArena(config, arenaName), this.executorService);
            futures.add(future);
        }

        for (CompletableFuture<Arena> future : futures) {
            try {
                Arena arena = future.get(5, TimeUnit.SECONDS);
                if (arena != null) {
                    this.arenas.add(arena);
                }
            } catch (TimeoutException exception) {
                Logger.error("Arena loading timed out after 5 seconds");
                future.cancel(true);
            } catch (Exception exception) {
                Logger.error("Failed to load arena: " + exception.getMessage());
            }
        }
    }

    /**
     * Helper method to load a single arena from the configuration.
     *
     * @param config    the configuration file to load from.
     * @param arenaName the name of the arena to load.
     * @return the loaded Arena object, or null if loading failed.
     */
    private Arena loadSingleArena(FileConfiguration config, String arenaName) {
        try {
            String name = "arenas." + arenaName;

            ArenaType arenaType = ArenaType.valueOf(config.getString(name + ".type"));
            Location minimum = Serializer.deserializeLocation(config.getString(name + ".minimum"));
            Location maximum = Serializer.deserializeLocation(config.getString(name + ".maximum"));

            Arena arena = this.createArenaByType(arenaType, arenaName, minimum, maximum, config, name);
            this.configureArena(arena, config, name);

            return arena;
        } catch (Exception exception) {
            Logger.error("Error loading arena " + arenaName + ": " + exception.getMessage());
            return null;
        }
    }

    /**
     * Factory method to create an Arena instance based on its type.
     *
     * @param arenaType The type of the arena (SHARED, STANDALONE, FFA).
     * @param arenaName The name of the arena.
     * @param minimum   The minimum corner location of the arena.
     * @param maximum   The maximum corner location of the arena.
     * @param config    The configuration file to load additional settings from.
     * @param name      The base path in the configuration for this arena.
     * @return A new instance of the appropriate Arena subclass.
     */
    private Arena createArenaByType(ArenaType arenaType, String arenaName, Location minimum, Location maximum, FileConfiguration config, String name) {
        switch (arenaType) {
            case SHARED:
                return new SharedArena(arenaName, minimum, maximum);

            case STANDALONE:
                int heightLimit = config.getInt(name + ".height-limit", 7);
                int voidLevel = config.getInt(name + ".void-level", 70);
                return new StandAloneArena(
                        arenaName, minimum, maximum,
                        Serializer.deserializeLocation(config.getString(name + ".team-one-portal")),
                        Serializer.deserializeLocation(config.getString(name + ".team-two-portal")),
                        heightLimit, voidLevel
                );

            case FFA:
                return new FreeForAllArena(
                        arenaName,
                        Serializer.deserializeLocation(config.getString(name + ".safe-zone.pos1")),
                        Serializer.deserializeLocation(config.getString(name + ".safe-zone.pos2"))
                );

            default:
                throw new IllegalStateException("Unexpected arena type: " + arenaType);
        }
    }

    /**
     * Configures common properties for an arena from the configuration.
     *
     * @param arena  The arena to configure.
     * @param config The configuration file to load settings from.
     * @param name   The base path in the configuration for this arena.
     */
    private void configureArena(Arena arena, FileConfiguration config, String name) {
        if (config.contains(name + ".kits")) {
            Set<String> validKits = new HashSet<>();
            for (String kitName : config.getStringList(name + ".kits")) {
                if (kitService.getKit(kitName) != null) {
                    validKits.add(kitName);
                }
            }
            arena.getKits().addAll(validKits);
        }

        if (config.contains(name + ".pos1")) {
            arena.setPos1(Serializer.deserializeLocation(config.getString(name + ".pos1")));
        }
        if (config.contains(name + ".pos2")) {
            arena.setPos2(Serializer.deserializeLocation(config.getString(name + ".pos2")));
        }
        if (config.contains(name + ".center")) {
            arena.setCenter(Serializer.deserializeLocation(config.getString(name + ".center")));
        }
        if (config.contains(name + ".display-name")) {
            arena.setDisplayName(config.getString(name + ".display-name"));
        }
        if (config.contains(name + ".enabled")) {
            arena.setEnabled(config.getBoolean(name + ".enabled"));
        }
    }

    @Override
    public StandAloneArena createTemporaryArenaCopy(StandAloneArena originalArena) {
        if (originalArena.isTemporaryCopy()) {
            throw new IllegalArgumentException("Cannot create a temporary copy of a temporary arena.");
        }

        int copyId = copyIdCounter.incrementAndGet();
        Location copyLocation = getNextCopyLocationForArena(originalArena);

        Location originalPos1 = originalArena.getPos1();
        Location originalMin = originalArena.getMinimum();
        Location originalMax = originalArena.getMaximum();

        if (originalPos1 != null && originalMin != null && originalMax != null) {
            int actualMinY = Math.min(originalMin.getBlockY(), originalMax.getBlockY());
            int pos1OffsetFromActualMin = originalPos1.getBlockY() - actualMinY;
            int targetMinY = 100 - pos1OffsetFromActualMin;
            copyLocation.setY(targetMinY);
        }

        StandAloneArena copiedArena = originalArena.createCopy(temporaryWorld, copyLocation, copyId);
        copiedArena.setHeightLimit(copiedArena.getPos1().getBlockY() + copiedArena.getHeightLimit());

        this.arenaSchematicService.paste(copyLocation, this.arenaSchematicService.getSchematicFile(originalArena.getName()));
        this.temporaryArenas.add(copiedArena);
        return copiedArena;
    }

    public Location getNextCopyLocationForArena(StandAloneArena originalArena) {
        Location location = this.nextCopyLocation.clone();

        Location originalPos1 = originalArena.getPos1();
        Location originalMin = originalArena.getMinimum();

        if (originalPos1 != null && originalMin != null) {
            int pos1OffsetFromMin = originalPos1.getBlockY() - originalMin.getBlockY();
            location.setY(100 - pos1OffsetFromMin);
        }

        this.nextCopyLocation.add(this.arenaSpacing, 0, 0);
        if (this.nextCopyLocation.getX() > this.arenaSpacing * 10) {
            this.nextCopyLocation.setX(0);
            this.nextCopyLocation.add(0, 0, this.arenaSpacing);
        }

        return location;
    }

    public void cleanupTemporaryArenas() {
        for (StandAloneArena arena : new ArrayList<>(this.temporaryArenas)) {
            arena.deleteCopiedArena();
        }
        this.temporaryArenas.clear();
    }

    /**
     * Cleans up an existing world by unloading it and deleting its corresponding folder.
     * This includes teleporting any players in the world back to the spawn location of the
     * first loaded world.
     *
     * @param worldName the name of the world to be cleaned up
     */
    private void cleanupExistingWorld(String worldName) {
        World existingWorld = this.plugin.getServer().getWorld(worldName);
        if (existingWorld != null) {
            existingWorld.getPlayers().forEach(player ->
                    player.teleport(this.plugin.getServer().getWorlds().get(0).getSpawnLocation())
            );

            boolean unloaded = this.plugin.getServer().unloadWorld(existingWorld, false);
            if (!unloaded) {
                Logger.error("Failed to unload world: " + worldName);
            }
        }

        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
        if (worldFolder.exists()) {
            FileUtil.deleteWorldFolder(worldFolder);
        }
    }

    @Override
    public List<Arena> getArenas() {
        return Collections.unmodifiableList(this.arenas);
    }

    @Override
    public List<StandAloneArena> getTemporaryArenas() {
        return Collections.unmodifiableList(this.temporaryArenas);
    }

    @Override
    public void saveArena(Arena arena) {
        if (arena == null) {
            return;
        }

        arena.saveArena();
        this.buildCaches();
    }

    @Override
    public void deleteArena(Arena arena) {
        if (arena == null) {
            return;
        }

        arena.deleteArena();
        this.arenas.remove(arena);
        this.buildCaches();
    }

    @Override
    public void deleteTemporaryArena(StandAloneArena arena) {
        if (arena == null || !this.temporaryArenas.contains(arena)) {
            return;
        }
        arena.deleteCopiedArena();
        this.temporaryArenas.remove(arena);
    }

    @Override
    public Arena getRandomArena(Kit kit) {
        List<Arena> availableArenas = this.arenasByKit.get(kit.getName());
        if (availableArenas == null || availableArenas.isEmpty()) {
            return null;
        }

        Arena selectedArena = availableArenas.get(this.random.nextInt(availableArenas.size()));
        if (selectedArena instanceof StandAloneArena) {
            return createTemporaryArenaCopy((StandAloneArena) selectedArena);
        }
        return selectedArena;
    }

    @Override
    public Arena getArenaByName(String name) {
        return this.arenasByName.get(name.toLowerCase());
    }

    @Override
    public Arena selectArenaWithPotentialTemporaryCopy(Arena arena) {
        if (arena instanceof StandAloneArena) {
            return this.createTemporaryArenaCopy((StandAloneArena) arena);
        }
        return arena;
    }

    @Override
    public void registerNewArena(Arena arena) {
        if (arena != null && !this.arenasByName.containsKey(arena.getName().toLowerCase())) {
            this.arenas.add(arena);
            this.buildCaches();
        }
    }

    /**
     * Refresh caches when kits or arenas are modified
     */
    public void refreshCaches() {
        CompletableFuture.runAsync(this::buildCaches, this.executorService);
    }
}