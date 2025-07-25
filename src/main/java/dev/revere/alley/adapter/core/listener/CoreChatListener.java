package dev.revere.alley.adapter.core.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.filter.FilterService;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.adapter.core.Core;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.adapter.core.CoreAdapter;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
public class CoreChatListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        Core core = Alley.getInstance().getService(CoreAdapter.class).getCore();

        FilterService filterService = Alley.getInstance().getService(FilterService.class);

        if (filterService.isProfanity(event.getMessage())) {
            filterService.notifyStaff(event.getMessage(), player);
        }

        String format = core.getChatFormat(player, event.getMessage(), CC.translate("&7: &f"));
        String censoredFormat = core.getChatFormat(player, filterService.censorWords(event.getMessage()), CC.translate("&7: &f"));

        Bukkit.getConsoleSender().sendMessage(format);

        for (Player recipient : event.getRecipients()) {
            Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(recipient.getUniqueId());
            if (profile.getProfileData().getSettingData().isProfanityFilterEnabled()) {
                if (!event.isCancelled()) {
                    recipient.sendMessage(censoredFormat);
                }
            } else {
                if (!event.isCancelled()) {
                    recipient.sendMessage(format);
                }
            }
        }

        event.setCancelled(true);
    }
}
