package dev.revere.alley.game.ffa.command.impl.player;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.game.ffa.FFAService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class FFAJoinCommand extends BaseCommand {
    @CommandData(name = "ffa.join")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage(CC.translate("&6Usage: &e/ffa join &b<kit>"));
            return;
        }

        String kitName = args[0];
        Kit kit = this.plugin.getKitService().getKit(kitName);
        if (kit == null) {
            player.sendMessage("Kit not found.");
            return;
        }

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        if (profile.getParty() != null) {
            player.sendMessage(CC.translate("&cYou must leave your party to join FFA."));
            return;
        }

        FFAService ffaService = this.plugin.getFfaService();
        ffaService.getMatches().stream()
                .filter(match -> match.getKit().equals(kit))
                .filter(match -> match.getPlayers().size() < match.getMaxPlayers())
                .findFirst()
                .ifPresent(match -> match.join(player));

    }
}