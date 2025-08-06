package dev.revere.alley.feature.party.command.impl.external;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.party.PartyService;
import dev.revere.alley.feature.party.Party;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 23/01/2025
 */
public class PartyLookupCommand extends BaseCommand {
    @CommandData(name = "party.lookup", aliases = {"pl"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/party lookup &6<player>"));
            return;
        }

        Player target = this.plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cInvalid player."));
            return;
        }

        Party party = this.plugin.getService(PartyService.class).getParty(target);
        if (party == null) {
            player.sendMessage(CC.translate("&cThis player is not in a party."));
            return;
        }

        Arrays.asList(
                "&6&l" + party.getLeader().getName() + "'s Party",
                " &6│ &6Leader: &f" + party.getLeader().getName(),
                " &6│ &6Members: &f" + party.getMembers().size(),
                " &6│ &6Status: &f" + (party.getState().getName()),
                " &6│ &6Privacy: &f" + (party.getState().getDescription()
                )).forEach(msg -> player.sendMessage(CC.translate(msg)));
    }
}