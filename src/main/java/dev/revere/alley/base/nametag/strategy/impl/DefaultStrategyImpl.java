package dev.revere.alley.base.nametag.strategy.impl;

import dev.revere.alley.base.nametag.NametagContext;
import dev.revere.alley.base.nametag.NametagView;
import dev.revere.alley.base.nametag.strategy.NametagStrategy;
import dev.revere.alley.util.chat.CC;
import org.bukkit.ChatColor;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
public class DefaultStrategyImpl implements NametagStrategy {
    @Override
    public NametagView createNametagView(NametagContext context) {
        String prefix = context.getTargetProfile().getNameColor().toString();

        if (prefix == null || prefix.isEmpty()) {
            prefix = ChatColor.GRAY.toString();
        }

        return new NametagView(CC.translate(prefix), "");
    }
}