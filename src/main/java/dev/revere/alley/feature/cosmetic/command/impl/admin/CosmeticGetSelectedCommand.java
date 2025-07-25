package dev.revere.alley.feature.cosmetic.command.impl.admin;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.cosmetic.CosmeticType;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.impl.ProfileCosmeticData;
import dev.revere.alley.util.StringUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
public class CosmeticGetSelectedCommand extends BaseCommand {
    @CommandData(name = "cosmetic.getselected", aliases = {"cosmetic.get"}, isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage(CC.translate("&cUsage: /cosmetic get <player>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found"));
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());
        player.sendMessage(CC.translate("     &6&lSelected Cosmetics for " + target.getName()));

        ProfileCosmeticData cosmeticData = profile.getProfileData().getCosmeticData();

        for (CosmeticType type : CosmeticType.values()) {
            String selectedName = cosmeticData.getSelected(type);

            String friendlyTypeName = StringUtil.formatEnumName(type);

            player.sendMessage(CC.translate(String.format("      &f● &6%s: &f%s", friendlyTypeName, selectedName)));
        }
    }
}