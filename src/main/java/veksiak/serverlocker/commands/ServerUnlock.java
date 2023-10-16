package veksiak.serverlocker.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import veksiak.serverlocker.ServerLocker;

import java.util.Objects;

public final class ServerUnlock implements CommandExecutor {
    private final ServerLocker plugin;

    public ServerUnlock(ServerLocker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (!player.hasPermission("serverlocker.serverunlock")) {
                player.sendMessage(ChatColor.GOLD + "[ServerLocker] " + ChatColor.RED + "You don't have permission to use this command.");
                plugin.getLogger().info(player.getName() + " (UUID: " + player.getUniqueId() + ") Tried to unlock the server, but failed because of permissions.");
                return true;
            }
            if (Objects.equals(plugin.getLockType(), "none")) {
                player.sendMessage(ChatColor.GOLD + "[ServerLocker] " + ChatColor.RED + "Error! The server is not locked.");
                return true;
            }
            plugin.updateConfigAndVariables("none", null, null, null, null);
            player.sendMessage(ChatColor.GOLD + "[ServerLocker] " + ChatColor.GREEN + "The server has been successfully unlocked.");
            plugin.getLogger().info("Server has been successfully unlocked by " + player.getName() + " UUID: " + player.getUniqueId());
        } else if (commandSender instanceof ConsoleCommandSender) {
            ConsoleCommandSender sender = (ConsoleCommandSender) commandSender;
            if (Objects.equals(plugin.getLockType(), "none")) {
                sender.sendMessage("[ServerLocker] " + "Error! The server is not locked.");
                return true;
            }
            plugin.updateConfigAndVariables("none", null, null, null, null);
            sender.sendMessage("[ServerLocker] The server has been successfully unlocked.");
        }
        return true;
    }
}
