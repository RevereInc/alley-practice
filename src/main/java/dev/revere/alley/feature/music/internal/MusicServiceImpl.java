package dev.revere.alley.feature.music.internal;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.bootstrap.AlleyContext;
import dev.revere.alley.bootstrap.annotation.Service;
import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.common.reflect.ReflectionService;
import dev.revere.alley.common.reflect.internal.types.DefaultReflectionImpl;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.time.TimeUtil;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.music.MusicDisc;
import dev.revere.alley.feature.music.MusicService;
import dev.revere.alley.feature.music.MusicSession;
import dev.revere.alley.feature.spawn.SpawnService;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author Emmy & Remi
 * @project alley-practice
 * @since 19/07/2025
 */
@Service(provides = MusicService.class, priority = 175)
public class MusicServiceImpl implements MusicService {
    private static final int RECORD_PLAY_EFFECT_ID = 1005;
    private static final String PLAYER_PREFIX = "&7[&6♬&7] &r";

    private final ProfileService profileService;
    private final SpawnService spawnService;

    private final Map<UUID, MusicSession> activeSessions = new ConcurrentHashMap<>();
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    private DefaultReflectionImpl reflection;
    private MusicDisc[] allDiscs;

    /**
     * DI Constructor for the MusicService class.
     *
     * @param profileService The profile service to be used by this music service.
     * @param spawnService   The spawn service to be used by this music service.
     */
    public MusicServiceImpl(ProfileService profileService, SpawnService spawnService) {
        this.profileService = profileService;
        this.spawnService = spawnService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.allDiscs = MusicDisc.values();
        this.reflection = context.getPlugin().getService(ReflectionService.class).getReflectionService(DefaultReflectionImpl.class);
    }

    @Override
    public void startMusic(Player player) {
        this.stopMusic(player);

        Profile profile = this.profileService.getProfile(player.getUniqueId());
        if (profile == null || !profile.getProfileData().getSettingData().isLobbyMusicEnabled()) {
            return;
        }

        MusicDisc disc = this.getRandomSelectedMusicDisc(profile);
        Location jukeboxLocation = spawnService.getLocation();
        this.sendPlaySoundPacket(player, disc, jukeboxLocation);

        String formattedDuration = TimeUtil.formatTimeFromSeconds(disc.getDuration());
        String message = CC.translate(PLAYER_PREFIX + "&fNow playing: &6" + disc.getTitle() + " &7(" + formattedDuration + ")");
        player.sendMessage(message);

        MusicSession session = new MusicSession(disc, jukeboxLocation);
        MusicTask task = new MusicTask(player, this, profileService);
        session.setTask(task.runTaskTimer(AlleyPlugin.getInstance(), 20L, 20L));

        activeSessions.put(player.getUniqueId(), session);
    }

    @Override
    public void stopMusic(Player player) {
        MusicSession session = activeSessions.remove(player.getUniqueId());
        if (session != null) {
            this.sendStopSoundPacket(player, session.getJukeboxLocation());
            session.getTask().cancel();
            //player.sendMessage(CC.translate(PLAYER_PREFIX + "&fMusic stopped."));
        }
    }

    @Override
    public MusicDisc getRandomMusicDisc() {
        if (allDiscs == null || allDiscs.length == 0) {
            return null;
        }
        return allDiscs[random.nextInt(allDiscs.length)];
    }

    @Override
    public Set<MusicDisc> getSelectedMusicDiscs(Profile profile) {
        return profile.getProfileData().getMusicData().getSelectedDiscs().stream()
                .map(name -> {
                    try {
                        return MusicDisc.valueOf(name);
                    } catch (IllegalArgumentException exception) {
                        Logger.logException("Invalid music disc: " + name + " for " + profile.getUuid(), exception);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public MusicDisc getRandomSelectedMusicDisc(Profile profile) {
        Set<MusicDisc> selectedDiscsSet = this.getSelectedMusicDiscs(profile);
        if (selectedDiscsSet.isEmpty()) {
            return this.getRandomMusicDisc();
        }

        List<MusicDisc> selectedDiscs = new ArrayList<>(selectedDiscsSet);
        return selectedDiscs.get(this.random.nextInt(selectedDiscs.size()));
    }

    @Override
    public Optional<MusicSession> getMusicState(UUID playerUuid) {
        return Optional.ofNullable(activeSessions.get(playerUuid));
    }

    @Override
    public List<MusicDisc> getMusicDiscs() {
        return Arrays.asList(this.allDiscs);
    }

    /**
     * Retrieves the active music session for a player by their UUID.
     *
     * @param playerUuid The UUID of the player.
     * @return The active MusicSession, or null if none exists.
     */
    public MusicSession getSession(UUID playerUuid) {
        return this.activeSessions.get(playerUuid);
    }

    /**
     * Sends a packet to the player to play a music disc sound effect at the specified location.
     *
     * @param player   The player to send the packet to.
     * @param disc     The music disc to play.
     * @param location The location where the sound effect should be played.
     */
    @SuppressWarnings("deprecation")
    public void sendPlaySoundPacket(Player player, MusicDisc disc, Location location) {
        BlockPosition pos = new BlockPosition(
                location.getX(),
                location.getY(),
                location.getZ()
        );

        PacketPlayOutWorldEvent packet = new PacketPlayOutWorldEvent(RECORD_PLAY_EFFECT_ID, pos, disc.getMaterial().getId(), false);
        reflection.sendPacket(player, packet);
    }

    /**
     * Sends a packet to the player to stop the music disc sound effect at the specified location.
     *
     * @param player   The player to send the packet to.
     * @param location The location where the sound effect should be stopped.
     */
    public void sendStopSoundPacket(Player player, Location location) {
        BlockPosition pos = new BlockPosition(
                location.getX(),
                location.getY(),
                location.getZ()
        );

        PacketPlayOutWorldEvent packet = new PacketPlayOutWorldEvent(RECORD_PLAY_EFFECT_ID, pos, 0, false);
        reflection.sendPacket(player, packet);
    }
}