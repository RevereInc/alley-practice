package dev.revere.alley.feature.abilities.command;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.feature.abilities.AbilityService;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Getter
@Setter
public class AbilityCommand extends BaseCommand {

    private Alley plugin = Alley.getInstance();

    @CommandData(name = "ability", permission = "hypractice.command.ability")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();
        if (args.length < 1) {
            this.getUsage(player, "ability");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "give":
                if (args.length < 4) {
                    CC.sender(player, "&cUsage: /" + "ability" + " give <player> <ability|all> <amount>");
                    return;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    CC.sender(player, "&cPlayer '" + args[1] + "' not found.");
                    return;
                }

                Integer amount;
                try {
                    amount = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    amount = null;
                }

                if (amount == null) {
                    CC.sender(player, "&cAmount must be a number.");
                    return;
                }
                if (amount <= 0) {
                    CC.sender(player, "&cAmount must be positive.");
                    return;
                }

                Integer finalAmount = amount;
                plugin.getService(AbilityService.class).getAbilityKeys().forEach(ability -> {
                    String displayName = Alley.getInstance().getService(ConfigService.class).getAbilityConfig().getString(ability + ".ICON.DISPLAYNAME");
                    if (args[2].equalsIgnoreCase(ability)) {
                        plugin.getService(AbilityService.class).giveAbility(player, target, ability, displayName, finalAmount);
                        return;
                    }
                    if (args[2].equals("all")) {
                        plugin.getService(AbilityService.class).giveAbility(player, target, ability, displayName, finalAmount);
                    }
                });
                break;
            case "list":
                CC.sender(player, "&7&m-----------------------------");
                CC.sender(player, "&c&lAbilities List &7(" + this.plugin.getService(AbilityService.class).getAbilityKeys().size() + ")");
                CC.sender(player, "");
                plugin.getService(AbilityService.class).getAbilityKeys().forEach(
                        ability -> CC.sender(player, " &7- &4" + ability));
                CC.sender(player, "&7&m-----------------------------");
                break;
        }
        return;
    }

    private void getUsage(CommandSender sender, String label) {
        CC.sender(sender, "&7&m-----------------------------");
        CC.sender(sender, "&c&lAbility Help");
        CC.sender(sender, "");
        CC.sender(sender, "&4/" + label + " give <player> <ability|all> <amount>");
        CC.sender(sender, "&4/" + label + " list");
        CC.sender(sender, "&7&m-----------------------------");
    }
}
