package dev.revere.alley.feature.abilities.command;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.feature.abilities.AbilityService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AbilityCommand extends BaseCommand {
    @CommandData(
            name = "ability",
            permission = "hypractice.command.ability"
    )
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
                    player.sendMessage(CC.translate("&cUsage: /ability give <player> <ability|all> <amount>"));
                    return;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    player.sendMessage(CC.translate("&cPlayer '" + args[1] + "' not found."));
                    return;
                }

                Integer amount;
                try {
                    amount = Integer.parseInt(args[3]);
                } catch (NumberFormatException exception) {
                    amount = null;
                }

                if (amount == null) {
                    player.sendMessage(CC.translate("&cAmount must be a number."));
                    return;
                }
                if (amount <= 0) {
                    player.sendMessage(CC.translate("&cAmount must be positive."));
                    return;
                }

                int finalAmount = amount;
                plugin.getService(AbilityService.class).getAbilityKeys().forEach(ability -> {
                    String displayName = plugin.getService(ConfigService.class)
                            .getAbilityConfig()
                            .getString(ability + ".ICON.DISPLAYNAME");

                    if (args[2].equalsIgnoreCase(ability)) {
                        plugin.getService(AbilityService.class).giveAbility(player, target, ability, displayName, finalAmount);
                        return;
                    }

                    if (args[2].equalsIgnoreCase("all")) {
                        plugin.getService(AbilityService.class).giveAbility(player, target, ability, displayName, finalAmount);
                    }
                });
                break;

            case "list":
                player.sendMessage(CC.translate("&7&m-----------------------------"));
                player.sendMessage(CC.translate("&c&lAbilities List &7(" +
                        this.plugin.getService(AbilityService.class).getAbilityKeys().size() + ")"));
                player.sendMessage("");

                plugin.getService(AbilityService.class).getAbilityKeys().forEach(
                        ability -> player.sendMessage(CC.translate(" &7- &4" + ability)));

                player.sendMessage(CC.translate("&7&m-----------------------------"));
                break;
        }
    }

    private void getUsage(CommandSender sender, String label) {
        sender.sendMessage(CC.translate("&7&m-----------------------------"));
        sender.sendMessage(CC.translate("&c&lAbility Help"));
        sender.sendMessage("");
        sender.sendMessage(CC.translate("&4/" + label + " give <player> <ability|all> <amount>"));
        sender.sendMessage(CC.translate("&4/" + label + " list"));
        sender.sendMessage(CC.translate("&7&m-----------------------------"));
    }
}