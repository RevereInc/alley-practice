package dev.revere.alley.feature.leaderboard.internal;

import com.mongodb.client.MongoCollection;
import dev.revere.alley.bootstrap.AlleyContext;
import dev.revere.alley.bootstrap.annotation.Service;
import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.core.database.MongoService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.data.ProfileData;
import dev.revere.alley.core.profile.data.types.ProfileFFAData;
import dev.revere.alley.core.profile.data.types.ProfileRankedKitData;
import dev.revere.alley.core.profile.data.types.ProfileUnrankedKitData;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.feature.leaderboard.LeaderboardService;
import dev.revere.alley.feature.leaderboard.LeaderboardType;
import dev.revere.alley.feature.leaderboard.data.LeaderboardPlayerData;
import dev.revere.alley.feature.leaderboard.model.LeaderboardRecord;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @since 03/03/2025
 */
@Getter
@Service(provides = LeaderboardService.class, priority = 280)
public class LeaderboardServiceImpl implements LeaderboardService {
    private final MongoService mongoService;
    private final KitService kitService;
    private final ProfileService profileService;
    private final ExecutorService executorService;

    private final Map<Kit, List<LeaderboardRecord>> leaderboardCache = new ConcurrentHashMap<>();
    private final Map<String, Integer> onlinePlayerCache = new ConcurrentHashMap<>();

    /**
     * DI Constructor for the LeaderboardServiceImpl class.
     *
     * @param mongoService   The MongoService instance.
     * @param kitService     The KitService instance.
     * @param profileService The ProfileService instance.
     */
    public LeaderboardServiceImpl(MongoService mongoService, KitService kitService, ProfileService profileService) {
        this.mongoService = mongoService;
        this.kitService = kitService;
        this.profileService = profileService;
        this.executorService = Executors.newFixedThreadPool(4);
    }

    @Override
    public void initialize(AlleyContext context) {
        this.forceRecalculateAll();
    }

    @Override
    public void shutdown(AlleyContext context) {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            Logger.info("Leaderboard executor service has been shut down.");
        }
    }

    @Override
    public void forceRecalculateAll() {
        MongoCollection<Document> profileCollection = this.mongoService.getMongoDatabase().getCollection("profiles");

        CompletableFuture.allOf(this.kitService.getKits().stream()
                .map(kit -> CompletableFuture.runAsync(() -> calculateLeaderboardForKit(kit, profileCollection), executorService))
                .toArray(CompletableFuture[]::new)).join();
    }

    /**
     * Calculates the leaderboard for a specific kit and updates the cache.
     *
     * @param kit               The kit for which to calculate the leaderboard.
     * @param profileCollection The MongoDB collection containing profile data.
     */
    private void calculateLeaderboardForKit(Kit kit, MongoCollection<Document> profileCollection) {
        List<LeaderboardRecord> records = Collections.synchronizedList(new ArrayList<>());

        for (LeaderboardType type : LeaderboardType.values()) {
            List<LeaderboardPlayerData> playerDataList = fetchOptimizedLeaderboard(profileCollection, kit, type);
            records.add(new LeaderboardRecord(type, playerDataList));
        }

        this.leaderboardCache.put(kit, records);
    }

    /**
     * Fetches the leaderboard data using an optimized aggregation pipeline.
     *
     * @param profileCollection The MongoDB collection containing profile data.
     * @param kit               The kit for which to fetch the leaderboard.
     * @param type              The type of leaderboard to fetch.
     * @return A list of LeaderboardPlayerData objects representing the leaderboard entries.
     */
    private List<LeaderboardPlayerData> fetchOptimizedLeaderboard(MongoCollection<Document> profileCollection, Kit kit, LeaderboardType type) {
        List<LeaderboardPlayerData> playerDataList = new ArrayList<>();

        List<Document> pipeline = this.buildAggregationPipeline(kit, type);

        for (Document doc : profileCollection.aggregate(pipeline)) {
            try {
                String name = doc.getString("name");
                UUID uuid = UUID.fromString(doc.getString("uuid"));
                int value = doc.getInteger("value", 0);

                if (value > 0 || type == LeaderboardType.RANKED) {
                    playerDataList.add(new LeaderboardPlayerData(name, uuid, kit, value));
                }
            } catch (Exception ignored) {
            }
        }

        return playerDataList;
    }

    /**
     * Builds the aggregation pipeline for fetching leaderboard data.
     *
     * @param kit  The kit for which to build the pipeline.
     * @param type The type of leaderboard to build the pipeline for.
     * @return A list of Documents representing the aggregation pipeline stages.
     */
    private List<Document> buildAggregationPipeline(Kit kit, LeaderboardType type) {
        List<Document> pipeline = new ArrayList<>();

        Document projectStage = new Document("$project", new Document()
                .append("uuid", 1)
                .append("name", 1)
                .append("value", buildValueExtraction(kit, type))
        );

        pipeline.add(projectStage);

        if (type != LeaderboardType.RANKED) {
            pipeline.add(new Document("$match", new Document("value", new Document("$gt", 0))));
        }

        pipeline.add(new Document("$sort", new Document("value", -1)));
        pipeline.add(new Document("$limit", 100));

        return pipeline;
    }

    /**
     * Builds the value extraction part of the aggregation pipeline based on the leaderboard type.
     *
     * @param kit  The kit for which to build the value extraction.
     * @param type The type of leaderboard.
     * @return A Document representing the value extraction logic.
     */
    private Document buildValueExtraction(Kit kit, LeaderboardType type) {
        String kitName = kit.getName();

        switch (type) {
            case RANKED:
                return new Document("$ifNull", Arrays.asList(
                        new Document("$getField", new Document()
                                .append("field", "elo")
                                .append("input", new Document("$getField", new Document()
                                        .append("field", kitName)
                                        .append("input", "$profileData.rankedKitData")))),
                        1000
                ));
            case UNRANKED:
                return new Document("$ifNull", Arrays.asList(
                        new Document("$getField", new Document()
                                .append("field", "wins")
                                .append("input", new Document("$getField", new Document()
                                        .append("field", kitName)
                                        .append("input", "$profileData.unrankedKitData")))),
                        0
                ));
            case FFA:
                return new Document("$ifNull", Arrays.asList(
                        new Document("$getField", new Document()
                                .append("field", "kills")
                                .append("input", new Document("$getField", new Document()
                                        .append("field", kitName)
                                        .append("input", "$profileData.ffaData")))),
                        0
                ));
            case WIN_STREAK:
                return new Document("$ifNull", Arrays.asList(
                        new Document("$getField", new Document()
                                .append("field", "winstreak")
                                .append("input", new Document("$getField", new Document()
                                        .append("field", kitName)
                                        .append("input", "$profileData.unrankedKitData")))),
                        0
                ));
            default:
                return new Document("$literal", 0);
        }
    }

    @Override
    public List<LeaderboardPlayerData> getLeaderboardEntries(Kit kit, LeaderboardType type) {
        this.refreshOnlinePlayersOptimized(kit, type);

        return this.leaderboardCache.getOrDefault(kit, Collections.emptyList())
                .stream()
                .filter(record -> record.getType() == type)
                .findFirst()
                .map(LeaderboardRecord::getParticipants)
                .orElse(Collections.emptyList());
    }

    private void refreshOnlinePlayersOptimized(Kit kit, LeaderboardType type) {
        LeaderboardRecord record = this.leaderboardCache.getOrDefault(kit, Collections.emptyList())
                .stream()
                .filter(r -> r.getType() == type)
                .findFirst()
                .orElse(null);

        if (record == null) return;

        List<LeaderboardPlayerData> leaderboard = record.getParticipants();

        Map<UUID, Integer> onlinePlayerUpdates = new HashMap<>();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Profile profile = this.profileService.getProfile(onlinePlayer.getUniqueId());
            if (profile != null) {
                int newValue = getValueForType(profile, kit, type);
                onlinePlayerUpdates.put(profile.getUuid(), newValue);
            }
        }

        for (LeaderboardPlayerData playerData : leaderboard) {
            Integer newValue = onlinePlayerUpdates.get(playerData.getUuid());
            if (newValue != null) {
                playerData.setValue(newValue);
            }
        }

        Set<UUID> leaderboardUuids = leaderboard.stream()
                .map(LeaderboardPlayerData::getUuid)
                .collect(Collectors.toSet());

        for (Map.Entry<UUID, Integer> entry : onlinePlayerUpdates.entrySet()) {
            if (!leaderboardUuids.contains(entry.getKey())) {
                Player player = Bukkit.getPlayer(entry.getKey());
                if (player != null) {
                    leaderboard.add(new LeaderboardPlayerData(player.getName(), entry.getKey(), kit, entry.getValue()));
                }
            }
        }

        leaderboard.sort(Comparator.comparingInt(LeaderboardPlayerData::getValue).reversed());
    }

    /**
     * Gets the value for a specific leaderboard type from a player's profile.
     *
     * @param profile the player's profile.
     * @param kit     the kit to consider.
     * @param type    the leaderboard type.
     * @return the value corresponding to the leaderboard type.
     */
    private int getValueForType(Profile profile, Kit kit, LeaderboardType type) {
        ProfileData data = profile.getProfileData();
        switch (type) {
            case RANKED:
                return data.getRankedKitData().getOrDefault(kit.getName(), new ProfileRankedKitData()).getElo();
            case UNRANKED:
                return data.getUnrankedKitData().getOrDefault(kit.getName(), new ProfileUnrankedKitData()).getWins();
            case FFA:
                return data.getFfaData().getOrDefault(kit.getName(), new ProfileFFAData()).getKills();
            case WIN_STREAK:
                return data.getUnrankedKitData().getOrDefault(kit.getName(), new ProfileUnrankedKitData()).getWinstreak();
            default:
                return 0;
        }
    }
}