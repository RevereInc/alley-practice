package dev.revere.alley.feature.troll.command;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.troll.TrollService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author Emmy
 * @project alley-practice
 * @since 01/09/2025
 */
public class TrapMonkeyCommand extends BaseCommand {
    @CommandData(
            name = "trapmonkey",
            aliases = {"cage", "trapremi"},
            description = "Traps Remi in a cage (zoo)",
            usage = "/trapmonkey <player>",
            isAdminOnly = true,
            cooldown = 10
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/trapmonkey &6<player>"));
            return;
        }

        Player target = this.plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        TrollService trollService = this.plugin.getService(TrollService.class);

        boolean isMonkey = false;
        for (String monkey : trollService.getMonkeyRegistry().getMonkeys()) {
            if (target.getUniqueId().equals(UUID.fromString(monkey))) {
                isMonkey = true;
                break;
            }
        }

        if (!isMonkey) {
            Arrays.asList(
                    "",
                    "&cHmm, We aren't quite sure if the provided player is a monkey...",
                    "&fHint: Only confirmed monkeys can be trapped.",
                    "&fThose would be:",
                    " &6- The Skidder God",
                    " &6- chapter 14",
                    ""
            ).forEach(line -> player.sendMessage(CC.translate(line)));
            return;
        }

        trollService.trapMonkey(player, target);
    }
}