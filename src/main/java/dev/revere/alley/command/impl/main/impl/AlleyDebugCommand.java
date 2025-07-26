package dev.revere.alley.command.impl.main.impl;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.arena.ArenaService;
import dev.revere.alley.base.combat.CombatService;
import dev.revere.alley.base.cooldown.CooldownService;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.base.kit.setting.KitSettingService;
import dev.revere.alley.base.queue.QueueService;
import dev.revere.alley.feature.emoji.EmojiService;
import dev.revere.alley.game.duel.DuelRequestService;
import dev.revere.alley.game.match.MatchService;
import dev.revere.alley.game.match.snapshot.SnapshotService;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
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

    @Override
    @CommandData(name = "alley.debug", isAdminOnly = true, usage = "/alley debug <memory/instance/profile/profileData>", description = "Displays debug information for development purposes.")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();
        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/alley debug &6<memory/instance/profile/profiledata>"));
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
                player.sendMessage(CC.translate("&6Usage: &e/alley debug &6<memory/instance/profile/profileData>"));
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
                "      &f│ Max Memory: &6" + this.formatNumber((int) (maxMemory / 1024 / 1024)) + "MB",
                "      &f│ Allocated Memory: &6" + this.formatNumber((int) (allocatedMemory / 1024 / 1024)) + "MB",
                "      &f│ Free Memory: &6" + this.formatNumber((int) (freeMemory / 1024 / 1024)) + "MB",
                "      &f│ Used Memory: &6" + this.formatNumber((int) (usedMemory / 1024 / 1024)) + "MB",
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
                "      &f│ Profiles: &6" + this.formatNumber(this.plugin.getService(ProfileService.class).getProfiles().size()),
                "      &f│ Matches: &6" + this.formatNumber(this.plugin.getService(MatchService.class).getMatches().size()),
                "      &f│ Queues: &6" + this.formatNumber(this.plugin.getService(QueueService.class).getQueues().size()),
                "      &f│ Queue profiles: &6" + this.formatNumber(Arrays.stream(this.plugin.getService(QueueService.class).getQueues().stream().mapToInt(queue -> queue.getProfiles().size()).toArray()).sum()),
                "      &f│ Cooldowns: &6" + this.formatNumber(this.plugin.getService(CooldownService.class).getCooldowns().size()),
                "      &f│ Active Cooldowns: &6" + this.formatNumber((int) this.plugin.getService(CooldownService.class).getCooldowns().stream().filter(cooldown -> cooldown.getC().isActive()).count()),
                "      &f│ Combats: &6" + this.formatNumber(this.plugin.getService(CombatService.class).getCombatMap().size()),
                "      &f│ Kits: &6" + this.formatNumber(this.plugin.getService(KitService.class).getKits().size()),
                "      &f│ Kit Settings: &6" + this.formatNumber(this.plugin.getService(KitSettingService.class).getSettings().size()),
                "      &f│ Parties: &6" + this.formatNumber(this.plugin.getService(PartyService.class).getParties().size()),
                "      &f│ Arenas: &6" + this.formatNumber(this.plugin.getService(ArenaService.class).getArenas().size()),
                "      &f│ Snapshots: &6" + this.formatNumber(this.plugin.getService(SnapshotService.class).getSnapshots().size()),
                "      &f│ Duel Requests: &6" + this.formatNumber(this.plugin.getService(DuelRequestService.class).getDuelRequests().size()),
                "      &f│ Emojis: &6" + this.formatNumber(this.plugin.getService(EmojiService.class).getEmojis().size()),
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
                "      &f│ UUID: &6" + profile.getUuid(),
                "      &f│ Elo: &6" + this.formatNumber(profile.getProfileData().getElo()),
                "      &f│ Coins: &6" + this.formatNumber(profile.getProfileData().getCoins()),
                "      &f│ State: &6" + profile.getState() + " &7(" + profile.getState().getDescription() + ")",
                "      &f│ Queue Profile: &6" + (profile.getQueueProfile() != null ? profile.getQueueProfile().getQueue().getKit().getName() : "&c&lNULL"),
                "      &f│ Ranked: &6" + banned,
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
                "      &f│ Unranked Wins: &6" + this.formatNumber(profile.getProfileData().getUnrankedWins()),
                "      &f│ Unranked Losses: &6" + this.formatNumber(profile.getProfileData().getUnrankedLosses()),
                "      &f│ Ranked Wins: &6" + this.formatNumber(profile.getProfileData().getRankedWins()),
                "      &f│ Ranked Losses: &6" + this.formatNumber(profile.getProfileData().getRankedLosses()),
                "      &f│ Total FFA Kills: &6" + this.formatNumber(profile.getProfileData().getTotalFFAKills()),
                "      &f│ Total FFA Deaths: &6" + this.formatNumber(profile.getProfileData().getTotalFFADeaths()),
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