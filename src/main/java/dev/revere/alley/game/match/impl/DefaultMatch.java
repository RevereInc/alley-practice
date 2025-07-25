package dev.revere.alley.game.match.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.service.BaseRaidingService;
import dev.revere.alley.base.kit.setting.impl.mechanic.KitSettingDropItemsImpl;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingRaiding;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingRespawnTimer;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.feature.layout.data.LayoutData;
import dev.revere.alley.game.match.Match;
import dev.revere.alley.game.match.enums.MatchState;
import dev.revere.alley.game.match.player.data.MatchGamePlayerData;
import dev.revere.alley.game.match.player.enums.BaseRaiderRole;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.utility.MatchUtility;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.progress.ProgressService;
import dev.revere.alley.profile.progress.PlayerProgress;
import dev.revere.alley.tool.elo.EloCalculator;
import dev.revere.alley.tool.elo.result.EloResult;
import dev.revere.alley.tool.elo.result.OldEloResult;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.tool.reflection.ReflectionRepository;
import dev.revere.alley.tool.reflection.impl.TitleReflectionServiceImpl;
import dev.revere.alley.util.InventoryUtil;
import dev.revere.alley.util.ListenerUtil;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Setter
public class DefaultMatch extends Match {
    private final GameParticipant<MatchGamePlayerImpl> participantA;
    private final GameParticipant<MatchGamePlayerImpl> participantB;

    public final ChatColor teamAColor;
    public final ChatColor teamBColor;

    private GameParticipant<MatchGamePlayerImpl> winner;
    private GameParticipant<MatchGamePlayerImpl> loser;

    /**
     * Constructor for the MatchRegularImpl class.
     *
     * @param queue        The queue of the match.
     * @param kit          The kit of the match.
     * @param arena        The arena of the match.
     * @param ranked       Whether the match is ranked or not.
     * @param participantA The first participant.
     * @param participantB The second participant.
     */
    public DefaultMatch(Queue queue, Kit kit, Arena arena, boolean ranked, GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB) {
        super(queue, kit, arena, ranked);
        this.participantA = participantA;
        this.participantB = participantB;
        this.teamAColor = ChatColor.BLUE;
        this.teamBColor = ChatColor.RED;
    }

    @Override
    public void setupPlayer(Player player) {
        super.setupPlayer(player);
        this.applyColorKit(player);

        Location spawnLocation = this.getParticipantA().containsPlayer(player.getUniqueId()) ? getArena().getPos1() : getArena().getPos2();
        player.teleport(spawnLocation);

        if (this.getKit().isSettingEnabled(KitSettingRaiding.class)) {
            this.determineRolesAndGiveKit(player);
        }
    }

    @Override
    public List<GameParticipant<MatchGamePlayerImpl>> getParticipants() {
        return Arrays.asList(getParticipantA(), getParticipantB());
    }

    /**
     * Get the team color of a participant.
     *
     * @param participant The participant to get the team color of.
     * @return The team color of the participant.
     */
    public ChatColor getTeamColor(GameParticipant<MatchGamePlayerImpl> participant) {
        return participant == this.getParticipantA() ? this.teamAColor : this.teamBColor;
    }

    /**
     * Applies the wool color to the player based on their team.
     *
     * @param player The player to apply the wool color to.
     */
    public void applyColorKit(Player player) {
        GameParticipant<MatchGamePlayerImpl> participant = this.getParticipant(player);
        if (participant == null) {
            return;
        }

        final InventoryUtil.TeamColor colorToApply = (participant == this.getParticipantA())
                ? InventoryUtil.TeamColor.BLUE
                : InventoryUtil.TeamColor.RED;

        participant.getPlayers().stream()
                .map(MatchGamePlayerImpl::getTeamPlayer)
                .filter(p -> p != null && p.isOnline())
                .forEach(teamPlayer -> InventoryUtil.applyTeamColorToInventory(teamPlayer, colorToApply));
    }

    @Override
    protected boolean shouldHandleRegularRespawn(Player player) {
        return !this.getKit().isSettingEnabled(KitSettingRespawnTimer.class);
    }

    @Override
    public void handleRoundEnd() {
        final boolean teamADead = this.getParticipantA().isAllEliminated() || this.getParticipantA().isAllDead();
        final GameParticipant<MatchGamePlayerImpl> winner = teamADead ? this.getParticipantB() : this.getParticipantA();
        final GameParticipant<MatchGamePlayerImpl> loser = teamADead ? this.getParticipantA() : this.getParticipantB();

        this.winner = winner;
        this.loser = loser;

        broadcastMatchOutcome(winner, loser);
        processStatistics(winner, loser);

        if (!this.getSpectators().isEmpty()) {
            this.broadcastAndStopSpectating();
        }

        super.handleRoundEnd();
    }

    /**
     * Handles all player-facing messages at the end of a match, including titles and results.
     */
    private void broadcastMatchOutcome(GameParticipant<MatchGamePlayerImpl> winner, GameParticipant<MatchGamePlayerImpl> loser) {
        winner.getPlayers().forEach(gamePlayer -> sendVictory(gamePlayer.getTeamPlayer()));
        loser.getPlayers().forEach(gamePlayer -> sendDefeat(gamePlayer.getTeamPlayer()));

        if (this.isTeamMatch()) {
            MatchUtility.sendConjoinedMatchResult(this, winner, loser);
        } else {
            MatchGamePlayerImpl winnerPlayer = winner.getLeader();
            MatchGamePlayerImpl loserPlayer = loser.getLeader();
            MatchUtility.sendMatchResult(
                    this,
                    winnerPlayer.getUsername(),
                    loserPlayer.getUsername(),
                    winnerPlayer.getUuid(),
                    loserPlayer.getUuid()
            );
        }
    }

    /**
     * Processes all backend statistics if the match is configured to affect them.
     */
    private void processStatistics(GameParticipant<MatchGamePlayerImpl> winner, GameParticipant<MatchGamePlayerImpl> loser) {
        if (!this.isAffectStatistics()) {
            return;
        }

        handleMatchData(winner, loser);

        if (!this.isRanked()) {
            this.sendProgressToWinner(winner.getLeader().getTeamPlayer());
        }
    }

    /**
     * Routes to the correct statistics handling method based on the match type.
     *
     * @param winner The winning participant.
     * @param loser  The losing participant.
     */
    private void handleMatchData(GameParticipant<MatchGamePlayerImpl> winner, GameParticipant<MatchGamePlayerImpl> loser) {
        if (this.isTeamMatch()) {
            return;
        }

        if (this.isRanked()) {
            updateRankedStats(winner, loser);
        } else {
            updateUnrankedStats(winner, loser);
        }
    }

    /**
     * Updates player profiles and Elo for a ranked match.
     */
    private void updateRankedStats(GameParticipant<MatchGamePlayerImpl> winner, GameParticipant<MatchGamePlayerImpl> loser) {
        OldEloResult result = this.getOldEloResult(winner, loser);
        EloResult eloResult = this.getEloResult(result.getOldWinnerElo(), result.getOldLoserElo());

        this.handleWinner(eloResult.getNewWinnerElo(), winner);
        this.handleLoser(eloResult.getNewLoserElo(), loser);

        this.sendEloResult(
                winner.getLeader().getTeamPlayer().getName(),
                loser.getLeader().getTeamPlayer().getName(),
                result.getOldWinnerElo(),
                result.getOldLoserElo(),
                eloResult.getNewWinnerElo(),
                eloResult.getNewLoserElo()
        );
    }

    /**
     * Updates player profiles with wins/losses for an unranked match.
     */
    private void updateUnrankedStats(GameParticipant<MatchGamePlayerImpl> winner, GameParticipant<MatchGamePlayerImpl> loser) {
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);

        Profile winnerProfile = profileService.getProfile(winner.getLeader().getUuid());
        winnerProfile.getProfileData().getUnrankedKitData().get(getKit().getName()).incrementWins();
        winnerProfile.getProfileData().incrementUnrankedWins();
        winnerProfile.getProfileData().determineTitles();

        Profile loserProfile = profileService.getProfile(loser.getLeader().getUuid());
        loserProfile.getProfileData().getUnrankedKitData().get(getKit().getName()).incrementLosses();
        loserProfile.getProfileData().incrementUnrankedLosses();
    }

    /**
     * Send the winner title to the player.
     *
     * @param player The player to send the title to.
     */
    private void sendVictory(Player player) {
        Alley.getInstance().getService(ReflectionRepository.class).getReflectionService(TitleReflectionServiceImpl.class).sendTitle(
                player,
                "&a&lVICTORY!",
                "&fYou have won the match!"
        );
    }

    /**
     * Send the loser title to the player.
     *
     * @param player The player to send the title to.
     */
    private void sendDefeat(Player player) {
        Alley.getInstance().getService(ReflectionRepository.class).getReflectionService(TitleReflectionServiceImpl.class).sendTitle(
                player,
                "&c&lDEFEAT!",
                "&fYou have lost the match!"
        );
    }

    /**
     * Sends the elo result message.
     *
     * @param winnerName   The name of the winner.
     * @param loserName    The name of the loser.
     * @param oldEloWinner The old elo of the winner.
     * @param oldEloLoser  The old elo of the loser.
     * @param newEloWinner The new elo of the winner.
     * @param newEloLoser  The new elo of the loser.
     */
    public void sendEloResult(String winnerName, String loserName, int oldEloWinner, int oldEloLoser, int newEloWinner, int newEloLoser) {
        FileConfiguration config = Alley.getInstance().getService(ConfigService.class).getMessagesConfig();

        List<String> list = config.getStringList("match.ended.elo-changes.format");
        String winnerIndicatorColor = config.getString("match.ended.elo-changes.winner-indicator-color", "&a");
        String loserIndicatorColor = config.getString("match.ended.elo-changes.loser-indicator-color", "&c");

        list.replaceAll(string -> string
                .replace("{winner}", winnerName)
                .replace("{loser}", loserName)
                .replace("{old-winner-elo}", String.valueOf(oldEloWinner))
                .replace("{old-loser-elo}", String.valueOf(oldEloLoser))
                .replace("{new-winner-elo}", String.valueOf(newEloWinner))
                .replace("{new-loser-elo}", String.valueOf(newEloLoser))
                .replace("{winner-indicator}", newEloWinner > oldEloWinner ? "+" : "-")
                .replace("{loser-indicator}", newEloLoser > oldEloLoser ? "+" : "-")
                .replace("{winner-color}", newEloWinner > oldEloWinner ? winnerIndicatorColor : loserIndicatorColor)
                .replace("{loser-color}", newEloLoser > oldEloLoser ? winnerIndicatorColor : loserIndicatorColor)
                .replace("{math-winner-elo}", String.valueOf(Math.abs(oldEloWinner - newEloWinner)))
                .replace("{math-loser-elo}", String.valueOf(Math.abs(oldEloLoser - newEloLoser)))
        );

        list.forEach(this::notifyParticipants);
    }

    /**
     * Sends the progress of the winner to the player using the ProgressService.
     * The method no longer needs Division or Tier passed in.
     *
     * @param winner The winning player.
     */
    public void sendProgressToWinner(Player winner) {
        Profile winnerProfile = Alley.getInstance().getService(ProfileService.class).getProfile(winner.getUniqueId());

        PlayerProgress progress = Alley.getInstance().getService(ProgressService.class).calculateProgress(winnerProfile, this.getKit().getName());

        String progressLine;

        if (progress.isMaxRank() && progress.getCurrentWins() >= progress.getWinsForNextTier()) {
            progressLine = " &6&l● &fCONGRATULATIONS! You have reached the maximum rank!";
        } else {
            progressLine = String.format(" &6&l● &fUnlock &6%s &fwith %d more %s!",
                    progress.getNextRankName(),
                    progress.getWinsRequired(),
                    progress.getWinOrWins()
            );
        }

        Arrays.asList(
                "&6&lProgress",
                progressLine,
                "  &7(" + progress.getProgressBar(12, "■") + "&7) " + progress.getProgressPercentage(),
                " &6&l● &fDaily Streak: &6" + "N/A" + " &f(Best: " + "N/A" + ")",
                " &6&l● &fWin Streak: &6" + "N/A" + " &f(Best: " + "N/A" + ")",
                ""
        ).forEach(line -> winner.sendMessage(CC.translate(line)));
    }

    /**
     * Method to get the old elo result.
     *
     * @return The old elo result.
     */
    public @NotNull OldEloResult getOldEloResult(GameParticipant<MatchGamePlayerImpl> winner, GameParticipant<MatchGamePlayerImpl> loser) {
        int oldWinnerElo = winner.getLeader().getElo();
        int oldLoserElo = loser.getLeader().getElo();
        return new OldEloResult(oldWinnerElo, oldLoserElo);
    }

    /**
     * Method to get the elo result.
     *
     * @param oldWinnerElo The old elo of the winner.
     * @param oldLoserElo  The old elo of the loser.
     * @return The elo result.
     */
    public @NotNull EloResult getEloResult(int oldWinnerElo, int oldLoserElo) {
        EloCalculator eloCalculator = Alley.getInstance().getService(EloCalculator.class);
        int newWinnerElo = eloCalculator.determineNewElo(oldWinnerElo, oldLoserElo, true);
        int newLoserElo = eloCalculator.determineNewElo(oldLoserElo, oldWinnerElo, false);
        return new EloResult(newWinnerElo, newLoserElo);
    }

    /**
     * Method to handle the winner.
     *
     * @param elo    The new elo of the winner.
     * @param winner The winner of the match.
     */
    public void handleWinner(int elo, GameParticipant<MatchGamePlayerImpl> winner) {
        Profile winnerProfile = Alley.getInstance().getService(ProfileService.class).getProfile(winner.getLeader().getUuid());
        winnerProfile.getProfileData().getRankedKitData().get(getKit().getName()).setElo(elo);
        winnerProfile.getProfileData().getRankedKitData().get(getKit().getName()).incrementWins();
        winnerProfile.getProfileData().incrementRankedWins();
        winnerProfile.getProfileData().updateElo(winnerProfile);
    }

    /**
     * Method to handle the loser.
     *
     * @param elo   The new elo of the loser.
     * @param loser The loser of the match.
     */
    public void handleLoser(int elo, GameParticipant<MatchGamePlayerImpl> loser) {
        Profile loserProfile = Alley.getInstance().getService(ProfileService.class).getProfile(loser.getLeader().getUuid());
        loserProfile.getProfileData().getRankedKitData().get(getKit().getName()).setElo(elo);
        loserProfile.getProfileData().getRankedKitData().get(getKit().getName()).incrementLosses();
        loserProfile.getProfileData().incrementRankedLosses();
        loserProfile.getProfileData().updateElo(loserProfile);
    }

    @Override
    public boolean canStartRound() {
        return false;
    }

    @Override
    public boolean canEndRound() {
        return (this.getParticipantA().isAllDead() || this.getParticipantB().isAllDead())
                || (this.getParticipantA().getAllPlayers().stream().allMatch(MatchGamePlayerImpl::isDisconnected)
                || this.getParticipantB().getAllPlayers().stream().allMatch(MatchGamePlayerImpl::isDisconnected));
    }

    @Override
    public boolean canEndMatch() {
        return true;
    }

    @Override
    public void handleRespawn(Player player) {
        player.spigot().respawn();
        PlayerUtil.reset(player, false, true);
    }

    @Override
    public void handleDeathItemDrop(Player player, PlayerDeathEvent event) {
        if (this.getKit().isSettingEnabled(KitSettingDropItemsImpl.class)) {
            ListenerUtil.clearDroppedItemsOnDeath(event, player);
        } else {
            event.getDrops().clear();
        }
    }

    @Override
    public void handleDisconnect(Player player) {
        if (!(this.getState() == MatchState.STARTING || this.getState() == MatchState.RUNNING)) {
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId());
        this.sendMessage(profile.getFancyName() + " &fdisconnected.");

        MatchGamePlayerImpl gamePlayer = this.getFromAllGamePlayers(player);
        if (gamePlayer != null) {
            gamePlayer.setDisconnected(true);
            gamePlayer.setEliminated(true);
            if (!gamePlayer.isDead()) {
                this.handleDeath(player, EntityDamageEvent.DamageCause.CUSTOM);
            }
        }

        if (player.isOnline()) {
            this.finalizePlayer(player);
        }
    }

    /**
     * Gives the base raiding kit to the player based on their team.
     *
     * @param player The player to give the kit to.
     */
    public void determineRolesAndGiveKit(Player player) {
        if (this.getParticipantA() == null || this.getParticipantB() == null) {
            return;
        }

        Kit parentKit = this.getKit();
        if (parentKit == null) {
            Logger.error("&cCould not determine the parent kit for the raiding match.");
            return;
        }

        BaseRaiderRole role = getParticipantA().containsPlayer(player.getUniqueId())
                ? BaseRaiderRole.TRAPPER
                : BaseRaiderRole.RAIDER;

        Kit kitToGive = Alley.getInstance().getService(BaseRaidingService.class).getRaidingKitByRole(parentKit, role);
        if (kitToGive == null) {
            Logger.info("&cNo kit found for role: " + role.name() + " linked to parent kit.");
            return;
        }

        MatchGamePlayerData data = this.getGamePlayer(player).getData();
        data.setRole(role);

        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        LayoutData layout = profile.getProfileData().getLayoutData().getLayouts().get(kitToGive.getName()).get(0);

        //TODO: after implementing multiple layouts, we need to give the books here, if multiple layouts are present in profile.

        if (layout != null) {
            player.getInventory().setContents(layout.getItems());
        } else {
            player.getInventory().setContents(kitToGive.getItems());
        }

        player.getInventory().setArmorContents(kitToGive.getArmor());
        player.updateInventory();
    }
}