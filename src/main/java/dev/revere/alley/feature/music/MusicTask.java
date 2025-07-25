package dev.revere.alley.feature.music;

import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Remi
 * @project alley-practice
 * @date 20/07/2025
 */
@RequiredArgsConstructor
public class MusicTask extends BukkitRunnable {
    private final Player player;
    private final MusicServiceImpl musicServiceImpl;
    private final ProfileService profileService;

    @Override
    public void run() {
        if (!player.isOnline()) {
            cancel();
            return;
        }

        MusicSession session = musicServiceImpl.getSession(player.getUniqueId());
        if (session == null || session.getTask().getTaskId() != getTaskId()) {
            cancel();
            return;
        }

        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile == null) {
            musicServiceImpl.stopMusic(player);
            return;
        }

        boolean shouldPlay = shouldPlayMusic(profile);

        if (!shouldPlay && !session.isPaused()) {
            musicServiceImpl.sendStopSoundPacket(player, session.getJukeboxLocation());
            session.setPaused(true);
            return;
        }

        if (shouldPlay && session.isPaused()) {
            musicServiceImpl.sendPlaySoundPacket(player, session.getDisc(), session.getJukeboxLocation());
            session.setPaused(false);
            session.setElapsedSeconds(0);
        }

        if (shouldPlay && !session.isPaused()) {
            if (session.isFinished()) {
                cancel();
                musicServiceImpl.startMusic(player);
            }

            session.setElapsedSeconds(session.getElapsedSeconds() + 1);
        }
    }

    private boolean shouldPlayMusic(Profile profile) {
        boolean inLobby = profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.WAITING;
        return inLobby && profile.getProfileData().getSettingData().isLobbyMusicEnabled();
    }
}