package dev.revere.alley.feature.cosmetic.command.impl.admin;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.cosmetic.CosmeticType;
import dev.revere.alley.feature.cosmetic.interfaces.Cosmetic;
import dev.revere.alley.feature.cosmetic.BaseCosmeticRepository;
import dev.revere.alley.feature.cosmetic.CosmeticService;
import dev.revere.alley.util.StringUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
public class CosmeticListCommand extends BaseCommand {
    @CommandData(name = "cosmetic.list", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Map<CosmeticType, BaseCosmeticRepository<?>> repositories = this.plugin.getService(CosmeticService.class).getRepositories();

        player.sendMessage("");

        if (repositories.isEmpty()) {
            player.sendMessage(CC.translate("&cNo cosmetic repositories are registered."));
            player.sendMessage("");
            return;
        }

        for (Map.Entry<CosmeticType, BaseCosmeticRepository<?>> entry : repositories.entrySet()) {
            CosmeticType type = entry.getKey();
            BaseCosmeticRepository<?> repository = entry.getValue();

            List<? extends Cosmetic> cosmetics = repository.getCosmetics();

            if (cosmetics.isEmpty()) {
                continue;
            }

            String friendlyTypeName = StringUtil.formatEnumName(type);
            String header = String.format("     &6&l%s &f(%d)", friendlyTypeName, cosmetics.size());
            player.sendMessage(CC.translate(header));

            for (Cosmetic cosmetic : cosmetics) {
                player.sendMessage(CC.translate("      &f● &6" + cosmetic.getName()));
            }
        }


        player.sendMessage("");
    }
}
