package dev.revere.alley.base.kit.command.impl.manage.raiding;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.service.BaseRaidingService;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingRaiding;
import dev.revere.alley.game.match.player.enums.BaseRaiderRole;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @since 16/06/2025
 */
public class KitRemoveRaidingRoleKitCommand extends BaseCommand {
    @CommandData(name = "kit.removeraidingrolekit", isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 3) {
            sender.sendMessage(CC.translate("&6Usage: &e/kit removeraidingrolekit &6<kitName> <role> <kitName>"));
            return;
        }

        String kitName = args[0];
        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(kitName);
        if (kit == null) {
            sender.sendMessage(CC.translate("&cThe &6" + kitName + " &ckit does not exist."));
            return;
        }

        if (!kit.isSettingEnabled(KitSettingRaiding.class)) {
            sender.sendMessage(CC.translate("&cThe &6" + kit.getName() + " &ckit does not have &6base raiding &csetting enabled."));
            return;
        }

        String roleName = args[1].toUpperCase();
        BaseRaiderRole role;
        try {
            role = BaseRaiderRole.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(CC.translate("&cInvalid raiding role: " + roleName + ". Valid roles are: RAIDER, TRAPPER"));
            return;
        }

        String roleKitName = args[2];
        Kit roleKit = kitService.getKit(roleKitName);
        if (roleKit == null) {
            sender.sendMessage(CC.translate("&cThe &6" + roleKitName + " &ckit does not exist."));
            return;
        }

        if (roleKit.isEnabled()) {
            sender.sendMessage(CC.translate("&cThe &6" + roleKitName + " &ckit is currently enabled. Please disable it before setting it as a raiding role kit."));
            return;
        }

        BaseRaidingService raidingService = this.plugin.getService(BaseRaidingService.class);

        if (raidingService.getRaidingKitByRole(kit, role) == null) {
            sender.sendMessage(CC.translate("&cThe &6" + kit.getName() + " &ckit does not have a raiding kit mapped for the &6" + role.name() + " &crole."));
            return;
        }

        raidingService.removeRaidingKitMapping(kit, role);

        sender.sendMessage(CC.translate("&aSuccessfully removed the &6" + role + " &araiding role kit from &6" + roleKit.getName() + "&a for the &6" + kit.getName() + " &akit."));
    }
}