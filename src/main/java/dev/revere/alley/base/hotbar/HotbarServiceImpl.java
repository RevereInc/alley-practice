package dev.revere.alley.base.hotbar;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.base.hotbar.data.HotbarActionData;
import dev.revere.alley.base.hotbar.data.HotbarTypeData;
import dev.revere.alley.base.hotbar.enums.HotbarAction;
import dev.revere.alley.base.hotbar.enums.HotbarType;
import dev.revere.alley.base.queue.QueueService;
import dev.revere.alley.base.queue.menu.sub.RankedMenu;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.tool.reflection.utility.ReflectionUtility;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Getter
@Service(provides = HotbarService.class, priority = 190)
public class HotbarServiceImpl implements HotbarService {
    private final ProfileService profileService;
    private final ConfigService configService;

    private final List<HotbarItem> hotbarItems = new ArrayList<>();

    /**
     * DI Constructor for the HotbarService class.
     *
     * @param profileService The profile service to manage player profiles.
     * @param configService  The configuration service to manage hotbar configurations.
     */
    public HotbarServiceImpl(ProfileService profileService, ConfigService configService) {
        this.profileService = profileService;
        this.configService = configService;
    }

    @Override
    public void initialize(AlleyContext context) {
        FileConfiguration hotbarConfig = this.configService.getHotbarConfig();
        ConfigurationSection hotbarSection = hotbarConfig.getConfigurationSection("hotbar-items");
        if (hotbarSection == null) {
            Logger.error("Hotbar items section is missing in the hotbar configuration file.");
            return;
        }

        for (String key : hotbarSection.getKeys(false)) {
            ConfigurationSection itemSection = hotbarSection.getConfigurationSection(key);
            if (itemSection == null) continue;

            String displayName = itemSection.getString("display-name", "&fNULL");
            displayName = CC.translate(displayName);

            List<String> lore = itemSection.getStringList("lore");
            if (lore == null) {
                lore = Collections.singletonList(CC.translate("&f" + key + " has not been configured properly."));
            } else {
                lore = lore.stream().map(CC::translate).collect(Collectors.toList());
            }

            Material material = Material.matchMaterial(itemSection.getString("material", "STONE").toUpperCase());
            int durability = itemSection.getInt("durability", 0);

            List<HotbarTypeData> typeData = new ArrayList<>();

            ConfigurationSection typesSection = itemSection.getConfigurationSection("types");
            if (typesSection == null) {
                Logger.error("Types section is missing for hotbar item. Keep in mind that the types section is required for hotbar items to work properly.");
            } else {
                for (String typeKey : typesSection.getKeys(false)) {
                    HotbarType type = HotbarType.valueOf(typeKey.toUpperCase());
                    int slot = typesSection.getInt(typeKey + ".slot", -1);
                    HotbarTypeData hotbarTypeData = new HotbarTypeData(type, slot);
                    hotbarTypeData.setEnabled(typesSection.getBoolean(typeKey + ".enabled", false));
                    typeData.add(hotbarTypeData);
                }
            }

            String command = itemSection.getString("command");
            HotbarAction action = command.isEmpty() ? HotbarAction.OPEN_MENU : HotbarAction.RUN_COMMAND;
            HotbarActionData actionData = new HotbarActionData(action);

            if (action == HotbarAction.RUN_COMMAND) {
                actionData.setCommand(command);
            } else {
                String menuName = itemSection.getString("menu");
                if (menuName != null && !menuName.isEmpty()) {
                    Menu menu = this.getMenuInstanceFromName(menuName);
                    try {
                        actionData.setMenu(menu);
                    } catch (Exception exception) {
                        Logger.error("Failed to set menu for hotbar item: " + key + ". Menu: " + menuName + " does not exist or is not properly configured.");
                    }
                } else {
                    Logger.error("Menu name is missing for hotbar item: " + key);
                }
            }

            HotbarItem hotbarItem = this.createHotbarItem(key, displayName, lore, material, durability, typeData, actionData);
            this.hotbarItems.add(hotbarItem);
        }
    }

    /**
     * Creates a HotbarItem with the specified parameters.
     *
     * @param name        The name of the hotbar item.
     * @param displayName The display name of the hotbar item.
     * @param lore        The lore of the hotbar item.
     * @param material    The material of the hotbar item.
     * @param durability  The durability of the hotbar item.
     * @param typeData    The type data for the hotbar item.
     * @param actionData  The action data for the hotbar item.
     * @return A new HotbarItem instance.
     */
    private HotbarItem createHotbarItem(String name, String displayName, List<String> lore, Material material, int durability, List<HotbarTypeData> typeData, HotbarActionData actionData) {
        HotbarItem hotbarItem = new HotbarItem(name);
        hotbarItem.setDisplayName(displayName);
        hotbarItem.setLore(lore);
        hotbarItem.setMaterial(material);
        hotbarItem.setDurability(durability);
        hotbarItem.setTypeData(typeData);
        hotbarItem.setActionData(actionData);
        return hotbarItem;
    }

    /**
     * Builds an ItemStack for a HotbarItem that can be received by players.
     *
     * @param hotbarItem The HotbarItem to build the ItemStack for.
     * @return An ItemStack representing the HotbarItem.
     */
    @Override
    public ItemStack buildReceivableItem(HotbarItem hotbarItem) {
        ItemStack itemStack = new ItemBuilder(hotbarItem.getMaterial())
                .name(hotbarItem.getDisplayName())
                .lore(hotbarItem.getLore())
                .durability(hotbarItem.getDurability())
                .build();

        itemStack = ReflectionUtility.setUnbreakable(itemStack, true);
        return itemStack;
    }

    @Override
    public void applyHotbarItems(Player player, HotbarType type) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        List<HotbarItem> itemsToApply = this.getItemsForType(type);
        if (itemsToApply == null) return;

        for (HotbarItem item : itemsToApply) {
            ItemStack itemStack = this.buildReceivableItem(item);
            player.getInventory().setItem(item.getTypeData().get(type.ordinal()).getSlot(), itemStack);
        }

        Alley.getInstance().getServer().getScheduler().runTaskLater(Alley.getInstance(), player::updateInventory, 1L);
    }

    @Override
    public void applyHotbarItems(Player player) {
        Profile profile = this.profileService.getProfile(player.getUniqueId());
        this.applyHotbarItems(player, this.getCorrespondingType(profile));
    }

    @Override
    public List<HotbarItem> getItemsForType(HotbarType type) {
        return this.hotbarItems.stream()
                .filter(item -> item.getTypeData().stream().anyMatch(data -> data.getType() == type && data.isEnabled()))
                .collect(Collectors.toList());
    }

    @Override
    public HotbarType getCorrespondingType(Profile profile) {
        HotbarType type;

        switch (profile.getState()) {
            case WAITING:
                type = HotbarType.QUEUE;
                break;
            case SPECTATING:
                type = HotbarType.SPECTATOR;
                break;
            case LOBBY:
                return (profile.getParty() != null) ? HotbarType.PARTY : HotbarType.LOBBY;
            case PLAYING_TOURNAMENT:
                type = HotbarType.TOURNAMENT;
                break;
            default:
                type = HotbarType.LOBBY;
                break;
        }

        return type;
    }

    @Override
    public boolean isHotbarItem(ItemStack itemStack, HotbarType type) {
        if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return false;
        }

        List<HotbarItem> items = this.getItemsForType(type);
        return items.stream().anyMatch(hotbarItem -> hotbarItem.getDisplayName().equals(itemStack.getItemMeta().getDisplayName()));
    }

    @Override
    public HotbarItem getHotbarItem(ItemStack itemStack, HotbarType type) {
        if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return null;
        }

        List<HotbarItem> items = this.getItemsForType(type);
        return items.stream()
                .filter(hotbarItem -> hotbarItem.getDisplayName().equals(itemStack.getItemMeta().getDisplayName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets a menu instance by a given name.
     *
     * @param name the name of the menu
     * @return the menu instance
     */
    public Menu getMenuInstanceFromName(String name) {
        switch (name) {
            case "UNRANKED_MENU":
                return Alley.getInstance().getService(QueueService.class).getQueueMenu();
            case "RANKED_MENU":
                return new RankedMenu();
            default:
                throw new IllegalArgumentException("Unknown menu type: " + name);

        }
    }
}