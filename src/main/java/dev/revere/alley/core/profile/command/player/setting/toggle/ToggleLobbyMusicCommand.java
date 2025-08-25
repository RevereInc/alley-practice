package dev.revere.alley.core.profile.command.player.setting.toggle;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.internal.locale.impl.ProfileLocale;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.music.MusicService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 19/07/2025
 */
public class ToggleLobbyMusicCommand extends BaseCommand {
    @CommandData(name = "togglelobbymusic", cooldown = 1)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        MusicService musicService = this.plugin.getService(MusicService.class);

        Profile profile = profileService.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setLobbyMusicEnabled(!profile.getProfileData().getSettingData().isLobbyMusicEnabled());

        boolean isEnabled = profile.getProfileData().getSettingData().isLobbyMusicEnabled();
        if (isEnabled) {
            musicService.startMusic(player);
        } else {
            musicService.stopMusic(player);
        }

        player.sendMessage(CC.translate(ProfileLocale.TOGGLED_LOBBY_MUSIC.getMessage().replace("{status}", profile.getProfileData().getSettingData().isLobbyMusicEnabled() ? "&aenabled" : "&cdisabled")));
    }
}
