package dev.revere.alley.feature.tip.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.tip.Tip;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 25/04/2025
 */
public class TipCommand extends BaseCommand {
    private final Tip tip;

    public TipCommand() {
        this.tip = new Tip();
    }

    @CommandData(name = "tip", aliases = {"tips"}, cooldown = 5)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage(CC.translate(this.tip.getRandomTip()));
    }
}