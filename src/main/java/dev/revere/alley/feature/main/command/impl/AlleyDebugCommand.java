package dev.revere.alley.feature.main.command.impl;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.combat.CombatService;
import dev.revere.alley.feature.cooldown.CooldownService;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.feature.kit.setting.KitSettingService;
import dev.revere.alley.feature.queue.QueueService;
import dev.revere.alley.feature.emoji.EmojiService;
import dev.revere.alley.feature.duel.DuelRequestService;
import dev.revere.alley.feature.match.MatchService;
import dev.revere.alley.feature.match.snapshot.SnapshotService;
import dev.revere.alley.feature.party.PartyService;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

/**
 * @author Emmy
 * @project Alley
 * @date 30/05/2024 - 12:15
 */
public class AlleyDebugCommand extends BaseCommand {

    @CommandData(
            name = "alley.debug",
            isAdminOnly = true,
            usage = "alley debug <memory/instance/profile/profileData>",
            description = "Displays debug information for development purposes."
    )

    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();
        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        switch (args[0]) {
            case "memory":
                this.sendMemoryInfo(player);
                break;
            case "instance":
                this.sendInstanceInfo(player);
                break;
            case "profile":
                this.sendProfileInfo(profile, player);
                break;
            case "profiledata":
                this.sendProfileData(profile, player);
                break;
            default:
                command.sendUsage();
                break;
        }
    }

    /**
     * Sends memory information to the player.
     *
     * @param player The player to send the information to.
     */
    private void sendMemoryInfo(Player player) {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = allocatedMemory - freeMemory;

        Arrays.asList(
                "",
                "     &6&lAlley &7│ &fMemory Information",
                "      &6&l│ &fMax Memory: &6" + this.formatNumber((int) (maxMemory / 1024 / 1024)) + "MB",
                "      &6&l│ &fAllocated Memory: &6" + this.formatNumber((int) (allocatedMemory / 1024 / 1024)) + "MB",
                "      &6&l│ &fFree Memory: &6" + this.formatNumber((int) (freeMemory / 1024 / 1024)) + "MB",
                "      &6&l│ &fUsed Memory: &6" + this.formatNumber((int) (usedMemory / 1024 / 1024)) + "MB",
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }

    /**
     * Sends instance information to the player.
     *
     * @param player The player to send the information to.
     */
    private void sendInstanceInfo(Player player) {
        Arrays.asList(
                "",
                "     &6&lAlley &7│ &fInstance Information",
                "      &6&l│ &fProfiles: &6" + this.formatNumber(this.plugin.getService(ProfileService.class).getProfiles().size()),
                "      &6&l│ &fMatches: &6" + this.formatNumber(this.plugin.getService(MatchService.class).getMatches().size()),
                "      &6&l│ &fQueues: &6" + this.formatNumber(this.plugin.getService(QueueService.class).getQueues().size()),
                "      &6&l│ &fQueue profiles: &6" + this.formatNumber(Arrays.stream(this.plugin.getService(QueueService.class).getQueues().stream().mapToInt(queue -> queue.getProfiles().size()).toArray()).sum()),
                "      &6&l│ &fCooldowns: &6" + this.formatNumber(this.plugin.getService(CooldownService.class).getCooldowns().size()),
                "      &6&l│ &fActive Cooldowns: &6" + this.formatNumber((int) this.plugin.getService(CooldownService.class).getCooldowns().stream().filter(cooldown -> cooldown.getC().isActive()).count()),
                "      &6&l│ &fCombats: &6" + this.formatNumber(this.plugin.getService(CombatService.class).getCombatMap().size()),
                "      &6&l│ &fKits: &6" + this.formatNumber(this.plugin.getService(KitService.class).getKits().size()),
                "      &6&l│ &fKit Settings: &6" + this.formatNumber(this.plugin.getService(KitSettingService.class).getSettings().size()),
                "      &6&l│ &fParties: &6" + this.formatNumber(this.plugin.getService(PartyService.class).getParties().size()),
                "      &6&l│ &fArenas: &6" + this.formatNumber(this.plugin.getService(ArenaService.class).getArenas().size()),
                "      &6&l│ &fSnapshots: &6" + this.formatNumber(this.plugin.getService(SnapshotService.class).getSnapshots().size()),
                "      &6&l│ &fDuel Requests: &6" + this.formatNumber(this.plugin.getService(DuelRequestService.class).getDuelRequests().size()),
                "      &6&l│ &fEmojis: &6" + this.formatNumber(this.plugin.getService(EmojiService.class).getEmojis().size()),
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }

    /**
     * Sends profile information to the player.
     *
     * @param profile The profile to send the information for.
     * @param player  The player to send the information to.
     */
    private void sendProfileInfo(Profile profile, Player player) {
        String banned = profile.getProfileData().isRankedBanned() ? "&c&lBANNED" : "&a&lNOT BANNED";
        Arrays.asList(
                "",
                "     &6&lProfile &7│ &f" + profile.getName(),
                "      &6&l│ &fUUID: &6" + profile.getUuid(),
                "      &6&l│ &fElo: &6" + this.formatNumber(profile.getProfileData().getElo()),
                "      &6&l│ &fCoins: &6" + this.formatNumber(profile.getProfileData().getCoins()),
                "      &6&l│ &fState: &6" + profile.getState() + " &7(" + profile.getState().getDescription() + ")",
                "      &6&l│ &fQueue Profile: &6" + (profile.getQueueProfile() != null ? profile.getQueueProfile().getQueue().getKit().getName() : "&c&lNULL"),
                "      &6&l│ &fRanked: &6" + banned,
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line).replace("The player", profile.getName())));
    }

    /**
     * Sends profile data to the player.
     *
     * @param profile The profile to send the data for.
     * @param player  The player to send the data to.
     */
    private void sendProfileData(Profile profile, Player player) {
        Arrays.asList(
                "",
                "     &6&lProfile Data &7│ &f" + profile.getName(),
                "      &6&l│ &fUnranked Wins: &6" + this.formatNumber(profile.getProfileData().getUnrankedWins()),
                "      &6&l│ &fUnranked Losses: &6" + this.formatNumber(profile.getProfileData().getUnrankedLosses()),
                "      &6&l│ &fRanked Wins: &6" + this.formatNumber(profile.getProfileData().getRankedWins()),
                "      &6&l│ &fRanked Losses: &6" + this.formatNumber(profile.getProfileData().getRankedLosses()),
                "      &6&l│ &fTotal FFA Kills: &6" + this.formatNumber(profile.getProfileData().getTotalFFAKills()),
                "      &6&l│ &fTotal FFA Deaths: &6" + this.formatNumber(profile.getProfileData().getTotalFFADeaths()),
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));
    }

    /**
     * Formats a number with commas.
     *
     * @param number The number to format
     * @return The formatted number
     */
    private String formatNumber(int number) {
        return NumberFormat.getInstance(Locale.US).format(number);
    }
}