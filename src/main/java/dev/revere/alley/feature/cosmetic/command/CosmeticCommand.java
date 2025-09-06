package dev.revere.alley.feature.cosmetic.command;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.cosmetic.command.impl.admin.CosmeticGetSelectedCommand;
import dev.revere.alley.feature.cosmetic.command.impl.admin.CosmeticListCommand;
import dev.revere.alley.feature.cosmetic.command.impl.admin.CosmeticSetCommand;
import dev.revere.alley.feature.cosmetic.command.impl.player.CosmeticsCommand;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
public class CosmeticCommand extends BaseCommand {

    public CosmeticCommand() {
        new CosmeticsCommand();
        new CosmeticListCommand();
        new CosmeticSetCommand();
        new CosmeticGetSelectedCommand();
    }

    @CommandData(name = "cosmetic", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(" ");
        player.sendMessage(CC.translate("&6&lCosmetic Commands Help:"));
        player.sendMessage(CC.translate(" &f● &6/cosmetic list &7| List all cosmetics"));
        player.sendMessage(CC.translate(" &f● &6/cosmetic get &8(&7player&8)  &7| Get selected cosmetics"));
        player.sendMessage(CC.translate(" &f● &6/cosmetic set &8(&7player&8) &8(&7cosmetic&8)  &7| Set active cosmetic"));
        player.sendMessage("");

    }
}
