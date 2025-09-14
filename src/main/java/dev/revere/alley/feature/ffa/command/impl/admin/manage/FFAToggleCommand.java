package dev.revere.alley.feature.ffa.command.impl.admin.manage;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.command.FFALocaleImpl;
import dev.revere.alley.core.locale.internal.impl.command.KitLocaleImpl;
import dev.revere.alley.feature.ffa.FFAService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 11/04/2025
 */
public class FFAToggleCommand extends BaseCommand {
    @CommandData(
            name = "ffa.toggle",
            isAdminOnly = true,
            usage = "ffa toggle <kitName>",
            description = "Toggle a kit's eligibility for FFA matches."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/ffa toggle &6<kitName>"));
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(this.getMessage(KitLocaleImpl.NOT_FOUND).replace("{kit-name}", args[0]));
            return;
        }

        FFAService ffaService = this.plugin.getService(FFAService.class);
        if (ffaService.isNotEligibleForFFA(kit)) {
            player.sendMessage(this.getMessage(FFALocaleImpl.KIT_NOT_ELIGIBLE));
            return;
        }

        kit.setFfaEnabled(!kit.isFfaEnabled());
        boolean ffaEnabled = kit.isFfaEnabled();

        kitService.saveKit(kit);
        ffaService.reloadFFAKits();
        player.sendMessage(this.getMessage(FFALocaleImpl.TOGGLED)
                .replace("{kit-name}", kit.getName())
                .replace("{status}", ffaEnabled ? "enabled" : "disabled")
        );
        player.sendMessage(this.getMessage(FFALocaleImpl.KITS_RELOADED));
    }
}