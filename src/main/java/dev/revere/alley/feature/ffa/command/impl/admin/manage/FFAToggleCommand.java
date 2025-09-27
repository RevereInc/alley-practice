package dev.revere.alley.feature.ffa.command.impl.admin.manage;

import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
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
            command.sendUsage();
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.KIT_NOT_FOUND).replace("{kit-name}", args[0]));
            return;
        }

        FFAService ffaService = this.plugin.getService(FFAService.class);
        if (ffaService.isNotEligibleForFFA(kit)) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.FFA_KIT_NOT_ELIGIBLE));
            return;
        }

        kit.setFfaEnabled(!kit.isFfaEnabled());
        boolean ffaEnabled = kit.isFfaEnabled();

        kitService.saveKit(kit);
        ffaService.reloadFFAKits();
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.FFA_TOGGLED)
                .replace("{kit-name}", kit.getName())
                .replace("{status}", ffaEnabled ? "enabled" : "disabled")
        );
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.FFA_KITS_RELOADED));
    }
}