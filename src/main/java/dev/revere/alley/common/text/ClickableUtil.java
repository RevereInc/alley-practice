package dev.revere.alley.common.text;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 20:16
 */
@UtilityClass
public class ClickableUtil {
    private final String EMPTY_SPACE_BETWEEN_COMPONENTS = "    ";

    /**
     * Create a clickable component with a command and hover text.
     *
     * @param message   the message to be displayed.
     * @param command   the command to be executed when clicked.
     * @param hoverText the text to be displayed when hovered over.
     * @return a TextComponent that is clickable and has hover text.
     */
    public @NotNull TextComponent createComponent(String message, String command, String hoverText) {
        TextComponent clickableMessage = new TextComponent(CC.translate(message));
        clickableMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));

        String hover = CC.translate(hoverText);
        BaseComponent[] hoverComponent = new ComponentBuilder(hover).create();

        clickableMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent));
        return clickableMessage;
    }

    /**
     * Send clickable page navigation messages to a player.
     * This calculates the current page, the total number of pages, and the base command for navigation.
     * This allows the player to navigate through pages with ease.
     *
     * @param player         the player
     * @param page           the current page
     * @param totalPages     the total number of pages
     * @param command        the command to be executed upon clicking
     * @param keepInPosition whether to keep the next page button in the same position in every page (displayBoth must be false)
     * @param displayBoth    whether to always show both buttons or not (keepInPosition won't affect this, it will be ignored)
     */
    public void sendPageNavigation(Player player, int page, int totalPages, String command, boolean keepInPosition, boolean displayBoth) {
        TextComponent nextPage = createComponent(page == totalPages ? "&a&m[Next Page]" : "&a[Next Page]", command + " " + (page + 1), page == totalPages ? "&cYou are already on the last page." : "&7Click to view page &6" + (page + 1) + "&7.");
        TextComponent previousPage = createComponent(page == 1 ? "&c&m[Previous Page]" : "&c[Previous Page]", command + " " + (page - 1), page == 1 ? "&cYou are already on the first page." : "&7Click to view page &6" + (page - 1) + "&7.");

        if (displayBoth) {
            TextComponent component = new TextComponent("");
            component.addExtra(EMPTY_SPACE_BETWEEN_COMPONENTS);
            component.addExtra(previousPage);
            component.addExtra(EMPTY_SPACE_BETWEEN_COMPONENTS);
            component.addExtra(nextPage);
            player.spigot().sendMessage(component);
        } else {
            if (page > 1 && page < totalPages) {
                TextComponent component = new TextComponent("");

                if (keepInPosition) {
                    component.addExtra(EMPTY_SPACE_BETWEEN_COMPONENTS);
                    component.addExtra(nextPage);
                    component.addExtra(EMPTY_SPACE_BETWEEN_COMPONENTS);
                    component.addExtra(previousPage);
                } else {
                    component.addExtra(EMPTY_SPACE_BETWEEN_COMPONENTS);
                    component.addExtra(previousPage);
                    component.addExtra(EMPTY_SPACE_BETWEEN_COMPONENTS);
                    component.addExtra(nextPage);
                }

                player.spigot().sendMessage(component);
            } else if (page < totalPages) {
                TextComponent component = new TextComponent("");
                component.addExtra(EMPTY_SPACE_BETWEEN_COMPONENTS);
                component.addExtra(nextPage);
                player.spigot().sendMessage(component);
            } else if (page > 1) {
                TextComponent component = new TextComponent("");
                component.addExtra(EMPTY_SPACE_BETWEEN_COMPONENTS);
                component.addExtra(previousPage);
                player.spigot().sendMessage(component);
            }
        }
    }
}