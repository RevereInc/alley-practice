package dev.revere.alley.adapter.core.listener;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.adapter.core.Core;
import dev.revere.alley.adapter.core.CoreAdapter;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.SettingsLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.filter.FilterService;
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
    protected final AlleyPlugin plugin = AlleyPlugin.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        Core core = this.plugin.getService(CoreAdapter.class).getCore();
        FilterService filterService = this.plugin.getService(FilterService.class);

        String eventMessage = event.getMessage();

        if (filterService.isProfanity(eventMessage)) {
            filterService.notifyStaff(eventMessage, player);
        }

        LocaleService localeService = this.plugin.getService(LocaleService.class);

        if (localeService.getBoolean(SettingsLocaleImpl.SERVER_CHAT_FORMAT_ENABLED_BOOLEAN)) {
            String separator = localeService.getString(SettingsLocaleImpl.SERVER_CHAT_FORMAT_SEPARATOR);
            String format = core.getChatFormat(player, eventMessage, CC.translate(separator));
            String censoredFormat = core.getChatFormat(player, filterService.censorWords(eventMessage), CC.translate(separator));

            for (Player recipient : event.getRecipients()) {
                Profile profile = this.plugin.getService(ProfileService.class).getProfile(recipient.getUniqueId());
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

            this.plugin.getServer().getConsoleSender().sendMessage(format);

            event.setCancelled(true);
        }
    }
}