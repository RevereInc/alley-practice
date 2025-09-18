package dev.revere.alley.feature.ffa.command.impl.player;

import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class FFASpawnCommand extends BaseCommand {
    @CommandData(
            name = "ffa.spawn",
            usage = "ffa spawn",
            description = "Teleport to the ffa safe zone."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() != ProfileState.FFA) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.FFA_NOT_IN_A_MATCH));
            return;
        }

        profile.getFfaMatch().teleportToSafeZone(player);
    }
}