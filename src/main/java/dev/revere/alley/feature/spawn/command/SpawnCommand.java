package dev.revere.alley.feature.spawn.command;

import dev.revere.alley.core.config.internal.locale.impl.ServerLocale;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.feature.spawn.SpawnService;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.feature.ffa.FFAMatch;
import dev.revere.alley.feature.ffa.FFAState;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.common.PlayerUtil;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 29/04/2024 - 19:01
 */
public class SpawnCommand extends BaseCommand {
    @Override
    @CommandData(name = "spawn", isAdminOnly = true)
    public void onCommand(CommandArgs args) {
        Player player = args.getPlayer();
        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        ProfileState state = profile.getState();

        switch (state) {
            case FFA:
                FFAMatch ffaMatch = profile.getFfaMatch();
                if (ffaMatch == null) return;

                if (ffaMatch.getGameFFAPlayer(player).getState() == FFAState.FIGHTING) {
                    ffaMatch.teleportToSafeZone(player);
                } else {
                    ffaMatch.leave(player);
                }
                break;
            case PLAYING:
                player.sendMessage(CC.translate("&cYou cannot do this right now."));
                break;
            case SPECTATING:
                profile.getMatch().removeSpectator(player, false);
                break;
            default:
                this.sendToSpawn(player);
                break;
        }
    }

    /**
     * Sends the player to the spawn location and resets their state.
     *
     * @param player The player to send to spawn.
     */
    private void sendToSpawn(Player player) {
        PlayerUtil.reset(player, false, true);

        this.plugin.getService(SpawnService.class).teleportToSpawn(player);
        this.plugin.getService(HotbarService.class).applyHotbarItems(player);

        player.sendMessage(ServerLocale.SPAWN_TELEPORTED.getMessage());
    }
}