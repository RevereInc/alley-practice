package dev.revere.alley.game.match.command.admin.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.arena.Arena;
import dev.revere.alley.arena.ArenaType;
import dev.revere.alley.kit.Kit;
import dev.revere.alley.kit.settings.impl.KitSettingLivesImpl;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.impl.MatchLivesRegularImpl;
import dev.revere.alley.game.match.impl.MatchRegularImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.queue.Queue;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.Completer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class MatchStartCommand extends BaseCommand {

    @Completer(name = "match.start")
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
                    // all arenas:
                    // Alley.getInstance().getArenaRepository().getArenas().forEach(arena -> completion.add(arena.getName()));

                    //excluding FFA arenas
                    Alley.getInstance().getArenaRepository().getArenas().stream().filter(arena -> arena.getType() != ArenaType.FFA).forEach(arena -> completion.add(arena.getName()));
                    break;
                case 4:
                    Alley.getInstance().getKitRepository().getKits().forEach(kit -> completion.add(kit.getName()));
                    break;
                default:
                    break;
            }
        }

        return completion;
    }

    @Command(name = "match.start", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 4) {
            player.sendMessage(CC.translate("&6Usage: &e/match start &b<player1> <player2> <arena> <kit>"));
            return;
        }

        Player player1 = player.getServer().getPlayer(args[0]);
        Player player2 = player.getServer().getPlayer(args[1]);
        String arenaName = args[2];
        String kitName = args[3];

        if (player1 == null || player2 == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        Arena arena = Alley.getInstance().getArenaRepository().getArenaByName(arenaName);
        if (arena == null) {
            player.sendMessage(CC.translate("&cArena not found."));
            return;
        }

        Kit kit = Alley.getInstance().getKitRepository().getKit(kitName);
        if (kit == null) {
            player.sendMessage(CC.translate("&cKit not found."));
            return;
        }

        MatchGamePlayerImpl playerA = new MatchGamePlayerImpl(player1.getUniqueId(), player1.getName());
        MatchGamePlayerImpl playerB = new MatchGamePlayerImpl(player2.getUniqueId(), player2.getName());

        GameParticipant<MatchGamePlayerImpl> participantA = new GameParticipant<>(playerA);
        GameParticipant<MatchGamePlayerImpl> participantB = new GameParticipant<>(playerB);

        for (Queue queue : Alley.getInstance().getQueueRepository().getQueues()) {
            if (queue.getKit().equals(kit) && !queue.isRanked()) {
                if (queue.getKit().isSettingEnabled(KitSettingLivesImpl.class)) {
                    AbstractMatch match = new MatchLivesRegularImpl(queue, kit, arena, true, participantA, participantB);
                    match.startMatch();
                } else {
                    AbstractMatch match = new MatchRegularImpl(queue, kit, arena, false, participantA, participantB);
                    match.startMatch();
                }
            }
        }
    }
}
