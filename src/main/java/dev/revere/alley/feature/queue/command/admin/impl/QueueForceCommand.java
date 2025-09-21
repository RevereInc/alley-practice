package dev.revere.alley.feature.queue.command.admin.impl;

import dev.revere.alley.common.PlayerUtil;
import dev.revere.alley.common.SoundUtil;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.feature.queue.QueueService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class QueueForceCommand extends BaseCommand {
    @CommandData(
            name = "queue.force",
            aliases = {"forcequeue"},
            isAdminOnly = true,
            usage = "queue force <player> <kit> <ranked>",
            description = "Force a player into a queue."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 3) {
            command.sendUsage();
            player.sendMessage(CC.translate("&7Example: /queue force hmRemi Boxing true"));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        String kitType = args[1];
        boolean ranked = Boolean.parseBoolean(args[2]);

        if (target == null) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        Kit kit = this.plugin.getService(KitService.class).getKit(kitType);
        if (kit == null) {
            player.sendMessage(CC.translate("&cKit not found."));
            return;
        }

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());
        for (Queue queue : this.plugin.getService(QueueService.class).getQueues()) {
            if (queue.getKit().equals(kit) && queue.isRanked() == ranked) {
                queue.addPlayer(target, queue.isRanked() ? profile.getProfileData().getRankedKitData().get(queue.getKit().getName()).getElo() : 0);
                PlayerUtil.reset(target, false, true);
                SoundUtil.playBanHammer(target);
                this.plugin.getService(HotbarService.class).applyHotbarItems(target);

                player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.QUEUE_FORCED_PLAYER)
                        .replace("{player}", target.getName())
                        .replace("{kit}", kit.getName())
                        .replace("{ranked}", ranked ? "ranked" : "unranked")
                );

                if (ranked && profile.getProfileData().isRankedBanned()) {
                    player.sendMessage(CC.translate("&cKeep in mind that &6" + target.getName() + " &cis currently banned from ranked queues!"));
                }

                return;
            }
        }
    }
}
