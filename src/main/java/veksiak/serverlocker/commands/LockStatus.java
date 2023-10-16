package veksiak.serverlocker.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import veksiak.serverlocker.ServerLocker;

public final class LockStatus implements CommandExecutor {
    private final ServerLocker plugin;

    public LockStatus(ServerLocker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (!player.hasPermission("serverlocker.lockstatus")) {
                player.sendMessage(ChatColor.GOLD + "[ServerLocker] " + ChatColor.RED + "You don't have permission to use this command.");
                plugin.getLogger().info(player.getName() + " (UUID: " + player.getUniqueId() + ") Tried to check the server's lock status, but failed because of permissions.");
                return true;
            }
            player.sendMessage(ChatColor.GOLD + "[ServerLocker] Status: ");
            if (plugin.getSender() != null && plugin.getSenderName() != null)
                player.sendMessage(ChatColor.GREEN + "Command sender: " + ChatColor.AQUA + plugin.getSenderName());
            player.sendMessage(ChatColor.GREEN + "Lock type: " + ChatColor.AQUA + plugin.getLockType());
            switch (plugin.getLockType()) {
                case "permission":
                    player.sendMessage(ChatColor.GREEN + "Permission: " + ChatColor.AQUA + plugin.getPermission());
                    break;
                case "whitelist":
                    player.sendMessage(ChatColor.GREEN + "Whitelist: " + ChatColor.AQUA + "Due to safety reasons: Available in the config of the plugin.");
                    break;
                case "blacklist":
                    player.sendMessage(ChatColor.GREEN + "Blacklist: " + ChatColor.AQUA + "Due to safety reasons: Available in the config of the plugin.");
                    break;
            }
            player.sendMessage(ChatColor.LIGHT_PURPLE + "IgnoreSender: " + ChatColor.YELLOW + plugin.isIgnoreSender());
            player.sendMessage(ChatColor.LIGHT_PURPLE + "IgnoreOperators: " + ChatColor.YELLOW + plugin.isIgnoreOperators());
        } else if (commandSender instanceof ConsoleCommandSender) {
            ConsoleCommandSender sender = (ConsoleCommandSender) commandSender;
            sender.sendMessage("[ServerLocker] Status: ");
            if (plugin.getSender() != null && plugin.getSenderName() != null)
                sender.sendMessage("Command sender: " + plugin.getSenderName());
            sender.sendMessage("Lock type: " + plugin.getLockType());
            switch (plugin.getLockType()) {
                case "permission":
                    sender.sendMessage("Permission: " + plugin.getPermission());
                    break;
                case "whitelist":
                    sender.sendMessage("Whitelist: " + "Due to safety reasons: Available in the config.yml of the plugin.");
                    break;
                case "blacklist":
                    sender.sendMessage("Blacklist: " + "Due to safety reasons: Available in the config.yml of the plugin.");
                    break;
            }
            sender.sendMessage("IgnoreSender: " + plugin.isIgnoreSender());
            sender.sendMessage("IgnoreOperators: " + plugin.isIgnoreOperators());
        }
        return true;
    }
}
