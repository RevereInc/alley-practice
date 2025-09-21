package dev.revere.alley.feature.kit.command.impl.data.potion;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 14/03/2025
 */
public class KitAddPotionCommand extends BaseCommand {
    @CommandData(
            name = "kit.addpotion",
            aliases = {"kit.potion"},
            isAdminOnly = true,
            usage = "kit addpotion <kitName>",
            description = "Add potion effects to a kit based on the potion you are holding."
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
            player.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.KIT_NOT_FOUND)));
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInHand();
        if (itemInHand == null || itemInHand.getType() != Material.POTION) {
            player.sendMessage(CC.translate("&cYou must hold a potion bottle to set effects for this kit!"));
            return;
        }

        if (!(itemInHand.getItemMeta() instanceof PotionMeta)) {
            player.sendMessage(CC.translate("&cInvalid potion!"));
            return;
        }

        PotionMeta potionMeta = (PotionMeta) itemInHand.getItemMeta();
        List<PotionEffect> effects = potionMeta.getCustomEffects();
        if (effects.isEmpty()) {
            player.sendMessage(CC.translate("&cThe potion you are holding has no custom effects!"));
            return;
        }

        effects.forEach(effect -> kit.getPotionEffects().add(effect));
        kitService.saveKit(kit);
        player.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.KIT_POTION_EFFECTS_SET)).replace("{kit-name}", kit.getName()));
    }
}