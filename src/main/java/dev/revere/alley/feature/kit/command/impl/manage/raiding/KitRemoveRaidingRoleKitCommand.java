package dev.revere.alley.feature.kit.command.impl.manage.raiding;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.feature.kit.raiding.BaseRaidingService;
import dev.revere.alley.feature.kit.setting.types.mode.KitSettingRaiding;
import dev.revere.alley.feature.match.model.BaseRaiderRole;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
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
        } catch (IllegalArgumentException exception) {
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