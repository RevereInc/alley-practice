package dev.revere.alley.feature.queue.menu;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.text.LoreHelper;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.ffa.FFAMatch;
import dev.revere.alley.feature.ffa.FFAService;
import dev.revere.alley.feature.ffa.menu.FFAButton;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitCategory;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.feature.queue.QueueService;
import dev.revere.alley.feature.queue.QueueType;
import dev.revere.alley.feature.queue.menu.button.BotButton;
import dev.revere.alley.feature.queue.menu.button.UnrankedButton;
import dev.revere.alley.feature.queue.menu.extra.button.QueueModeSwitcherButton;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 01:28
 */
public class QueuesMenuModern extends Menu {
    /**
     * Get the title of the menu.
     *
     * @param player the player to get the title for
     * @return the title of the menu
     */
    @Override
    public String getTitle(Player player) {
        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        return "&6&l" + profile.getQueueType().getMenuTitle();
    }

    /**
     * Get the buttons for the menu.
     *
     * @param player the player to get the buttons for
     * @return the buttons for the menu
     */
    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        QueueService queueService = AlleyPlugin.getInstance().getService(QueueService.class);
        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        buttons.put(2, new QueuesButtonModern("&6&lUnranked", Material.DIAMOND_SWORD, 0, Arrays.asList(
                CC.MENU_BAR,
                "&7Casual 1v1s with",
                "&7no loss penalty.",
                "",
                "&6│ &fPlayers: &6" + queueService.getPlayerCountOfGameType("Unranked"),
                "",
                this.getLore(profile, QueueType.UNRANKED),
                CC.MENU_BAR
        )));

        buttons.put(4, new QueuesButtonModern("&6&lUnranked Duos", Material.GOLD_SWORD, 0, Arrays.asList(
                CC.MENU_BAR,
                "&7Casual 2v2s with",
                "&7no penalty loss",
                "",
                "&6│ &fPlayers: &6" + queueService.getPlayerCountOfGameType("Duos"),
                "",
                this.getLore(profile, QueueType.DUOS),
                CC.MENU_BAR
        )));

        buttons.put(6, new QueuesButtonModern("&6&lFFA", Material.GOLD_AXE, 0, Arrays.asList(
                CC.MENU_BAR,
                "&7Free for all with",
                "&7infinity respawns.",
                "",
                "&6│ &fPlayers: &6" + queueService.getPlayerCountOfGameType("FFA"),
                "",
                this.getLore(profile, QueueType.FFA),
                CC.MENU_BAR
        )));

        int slot = 10;
        switch (profile.getQueueType()) {
            case UNRANKED:
                for (Queue queue : AlleyPlugin.getInstance().getService(QueueService.class).getQueues()) {
                    if (!queue.isRanked() && !queue.isDuos() && queue.getKit().getCategory() == KitCategory.NORMAL) {
                        slot = this.skipIfSlotCrossingBorder(slot);
                        buttons.put(slot++, new UnrankedButton(queue));
                    }
                }

                buttons.put(40, new QueueModeSwitcherButton(QueueType.UNRANKED, KitCategory.EXTRA));

                break;
            case BOTS:
                for (Kit kit : AlleyPlugin.getInstance().getService(KitService.class).getKits()) {
                    slot = this.skipIfSlotCrossingBorder(slot);
                    buttons.put(slot++, new BotButton(kit));
                }

                break;
            case DUOS:
                for (Queue queue : AlleyPlugin.getInstance().getService(QueueService.class).getQueues()) {
                    if (!queue.isRanked() && queue.isDuos() && queue.getKit().getCategory() == KitCategory.NORMAL) {
                        slot = this.skipIfSlotCrossingBorder(slot);
                        buttons.put(slot++, new UnrankedButton(queue));
                    }
                }

                buttons.put(40, new QueueModeSwitcherButton(QueueType.DUOS, KitCategory.EXTRA));

                break;
            case FFA:
                for (FFAMatch match : AlleyPlugin.getInstance().getService(FFAService.class).getMatches()) {
                    buttons.put(match.getKit().getFfaSlot(), new FFAButton(match));
                }

                break;
        }

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }

    @AllArgsConstructor
    public static class QueuesButtonModern extends Button {
        private String displayName;
        private Material material;
        private int durability;
        private List<String> lore;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(this.material)
                    .name(this.displayName)
                    .durability(this.durability)
                    .lore(this.lore)
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            if (clickType != ClickType.LEFT) return;

            Profile profile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
            switch (this.material) {
                case DIAMOND_SWORD:
                    profile.setQueueType(QueueType.UNRANKED);
                    break;
                case GOLD_SWORD:
                    profile.setQueueType(QueueType.DUOS);
                    break;
                case GOLD_AXE:
                    profile.setQueueType(QueueType.FFA);
                    break;
            }

            new QueuesMenuModern().openMenu(player);

            this.playNeutral(player);
        }
    }

    /**
     * Get the lore for the selected queue type.
     *
     * @param profile the player's profile
     * @param type    the queue type to check
     */
    private String getLore(Profile profile, QueueType type) {
        return LoreHelper.selectionLore(profile.getQueueType() == type, "select");
    }
}