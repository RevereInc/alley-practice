package dev.revere.alley.base.spawn.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.hotbar.HotbarService;
import dev.revere.alley.base.spawn.SpawnService;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.game.ffa.FFAMatch;
import dev.revere.alley.game.ffa.enums.FFAState;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
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

        player.sendMessage(CC.translate(this.plugin.getService(ConfigService.class).getMessagesConfig().getString("spawn.teleported")));
    }
}