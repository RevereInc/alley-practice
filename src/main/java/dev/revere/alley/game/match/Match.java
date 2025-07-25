package dev.revere.alley.game.match;

import dev.revere.alley.Alley;
import dev.revere.alley.adapter.knockback.KnockbackAdapter;
import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.arena.ArenaService;
import dev.revere.alley.base.arena.impl.StandAloneArena;
import dev.revere.alley.base.combat.CombatService;
import dev.revere.alley.base.hotbar.HotbarService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.setting.impl.mechanic.KitSettingCampProtectionImpl;
import dev.revere.alley.base.kit.setting.impl.mode.*;
import dev.revere.alley.base.kit.setting.impl.visual.KitSettingHealthBar;
import dev.revere.alley.base.nametag.NametagService;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.base.spawn.SpawnService;
import dev.revere.alley.base.visibility.VisibilityService;
import dev.revere.alley.feature.cosmetic.BaseCosmetic;
import dev.revere.alley.feature.cosmetic.CosmeticType;
import dev.revere.alley.feature.cosmetic.impl.killmessage.KillMessagePack;
import dev.revere.alley.feature.cosmetic.repository.BaseCosmeticRepository;
import dev.revere.alley.feature.cosmetic.repository.CosmeticService;
import dev.revere.alley.feature.layout.LayoutService;
import dev.revere.alley.feature.layout.data.LayoutData;
import dev.revere.alley.game.match.data.MatchData;
import dev.revere.alley.game.match.data.impl.MatchDataSoloImpl;
import dev.revere.alley.game.match.enums.MatchState;
import dev.revere.alley.game.match.impl.RoundsMatch;
import dev.revere.alley.game.match.player.GamePlayer;
import dev.revere.alley.game.match.player.data.MatchGamePlayerData;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.task.MatchTask;
import dev.revere.alley.game.match.task.mode.PlatformDecayTask;
import dev.revere.alley.game.match.task.other.MatchCampProtectionTask;
import dev.revere.alley.game.match.task.other.MatchRespawnTask;
import dev.revere.alley.game.match.snapshot.SnapshotService;
import dev.revere.alley.game.match.snapshot.Snapshot;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.tool.reflection.ReflectionRepository;
import dev.revere.alley.tool.reflection.impl.ActionBarReflectionServiceImpl;
import dev.revere.alley.tool.reflection.impl.TitleReflectionServiceImpl;
import dev.revere.alley.util.ListenerUtil;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.SoundUtil;
import dev.revere.alley.util.TimeUtil;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Setter
public abstract class Match {
    protected final Alley plugin = Alley.getInstance();

    private final Queue queue;
    private final Kit kit;
    private final Arena arena;
    private final boolean ranked;

    private final Map<BlockState, Location> brokenBlocks = new ConcurrentHashMap<>();
    private final Map<BlockState, Location> placedBlocks = new ConcurrentHashMap<>();
    private final List<UUID> spectators = new CopyOnWriteArrayList<>();
    private final List<Snapshot> snapshots = new ArrayList<>();

    private boolean teamMatch;
    private boolean affectStatistics = true;

    private MatchTask runnable;
    private MatchState state;
    private long startTime;
    private long endTime;

    /**
     * Constructor for the AbstractMatch class.
     *
     * @param queue  The queue associated with the match.
     * @param kit    The kit used in the match.
     * @param arena  The arena where the match takes place.
     * @param ranked Whether the match is ranked.
     */
    public Match(Queue queue, Kit kit, Arena arena, boolean ranked) {
        this.queue = Objects.requireNonNull(queue, "Queue cannot be null");
        this.kit = Objects.requireNonNull(kit, "Kit cannot be null");
        this.arena = Objects.requireNonNull(arena, "Arena cannot be null");
        this.ranked = ranked;
    }

    public abstract List<GameParticipant<MatchGamePlayerImpl>> getParticipants();

    public abstract void handleDisconnect(Player player);

    public abstract void handleRespawn(Player player);

    public abstract boolean canStartRound();

    public abstract boolean canEndRound();

    public abstract boolean canEndMatch();

    /**
     * Handles the item drop on death for a player.
     * This method clears the drops to prevent items from being dropped on death.
     *
     * @param player The player that died.
     * @param event  The PlayerDeathEvent that triggered this method.
     */
    public abstract void handleDeathItemDrop(Player player, PlayerDeathEvent event);

    /**
     * Starts the match by setting the state and updating player profiles and running the match task.
     */
    public void startMatch() {
        this.sendPlayerVersusPlayerMessage();

        this.state = MatchState.STARTING;

        this.handleMatchTasks();

        this.getParticipants().forEach(this::initializeParticipant);
        this.updateParticipantNametags();

        this.startTime = System.currentTimeMillis();
    }

    public void endMatch() {
        deleteArenaCopyIfStandalone();

        this.getParticipants().forEach(this::finalizeParticipant);
        this.updateParticipantNametags();

        this.cleanupTasks();
        this.cleanupHealthDisplay();

        Alley.getInstance().getService(MatchService.class).removeMatch(this);
    }

    private void deleteArenaCopyIfStandalone() {
        if (!(this.arena instanceof StandAloneArena)) {
            return;
        }

        ArenaService arenaService = Alley.getInstance().getService(ArenaService.class);
        arenaService.deleteTemporaryArena((StandAloneArena) this.arena);
    }

    /**
     * Helper method to trigger a nametag update for all participants in the match.
     */
    private void updateParticipantNametags() {
        NametagService nametagService = Alley.getInstance().getService(NametagService.class);

        getParticipants().forEach(participant -> {
            List<MatchGamePlayerImpl> playersToUpdate = participant.getAllPlayers();

            playersToUpdate.stream()
                    .map(gamePlayer -> plugin.getServer().getPlayer(gamePlayer.getUuid()))
                    .filter(Objects::nonNull)
                    .forEach(nametagService::updatePlayerState);
        });
    }

    /**
     * Initializes a game participant and updates the player profiles.
     *
     * @param gameParticipant The game participant to initialize.
     */
    private void initializeParticipant(GameParticipant<MatchGamePlayerImpl> gameParticipant) {
        VisibilityService visibilityService = Alley.getInstance().getService(VisibilityService.class);
        KnockbackAdapter knockbackAdapter = Alley.getInstance().getService(KnockbackAdapter.class);

        gameParticipant.getPlayers().forEach(gamePlayer -> {
            Player player = this.plugin.getServer().getPlayer(gamePlayer.getUuid());
            if (player == null) {
                return;
            }

            this.updatePlayerProfileForMatch(player);
            this.setupPlayer(player);

            visibilityService.updateVisibility(player);
            knockbackAdapter.getKnockbackImplementation().applyKnockback(player, getKit().getKnockbackProfile());

            this.registerHealthObjectiveForPlayer(player);
            this.registerCampProtectionTask(player);
        });
    }

    /**
     * Finalizes a game participant and updates the player profiles.
     *
     * @param gameParticipant The game participant to finalize.
     */
    private void finalizeParticipant(GameParticipant<MatchGamePlayerImpl> gameParticipant) {
        gameParticipant.getPlayers().stream()
                .filter(gamePlayer -> !gamePlayer.isDisconnected())
                .forEach(gamePlayer -> {
                    Player player = this.plugin.getServer().getPlayer(gamePlayer.getUuid());
                    if (player != null) {
                        finalizePlayer(player);
                    }
                });
    }

    /**
     * Method to finalize a player after the match ends.
     * This method resets the player's state, updates their profile for the lobby,
     *
     * @param player The player to finalize.
     */
    public void finalizePlayer(Player player) {
        VisibilityService visibilityService = Alley.getInstance().getService(VisibilityService.class);
        this.resetPlayerState(player);
        this.updatePlayerProfileForLobby(player);
        visibilityService.updateVisibility(player);
        this.teleportPlayerToSpawn(player);
    }

    /**
     * Registers the below-name health objective for a player if the setting is enabled.
     *
     * @param player The player to register the objective for.
     */
    private void registerHealthObjectiveForPlayer(Player player) {
        if (!this.getKit().isSettingEnabled(KitSettingHealthBar.class)) {
            return;
        }

        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard.equals(this.plugin.getServer().getScoreboardManager().getMainScoreboard())) {
            scoreboard = this.plugin.getServer().getScoreboardManager().getNewScoreboard();
            player.setScoreboard(scoreboard);
        }

        Objective objective = scoreboard.getObjective("showhealth");
        if (objective == null) {
            objective = scoreboard.registerNewObjective("showhealth", "health");
        }

        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        objective.setDisplayName(ChatColor.RED + "\u2764");
    }

    /**
     * Registers a camp protection task for a player if the kit setting is enabled.
     *
     * @param player The player to register the task for.
     */
    private void registerCampProtectionTask(Player player) {
        if (!this.getKit().isSettingEnabled(KitSettingCampProtectionImpl.class)) {
            return;
        }

        MatchCampProtectionTask campProtectionTask = new MatchCampProtectionTask(player);
        campProtectionTask.runTaskTimer(this.plugin, 0L, 20L);
    }

    /**
     * Cleans up and unregisters the health objective from all match participants.
     */
    private void cleanupHealthDisplay() {
        if (!this.getKit().isSettingEnabled(KitSettingHealthBar.class)) {
            return;
        }

        getParticipants().stream()
                .flatMap(participant -> participant.getPlayers().stream())
                .map(gamePlayer -> this.plugin.getServer().getPlayer(gamePlayer.getUuid()))
                .filter(Objects::nonNull)
                .forEach(player -> {
                    Scoreboard scoreboard = player.getScoreboard();
                    Objective objective = scoreboard.getObjective("showhealth");
                    if (objective != null) {
                        objective.unregister();
                    }
                });
    }

    private void cleanupTasks() {
        this.runnable.cancel();
    }

    /**
     * Sets up a player for the match.
     *
     * @param player The player to set up.
     */
    public void setupPlayer(Player player) {
        MatchGamePlayerImpl gamePlayer = getGamePlayer(player);
        if (gamePlayer == null) {
            return;
        }

        gamePlayer.setDead(false);
        PlayerUtil.reset(player, true, true);
        this.giveLoadout(player, this.kit);
    }

    /**
     * Gives a loadout to a player.
     *
     * @param player The player to give the kit to.
     */
    public void giveLoadout(Player player, Kit kit) {
        LayoutService layoutService = Alley.getInstance().getService(LayoutService.class);
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);

        player.getInventory().setArmorContents(kit.getArmor());

        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getProfileData().getLayoutData().getLayouts().size() > 1) {
            LayoutData kitLayout = profile.getProfileData().getLayoutData().getLayouts().get(kit.getName()).get(0);
            player.getInventory().setContents(kitLayout.getItems());
        } else {
            layoutService.giveBooks(player, kit.getName());
        }

        player.updateInventory();

        this.kit.applyPotionEffects(player);
    }

    /**
     * Handles the death of a player.
     *
     * @param player The player that died.
     */
    public void handleDeath(Player player, EntityDamageEvent.DamageCause cause) {
        if (!(this.state == MatchState.STARTING || this.state == MatchState.RUNNING)) {
            return;
        }

        CombatService combatService = Alley.getInstance().getService(CombatService.class);
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);

        GameParticipant<MatchGamePlayerImpl> participant = this.getParticipant(player);
        MatchGamePlayerImpl gamePlayer = this.getFromAllGamePlayers(player);
        if (participant.isAllEliminated() && !gamePlayer.isDisconnected()) {
            return;
        }

        this.handleParticipant(player, gamePlayer);

        Player killer = combatService.getLastAttacker(player);
        Profile victimProfile = profileService.getProfile(player.getUniqueId());
        Profile killerProfile = (killer != null) ? profileService.getProfile(killer.getUniqueId()) : null;

        if (!gamePlayer.isDisconnected()) {
            this.handleDeathMessages(player, killer, victimProfile, killerProfile, cause);
        }

        this.createSnapshot(player);

        player.setVelocity(new Vector());

        if (this.canEndRound()) {
            this.state = MatchState.ENDING_ROUND;
            this.handleRoundEnd();

            if (this.canEndMatch()) {
                if (killer != null) {
                    this.handleDeathEffects(player, killer);
                }

                this.state = MatchState.ENDING_MATCH;
            }
            this.runnable.setStage(4);
            return;
        }

        if (handleSpectator(player, victimProfile, participant)) {
            if (killer != null) {
                this.handleDeathEffects(player, killer);
            }
            this.setupSpectatorProfile(player);
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> this.addSpectator(player), 1L);
            return;
        }

        if (gamePlayer.isEliminated()) {
            return;
        }

        if (this.shouldHandleRegularRespawn(player)) {
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> this.handleRespawn(player), 1L);
        }

        if (!this.shouldHandleRegularRespawn(player)) {
            this.startRespawnProcess(player);
        }
    }

    /**
     * Handles the player becoming a spectator based on the match state and kit settings.
     *
     * @param player      The player to handle.
     * @param profile     The profile of the player.
     * @param participant The participant of the match.
     */
    private boolean handleSpectator(Player player, Profile profile, GameParticipant<MatchGamePlayerImpl> participant) {
        Kit matchKit = profile.getMatch().getKit();

        MatchGamePlayerImpl gamePlayer = this.getFromAllGamePlayers(player);
        if (gamePlayer.isDisconnected()) {
            return false;
        }

        return this.shouldBecomeSpectatorForEliminationKit(participant, matchKit, player) || shouldBecomeSpectatorForNonRoundKit(participant, matchKit);
    }

    private boolean shouldBecomeSpectatorForEliminationKit(GameParticipant<MatchGamePlayerImpl> participant, Kit matchKit, Player player) {
        if (!participant.isAllEliminated() && this.hasEliminationBasedKit(matchKit)) {
            MatchGamePlayerImpl gamePlayer = this.getGamePlayer(player);
            return gamePlayer.isEliminated();
        }
        return false;
    }

    private boolean shouldBecomeSpectatorForNonRoundKit(GameParticipant<MatchGamePlayerImpl> participant, Kit matchKit) {
        return !participant.isAllDead() && !this.isRoundBasedKit(matchKit) && !this.hasEliminationBasedKit(matchKit);
    }

    /**
     * Checks if the kit is elimination-based.
     *
     * @param kit The kit to check.
     * @return True if the kit is elimination-based, false otherwise.
     */
    private boolean hasEliminationBasedKit(Kit kit) {
        return kit.isSettingEnabled(KitSettingBed.class) || kit.isSettingEnabled(KitSettingCheckpoint.class) || kit.isSettingEnabled(KitSettingLives.class);
    }

    /**
     * Checks if the kit is round-based.
     *
     * @param kit The kit to check.
     * @return True if the kit is round-based, false otherwise.
     */
    private boolean isRoundBasedKit(Kit kit) {
        return kit.isSettingEnabled(KitSettingStickFight.class) || kit.isSettingEnabled(KitSettingRounds.class);
    }

    private void handleDeathMessages(Player victim, Player killer, Profile victimProfile, Profile killerProfile, EntityDamageEvent.DamageCause cause) {
        if (killer == null || killerProfile == null) {
            handleDefaultDeathMessages(victim, null, victimProfile);
            return;
        }

        String selectedPackName = killerProfile.getProfileData().getCosmeticData().getSelected(CosmeticType.KILL_MESSAGE);

        if (selectedPackName == null || selectedPackName.equalsIgnoreCase("None")) {
            handleDefaultDeathMessages(victim, killer, victimProfile);
            return;
        }

        CosmeticService cosmeticRepository = Alley.getInstance().getService(CosmeticService.class);
        BaseCosmeticRepository<?> repository = cosmeticRepository.getRepository(CosmeticType.KILL_MESSAGE);
        KillMessagePack pack = (KillMessagePack) repository.getCosmetic(selectedPackName);

        if (pack == null) {
            handleDefaultDeathMessages(victim, killer, victimProfile);
            return;
        }

        String messageTemplate = pack.getRandomMessage(cause);

        if (messageTemplate != null) {
            String finalMessage = messageTemplate.replace("{victim}", victimProfile.getNameColor() + victim.getName() + "&f");
            finalMessage = finalMessage.replace("{killer}", killerProfile.getNameColor() + killer.getName() + "&f");

            this.notifyAll(CC.translate("&c&lDEATH! " + finalMessage));
            processKillerStatActions(killer);
        } else {
            handleDefaultDeathMessages(victim, killer, victimProfile);
        }
    }

    private void handleDefaultDeathMessages(Player victim, Player killer, Profile victimProfile) {
        if (killer == null) {
            this.notifyAll("&c&lDEATH! &r&c" + victimProfile.getFancyName() + " &fdied.");
        } else {
            processKillerActions(victim, killer, victimProfile);
        }
    }

    private void processKillerActions(Player victim, Player killer, Profile victimProfile) {
        processKillerStatActions(killer);


        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        ReflectionRepository reflectionRepository = Alley.getInstance().getService(ReflectionRepository.class);

        Profile killerProfile = profileService.getProfile(killer.getUniqueId());

        reflectionRepository.getReflectionService(ActionBarReflectionServiceImpl.class).sendDeathMessage(killer, victim);

        this.notifyAll("&c&lDEATH! &r&c" + victimProfile.getNameColor() + victim.getName() + " &fwas slain by &c" + killerProfile.getNameColor() + killer.getName() + "&f.");
    }

    private void processKillerStatActions(Player killer) {
        GameParticipant<MatchGamePlayerImpl> killerParticipant = getParticipant(killer);
        if (killerParticipant != null) {
            killerParticipant.getLeader().getData().incrementKills();
        }
    }

    /**
     * Handles applying all relevant on-kill cosmetic effects.
     * This is called when a player is confirmed to be eliminated from the match.
     *
     * @param player The player who died (the victim).
     * @param killer The player who got the kill.
     */
    private void handleDeathEffects(Player player, Player killer) {
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(killer.getUniqueId());

        String selectedKillEffectName = profile.getProfileData().getCosmeticData().getSelectedKillEffect();
        String selectedSoundEffectName = profile.getProfileData().getCosmeticData().getSelectedSoundEffect();

        this.applyCosmetic(CosmeticType.KILL_EFFECT, selectedKillEffectName, player);
        this.applyCosmetic(CosmeticType.SOUND_EFFECT, selectedSoundEffectName, killer);
    }

    /**
     * Applies a selected cosmetic to a target player in a generic, type-safe way.
     * This method is now updated to use the enum-based repository system.
     *
     * @param cosmeticType The type of cosmetic to apply.
     * @param cosmeticName The name of the cosmetic selected by the player.
     * @param targetPlayer The player to apply the effect to (e.g., the victim or the killer).
     */
    private void applyCosmetic(CosmeticType cosmeticType, String cosmeticName, Player targetPlayer) {
        if (cosmeticName == null || cosmeticName.equalsIgnoreCase("None")) {
            return;
        }

        CosmeticService cosmeticRepository = Alley.getInstance().getService(CosmeticService.class);
        BaseCosmeticRepository<?> repository = cosmeticRepository.getRepository(cosmeticType);
        if (repository == null) {
            Logger.error("Could not find cosmetic repository for type " + cosmeticType.name());
            return;
        }

        BaseCosmetic cosmetic = repository.getCosmetic(cosmeticName);
        if (cosmetic == null) {
            return;
        }

        cosmetic.execute(targetPlayer);
    }

    /**
     * Handles the start of a round.
     */
    public void handleRoundStart() {
        if (this instanceof RoundsMatch && ((RoundsMatch) this).getCurrentRound() > 0) {
            return;
        }
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Handles the end of a round.
     */
    public void handleRoundEnd() {
        this.endTime = System.currentTimeMillis();

        this.handleMatchHistoryData();

        this.getParticipants().forEach(
                participant -> participant.getAllPlayers().forEach(gamePlayer -> {
                    Player player = this.plugin.getServer().getPlayer(gamePlayer.getUuid());
                    if (player != null) {
                        this.createSnapshot(player);
                    }
                })
        );

        SnapshotService snapshotRepository = this.plugin.getService(SnapshotService.class);
        this.snapshots.forEach(snapshotRepository::addSnapshot);
    }

    private void handleMatchHistoryData() {
        if (this.isTeamMatch()) return; //TODO: either handle this case too or we're just not storing team match history

        this.getParticipants().forEach(gameParticipant -> gameParticipant.getAllPlayers().forEach(gamePlayer -> {
            Player player = this.plugin.getServer().getPlayer(gamePlayer.getUuid());
            if (player == null) return;

            Profile profile = this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId());

            UUID winnerID;
            UUID loserID;

            if (gamePlayer.isDead()) {
                winnerID = this.getOpponent(player).getLeader().getUuid();
                loserID = gamePlayer.getUuid();
            } else {
                winnerID = gamePlayer.getUuid();
                loserID = this.getOpponent(player).getLeader().getUuid();
            }

            String arenaName;
            if (this.arena instanceof StandAloneArena) {
                arenaName = ((StandAloneArena) this.arena).getOriginalArenaName();
            } else {
                arenaName = this.arena.getName();
            }

            MatchData matchData = new MatchDataSoloImpl(
                    this.getKit().getName(),
                    arenaName,
                    winnerID,
                    loserID
            );

            if (this.isRanked()) {
                matchData.setRanked(true);
            }

            profile.getProfileData().getPreviousMatches().add(matchData);
        }));
    }

    /**
     * Creates a snapshot of the current match state for a player.
     * This method captures various statistics and the opponent's UUID.
     *
     * @param player The player for whom to create the snapshot.
     */
    public void createSnapshot(Player player) {
        if (this.snapshots.stream().anyMatch(snapshot -> snapshot.getUuid().equals(player.getUniqueId()))) {
            return;
        }

        MatchGamePlayerImpl gamePlayer = this.getGamePlayer(player);
        if (gamePlayer == null || gamePlayer.isDisconnected()) {
            return;
        }

        Snapshot snapshot = new Snapshot(player, !gamePlayer.isDead());
        snapshot.setOpponent(this.getOpponent(player).getLeader().getUuid());
        snapshot.setLongestCombo(gamePlayer.getData().getLongestCombo());
        snapshot.setTotalHits(gamePlayer.getData().getHits());
        snapshot.setThrownPotions(gamePlayer.getData().getThrownPotions());
        snapshot.setMissedPotions(gamePlayer.getData().getMissedPotions());
        snapshot.setCriticalHits(gamePlayer.getData().getCriticalHits());
        snapshot.setBlockedHits(gamePlayer.getData().getBlockedHits());
        snapshot.setWTaps(gamePlayer.getData().getWTaps());

        this.snapshots.add(snapshot);
    }

    /**
     * Adds a player to the list of spectators.
     *
     * @param player The player to add.
     */
    public void addSpectator(Player player) {
        if (this.getGamePlayer(player) == null) {
            if (this.getState() == MatchState.ENDING_MATCH) {
                player.sendMessage(CC.translate("&cThis match has already ended."));
                return;
            }

            this.setupSpectatorProfile(player);
            this.spectators.add(player.getUniqueId());
        }

        NametagService nametagService = Alley.getInstance().getService(NametagService.class);
        VisibilityService visibilityService = Alley.getInstance().getService(VisibilityService.class);
        HotbarService hotbarService = Alley.getInstance().getService(HotbarService.class);

        nametagService.updatePlayerState(player);
        visibilityService.updateVisibility(player);
        hotbarService.applyHotbarItems(player);

        if (this.arena.getCenter() == null) {
            player.sendMessage(CC.translate("&cThe arena is not set up for spectating"));
            return;
        }

        player.setAllowFlight(true);
        player.setFlying(true);

        ListenerUtil.teleportAndClearSpawn(player, this.arena.getCenter());

        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        this.notifyAll("&6" + profile.getFancyName() + " &fis now spectating the match.");
    }

    /**
     * Removes a player from the list of spectators.
     *
     * @param player The player to remove from spectating.
     */
    public void removeSpectator(Player player, boolean notify) {
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        profile.setState(ProfileState.LOBBY);
        profile.setMatch(null);

        NametagService nametagService = Alley.getInstance().getService(NametagService.class);
        VisibilityService visibilityService = Alley.getInstance().getService(VisibilityService.class);

        nametagService.updatePlayerState(player);
        visibilityService.updateVisibility(player);

        player.setAllowFlight(false);
        player.setFlying(false);

        this.resetPlayerState(player);
        this.teleportPlayerToSpawn(player);
        this.spectators.remove(player.getUniqueId());

        if (notify) {
            this.notifyAll("&6" + profile.getFancyName() + " &fis no longer spectating the match.");
        }
    }

    /**
     * Starts the respawn process for a participant.
     *
     * @param player The player to start the respawn process for.
     */
    public void startRespawnProcess(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        player.setAllowFlight(true);
        player.setFlying(true);

        MatchGamePlayerImpl gamePlayer = this.getGamePlayer(player);
        if (gamePlayer != null) {
            gamePlayer.setDead(false);
        }

        Location spawnLocation = this.arena.getCenter();
        ListenerUtil.teleportAndClearSpawn(player, spawnLocation);

        new MatchRespawnTask(player, this, 3).runTaskTimer(this.plugin, 0L, 20L);
    }

    /**
     * Determines whether handleRespawn should be called for a player.
     * This method can be overridden by subclasses to control the respawn process.
     *
     * @param player The player to check.
     * @return True if handleRespawn should be called, false otherwise.
     */
    protected boolean shouldHandleRegularRespawn(Player player) {
        return true;
    }

    /**
     * Sets a participant as dead.
     *
     * @param player     The player to set as dead.
     * @param gamePlayer The game player to set as dead.
     */
    public void handleParticipant(Player player, MatchGamePlayerImpl gamePlayer) {
        gamePlayer.setDead(true);
    }

    /**
     * Notifies the participants with a message.
     *
     * @param message The message to notify.
     */
    public void notifyParticipants(String message) {
        this.getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(uuid -> {
            Player player = this.plugin.getServer().getPlayer(uuid.getUuid());
            if (player != null) {
                player.sendMessage(CC.translate(message));
            }
        }));
    }

    /**
     * Notifies the spectators with a message.
     *
     * @param message The message to notify.
     */
    public void notifySpectators(String message) {
        this.spectators.stream()
                .map(uuid -> this.plugin.getServer().getPlayer(uuid))
                .filter(Objects::nonNull)
                .forEach(player -> player.sendMessage(CC.translate(message)));
    }

    /**
     * Notifies all participants and spectators with a message.
     *
     * @param message The message to notify.
     */
    public void notifyAll(String message) {
        this.notifyParticipants(message);
        this.notifySpectators(message);
    }

    public void broadcastAndStopSpectating() {
        List<String> firstThreeSpectatorNames = new ArrayList<>();
        this.spectators.stream()
                .map(uuid -> this.plugin.getServer().getPlayer(uuid))
                .filter(Objects::nonNull)
                .limit(3)
                .forEach(player -> firstThreeSpectatorNames.add(player.getName()));

        List<Integer> remainingSpectators = new ArrayList<>();
        this.spectators.stream()
                .map(uuid -> this.plugin.getServer().getPlayer(uuid))
                .filter(Objects::nonNull)
                .skip(3)
                .forEach(player -> remainingSpectators.add(player.getEntityId()));

        this.sendMessage("&6&lSpectators: &f" + String.join(", ", firstThreeSpectatorNames) +
                (remainingSpectators.isEmpty() ? "" : " &7(and &6" + remainingSpectators.size() + " &7more...)"));

        this.spectators.forEach(uuid -> {
            Player player = this.plugin.getServer().getPlayer(uuid);
            if (player != null) {
                this.removeSpectator(player, false);
            }
        });
    }

    /**
     * Gets the duration of the match.
     *
     * @return The duration of the match.
     */
    public String getDuration() {
        if (this.state == MatchState.STARTING) {
            return TimeUtil.getFormattedElapsedTime(this.getElapsedTime());
        } else if (this.state == MatchState.ENDING_MATCH) {
            return TimeUtil.getFormattedElapsedTime(this.endTime - this.startTime);
        } else {
            return TimeUtil.getFormattedElapsedTime(this.getElapsedTime());
        }
    }

    /**
     * Gets the elapsed time of the match.
     *
     * @return The elapsed time of the match.
     */
    public long getElapsedTime() {
        return System.currentTimeMillis() - this.startTime;
    }

    /**
     * Gets the game player of a player.
     *
     * @param player The player to get the game player of.
     * @return The game player of the player.
     */
    public MatchGamePlayerImpl getGamePlayer(Player player) {
        return this.getParticipants().stream()
                .map(GameParticipant::getPlayers)
                .flatMap(List::stream)
                .filter(gamePlayer -> gamePlayer.getUuid().equals(player.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets a game player from all game players in the match.
     *
     * @param player The player to get the game player of.
     * @return The game player of the player, or null if not found.
     */
    public MatchGamePlayerImpl getFromAllGamePlayers(Player player) {
        return this.getParticipants().stream()
                .map(GameParticipant::getAllPlayers)
                .flatMap(List::stream)
                .filter(gamePlayer -> gamePlayer.getUuid().equals(player.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets a participant by a player.
     *
     * @param player The player to get the participant of.
     * @return The participant of the player.
     */
    public GameParticipant<MatchGamePlayerImpl> getParticipant(Player player) {
        return this.getParticipants().stream()
                .filter(gameParticipant -> gameParticipant.containsPlayer(player.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the opposing participant in a two-sided match.
     *
     * @param player The player object of a player on one side.
     * @return The opposing GameParticipant, or null if it cannot be determined.
     */
    public GameParticipant<MatchGamePlayerImpl> getOpponent(Player player) {
        GameParticipant<MatchGamePlayerImpl> participant = this.getParticipant(player);
        if (participant == null) {
            return null;
        }

        return this.getParticipants().stream()
                .filter(p -> !p.equals(participant))
                .findFirst()
                .orElse(null);
    }

    /**
     * Plays a sound for a player.
     *
     * @param sound The sound to play.
     */
    public void playSound(Sound sound) {
        this.getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(uuid -> {
            Player player = this.plugin.getServer().getPlayer(uuid.getUuid());
            if (player != null) {
                SoundUtil.playCustomSound(player, sound, 1.0F, 1.0F);
            }
        }));

        this.getSpectators().forEach(uuid -> {
            Player player = this.plugin.getServer().getPlayer(uuid);
            if (player != null) {
                SoundUtil.playCustomSound(player, sound, 1.0F, 1.0F);
            }
        });
    }

    /**
     * Sends a title to all participants and spectators.
     *
     * @param title    The title to send.
     * @param subtitle The subtitle to send.
     */
    public void sendTitle(String title, String subtitle) {
        ReflectionRepository reflectionRepository = Alley.getInstance().getService(ReflectionRepository.class);
        this.getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(uuid -> {
            Player player = this.plugin.getServer().getPlayer(uuid.getUuid());
            if (player != null) {
                reflectionRepository.getReflectionService(TitleReflectionServiceImpl.class).sendTitle(
                        player,
                        title,
                        subtitle
                );
            }
        }));

        this.getSpectators().forEach(uuid -> {
            Player player = this.plugin.getServer().getPlayer(uuid);
            if (player != null) {
                reflectionRepository.getReflectionService(TitleReflectionServiceImpl.class).sendTitle(
                        player,
                        title,
                        subtitle
                );
            }
        });
    }

    /**
     * Sends a list of messages to all participants.
     *
     * @param messages The list of messages to send.
     */
    public void sendMessage(List<String> messages) {
        messages.forEach(this::sendMessage);
    }

    /**
     * Sends a message to all participants.
     *
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        this.getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(uuid -> {
            Player player = this.plugin.getServer().getPlayer(uuid.getUuid());
            if (player != null) {
                player.sendMessage(CC.translate(message));
            }
        }));

        this.getSpectators().forEach(uuid -> {
            Player player = this.plugin.getServer().getPlayer(uuid);
            if (player != null) {
                player.sendMessage(CC.translate(message));
            }
        });
    }

    /**
     * Checks if the attacker is in the same participant team as the supposed victim.
     *
     * @param attacker The attacker.
     * @param victim   The victim.
     * @return If the attacker is in the same participant team as the victim.
     */
    public boolean isInSameTeam(Player attacker, Player victim) {
        GameParticipant<MatchGamePlayerImpl> attackerParticipant = this.getParticipant(attacker);
        GameParticipant<MatchGamePlayerImpl> victimParticipant = this.getParticipant(victim);

        return attackerParticipant.equals(victimParticipant);
    }

    /**
     * Intentionally made to deny player movement during a match countdown.
     *
     * @param participants the participants
     */
    public void denyPlayerMovement(List<GameParticipant<MatchGamePlayerImpl>> participants) {
        if (participants.size() == 2) {
            GameParticipant<?> participantA = participants.get(0);
            GameParticipant<?> participantB = participants.get(1);

            Location locationA = this.arena.getPos1();
            Location locationB = this.arena.getPos2();

            for (GamePlayer gamePlayer : participantA.getPlayers()) {
                Player participantPlayer = gamePlayer.getTeamPlayer();
                if (participantPlayer != null) {
                    this.teleportBackIfMoved(participantPlayer, locationA);
                }
            }

            for (GamePlayer gamePlayer : participantB.getPlayers()) {
                Player participantPlayer = gamePlayer.getTeamPlayer();
                if (participantPlayer != null) {
                    this.teleportBackIfMoved(participantPlayer, locationB);
                }
            }
        }
    }

    /**
     * Teleports the player back to their designated position if they moved.
     *
     * @param player   The player to check.
     * @param location The designated location.
     */
    protected void teleportBackIfMoved(Player player, Location location) {
        Location playerLocation = player.getLocation();

        double deltaX = Math.abs(playerLocation.getX() - location.getX());
        double deltaZ = Math.abs(playerLocation.getZ() - location.getZ());

        if (deltaX > 0.1 || deltaZ > 0.1) {
            player.teleport(new Location(location.getWorld(), location.getX(), playerLocation.getY(), location.getZ(), playerLocation.getYaw(), playerLocation.getPitch()));
        }
    }

    /**
     * Teleports a player to the spawn and applies spawn items.
     *
     * @param player The player to teleport.
     */
    private void teleportPlayerToSpawn(Player player) {
        if (player == null) return;

        HotbarService hotbarService = Alley.getInstance().getService(HotbarService.class);
        SpawnService spawnService = Alley.getInstance().getService(SpawnService.class);

        spawnService.teleportToSpawn(player);
        hotbarService.applyHotbarItems(player);
    }

    /**
     * Teleports a player to the spawn.
     *
     * @param player The player to teleport.
     */
    private void resetPlayerState(Player player) {
        player.setFireTicks(0);
        player.updateInventory();
        PlayerUtil.reset(player, false, true);
    }

    /**
     * Adds a block to the placed blocks map with the intention to handle block placement and removal.
     *
     * @param blockState The block state to add.
     * @param location   The location of the block.
     */
    public void addBlockToPlacedBlocksMap(BlockState blockState, Location location) {
        this.placedBlocks.put(blockState, location);
    }

    /**
     * Removes a block from the placed blocks map.
     *
     * @param blockState The block state to remove.
     */
    public void removeBlockFromPlacedBlocksMap(BlockState blockState, Location location) {
        this.placedBlocks.remove(blockState, location);
    }

    public void addBlockToBrokenBlocksMap(BlockState blockState, Location location) {
        if (this.placedBlocks.containsValue(location)) {
            this.placedBlocks.values().remove(location);
        } else {
            this.brokenBlocks.put(blockState, location);
        }
    }


    @SuppressWarnings("deprecation")
    public void resetBlockChanges() {

        if (this.getKit().isSettingEnabled(KitSettingRaiding.class)) {
            Arena arena = this.getArena();
            Location pos1 = arena.getPos1();
            Location pos2 = arena.getPos2();

            for (int x = pos1.getBlockX(); x <= pos2.getBlockX(); x++) {
                for (int z = pos1.getBlockZ(); z <= pos2.getBlockZ(); z++) {
                    for (int y = pos1.getBlockY(); y <= pos2.getBlockY(); y++) {
                        Location location = new Location(pos1.getWorld(), x, y, z);
                        Block block = location.getBlock();
                        if (ListenerUtil.isInteractiveBlock(block.getType())) {
                            BlockState originalState = block.getState();
                            if (originalState.getType() == Material.AIR) {
                                continue;
                            }
                            this.brokenBlocks.put(originalState, location);
                            block.setType(Material.AIR);
                        }
                    }
                }
            }
        }


        this.removePlacedBlocks();

        for (Map.Entry<BlockState, Location> entry : this.brokenBlocks.entrySet()) {
            Location location = entry.getValue();
            BlockState originalState = entry.getKey();

            Block block = location.getBlock();
            block.setType(originalState.getType());
            block.setData(originalState.getRawData());
        }

        this.brokenBlocks.clear();
    }

    public void removePlacedBlocks() {
        for (Map.Entry<BlockState, Location> entry : this.placedBlocks.entrySet()) {
            Location location = entry.getValue();
            location.getBlock().setType(Material.AIR);
        }

        this.placedBlocks.clear();
    }


    private void sendPlayerVersusPlayerMessage() {
        String prefix = CC.translate("&7[&6Match&7] &r");

        if (this.isTeamMatch()) {
            GameParticipant<MatchGamePlayerImpl> participantA = this.getParticipants().get(0);
            GameParticipant<MatchGamePlayerImpl> participantB = this.getParticipants().get(1);

            int teamSizeA = participantA.getPlayerSize();
            int teamSizeB = participantB.getPlayerSize();

            String message = CC.translate(prefix + "&6" + participantA.getLeader().getUsername() + "'s Team &7(&a" + teamSizeA + "&7) &avs &6" + participantB.getLeader().getUsername() + "'s Team &7(&a" + teamSizeB + "&7)");
            this.sendMessage(message);
        } else {
            GameParticipant<MatchGamePlayerImpl> participant = this.getParticipants().get(0);
            GameParticipant<MatchGamePlayerImpl> opponent = this.getParticipants().get(1);

            String message = CC.translate(prefix + "&6" + participant.getLeader().getUsername() + " &avs &6" + opponent.getLeader().getUsername());
            this.sendMessage(message);
        }
    }

    private void handleMatchTasks() {
        this.runnable = new MatchTask(this);
        this.runnable.runTaskTimer(this.plugin, 0L, 20L);

        if (this.getKit().isSettingEnabled(KitSettingPlatformDecay.class) && this.getArena() instanceof StandAloneArena) {
            PlatformDecayTask.start(this);
        }
    }

    private void updatePlayerProfileForMatch(Player player) {
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        profile.setState(ProfileState.PLAYING);
        profile.setMatch(this);
    }

    private void updatePlayerProfileForLobby(Player player) {
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        profile.setState(ProfileState.LOBBY);
        profile.setMatch(null);
    }

    private void setupSpectatorProfile(Player player) {
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        profile.setState(ProfileState.SPECTATING);
        profile.setMatch(this);

        PlayerUtil.reset(player, false, true);
    }

    /**
     * Calculates the coin reward for a player based on their performance in the match.
     * The calculation is based on kills, deaths, and missed potions.
     *
     * @param player The player to calculate the coin reward for.
     */
    public void calculateCoinReward(Player player) {
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        MatchGamePlayerData data = this.getGamePlayer(player).getData();
        int kills = data.getKills();
        int deaths = data.getDeaths();
        int missedPotions = data.getMissedPotions();

        double score = 0;

        score += kills * 15;
        score -= deaths * 10;

        int excessPotions = Math.max(missedPotions - 20, 0);
        score -= excessPotions * 1.5;

        int performanceScore = (int) Math.max(0, Math.min(100, score));
        profile.getProfileData().incrementCoins(performanceScore);
    }

    /**
     * Sends a reward message to the player after the match.
     * This message includes the number of coins earned.
     *
     * @param player The player to send the reward message to.
     */
    public void sendRewardMessage(Player player) {
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        int coins = profile.getProfileData().getCoins();

        player.sendMessage(CC.translate(" &7(&a+&6" + coins + "&f&7)"));
    }
}