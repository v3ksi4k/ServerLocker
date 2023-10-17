package veksiak.serverlocker.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
public class Help implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player sender = (Player) commandSender;
            if (!sender.hasPermission("serverlocker.help")) {
                sender.sendMessage(ChatColor.GOLD + "[ServerLocker] " + ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }
            //TODO: Wrap this monster in a method.
            sender.sendMessage(ChatColor.GOLD + "[ServerLocker] Commands: ");
            sender.sendMessage(ChatColor.GREEN + "/serverlock all" + ChatColor.AQUA + " - Kicks all players and locks the server for everyone.");
            sender.sendMessage(ChatColor.GREEN + "/serverlock permission <permission>" + ChatColor.AQUA + " - Kicks all players without a specified permission and locks the server for every player that doesn't have it.");
            sender.sendMessage(ChatColor.RED+"Warning! Always use real permissions when setting the permission server lock.");
            sender.sendMessage(ChatColor.GREEN + "/serverlock whitelist" + ChatColor.AQUA + " - Locks the server for all players except the players that are currently on the server.");
            sender.sendMessage(ChatColor.GREEN + "/serverlock blacklist new" + ChatColor.AQUA + " - Kicks all players and locks the server for them.");
            sender.sendMessage(ChatColor.GREEN + "/serverlock blacklist add" + ChatColor.AQUA + " - Adds all players that are currently on the server to a previously set blacklist.");
            sender.sendMessage(ChatColor.GREEN + "/serverunlock" + ChatColor.AQUA + " - Unlocks the server by lifting the current lock");
            sender.sendMessage(ChatColor.GREEN + "/lockstatus" + ChatColor.AQUA + " - Displays the status of the server lock.");
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Players with the permission " + ChatColor.YELLOW + "serverlocker.immunity" + ChatColor.LIGHT_PURPLE + " will be completely ignored by the plugin.");
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Have in mind that depending on what is set in the plugin's config the command sender and server operators may not be affected.");
        } else if (commandSender instanceof ConsoleCommandSender) {
            ConsoleCommandSender sender = (ConsoleCommandSender) commandSender;
            sender.sendMessage("[ServerLocker] Commands: ");
            sender.sendMessage("/serverlock all - Kicks all players and locks the server for everyone.");
            sender.sendMessage("/serverlock permission <permission> - Kicks all players without a specified permission and locks the server for every player that doesn't have it.");
            sender.sendMessage("Warning! Always use real permissions when setting the permission server lock.");
            sender.sendMessage("/serverlock whitelist - Locks the server for all players except the players that are currently on the server.");
            sender.sendMessage("/serverlock blacklist new - Kicks all players and locks the server for them.");
            sender.sendMessage("/serverlock blacklist add - Adds all players that are currently on the server to a previously set blacklist.");
            sender.sendMessage("/serverunlock - Unlocks the server by lifting the current lock.");
            sender.sendMessage("/lockstatus - Displays the status of the server lock.");
            sender.sendMessage("Players with the permission " + "serverlocker.immunity" + " will be completely ignored by the plugin.");
            sender.sendMessage("Have in mind that depending on what is set in the plugin's config the command sender and server operators may not be affected.");
        }
        return true;
    }
}
