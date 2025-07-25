package dev.revere.alley.game.match.utility;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.kit.setting.impl.mode.*;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.game.match.Match;
import dev.revere.alley.game.match.enums.MatchState;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 24/09/2024 - 17:12
 */
@UtilityClass
public class MatchUtility {
    private final Alley plugin = Alley.getInstance();

    /**
     * Check if a location is beyond the bounds of an arena excluding specific conditions.
     *
     * @param location the location
     * @param profile  the profile
     * @return if the location is beyond the bounds
     */
    public boolean isBeyondBounds(Location location, Profile profile) {
        Arena arena = profile.getMatch().getArena();
        Location corner1 = arena.getMinimum();
        Location corner2 = arena.getMaximum();

        double minX = Math.min(corner1.getX(), corner2.getX());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());

        boolean withinBounds;

        /*
         * If the match is ending or has specific kit settings enabled, we only check X and Z bounds and exclude Y bounds,
         * because there is a death y level coordinate that eliminates players when they fall below it.
         * This is to prevent players from being stuck in the air because by default, moving out of bounds is cancelled.
         */
        if (profile.getMatch().getState() == MatchState.ENDING_MATCH
                || profile.getMatch().getKit().isSettingEnabled(KitSettingBed.class)
                || profile.getMatch().getKit().isSettingEnabled(KitSettingLives.class)
                || profile.getMatch().getKit().isSettingEnabled(KitSettingRounds.class)
                || profile.getMatch().getKit().isSettingEnabled(KitSettingStickFight.class)
                || profile.getMatch().getKit().isSettingEnabled(KitSettingCheckpoint.class)) {
            withinBounds = location.getX() >= minX && location.getX() <= maxX && location.getZ() >= minZ && location.getZ() <= maxZ;
        } else {
            withinBounds = location.getX() >= minX && location.getX() <= maxX && location.getY() >= minY && location.getY() <= maxY && location.getZ() >= minZ && location.getZ() <= maxZ;
        }

        return !withinBounds;
    }

    /**
     * Sends a match result message to all participants and spectators.
     *
     * @param match         The match.
     * @param winnerName    The name of the winning team.
     * @param loserName     The name of the losing team.
     * @param winnerUuid    The UUID of the winning team.
     * @param loserUuid     The UUID of the losing team.
     */
    public void sendMatchResult(Match match, String winnerName, String loserName, UUID winnerUuid, UUID loserUuid) {
        FileConfiguration config = Alley.getInstance().getService(ConfigService.class).getMessagesConfig();

        String path = "match.ended.match-result.regular.";

        String winnerCommand = config.getString(path + "winner.command").replace("{winner}", String.valueOf(winnerUuid));
        String winnerHover = config.getString(path + "winner.hover").replace("{winner}", winnerName);
        String loserCommand = config.getString(path + "loser.command").replace("{loser}", String.valueOf(loserUuid));
        String loserHover = config.getString(path + "loser.hover").replace("{loser}", loserName);

        for (String line : Alley.getInstance().getService(ConfigService.class).getMessagesConfig().getStringList(path + "format")) {
            if (line.contains("{winner}") && line.contains("{loser}")) {
                String[] parts = line.split("\\{winner}", 2);

                if (parts.length > 1) {
                    String[] loserParts = parts[1].split("\\{loser}", 2);

                    TextComponent winnerComponent = new TextComponent(CC.translate(winnerName));
                    winnerComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, winnerCommand));
                    winnerComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(winnerHover)).create()));

                    TextComponent loserComponent = new TextComponent(CC.translate(loserName));
                    loserComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, loserCommand));
                    loserComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(loserHover)).create()));

                    sendCombinedSpigotMessage(match,
                            new TextComponent(CC.translate(parts[0])),
                            winnerComponent,
                            new TextComponent(CC.translate(loserParts[0])),
                            loserComponent,
                            new TextComponent(loserParts.length > 1 ? CC.translate(loserParts[1]) : "")
                    );
                }
            } else if (line.contains("{winner}")) {
                String[] parts = line.split("\\{winner}", 2);

                TextComponent winnerComponent = new TextComponent(CC.translate(winnerName));
                winnerComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, winnerCommand));
                winnerComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(winnerHover)).create()));

                sendCombinedSpigotMessage(match,
                        new TextComponent(CC.translate(parts[0])),
                        winnerComponent,
                        new TextComponent(parts.length > 1 ? CC.translate(parts[1]) : "")
                );
            } else if (line.contains("{loser}")) {
                String[] parts = line.split("\\{loser}", 2);

                TextComponent loserComponent = new TextComponent(CC.translate(loserName));
                loserComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, loserCommand));
                loserComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(loserHover)).create()));

                sendCombinedSpigotMessage(match,
                        new TextComponent(CC.translate(parts[0])),
                        loserComponent,
                        new TextComponent(parts.length > 1 ? CC.translate(parts[1]) : "")
                );
            } else {
                match.sendMessage(CC.translate(line));
            }
        }
    }

    /**
     * Sends the conjoined match result message.
     *
     * @param match             The match.
     * @param winnerParticipant The winner participant.
     * @param loserParticipant  The loser participant.
     */
    public void sendConjoinedMatchResult(Match match, GameParticipant<MatchGamePlayerImpl> winnerParticipant, GameParticipant<MatchGamePlayerImpl> loserParticipant) {
        String winnerTeamName = winnerParticipant.getLeader().getUsername();
        String loserTeamName = loserParticipant.getLeader().getUsername();

        match.sendMessage("");
        match.sendMessage(CC.translate("&aWinner Team: &f" + winnerTeamName));

        for (MatchGamePlayerImpl player : winnerParticipant.getAllPlayers()) {
            String playerName = player.getUsername();

            TextComponent playerComponent = new TextComponent(CC.translate("&7- &f" + playerName));
            playerComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/inventory " + playerName));
            playerComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(CC.translate("&eClick to view " + playerName + "'s inventory")).create()));

            sendCombinedSpigotMessage(match, playerComponent);
        }

        match.sendMessage("");
        match.sendMessage(CC.translate("&cLoser Team: &f" + loserTeamName));

        for (MatchGamePlayerImpl player : loserParticipant.getAllPlayers()) {
            String playerName = player.getUsername();

            TextComponent playerComponent = new TextComponent(CC.translate("&7- &f" + playerName));
            playerComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/inventory " + playerName));
            playerComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(CC.translate("&eClick to view " + playerName + "'s inventory")).create()));

            sendCombinedSpigotMessage(match, playerComponent);
        }

        match.sendMessage(CC.translate(""));
    }

    /**
     * Sends a combined spigot (clickable) message to all participants including spectators.
     *
     * @param message The message to send.
     */
    public void sendCombinedSpigotMessage(Match match, BaseComponent... message) {
        match.getParticipants().forEach(gameParticipant -> {
            gameParticipant.getPlayers().forEach(uuid -> {
                Player player = plugin.getServer().getPlayer(uuid.getUuid());
                if (player != null) {
                    player.spigot().sendMessage(message);
                }
            });
        });

        match.getSpectators().forEach(uuid -> {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player != null) {
                player.spigot().sendMessage(message);
            }
        });
    }
}
