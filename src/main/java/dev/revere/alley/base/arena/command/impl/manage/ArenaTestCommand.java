package dev.revere.alley.base.arena.command.impl.manage;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.arena.ArenaService;
import dev.revere.alley.base.arena.impl.StandAloneArena;
import dev.revere.alley.game.match.Match;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 20/06/2025
 */
public class ArenaTestCommand extends BaseCommand {

    @CommandData(name = "arena.test", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("World: " + player.getWorld());
        player.sendMessage("Location: " + player.getLocation());

        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        player.sendMessage("Copied arenas: " + arenaService.getTemporaryArenas().size());

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        Match match = profile.getMatch();
        if (match != null) {
            player.sendMessage("Match Arena: " + match.getArena());

            StandAloneArena arena = (StandAloneArena) match.getArena();
            if (arena != null) {
                player.sendMessage("Arena Name: " + arena.getName());
                player.sendMessage("Arena Type: " + arena.getType());
                player.sendMessage("Arena Positions: " + arena.getPos1() + " - " + arena.getPos2());
                player.sendMessage("Arena Display Name: " + arena.getDisplayName());
                player.sendMessage("Arena World " + arena.getMinimum().getWorld().getName());
                player.sendMessage("Is copied: " + arena.isTemporaryCopy());
                player.sendMessage("Arena Center: " + arena.getCenter());
                player.sendMessage("Arena Enabled: " + arena.isEnabled());
                arena.verifyArenaExists();
            } else {
                player.sendMessage("No arena found for this match.");
            }
        } else {
            player.sendMessage("No match found for this profile.");
        }
    }
}
