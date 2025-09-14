package dev.revere.alley.feature.command.impl.other.troll;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.ErrorLocaleImpl;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 29/06/2024 - 11:51
 */
public class FakeExplosionCommand extends BaseCommand {
    @CommandData(
            name = "fakeexplosion",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "fakeexplosion",
            description = "Fake an explosion"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/fakeexplosion &6<player>"));
            return;
        }

        Player targetPlayer = this.plugin.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.INVALID_PLAYER));
            return;
        }

        targetPlayer.getWorld().createExplosion(targetPlayer.getLocation(), 0.0F, false);
        player.sendMessage(CC.translate("&6You've fake exploded &7" + targetPlayer.getName()));
    }
}