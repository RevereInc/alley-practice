package dev.revere.alley.feature.command.impl.other.troll;

import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author Emmy
 * @project Alley
 * @date 28/10/2024 - 09:00
 */
public class TrollCommand extends BaseCommand {
    @CommandData(
            name = "troll",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "troll <player>",
            description = "Opens demo screen for target player"
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&6Usage: &e/troll &6(player)"));
            return;
        }

        Player targetPlayer = this.plugin.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        try {
            String path = this.plugin.getServer().getClass().getPackage().getName();
            String version = path.substring(path.lastIndexOf(".") + 1);

            Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
            Class<?> PacketPlayOutGameStateChange = Class.forName("net.minecraft.server." + version + ".PacketPlayOutGameStateChange");
            Class<?> Packet = Class.forName("net.minecraft.server." + version + ".Packet");
            Constructor<?> playOutConstructor = PacketPlayOutGameStateChange.getConstructor(int.class, float.class);
            Object packet = playOutConstructor.newInstance(5, 0);
            Object craftPlayerObject = craftPlayer.cast(targetPlayer);
            Method getHandleMethod = craftPlayer.getMethod("getHandle");
            Object handle = getHandleMethod.invoke(craftPlayerObject);
            Object pc = handle.getClass().getField("playerConnection").get(handle);
            Method sendPacketMethod = pc.getClass().getMethod("sendPacket", Packet);
            sendPacketMethod.invoke(pc, packet);
        } catch (Exception ex) {
            Logger.error("An error occurred while trying to troll " + targetPlayer.getName() + ": " + ex.getMessage());
        }

        sender.sendMessage(CC.translate("&eYou have trolled &d" + targetPlayer.getName() + "&e."));
    }
}