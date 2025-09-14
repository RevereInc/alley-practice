package dev.revere.alley.feature.ffa.command.impl.player;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.ErrorLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.feature.ffa.FFAService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class FFAJoinCommand extends BaseCommand {
    @CommandData(
            name = "ffa.join",
            usage = "ffa join <kit>",
            description = "Join a FFA match."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage(CC.translate("&6Usage: &e/ffa join &6<kit>"));
            return;
        }

        String kitName = args[0];
        Kit kit = this.plugin.getService(KitService.class).getKit(kitName);
        if (kit == null) {
            player.sendMessage("Kit not found.");
            return;
        }

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getParty() != null) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.MUST_LEAVE_PARTY_TO_JOIN_FFA));
            return;
        }

        if (profile.getState() != ProfileState.LOBBY) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.MUST_BE_IN_LOBBY));
            return;
        }

        FFAService ffaService = this.plugin.getService(FFAService.class);
        ffaService.getMatches().stream()
                .filter(match -> match.getKit().equals(kit))
                .filter(match -> match.getPlayers().size() < match.getMaxPlayers())
                .findFirst()
                .ifPresent(match -> match.join(player));
    }
}