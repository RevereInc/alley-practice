package dev.revere.alley.feature.kit.command.helper.impl;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project alley-practice
 * @date 28/05/2024 - 20:16
 */
public class RenameCommand extends BaseCommand {
    @CommandData(
            name = "rename",
            isAdminOnly = true,
            usage = "rename <name>",
            description = "Rename the item in your hand"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (command.getArgs().length == 0) {
            player.sendMessage(CC.translate("&cUsage: /rename <name>"));
            return;
        }

        String itemRename = String.join(" ", command.getArgs());

        ItemStack itemStack = player.getItemInHand();
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ERROR_MUST_HOLD_ITEM));
            return;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            player.sendMessage(CC.translate("&cFailed to rename the item."));
            return;
        }

        String originalName = itemMeta.hasDisplayName() ? itemMeta.getDisplayName() : translate(itemStack.getType().name());

        itemMeta.setDisplayName(CC.translate(itemRename));
        itemStack.setItemMeta(itemMeta);

        player.updateInventory();

//        String renameMessage = this.plugin.getService(ConfigService.class).getMessagesConfig().getString("rename-item.renamed")
//                .replace("{item}", originalName)
//                .replace("{renamed}", itemRename);
        player.sendMessage(CC.translate("&aRenamed &f" + originalName + " &ato &f" + itemRename));
    }

    private String translate(String name) {
        return Arrays.stream(name.split("_"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
