package dev.revere.alley.feature.troll.command;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Emmy
 * @project alley-practice
 * @since 08/09/2025
 */
public class ConfuseCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "confuse",
            aliases = "nausea",
            isAdminOnly = true
    )
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /confuse <player>"));
            return;
        }

        Player targetPlayer = player.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&fNo player matching &b" + args[0] + " &fis connected to this server."));
            return;
        }

        if (targetPlayer.hasPotionEffect(PotionEffectType.CONFUSION)) {
            targetPlayer.removePotionEffect(PotionEffectType.CONFUSION);
            player.sendMessage(CC.translate("&fYou've removed &b" + targetPlayer.getName() + "'s &fnausea."));
            return;
        }

        targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000000, 1));
        player.sendMessage(CC.translate("&fYou've confused &b" + targetPlayer.getName()));
    }
}