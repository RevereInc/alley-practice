package dev.revere.alley.feature.cosmetic.command.impl.admin;

import dev.revere.alley.common.text.EnumFormatter;
import dev.revere.alley.core.config.internal.locale.impl.CosmeticLocale;
import dev.revere.alley.core.config.internal.locale.impl.ErrorLocale;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.cosmetic.model.BaseCosmetic;
import dev.revere.alley.feature.cosmetic.model.CosmeticType;
import dev.revere.alley.feature.cosmetic.internal.repository.BaseCosmeticRepository;
import dev.revere.alley.feature.cosmetic.CosmeticService;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.common.text.StringUtil;
import dev.revere.alley.common.text.CC;
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
            player.sendMessage(ErrorLocale.INVALID_PLAYER.getMessage());
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());

        String typeName = args[1];
        String cosmeticName = args[2];

        CosmeticType cosmeticType = CosmeticType.fromString(typeName);
        if (cosmeticType == null) {
            player.sendMessage(EnumFormatter.outputAvailableValues(CosmeticType.class));
            return;
        }

        BaseCosmeticRepository<?> repository = this.plugin.getService(CosmeticService.class).getRepository(cosmeticType);
        if (repository == null) {
            player.sendMessage(CC.translate("&cError: No repository found for that type."));
            return;
        }

        BaseCosmetic cosmetic = repository.getCosmetic(cosmeticName);
        if (cosmetic == null) {
            player.sendMessage(CosmeticLocale.COSMETIC_NOT_FOUND.getMessage().replace("{input}", cosmeticName));
            return;
        }

        profile.getProfileData().getCosmeticData().setSelected(cosmetic);
        player.sendMessage(CosmeticLocale.SET_COSMETIC.getMessage()
                .replace("{type}", StringUtil.formatEnumName(cosmeticType))
                .replace("{cosmetic}", cosmetic.getName())
                .replace("{player}", target.getName())
        );
    }
}