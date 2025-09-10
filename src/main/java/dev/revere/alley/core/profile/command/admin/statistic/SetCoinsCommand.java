package dev.revere.alley.core.profile.command.admin.statistic;

import dev.revere.alley.core.locale.internal.types.ErrorLocaleImpl;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
public class SetCoinsCommand extends BaseCommand {
    @CommandData(
            name = "coins.set",
            isAdminOnly = true,
            usage = "coins set <player> <amount>",
            description = "Set a player's coins."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 2) {
            player.sendMessage(CC.translate("&cUsage: /coins set <player> <amount>"));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.INVALID_PLAYER));
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());

        try {
            int amount = Integer.parseInt(args[1]);
            profile.getProfileData().setCoins(amount);
            player.sendMessage(CC.translate("&aSuccessfully set " + target.getName() + "'s coins to " + amount + "."));
        } catch (NumberFormatException e) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.INVALID_NUMBER).replace("{input}", args[1]));
        }
    }
}
