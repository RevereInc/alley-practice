package dev.revere.alley.task;

import dev.revere.alley.Alley;
import dev.revere.alley.base.combat.CombatService;
import dev.revere.alley.game.duel.DuelRequest;
import dev.revere.alley.game.duel.DuelRequestService;
import dev.revere.alley.game.match.snapshot.SnapshotService;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.game.party.PartyRequest;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 09/03/2025
 */
public class RepositoryCleanupTask extends BukkitRunnable {
    protected final Alley plugin;

    /**
     * Constructor for the RepositoryCleanupTask class.
     *
     * @param plugin the Alley plugin instance
     */
    public RepositoryCleanupTask(Alley plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        CombatService combatService = Alley.getInstance().getService(CombatService.class);
        if (!combatService.getCombatMap().isEmpty()) {
            combatService.getCombatMap().forEach((uuid, combat) -> {
                Player player = this.plugin.getServer().getPlayer(uuid);
                if (combatService.isExpired(player)) {
                    combatService.removeLastAttacker(player, false);
                }
            });
        }

        DuelRequestService duelRequestService = Alley.getInstance().getService(DuelRequestService.class);
        if (!duelRequestService.getDuelRequests().isEmpty()) {
            List<DuelRequest> expiredRequests = new ArrayList<>();
            synchronized (duelRequestService.getDuelRequests()) {
                duelRequestService.getDuelRequests().removeIf(duelRequest -> {
                    if (duelRequest.hasExpired()) {
                        expiredRequests.add(duelRequest);
                        return true;
                    }
                    return false;
                });
            }
            this.notifyDuelRequestIndividuals(expiredRequests);
        }

        SnapshotService snapshotRepository = Alley.getInstance().getService(SnapshotService.class);
        snapshotRepository.getSnapshots().entrySet().removeIf(entry -> System.currentTimeMillis() - entry.getValue().getCreatedAt() >= 60_000);

        PartyService partyService = Alley.getInstance().getService(PartyService.class);
        if (!partyService.getParties().isEmpty()) {
            List<PartyRequest> expiredRequests = new ArrayList<>();
            synchronized (partyService.getPartyRequests()) {
                partyService.getPartyRequests().removeIf(request -> {
                    if (request.hasExpired()) {
                        expiredRequests.add(request);
                        return true;
                    }
                    return false;
                });
            }
            this.notifyPartyRequestIndividuals(expiredRequests);
        }
    }

    /**
     * Notify the sender and target that the duel request has expired.
     *
     * @param expiredRequests the expired requests
     */
    private void notifyDuelRequestIndividuals(List<DuelRequest> expiredRequests) {
        expiredRequests.forEach(duelRequest -> {
            if (duelRequest.getSender() == null || duelRequest.getTarget() == null) {
                return;
            }

            duelRequest.getSender().sendMessage(CC.translate("&cYour duel request to " + duelRequest.getTarget().getName() + " has expired."));
            duelRequest.getTarget().sendMessage(CC.translate("&cThe duel request from " + duelRequest.getSender().getName() + " has expired."));
        });
    }

    /**
     * Notifies the individuals that their party request has expired.
     *
     * @param partyRequests The party requests that have expired.
     */
    private void notifyPartyRequestIndividuals(List<PartyRequest> partyRequests) {
        partyRequests.forEach(partyRequest -> {
            if (partyRequest.getSender() == null || partyRequest.getTarget() == null) {
                return;
            }

            partyRequest.getSender().sendMessage(CC.translate("&cYour party request to &6" + partyRequest.getSender().getName() + " &chas expired."));
            partyRequest.getTarget().sendMessage(CC.translate("&cThe party request from &6" + partyRequest.getTarget().getName() + " &chas expired."));
        });
    }
}