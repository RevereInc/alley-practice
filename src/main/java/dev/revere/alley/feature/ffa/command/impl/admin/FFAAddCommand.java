package dev.revere.alley.feature.ffa.command.impl.admin;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.ErrorLocaleImpl;
import dev.revere.alley.core.locale.internal.impl.command.FFALocaleImpl;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.ffa.FFAMatch;
import dev.revere.alley.feature.ffa.FFAService;
import dev.revere.alley.feature.ffa.internal.DefaultFFAMatch;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 04/06/2025
 */
public class FFAAddCommand extends BaseCommand {
    @CommandData(
            name = "ffa.add",
            aliases = {"ffa.addplayer", "ffa.addp"},
            isAdminOnly = true,
            usage = "ffa add <player> <kit>",
            description = "Add a player to an FFA match"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/ffa add &6<player> <kit>"));
            return;
        }

        String targetName = args[0];
        FFAService ffaService = this.plugin.getService(FFAService.class);
        FFAMatch match = ffaService.getMatches().stream()
                .filter(m -> m.getKit().getName().equalsIgnoreCase(args[1]))
                .findFirst()
                .orElse(null);

        if (match == null) {
            player.sendMessage(this.getMessage(FFALocaleImpl.NOT_FOUND).replace("{ffa-name}", args[1]));
            return;
        }

        Player targetPlayer = this.plugin.getServer().getPlayer(targetName);
        if (targetPlayer == null) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.INVALID_PLAYER));
            return;
        }

        if (match.getPlayers().size() >= match.getMaxPlayers()) {
            player.sendMessage(this.getMessage(FFALocaleImpl.FFA_FULL));
            return;
        }

        DefaultFFAMatch defaultMatch = (DefaultFFAMatch) match;
        defaultMatch.forceJoin(targetPlayer);
        player.sendMessage(this.getMessage(FFALocaleImpl.ADDED_PLAYER)
                .replace("{player}", targetPlayer.getName())
                .replace("{ffa-name}", match.getName())
                .replace("{player-color}", String.valueOf(this.plugin.getService(ProfileService.class).getProfile(targetPlayer.getUniqueId()).getNameColor()))
        );
    }
}