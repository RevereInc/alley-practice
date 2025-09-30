package dev.revere.alley.core.profile.progress.command;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.message.GameMessagesLocaleImpl;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.progress.PlayerProgress;
import dev.revere.alley.core.profile.progress.ProgressService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/09/2025
 */
public class ProgressSimulationCommand extends BaseCommand {
    @CommandData(
            name = "alleyprogresssimulation",
            isAdminOnly = true,
            usage = "alleyprogresssimulation <kitName>",
            description = "Simulate a player's progress for a given kit for testing purposes, without needing to queue a match."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            command.sendUsage();
            return;
        }

        String kitName = args[0];
        Kit kit = AlleyPlugin.getInstance().getService(KitService.class).getKit(kitName);
        if (kit == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.KIT_NOT_FOUND));
            return;
        }
        Profile winnerProfile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());

        int winsBefore = winnerProfile.getProfileData().getUnrankedKitData().get(kit.getName()).getWins();

        winnerProfile.getProfileData().getUnrankedKitData().get(kit.getName()).incrementWins();
        winnerProfile.getProfileData().incrementUnrankedWins();
        winnerProfile.getProfileData().determineTitles();

        int winsAfter = winnerProfile.getProfileData().getUnrankedKitData().get(kit.getName()).getWins();

        player.sendMessage("Simulated match win. Wins before: " + winsBefore + ", Wins after: " + winsAfter);
        player.sendMessage("");

        ProgressService progressService = AlleyPlugin.getInstance().getService(ProgressService.class);
        PlayerProgress progress = progressService.calculateProgress(winnerProfile, kit.getName());

        LocaleService localeService = this.plugin.getService(LocaleService.class);
        if (!localeService.getBoolean(GameMessagesLocaleImpl.MATCH_DIVISION_PROGRESS_ENABLED_BOOLEAN)) {
            return;
        }

        List<String> message;

        if (progress.getCurrentWins() == progress.getCurrentTier().getRequiredWins()) {
            message = localeService.getStringList(GameMessagesLocaleImpl.MATCH_DIVISION_PROGRESS_REACHED_FORMAT)
                    .stream()
                    .map(line -> line.replace("{reached-new-division}", progress.getCurrentDivision().getName() + " " + progress.getCurrentTier().getName()))
                    .collect(java.util.stream.Collectors.toList());
        } else if (progress.getRemainingWins() != -1) {
            message = localeService.getStringList(GameMessagesLocaleImpl.MATCH_DIVISION_PROGRESS_ONGOING_FORMAT);
        } else {
            message = Collections.singletonList("&cYou absolute fucking piece of no life, how did you even get here?");
        }

        message.replaceAll(string -> string
                .replace("{next-division}", winnerProfile.getNextDivisionAndTier(kit.getName()))
                .replace("{wins-required}", String.valueOf(progress.getRemainingWins()))
                .replace("{win-or-wins}", progress.getRemainingWins() == 1 ? "win" : "wins")
                .replace("{progress-bar}", progress.getProgressBar(12, "■"))
                .replace("{progress-percentage}", progress.getProgressPercentage())
                .replace("{daily-streak}", "N/A")
                .replace("{best-daily-streak}", "N/A")
                .replace("{win-streak}", String.valueOf(winnerProfile.getProfileData().getUnrankedKitData().get(kit.getName()).getWinstreak()))
                .replace("{best-win-streak}", "N/A")
        );

        message.forEach(line -> player.sendMessage(CC.translate(line)));
    }
}
