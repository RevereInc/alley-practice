package dev.revere.alley.base.cooldown.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.cooldown.Cooldown;
import dev.revere.alley.base.cooldown.CooldownService;
import dev.revere.alley.base.cooldown.enums.CooldownType;
import dev.revere.alley.util.StringUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 24/06/2025
 */
public class CooldownResetCommand extends BaseCommand {
    @CommandData(name = "cooldown.reset", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /cooldown reset <player> <cooldown>"));
            return;
        }

        String targetName = args[0];
        Player target = this.plugin.getServer().getPlayer(targetName);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        CooldownType type;
        try {
            type = CooldownType.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(CC.translate("&cInvalid cooldown type. Valid types: " + String.join(", ", Arrays.stream(CooldownType.values()).map(Enum::name).toArray(String[]::new))));
            return;
        }

        CooldownService repository = this.plugin.getService(CooldownService.class);
        Cooldown cooldown = repository.getCooldown(target.getUniqueId(), type);
        if (cooldown == null) {
            player.sendMessage(CC.translate("&cNo cooldown found for " + target.getName() + " of type " + StringUtil.formatEnumName(type) + "."));
            return;
        }

        repository.removeCooldown(player.getUniqueId(), type);

        player.sendMessage(CC.translate("&aCooldown for " + target.getName() + " of type " + StringUtil.formatEnumName(type) + " has been reset."));
    }
}