package dev.revere.alley.game.party.command.impl.donator;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.cooldown.Cooldown;
import dev.revere.alley.base.cooldown.CooldownService;
import dev.revere.alley.base.cooldown.enums.CooldownType;
import dev.revere.alley.config.locale.impl.PartyLocale;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.game.party.enums.PartyState;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * @author Emmy
 * @project Alley
 * @date 17/11/2024 - 11:16
 */
public class PartyAnnounceCommand extends BaseCommand {
    @CommandData(name = "party.announce", aliases = {"p.announce"}, permission = "alley.donator.party.announce")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getParty() == null) {
            player.sendMessage(CC.translate(PartyLocale.NOT_IN_PARTY.getMessage()));
            return;
        }

        if (!profile.getState().equals(ProfileState.LOBBY)) {
            player.sendMessage(CC.translate("&cYou must be in the lobby to announce to your party."));
            return;
        }

        if (profile.getParty().getState() != PartyState.PUBLIC) {
            player.sendMessage(CC.translate("&cYour party is not open to the public to announce. Please run the following command: &7/party open"));
            return;
        }

        CooldownService cooldownService = this.plugin.getService(CooldownService.class);
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownService.getCooldown(player.getUniqueId(), CooldownType.PARTY_ANNOUNCE_COOLDOWN));
        if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
            player.sendMessage(CC.translate("&cYou must wait " + optionalCooldown.get().remainingTimeInMinutes() + " &cbefore announcing your party again."));
            return;
        }

        Cooldown cooldown = optionalCooldown.orElseGet(() -> {
            Cooldown newCooldown = new Cooldown(CooldownType.PARTY_ANNOUNCE_COOLDOWN, () -> player.sendMessage(CC.translate("&aYour party announce cooldown has expired.")));
            cooldownService.addCooldown(player.getUniqueId(), CooldownType.PARTY_ANNOUNCE_COOLDOWN, newCooldown);
            return newCooldown;
        });

        cooldown.resetCooldown();

        this.plugin.getService(PartyService.class).announceParty(profile.getParty());
    }
}