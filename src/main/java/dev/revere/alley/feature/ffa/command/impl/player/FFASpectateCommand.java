package dev.revere.alley.feature.ffa.command.impl.player;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.ErrorLocaleImpl;
import dev.revere.alley.core.locale.internal.impl.command.FFALocaleImpl;
import dev.revere.alley.core.locale.internal.impl.command.KitLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.feature.ffa.FFAMatch;
import dev.revere.alley.feature.ffa.FFAService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 04/06/2025
 */
public class FFASpectateCommand extends BaseCommand {
    @CommandData(
            name = "ffa.spectate",
            usage = "spectate <ffaKit>",
            aliases = {"specffa", "spectateffa"},
            description = "Spectate a FFA match"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/spectateffa &6<ffaKit>"));
            return;
        }

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.LOBBY) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.MUST_BE_IN_LOBBY));
            return;
        }

        String ffaKitName = args[0];
        FFAService ffaService = this.plugin.getService(FFAService.class);
        Kit ffaKit = ffaService.getFfaKits().stream()
                .filter(kit -> kit.getName().equalsIgnoreCase(ffaKitName))
                .findFirst()
                .orElse(null);

        if (ffaKit == null) {
            player.sendMessage(this.getMessage(KitLocaleImpl.NOT_FOUND).replace("{kit-name}", ffaKitName));
            return;
        }

        if (!ffaKit.isFfaEnabled()) {
            //should never happen, but just in case
            player.sendMessage(this.getMessage(FFALocaleImpl.DISABLED));
            return;
        }

        FFAMatch match = ffaService.getMatches().stream()
                .filter(m -> m.getKit().equals(ffaKit))
                .findFirst()
                .orElse(null);

        if (match == null) {
            player.sendMessage(this.getMessage(FFALocaleImpl.NOT_FOUND).replace("{ffa-name}", ffaKitName));
            return;
        }

        if (match.getSpectators().contains(player.getUniqueId())) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.ALREADY_SPECTATING_FFA));
            return;
        }

        match.addSpectator(player);
    }
}