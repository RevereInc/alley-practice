package dev.revere.alley.feature.kit.command.impl.manage;

import dev.revere.alley.core.locale.internal.types.KitLocaleImpl;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 11/04/2025
 */
public class KitSetProfileCommand extends BaseCommand {
    @CommandData(
            name = "kit.setprofile",
            aliases = "kit.setkbprofile",
            isAdminOnly = true,
            usage = "kit setprofile <kitName> <profileName>",
            description = "Set the knockback profile of a kit."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/kit setprofile &6<kitName> <profileName>"));
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(this.getMessage(KitLocaleImpl.KIT_NOT_FOUND).replace("{kit-name}", args[0]));
            return;
        }

        kit.setKnockbackProfile(args[1]);
        kitService.saveKit(kit);
        player.sendMessage(CC.translate("&aSuccessfully set profile for &6" + kit.getName() + "&a!"));
    }
}
