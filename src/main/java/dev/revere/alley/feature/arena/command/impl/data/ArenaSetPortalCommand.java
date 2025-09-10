package dev.revere.alley.feature.arena.command.impl.data;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.arena.ArenaType;
import dev.revere.alley.feature.arena.internal.types.StandAloneArena;
import dev.revere.alley.core.locale.internal.types.ArenaLocaleImpl;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 02/03/2025
 */
public class ArenaSetPortalCommand extends BaseCommand {
    @CommandData(
            name = "arena.setportal",
            isAdminOnly = true,
            usage = "arena setportal <name> <red/blue>",
            description = "Set the portal location for a standalone arena."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/arena setportal &6<name> <red/blue>"));
            return;
        }

        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        String name = args[0];

        Arena arena = arenaService.getArenaByName(name);
        if (arena == null) {
            player.sendMessage(this.getMessage(ArenaLocaleImpl.NOT_FOUND).replace("{arena-name}", name));
            return;
        }

        if (arena.getType() != ArenaType.STANDALONE) {
            player.sendMessage(this.getMessage(ArenaLocaleImpl.MUST_BE_STANDALONE).replace("{arena-name}", arena.getName()));
            return;
        }

        String portal = args[1];
        if (!portal.equalsIgnoreCase("red") && !portal.equalsIgnoreCase("blue")) {
            player.sendMessage(this.getMessage(ArenaLocaleImpl.INVALID_PORTAL));
            return;
        }

        StandAloneArena standAloneArena = (StandAloneArena) arena;
        if (portal.equalsIgnoreCase("red")) {
            standAloneArena.setTeam1Portal(player.getLocation());
        } else if (portal.equalsIgnoreCase("blue")) {
            standAloneArena.setTeam2Portal(player.getLocation());
        }

        arenaService.saveArena(arena);
        player.sendMessage(this.getMessage(ArenaLocaleImpl.PORTAL_SET)
                .replace("{arena-name}", arena.getName())
                .replace("{portal}", portal)
        );
    }
}