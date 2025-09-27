package dev.revere.alley.feature.match.listener.types;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.ListenerUtil;
import dev.revere.alley.common.reflect.ReflectionService;
import dev.revere.alley.common.reflect.internal.types.TitleReflectionServiceImpl;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.VisualsLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.feature.kit.setting.types.mode.KitSettingCheckpoint;
import dev.revere.alley.feature.match.internal.types.CheckpointMatch;
import dev.revere.alley.feature.match.model.GameParticipant;
import dev.revere.alley.feature.match.model.internal.MatchGamePlayer;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author Emmy
 * @project Alley
 * @since 08/02/2025
 */
public class MatchInteractListener implements Listener {
    @EventHandler
    private void handleParkourInteraction(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() != ProfileState.PLAYING) return;
        if (!profile.getMatch().getKit().isSettingEnabled(KitSettingCheckpoint.class)) return;

        if (event.getAction() != Action.PHYSICAL) {
            return;
        }

        Block block = event.getClickedBlock();

        if (ListenerUtil.notSteppingOnPlate(block)) return;

        CheckpointMatch matchCheckpoint = (CheckpointMatch) profile.getMatch();
        MatchGamePlayer matchGamePlayer = matchCheckpoint.getGamePlayer(player);
        if (matchGamePlayer == null) return;
        if (ListenerUtil.checkSteppingOnIronPressurePlate(block)) {
            Location checkpointLocation = player.getLocation();

            matchGamePlayer.setCheckpoint(checkpointLocation);

            boolean isNewCheckpoint = matchGamePlayer.getCheckpoints().stream()
                    .noneMatch(location -> location.getX() == checkpointLocation.getX() &&
                            location.getY() == checkpointLocation.getY() &&
                            location.getZ() == checkpointLocation.getZ());

            if (isNewCheckpoint) {
                matchGamePlayer.getCheckpoints().add(checkpointLocation);
                matchGamePlayer.setCheckpointCount(matchGamePlayer.getCheckpointCount() + 1);

                LocaleService localeService = AlleyPlugin.getInstance().getService(LocaleService.class);

                if (localeService.getBoolean(VisualsLocaleImpl.TITLE_CHECKPOINT_ENABLED_BOOLEAN)) {
                    String header = localeService.getString(VisualsLocaleImpl.TITLE_CHECKPOINT_HEADER);
                    String footer = localeService.getString(VisualsLocaleImpl.TITLE_CHECKPOINT_FOOTER)
                            .replace("{x}", String.valueOf(player.getLocation().getBlockX()))
                            .replace("{y}", String.valueOf(player.getLocation().getBlockY()))
                            .replace("{z}", String.valueOf(player.getLocation().getBlockZ()));
                    int fadeIn = localeService.getInt(VisualsLocaleImpl.TITLE_CHECKPOINT_FADE_IN);
                    int stay = localeService.getInt(VisualsLocaleImpl.TITLE_CHECKPOINT_STAY);
                    int fadeOut = localeService.getInt(VisualsLocaleImpl.TITLE_CHECKPOINT_FADEOUT);

                    AlleyPlugin.getInstance().getService(ReflectionService.class).getReflectionService(TitleReflectionServiceImpl.class).sendTitle(
                            player,
                            header,
                            footer,
                            fadeIn, stay, fadeOut
                    );
                }
            }

            return;
        }

        if (ListenerUtil.checkSteppingOnGoldPressurePlate(block)) {
            GameParticipant<MatchGamePlayer> opponent = matchCheckpoint.getParticipantA().containsPlayer(player.getUniqueId())
                    ? matchCheckpoint.getParticipantB()
                    : matchCheckpoint.getParticipantA();

            opponent.setLostCheckpoint(true);
            opponent.getPlayers().forEach(gamePlayer -> gamePlayer.setDead(true));
            opponent.getPlayers().stream().findAny().ifPresent(gamePlayer -> {
                matchCheckpoint.handleDeath(gamePlayer.getTeamPlayer(), EntityDamageEvent.DamageCause.CUSTOM);
            });
        }
    }
}