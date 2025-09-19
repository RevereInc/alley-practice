package dev.revere.alley.feature.match.command.admin.impl;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.feature.match.MatchService;
import dev.revere.alley.feature.match.model.GameParticipant;
import dev.revere.alley.feature.match.model.internal.MatchGamePlayer;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.library.command.annotation.CompleterData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class MatchStartCommand extends BaseCommand {
    @CompleterData(name = "match.start")
    public List<String> matchStartCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();
        Player player = command.getPlayer();

        if (player.hasPermission("alley.admin")) {
            switch (command.getArgs().length) {
                case 1:
                case 2:
                    for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
                        completion.add(onlinePlayer.getName());
                    }
                    break;
                case 3:
                    this.plugin.getService(KitService.class).getKits().forEach(kit -> completion.add(kit.getName()));
                    break;
                case 4:
                    Kit kit = this.plugin.getService(KitService.class).getKit(command.getArgs()[2]);
                    if (kit != null) {
                        this.plugin.getService(ArenaService.class).getArenas()
                                .stream()
                                .filter(arena -> arena.getKits().contains(kit.getName()))
                                .forEach(arena -> completion.add(arena.getName()));
                    }
                    break;
                default:
                    break;
            }
        }
        return completion;
    }

    @CommandData(
            name = "match.start",
            isAdminOnly = true,
            aliases = {"mstart"},
            cooldown = 1,
            usage = "match start <player1> <player2> <kit> <arena>",
            description = "Start a match between two players with a specific kit and arena"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 4) {
            player.sendMessage(CC.translate("&6Usage: &e/match start &6<player1> <player2> <kit> <arena>"));
            return;
        }

        Player player1 = player.getServer().getPlayer(args[0]);
        Player player2 = player.getServer().getPlayer(args[1]);
        String kitName = args[2];
        String arenaName = args[3];

        if (player1 == null || player2 == null) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        Kit kit = this.plugin.getService(KitService.class).getKit(kitName);
        if (kit == null) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.KIT_NOT_FOUND).replace("{kit-name}", kitName));
            return;
        }

        Arena arena = this.plugin.getService(ArenaService.class).getArenaByName(arenaName);
        if (arena == null) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ARENA_NOT_FOUND).replace("{arena-name}", kitName));
            return;
        }

        MatchGamePlayer playerA = new MatchGamePlayer(player1.getUniqueId(), player1.getName());
        MatchGamePlayer playerB = new MatchGamePlayer(player2.getUniqueId(), player2.getName());

        GameParticipant<MatchGamePlayer> participantA = new GameParticipant<>(playerA);
        GameParticipant<MatchGamePlayer> participantB = new GameParticipant<>(playerB);

        this.plugin.getService(MatchService.class).createAndStartMatch(
                kit, this.plugin.getService(ArenaService.class).selectArenaWithPotentialTemporaryCopy(arena), participantA, participantB, false, false, false
        );
    }
}