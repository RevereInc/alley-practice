package dev.revere.alley.feature.item;

import dev.revere.alley.config.ConfigService;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.tool.visual.TexturesConstant;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project alley-practice
 * @since 18/07/2025
 */
@Getter
@Service(provides = ItemService.class, priority = 440)
public class ItemServiceImpl implements ItemService {
    private final ConfigService configService;

    private String GOLDEN_HEAD_TEXTURE;
    private ItemStack goldenHead;

    /**
     * DI Constructor for the ItemService class.
     *
     * @param configService The configuration service to load item textures from.
     */
    public ItemServiceImpl(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.loadGoldenHeadTextureAndConstructItem();
    }

    private void loadGoldenHeadTextureAndConstructItem() {
        FileConfiguration config = this.configService.getTexturesConfig();
        if (config == null) {
            throw new IllegalStateException("Textures configuration is not loaded.");
        }

        String path = "golden-head";

        this.GOLDEN_HEAD_TEXTURE = config.getString(path + ".texture", TexturesConstant.GOLDEN_STEVE_SKIN);

        String name = config.getString(path + ".name", "&6Golden Head");
        List<String> lore = config.getStringList(path + ".lore");

        this.goldenHead = new ItemBuilder(Material.SKULL_ITEM)
                .name(name)
                .lore(lore)
                .durability(3)
                .setSkullTexture(this.GOLDEN_HEAD_TEXTURE)
                .build();
    }

    @Override
    public void performHeadConsume(Player player, ItemStack item) {
        Arrays.asList(
                new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 2), // Regeneration III for 5 seconds
                new PotionEffect(PotionEffectType.SPEED, 20 * 10, 0),       // Speed I for 10 seconds
                new PotionEffect(PotionEffectType.ABSORPTION, 20 * 120, 0)  // Absorption I for 2 minutes
        ).forEach(player::addPotionEffect);

        player.sendMessage(CC.translate("&aYou've consumed a &6&lGolden Head&a!"));

        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().remove(item);
        }
    }
}