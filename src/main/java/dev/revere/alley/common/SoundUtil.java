package dev.revere.alley.common;

import dev.revere.alley.feature.match.Match;
import lombok.experimental.UtilityClass;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 17:11
 */
@UtilityClass
public class SoundUtil {
    /**
     * Play a custom sound to the player
     *
     * @param player the player to play the sound to
     * @param sound  the sound to play
     * @param volume the volume of the sound
     * @param pitch  the pitch of the sound
     */
    public void playCustomSound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    /**
     * Play a ban hammer sound to the player
     *
     * @param player the player to play the sound to
     */
    public void playBanHammer(Player player) {
        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 1.5F);
    }

    /**
     * Play a sound to the player based on the success boolean
     *
     * @param player  the player to play the sound to
     * @param success the boolean to determine the sound
     */
    public void playSound(Player player, boolean success) {
        if (success) {
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 2F, 2F);
        } else {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 2F, 2F);
        }
    }

    /**
     * Play a fail sound to the player (Note Bass)
     *
     * @param player the player to play the sound to
     */
    public void playFail(Player player) {
        player.playSound(player.getLocation(), Sound.NOTE_BASS, 20F, 0.1F);

    }

    /**
     * Play a success sound to the player (Note Pling)
     *
     * @param player the player to play the sound to
     */
    public void playSuccess(Player player) {
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 20F, 15F);
    }

    /**
     * Play a click sound to the player (Click)
     *
     * @param player the player to play the sound to
     */
    public void playClick(Player player) {
        player.playSound(player.getLocation(), Sound.CLICK, 20F, 15F);
    }

    /**
     * Play a neutral sound to the player (Note Sticks)
     *
     * @param player the player to play the sound to
     */
    public void playNeutral(Player player) {
        player.playSound(player.getLocation(), Sound.NOTE_STICKS, 20F, 15F);
    }

    /**
     * Play an Explode sound to the player (Explosion)
     *
     * @param player the player to play the sound to
     */
    public void playExplode(Player player) {
        player.playSound(player.getLocation(), Sound.EXPLODE, 2.0F, 1.5F);
    }

    /**
     * Play a blast sound to the player (Firework Blast)
     *
     * @param player the player to play the sound to
     */
    public void playBlast(Player player) {
        player.playSound(player.getLocation(), Sound.FIREWORK_BLAST, 20F, 15F);
    }

    /**
     * Play a sound to all players in the match if enabled
     *
     * @param match   the match to play the sound in
     * @param sound   the sound to play
     * @param enabled whether the sound is enabled or not
     */
    private void playSoundIfEnabled(Match match, Sound sound, boolean enabled) {
        if (match == null) {
            throw new IllegalArgumentException("Match cannot be null");
        }

        if (enabled) {
            match.playSound(sound);
        }
    }
}