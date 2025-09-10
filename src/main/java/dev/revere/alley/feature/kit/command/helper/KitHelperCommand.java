package dev.revere.alley.feature.kit.command.helper;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.common.text.CC;

import java.util.Arrays;

/**
 * @author Emmy
 * @project alley-practice
 * @since 14/07/2025
 */
public class KitHelperCommand extends BaseCommand {
    @CommandData(
            name = "kithelper",
            isAdminOnly = true,
            usage = "kithelper",
            description = "Provides assistance for essentials."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Arrays.asList(
                "",
                "&6&lKit Helper Commands Help:",
                " &f● &6/enchant &8(&7enchantment&8) &8(&7level&8) &7| &fEnchant item in hand.",
                " &f● &6/glow &8(&7true|false&8) &7| &fSet item glow.",
                " &f● &6/potionduration &8(&7duration&8) &7| &fSet duration of a potion.",
                " &f● &6/removeenchants &7| &fRemoves enchants from item.",
                " &f● &6/rename &8(&7name&8) &7| &fRename item in hand.",
                " &f● &6/unbreakable &8(&7true|false&8) &7| &fSet item unbreakable.",
                ""
        ).forEach(message -> command.getSender().sendMessage(CC.translate(message)));
    }
}