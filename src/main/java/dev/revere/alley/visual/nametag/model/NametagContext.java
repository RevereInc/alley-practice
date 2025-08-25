package dev.revere.alley.visual.nametag.model;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
@Getter
public class NametagContext {
    private final Profile viewerProfile;
    private final Profile targetProfile;
    private final Player viewer;
    private final Player target;

    public NametagContext(Player viewer, Player target) {
        this.viewer = viewer;
        this.target = target;

        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        this.viewerProfile = profileService.getProfile(viewer.getUniqueId());
        this.targetProfile = profileService.getProfile(target.getUniqueId());
    }
}