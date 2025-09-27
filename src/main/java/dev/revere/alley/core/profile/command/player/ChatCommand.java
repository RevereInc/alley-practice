package dev.revere.alley.core.profile.command.player;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.enums.ChatChannel;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 22/10/2024 - 12:14
 */
public class ChatCommand extends BaseCommand {
    @CommandData(
            name = "chat",
            usage = "chat <chatChannel>",
            description = "Set your chat channel."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            player.sendMessage(CC.translate("&cAvailable chat channels: " + ChatChannel.getChatChannelsSorted()));
            return;
        }

        Profile profile = this.getProfile(player.getUniqueId());
        if (ChatChannel.getExactChatChannel(args[0], true) == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.CHAT_CHANNEL_NOT_EXIST).replace("{channel}", args[0]));
            return;
        }

        if (profile.getProfileData().getSettingData().getChatChannel().equalsIgnoreCase(args[0])) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.CHAT_CHANNEL_ALREADY_IN).replace("{channel}", args[0]));
            return;
        }

        profile.getProfileData().getSettingData().setChatChannel(ChatChannel.getExactChatChannel(args[0], true));
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.CHAT_CHANNEL_SET).replace("{channel}", args[0]));
    }
}