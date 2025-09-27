package dev.revere.alley.feature.command.impl.other.troll;

import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
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
            command.sendUsage();
            return;
        }

        Player targetPlayer = this.plugin.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        targetPlayer.getWorld().createExplosion(targetPlayer.getLocation(), 0.0F, false);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.TROLL_PLAYER_FAKE_EXPLODED)
                .replace("{name-color}", String.valueOf(this.getProfile(targetPlayer.getUniqueId()).getNameColor()))
                .replace("{player}", targetPlayer.getName())
        );
    }
}