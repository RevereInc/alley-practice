package dev.revere.alley.feature.emoji.command.impl;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.emoji.enums.EmojiType;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.chat.Symbol;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 22/01/2025 - 21:52
 */
public class EmojiListCommand extends BaseCommand {
    @CommandData(name = "emoji.list", aliases = "el", permission = "alley.donator.chat.symbol", usage = "/emoji list", description = "lists all available emojis usable in chat")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&6&lEmojis: &7(Alias: &f/el&7)"));
        for (EmojiType emoji : EmojiType.values()) {
            TextComponent emojiComponent = new TextComponent(CC.translate(" " + emoji.getIdentifier() + " &7" + Symbol.ARROW_R + " &f" + emoji.getFormat()));
            emojiComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, emoji.getIdentifier()));

            String hoverText = "&7Click to apply &f" + emoji.getFormat() + " &7to your chat input.";
            BaseComponent[] hoverComponent = new ComponentBuilder(CC.translate(hoverText)).create();
            emojiComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent));

            player.spigot().sendMessage(emojiComponent);
        }
        player.sendMessage("");
    }
}