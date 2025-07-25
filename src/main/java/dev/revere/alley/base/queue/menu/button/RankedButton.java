package dev.revere.alley.base.queue.menu.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.base.hotbar.HotbarService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.base.server.ServerService;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 13/03/2025
 */
@AllArgsConstructor
public class RankedButton extends Button {
    protected final Alley plugin = Alley.getInstance();
    private final Queue queue;

    @Override
    public ItemStack getButtonItem(Player player) {
        Kit kit = this.queue.getKit();
        return new ItemBuilder(kit.getIcon())
                .name(kit.getMenuTitle())
                .durability(kit.getDurability())
                .hideMeta()
                .lore(this.getLore(kit, player))
                .hideMeta().build();
    }

    /**
     * Get the lore for the kit.
     *
     * @param kit the kit to get the lore for
     * @return the lore for the kit
     */
    private @NotNull List<String> getLore(Kit kit, Player player) {
        List<String> lore = new ArrayList<>();
        lore.add(CC.MENU_BAR);

        if (!kit.getDescription().isEmpty()) {
            Collections.addAll(lore,
                    "&7" + kit.getDescription(),
                    ""
            );
        }
        Collections.addAll(lore,
                "&6│ &rPlaying: &6" + this.queue.getQueueFightCount(),
                "&6│ &rQueueing: &6" + this.queue.getProfiles().size(),
                "",
                "&f&lYour ELO: &6" + Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId()).getProfileData().getRankedKitData().get(kit.getName()).getElo(),
                " &f1. &6NULL &f- &6N/A",
                " &f2. &6NULL &f- &6N/A",
                " &f3. &6NULL &f- &6N/A",
                "",
                "&aClick to play.",
                CC.MENU_BAR
        );

        return lore;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        if (clickType != ClickType.LEFT) return;

        ServerService serverService = Alley.getInstance().getService(ServerService.class);
        if (!serverService.isQueueingAllowed()) {
            player.sendMessage(CC.translate("&cQueueing is temporarily disabled. Please try again later."));
            player.closeInventory();
            return;
        }

        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getProfileData().isRankedBanned()) {
            player.closeInventory();
            Arrays.asList(
                    "",
                    "&c&lRANKED BAN",
                    "&cYou are currently banned from ranked queues.",
                    "&7You may appeal at &6&ndiscord.gg/alley-practice&7.",
                    ""
            ).forEach(line -> player.sendMessage(CC.translate(line)));
            return;
        }

        this.queue.addPlayer(player, this.queue.isRanked() ? profile.getProfileData().getRankedKitData().get(queue.getKit().getName()).getElo() : 0);
        PlayerUtil.reset(player, false, true);
        player.closeInventory();
        this.playNeutral(player);
        Alley.getInstance().getService(HotbarService.class).applyHotbarItems(player);
    }
}
