package dev.revere.alley.feature.cosmetic.command.impl.admin;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.cosmetic.BaseCosmetic;
import dev.revere.alley.feature.cosmetic.CosmeticType;
import dev.revere.alley.feature.cosmetic.repository.BaseCosmeticRepository;
import dev.revere.alley.feature.cosmetic.repository.CosmeticService;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.StringUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
public class CosmeticSetCommand extends BaseCommand {
    @CommandData(name = "cosmetic.set", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 3) {
            player.sendMessage(CC.translate("&cUsage: /cosmetic set <player> <type> <cosmetic>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found"));
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());

        String typeName = args[1];
        String cosmeticName = args[2];

        CosmeticType cosmeticType = CosmeticType.fromString(typeName);
        if (cosmeticType == null) {
            player.sendMessage(CC.translate("&cInvalid cosmetic type."));
            return;
        }

        BaseCosmeticRepository<?> repository = this.plugin.getService(CosmeticService.class).getRepository(cosmeticType);
        if (repository == null) {
            player.sendMessage(CC.translate("&cError: No repository found for that type."));
            return;
        }

        BaseCosmetic cosmetic = repository.getCosmetic(cosmeticName);
        if (cosmetic == null) {
            player.sendMessage(CC.translate("&cCosmetic with name '" + cosmeticName + "' not found in that type."));
            return;
        }

        profile.getProfileData().getCosmeticData().setSelected(cosmetic);
        player.sendMessage(CC.translate("&aSuccessfully set &6" + cosmetic.getName() + " " + StringUtil.formatEnumName(cosmeticType) + " &aas the active cosmetic for &6" + target.getName()));
    }
}