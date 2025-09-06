package dev.revere.alley.feature.host.command;

import dev.revere.alley.core.config.internal.locale.impl.ErrorLocale;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.host.menu.HostMenu;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 08/06/2024 - 21:31
 */
public class HostCommand extends BaseCommand {
    @Override
    @CommandData(name = "host", permission = "alley.command.donator.host")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId()).getState() != ProfileState.LOBBY) {
            player.sendMessage(ErrorLocale.MUST_BE_IN_LOBBY.getMessage());
            return;
        }

        new HostMenu().openMenu(player);
    }
}
