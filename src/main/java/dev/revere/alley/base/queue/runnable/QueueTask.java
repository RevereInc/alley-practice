package dev.revere.alley.base.queue.runnable;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.arena.ArenaService;
import dev.revere.alley.base.arena.enums.ArenaType;
import dev.revere.alley.base.queue.QueueService;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.base.queue.QueueProfile;
import dev.revere.alley.game.match.MatchService;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.player.participant.TeamGameParticipant;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@RequiredArgsConstructor
public class QueueTask implements Runnable {
    /**
     * Main execution method that processes all active queues.
     * Called periodically by the scheduler.
     */
    @Override
    public void run() {
        QueueService queueService = Alley.getInstance().getService(QueueService.class);
        queueService.getQueues().forEach(this::processQueue);
    }

    /**
     * Processes a single queue by validating players, handling timeouts,
     * and attempting to create matches.
     *
     * @param queue The queue to process
     */
    public void processQueue(Queue queue) {
        validateAndCleanupQueuePlayers(queue);

        if (queue.isDuos()) {
            processDuosQueue(queue);
        } else {
            processSoloQueue(queue);
        }
    }

    /**
     * Validates all players in the queue and removes those who are offline,
     * have changed state, or have exceeded the maximum queue time.
     *
     * @param queue The queue to validate and cleanup
     */
    private void validateAndCleanupQueuePlayers(Queue queue) {
        List<QueueProfile> profilesToCheck = new ArrayList<>(queue.getProfiles());

        for (QueueProfile profile : profilesToCheck) {
            Player player = Bukkit.getPlayer(profile.getUuid());

            if (shouldRemovePlayerFromQueue(player, profile)) {
                queue.removePlayer(profile);
                notifyPlayerOfQueueRemoval(player, profile);
            } else {
                profile.queueRange(player);
            }
        }
    }

    /**
     * Determines if a player should be removed from the queue based on
     * their online status, profile state, and queue time.
     *
     * @param player  The player to check
     * @param profile The player's queue profile
     * @return true if the player should be removed from the queue
     */
    private boolean shouldRemovePlayerFromQueue(Player player, QueueProfile profile) {
        if (player == null || !player.isOnline()) {
            return true;
        }

        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);

        Profile playerProfile = profileService.getProfile(profile.getUuid());
        if (!playerProfile.getState().equals(ProfileState.WAITING)) {
            return true;
        }

        return profile.getElapsedTime() >= profile.getQueue().getMaxQueueTime();
    }

    /**
     * Sends appropriate notification message to a player being removed from queue.
     *
     * @param player  The player being removed
     * @param profile The player's queue profile
     */
    private void notifyPlayerOfQueueRemoval(Player player, QueueProfile profile) {
        if (player != null) {
            if (profile.getElapsedTime() >= profile.getQueue().getMaxQueueTime()) {
                player.sendMessage(CC.translate("&cYou have been removed from the queue due to inactivity"));
            } else {
                player.sendMessage(CC.translate("&cYou have been removed from the queue due to being offline or state change."));
            }
        }
    }

    /**
     * Processes solo queue matchmaking by finding two available solo players
     * and creating a 1v1 match between them.
     *
     * @param queue The solo queue to process
     */
    private void processSoloQueue(Queue queue) {
        if (queue.getProfiles().size() < 2) {
            return;
        }

        List<QueueProfile> availableSoloPlayers = getAvailableSoloPlayers(queue);

        if (availableSoloPlayers.size() < 2) {
            return;
        }

        attemptSoloMatches(queue, availableSoloPlayers);
    }

    /**
     * Gets a list of available solo players (not in parties) sorted by queue time.
     *
     * @param queue The queue to get solo players from
     * @return List of available solo players
     */
    private List<QueueProfile> getAvailableSoloPlayers(Queue queue) {
        PartyService partyService = Alley.getInstance().getService(PartyService.class);

        return queue.getProfiles().stream()
                .filter(queueProfile -> partyService.getParty(Bukkit.getPlayer(queueProfile.getUuid())) == null)
                .sorted(Comparator.comparingLong(QueueProfile::getElapsedTime))
                .collect(Collectors.toList());
    }

    /**
     * Attempts to create matches between available solo players.
     *
     * @param queue                The queue being processed
     * @param availableSoloPlayers List of available solo players
     */
    private void attemptSoloMatches(Queue queue, List<QueueProfile> availableSoloPlayers) {
        for (int i = 0; i < availableSoloPlayers.size(); i++) {
            QueueProfile firstProfile = availableSoloPlayers.get(i);
            Player firstPlayer = Bukkit.getPlayer(firstProfile.getUuid());

            if (isPlayerInvalidForMatch(firstPlayer, firstProfile, queue)) {
                continue;
            }

            for (int j = i + 1; j < availableSoloPlayers.size(); j++) {
                QueueProfile secondProfile = availableSoloPlayers.get(j);
                Player secondPlayer = Bukkit.getPlayer(secondProfile.getUuid());

                if (isPlayerInvalidForMatch(secondPlayer, secondProfile, queue)) {
                    continue;
                }

                if (createSoloMatch(queue, firstPlayer, secondPlayer, firstProfile, secondProfile)) {
                    return;
                }
            }
        }
    }

    /**
     * Validates if a player is ready for match creation.
     *
     * @param player  The player to validate
     * @param profile The player's queue profile
     * @param queue   The queue being processed
     * @return true if the player is valid for match creation
     */
    private boolean isPlayerInvalidForMatch(Player player, QueueProfile profile, Queue queue) {
        if (player == null || !player.isOnline()) {
            queue.removePlayer(profile);
            return true;
        }

        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);

        Profile playerProfile = profileService.getProfile(profile.getUuid());
        if (!playerProfile.getState().equals(ProfileState.WAITING)) {
            queue.removePlayer(profile);
            return true;
        }

        return false;
    }

    /**
     * Creates a solo match between two players.
     *
     * @param queue         The queue
     * @param firstPlayer   First player
     * @param secondPlayer  Second player
     * @param firstProfile  First player's profile
     * @param secondProfile Second player's profile
     * @return true if match was successfully created
     */
    private boolean createSoloMatch(Queue queue, Player firstPlayer, Player secondPlayer,
                                    QueueProfile firstProfile, QueueProfile secondProfile) {
        GamePlayerList gamePlayerList = getGamePlayerList(firstPlayer, secondPlayer, firstProfile, secondProfile);
        GameParticipantList gameParticipantList = getSoloGameParticipantList(gamePlayerList);

        Arena arena = this.getArena(queue);
        if (!isArenaAvailable(arena, Arrays.asList(firstPlayer, secondPlayer), queue)) {
            return false;
        }

        MatchService matchService = Alley.getInstance().getService(MatchService.class);
        matchService.createAndStartMatch(
                queue.getKit(), arena, gameParticipantList.participantA, gameParticipantList.participantB,
                false, true, queue.isRanked()
        );

        this.clearQueueProfiles(queue, Arrays.asList(firstProfile.getUuid(), secondProfile.getUuid()), false);
        return true;
    }

    /**
     * Processes duos queue matchmaking by attempting different team compositions:
     * 1. Two full parties (2v2)
     * 2. One full party vs two solo players
     * 3. Four solo players
     *
     * @param queue The duos queue to process
     */
    private void processDuosQueue(Queue queue) {
        if (queue.getProfiles().size() < 2 && queue.getTotalPlayerCount() < 4) {
            return;
        }

        List<QueueProfile> availableProfiles = new ArrayList<>(queue.getProfiles());
        availableProfiles.sort(Comparator.comparingLong(QueueProfile::getElapsedTime));

        List<QueueProfile> fullParties = getFullParties(availableProfiles);
        List<QueueProfile> soloDuosPlayers = getSoloDuosPlayers(availableProfiles);

        attemptDuosMatching(queue, fullParties, soloDuosPlayers);
    }

    /**
     * Gets profiles representing full parties (2 members) from available profiles.
     *
     * @param availableProfiles List of available profiles
     * @return List of profiles representing full parties
     */
    private List<QueueProfile> getFullParties(List<QueueProfile> availableProfiles) {
        PartyService partyService = Alley.getInstance().getService(PartyService.class);
        return availableProfiles.stream()
                .filter(qp -> {
                    Player leader = Bukkit.getPlayer(qp.getUuid());
                    if (leader == null) return false;
                    Party party = partyService.getPartyByLeader(leader);
                    return party != null && party.getMembers().size() == 2;
                })
                .collect(Collectors.toList());
    }

    /**
     * Gets profiles representing solo players or single-member parties for duos queue.
     *
     * @param availableProfiles List of available profiles
     * @return List of solo duos players
     */
    private List<QueueProfile> getSoloDuosPlayers(List<QueueProfile> availableProfiles) {
        PartyService partyService = Alley.getInstance().getService(PartyService.class);
        return availableProfiles.stream()
                .filter(qp -> {
                    Player player = Bukkit.getPlayer(qp.getUuid());
                    if (player == null) return false;
                    Party party = partyService.getPartyByLeader(player);
                    return party == null || party.getMembers().size() == 1;
                })
                .collect(Collectors.toList());
    }

    /**
     * Attempts various duos matching strategies in order of preference.
     *
     * @param queue           The duos queue
     * @param fullParties     List of full parties
     * @param soloDuosPlayers List of solo duos players
     */
    private void attemptDuosMatching(Queue queue, List<QueueProfile> fullParties, List<QueueProfile> soloDuosPlayers) {
        // Strategy 1: Match two full parties (2v2)
        if (fullParties.size() >= 2) {
            QueueProfile team1LeaderProfile = fullParties.get(0);
            QueueProfile team2LeaderProfile = fullParties.get(1);

            if (tryMatchDuos(queue, team1LeaderProfile, team2LeaderProfile)) {
                return;
            }
        }

        // Strategy 2: Match one full party with two solo-duo players
        if (!fullParties.isEmpty() && soloDuosPlayers.size() >= 2) {
            QueueProfile partyLeaderProfile = fullParties.get(0);
            QueueProfile soloDuo1Profile = soloDuosPlayers.get(0);
            QueueProfile soloDuo2Profile = soloDuosPlayers.get(1);

            if (tryMatchDuos(queue, partyLeaderProfile, soloDuo1Profile, soloDuo2Profile)) {
                return;
            }
        }

        // Strategy 3: Match four solo-duo players
        if (soloDuosPlayers.size() >= 4) {
            QueueProfile soloDuo1Profile = soloDuosPlayers.get(0);
            QueueProfile soloDuo2Profile = soloDuosPlayers.get(1);
            QueueProfile soloDuo3Profile = soloDuosPlayers.get(2);
            QueueProfile soloDuo4Profile = soloDuosPlayers.get(3);

            tryMatchDuos(queue, soloDuo1Profile, soloDuo2Profile, soloDuo3Profile, soloDuo4Profile);
        }
    }

    /**
     * Attempts to create a duos match with the given potential players.
     * Supports 2-4 potential players for different team compositions.
     *
     * @param queue            The duos queue
     * @param potentialPlayers Array of potential players for the match
     * @return true if match was successfully created
     */
    private boolean tryMatchDuos(Queue queue, QueueProfile... potentialPlayers) {
        if (potentialPlayers.length < 2 || potentialPlayers.length > 4) {
            Logger.error("Invalid number of potential players for tryMatchDuos: " + potentialPlayers.length);
            return false;
        }

        List<Player> onlinePlayers = new ArrayList<>();
        List<QueueProfile> validQueueProfiles = new ArrayList<>();

        if (!validatePotentialPlayers(potentialPlayers, onlinePlayers, validQueueProfiles)) {
            return false;
        }

        List<Player> allMatchPlayers = buildMatchPlayerList(onlinePlayers, potentialPlayers.length);

        if (allMatchPlayers.size() != 4) {
            Logger.info("Expected exactly 4 players for duos match, but got: " + allMatchPlayers.size());
            return false;
        }

        return createDuosMatch(queue, allMatchPlayers, validQueueProfiles, onlinePlayers, potentialPlayers.length);
    }

    /**
     * Validates that all potential players are online and ready for match creation.
     *
     * @param potentialPlayers   Array of potential players
     * @param onlinePlayers      Output list for validated online players
     * @param validQueueProfiles Output list for validated queue profiles
     * @return true if all players are valid
     */
    private boolean validatePotentialPlayers(QueueProfile[] potentialPlayers, List<Player> onlinePlayers, List<QueueProfile> validQueueProfiles) {
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        for (QueueProfile qp : potentialPlayers) {
            Player p = Bukkit.getPlayer(qp.getUuid());
            if (p == null || !p.isOnline() || !profileService.getProfile(p.getUniqueId()).getState().equals(ProfileState.WAITING)) {
                Logger.info("One of the potential players is not ready: " + qp.getUuid());
                return false;
            }
            onlinePlayers.add(p);
            validQueueProfiles.add(qp);
        }
        return true;
    }

    /**
     * Builds the complete list of players for the duos match by including
     * party members and handling different team compositions.
     *
     * @param onlinePlayers        List of validated online players
     * @param potentialPlayerCount Number of potential players
     * @return Complete list of match players
     */
    private List<Player> buildMatchPlayerList(List<Player> onlinePlayers, int potentialPlayerCount) {
        List<Player> allMatchPlayers = new ArrayList<>();

        Player team1Leader = onlinePlayers.get(0);
        allMatchPlayers.add(team1Leader);

        // Add team 1 party members
        addPartyMembersToMatch(team1Leader, allMatchPlayers);

        // Determine team 2 leader position based on potential player count
        int team2StartIndex = (potentialPlayerCount == 2) ? 1 : 2;
        Player team2Leader = onlinePlayers.get(team2StartIndex);
        allMatchPlayers.add(team2Leader);

        // Add team 2 party members
        addPartyMembersToMatch(team2Leader, allMatchPlayers);

        // Add remaining solo players
        addRemainingSoloPlayers(onlinePlayers, allMatchPlayers, team2StartIndex);

        return allMatchPlayers;
    }

    /**
     * Adds party members of a leader to the match player list.
     *
     * @param leader          The party leader
     * @param allMatchPlayers List to add party members to
     */
    private void addPartyMembersToMatch(Player leader, List<Player> allMatchPlayers) {
        PartyService partyService = Alley.getInstance().getService(PartyService.class);
        Party party = partyService.getPartyByLeader(leader);
        if (party != null && party.getMembers().size() == 2) {
            party.getMembers().stream()
                    .filter(uuid -> !uuid.equals(leader.getUniqueId()))
                    .findFirst()
                    .ifPresent(memberUUID -> {
                        Player memberPlayer = Bukkit.getPlayer(memberUUID);
                        if (memberPlayer != null && memberPlayer.isOnline()) {
                            allMatchPlayers.add(memberPlayer);
                        }
                    });
        }
    }

    /**
     * Adds remaining solo players to the match that aren't already included.
     *
     * @param onlinePlayers   List of all online players
     * @param allMatchPlayers Current match players list
     * @param team2StartIndex Index where team 2 starts
     */
    private void addRemainingSoloPlayers(List<Player> onlinePlayers,
                                         List<Player> allMatchPlayers,
                                         int team2StartIndex) {
        for (int i = 1; i < onlinePlayers.size(); i++) {
            if (i == team2StartIndex) continue;

            Player soloPlayer = onlinePlayers.get(i);
            if (!allMatchPlayers.contains(soloPlayer)) {
                allMatchPlayers.add(soloPlayer);
            }
        }
    }

    /**
     * Creates the actual duos match with proper team assignments.
     *
     * @param queue                The duos queue
     * @param allMatchPlayers      All players in the match
     * @param validQueueProfiles   Valid queue profiles
     * @param onlinePlayers        Online players list
     * @param potentialPlayerCount Number of potential players
     * @return true if match was successfully created
     */
    private boolean createDuosMatch(Queue queue, List<Player> allMatchPlayers,
                                    List<QueueProfile> validQueueProfiles,
                                    List<Player> onlinePlayers,
                                    int potentialPlayerCount) {
        int team2StartIndex = (potentialPlayerCount == 2) ? 1 : 2;
        Player team1Leader = onlinePlayers.get(0);
        Player team2Leader = onlinePlayers.get(team2StartIndex);

        QueueProfile team1LeaderProfile = validQueueProfiles.get(0);
        QueueProfile team2LeaderProfile = validQueueProfiles.get(team2StartIndex);

        GameParticipant<MatchGamePlayerImpl> participantA = createTeamParticipant(team1Leader, team1LeaderProfile);
        GameParticipant<MatchGamePlayerImpl> participantB = createTeamParticipant(team2Leader, team2LeaderProfile);

        assignPlayersToTeams(allMatchPlayers, validQueueProfiles, team1Leader, team2Leader, participantA, participantB);

        if (participantA.getPlayerSize() != 2 || participantB.getPlayerSize() != 2) {
            Logger.info("Teams don't have exactly 2 players each. Team A: " + participantA.getPlayerSize() + " Team B: " + participantB.getPlayerSize());
            return false;
        }

        Arena arena = this.getArena(queue);
        if (!isArenaAvailable(arena, allMatchPlayers, queue)) {
            List<UUID> allUUIDsToRemove = allMatchPlayers.stream()
                    .map(Player::getUniqueId)
                    .collect(Collectors.toList());
            clearQueueProfiles(queue, allUUIDsToRemove, true);
            return false;
        }

        MatchService matchService = Alley.getInstance().getService(MatchService.class);
        matchService.createAndStartMatch(
                queue.getKit(), arena, participantA, participantB, true, false, queue.isRanked()
        );

        List<UUID> allUUIDsToRemove = allMatchPlayers.stream()
                .map(Player::getUniqueId)
                .collect(Collectors.toList());
        clearQueueProfiles(queue, allUUIDsToRemove, false);
        return true;
    }

    /**
     * Creates a team participant with the given leader.
     *
     * @param leader        The team leader
     * @param leaderProfile The leader's queue profile
     * @return Team game participant
     */
    private GameParticipant<MatchGamePlayerImpl> createTeamParticipant(Player leader, QueueProfile leaderProfile) {
        MatchGamePlayerImpl gameLeader = new MatchGamePlayerImpl(leader.getUniqueId(), leader.getName(), leaderProfile.getElo());
        return new TeamGameParticipant<>(gameLeader);
    }

    /**
     * Assigns all match players to their appropriate teams based on party membership.
     *
     * @param allMatchPlayers    All players in the match
     * @param validQueueProfiles Valid queue profiles
     * @param team1Leader        Team 1 leader
     * @param team2Leader        Team 2 leader
     * @param participantA       Team A participant
     * @param participantB       Team B participant
     */
    private void assignPlayersToTeams(List<Player> allMatchPlayers,
                                      List<QueueProfile> validQueueProfiles,
                                      Player team1Leader, Player team2Leader,
                                      GameParticipant<MatchGamePlayerImpl> participantA,
                                      GameParticipant<MatchGamePlayerImpl> participantB) {
        PartyService partyService = Alley.getInstance().getService(PartyService.class);
        Party team1Party = partyService.getPartyByLeader(team1Leader);
        Party team2Party = partyService.getPartyByLeader(team2Leader);

        for (Player player : allMatchPlayers) {
            if (player.equals(team1Leader) || player.equals(team2Leader)) {
                continue;
            }

            int playerElo = getPlayerElo(player, validQueueProfiles);
            MatchGamePlayerImpl gamePlayer = new MatchGamePlayerImpl(player.getUniqueId(), player.getName(), playerElo);

            assignPlayerToTeam(player, gamePlayer, team1Party, team2Party, participantA, participantB);
        }
    }

    /**
     * Gets the ELO rating for a player from the queue profiles.
     *
     * @param player             The player
     * @param validQueueProfiles List of valid queue profiles
     * @return Player's ELO rating or default value
     */
    private int getPlayerElo(Player player, List<QueueProfile> validQueueProfiles) {
        return validQueueProfiles.stream()
                .filter(queueProfile -> queueProfile.getUuid().equals(player.getUniqueId()))
                .findFirst()
                .map(QueueProfile::getElo)
                .orElse(1000);
    }

    /**
     * Assigns a single player to the appropriate team based on party membership.
     *
     * @param player       The player to assign
     * @param gamePlayer   The game player implementation
     * @param team1Party   Team 1's party
     * @param team2Party   Team 2's party
     * @param participantA Team A participant
     * @param participantB Team B participant
     */
    private void assignPlayerToTeam(Player player, MatchGamePlayerImpl gamePlayer,
                                    Party team1Party, Party team2Party,
                                    GameParticipant<MatchGamePlayerImpl> participantA,
                                    GameParticipant<MatchGamePlayerImpl> participantB) {
        if (team1Party != null && team1Party.getMembers().contains(player.getUniqueId())) {
            participantA.addPlayer(gamePlayer);
        } else if (team2Party != null && team2Party.getMembers().contains(player.getUniqueId())) {
            participantB.addPlayer(gamePlayer);
        } else {
            if (participantA.getPlayerSize() < participantB.getPlayerSize()) {
                participantA.addPlayer(gamePlayer);
            } else {
                participantB.addPlayer(gamePlayer);
            }
        }
    }

    /**
     * Checks if an arena is available and suitable for the match.
     *
     * @param arena   The arena to check
     * @param players List of players for error messaging
     * @param queue   The queue for removing players on failure
     * @return true if arena is available and suitable
     */
    private boolean isArenaAvailable(Arena arena, List<Player> players, Queue queue) {
        if (arena == null || arena.getType().equals(ArenaType.FFA)) {
            players.forEach(p -> p.sendMessage(CC.translate("&cThere are no available arenas for this kit")));
            return false;
        }
        return true;
    }

    /**
     * Retrieves a random available arena for the given queue's kit.
     *
     * @param queue The queue requesting an arena
     * @return An available arena or null if none found
     */
    private Arena getArena(Queue queue) {
        ArenaService arenaService = Alley.getInstance().getService(ArenaService.class);
        return arenaService.getRandomArena(queue.getKit());
    }

    /**
     * Clears queue profiles for multiple players after a match has been created.
     * This method handles all cleanup including updating player profiles and
     * removing entries from the queue list.
     *
     * @param queue       The queue to clean up
     * @param playerUUIDs The UUIDs of all players who were placed in the match
     * @param removeQueue Whether to call queue.removePlayer() for cleanup
     */
    public void clearQueueProfiles(Queue queue, List<UUID> playerUUIDs, boolean removeQueue) {
        Set<QueueProfile> uniqueProfiles = getUniqueQueueProfiles(playerUUIDs);

        for (QueueProfile queueProfile : uniqueProfiles) {
            if (removeQueue) {
                queue.removePlayer(queueProfile);
            } else {
                performProfileCleanup(queue, queueProfile);
            }
        }
    }

    /**
     * Gets unique queue profiles for the given player UUIDs.
     *
     * @param playerUUIDs List of player UUIDs
     * @return Set of unique queue profiles
     */
    private Set<QueueProfile> getUniqueQueueProfiles(List<UUID> playerUUIDs) {
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Set<QueueProfile> uniqueProfiles = new HashSet<>();
        for (UUID uuid : playerUUIDs) {
            Profile profile = profileService.getProfile(uuid);
            if (profile != null && profile.getQueueProfile() != null) {
                uniqueProfiles.add(profile.getQueueProfile());
            }
        }
        return uniqueProfiles;
    }

    /**
     * Performs cleanup for a specific queue profile, including party members if applicable.
     *
     * @param queue        The queue being cleaned up
     * @param queueProfile The queue profile to clean up
     */
    private void performProfileCleanup(Queue queue, QueueProfile queueProfile) {
        Player leader = Bukkit.getPlayer(queueProfile.getUuid());
        if (leader == null) return;

        PartyService partyService = Alley.getInstance().getService(PartyService.class);
        Party party = partyService.getParty(leader);
        List<UUID> membersToClean = getMembersToClean(queue, party, leader);

        cleanupPlayerProfiles(membersToClean);
        queue.getProfiles().remove(queueProfile);
    }

    /**
     * Gets the list of member UUIDs that need profile cleanup.
     *
     * @param queue  The queue being processed
     * @param party  The party (can be null)
     * @param leader The leader player
     * @return List of UUIDs to clean up
     */
    private List<UUID> getMembersToClean(Queue queue, Party party, Player leader) {
        List<UUID> membersToClean = new ArrayList<>();
        if (queue.isDuos() && party != null) {
            membersToClean.addAll(party.getMembers());
        } else {
            membersToClean.add(leader.getUniqueId());
        }
        return membersToClean;
    }

    /**
     * Cleans up profiles for the given member UUIDs by setting their queue profile to null.
     *
     * @param memberUUIDs List of member UUIDs to clean up
     */
    private void cleanupPlayerProfiles(List<UUID> memberUUIDs) {
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        for (UUID memberId : memberUUIDs) {
            Profile memberProfile = profileService.getProfile(memberId);
            if (memberProfile != null) {
                memberProfile.setQueueProfile(null);
            }
        }
    }

    /**
     * Creates game participant list for solo matches.
     *
     * @param gamePlayerList The game player list containing both players
     * @return Game participant list with individual participants
     */
    private @NotNull GameParticipantList getSoloGameParticipantList(GamePlayerList gamePlayerList) {
        return new GameParticipantList(
                new GameParticipant<>(gamePlayerList.getFirstMatchGamePlayer()),
                new GameParticipant<>(gamePlayerList.getSecondMatchGamePlayer())
        );
    }

    /**
     * Creates a game player list from two players and their profiles.
     *
     * @param firstPlayer   The first player
     * @param secondPlayer  The second player
     * @param firstProfile  The first player's queue profile
     * @param secondProfile The second player's queue profile
     * @return Game player list containing both match game players
     */
    private @NotNull GamePlayerList getGamePlayerList(Player firstPlayer, Player secondPlayer,
                                                      QueueProfile firstProfile, QueueProfile secondProfile) {
        return new GamePlayerList(
                new MatchGamePlayerImpl(firstPlayer.getUniqueId(), firstPlayer.getName(), firstProfile.getElo()),
                new MatchGamePlayerImpl(secondPlayer.getUniqueId(), secondPlayer.getName(), secondProfile.getElo())
        );
    }

    /**
     * Data class representing a pair of game participants for match creation.
     * Contains two participants that will compete against each other.
     */
    @Getter
    private static class GameParticipantList {
        /**
         * The first participant in the match
         */
        public final GameParticipant<MatchGamePlayerImpl> participantA;
        /**
         * The second participant in the match
         */
        public final GameParticipant<MatchGamePlayerImpl> participantB;

        /**
         * Constructor for the GameParticipantList class.
         *
         * @param participantA The first participant
         * @param participantB The second participant
         */
        public GameParticipantList(GameParticipant<MatchGamePlayerImpl> participantA, GameParticipant<MatchGamePlayerImpl> participantB) {
            this.participantA = participantA;
            this.participantB = participantB;
        }

        /**
         * Gets both participants as a list for easier iteration.
         *
         * @return List containing both participants
         */
        public List<GameParticipant<MatchGamePlayerImpl>> getParticipants() {
            return Arrays.asList(this.participantA, this.participantB);
        }
    }

    /**
     * Data class representing a pair of match game players.
     * Used for organizing player data before creating participants.
     */
    @Getter
    private static class GamePlayerList {
        /**
         * The first match game player
         */
        public final MatchGamePlayerImpl firstMatchGamePlayer;
        /**
         * The second match game player
         */
        public final MatchGamePlayerImpl secondMatchGamePlayer;

        /**
         * Constructor for the GamePlayerList class.
         *
         * @param firstMatchGamePlayer  The first match game player
         * @param secondMatchGamePlayer The second match game player
         */
        public GamePlayerList(MatchGamePlayerImpl firstMatchGamePlayer, MatchGamePlayerImpl secondMatchGamePlayer) {
            this.firstMatchGamePlayer = firstMatchGamePlayer;
            this.secondMatchGamePlayer = secondMatchGamePlayer;
        }
    }
}