package veksiak.serverlocker.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import veksiak.serverlocker.ServerLocker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ServerLock implements TabExecutor {
    private final ServerLocker plugin;

    public ServerLock(ServerLocker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player sender = (Player) commandSender;
            if (!sender.hasPermission("serverlocker.serverlock")) {
                sender.sendMessage(ChatColor.GOLD + "[ServerLocker] " + ChatColor.RED + "You don't have permission to use this command.");
                //If the player doesn't have the permission to execute the command, a message will be sent to the console. I'm doing this for every important command.
                plugin.getLogger().info(sender.getName() + " (UUID: " + sender.getUniqueId() + ") Tried to lock the server, but failed because of permissions.");
                return true;
            }
            if (args.length == 1) {
                switch (args[0]) {
                    case "all":
                        plugin.updateConfigAndVariables("all", null, null, sender.getUniqueId().toString(), sender.getName());
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (isAffected(player, sender)) {
                                player.kickPlayer(ChatColor.GOLD + "[ServerLocker] " + ChatColor.RED + "The server is locked.");
                            }
                        }
                        sender.sendMessage(ChatColor.GOLD + "[ServerLocker] " + ChatColor.GREEN + "Server has been successfully locked." + ChatColor.BLUE + " Lock type: all");
                        plugin.getLogger().info("Server has been successfully locked by " + sender.getName() + " UUID: " + sender.getUniqueId() + " Lock type: " + plugin.getLockType());
                        return true;
                    case "whitelist":
                        List<String> whitelist = new ArrayList<>();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            whitelist.add(player.getUniqueId().toString());
                        }
                        plugin.updateConfigAndVariables("whitelist", whitelist, null, sender.getUniqueId().toString(), sender.getName());
                        sender.sendMessage(ChatColor.GOLD + "[ServerLocker] " + ChatColor.GREEN + "Server has been successfully locked." + ChatColor.BLUE + " Lock type: whitelist");
                        plugin.getLogger().info("Server has been successfully locked by " + sender.getName() + " UUID: " + sender.getUniqueId() + " Lock type: " + plugin.getLockType());
                        return true;
                }
            } else if (args.length == 2) {
                if (Objects.equals(args[0], "permission")) {
                    plugin.updateConfigAndVariables("permission", null, args[1], sender.getUniqueId().toString(), sender.getName());
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (isAffected(player, sender))
                            player.kickPlayer(ChatColor.GOLD + "[ServerLocker] " + ChatColor.RED + "The server is locked.");
                    }
                    sender.sendMessage(ChatColor.GOLD + "[ServerLocker] " + ChatColor.GREEN + "Server has been successfully locked." + ChatColor.BLUE + " Lock type: permission, Permission: " + args[1]);
                    plugin.getLogger().info("Server has been successfully locked by " + sender.getName() + " UUID: " + sender.getUniqueId() + " Lock type: " + plugin.getLockType());
                    return true;
                } else if (Objects.equals(args[0], "blacklist")) {
                    if (Objects.equals(args[1], "new")) {
                        List<String> blacklist = new ArrayList<>();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (isAffected(player, sender)) blacklist.add(player.getAddress().toString());
                        }
                        if (blacklist.size() == 0) {
                            sender.sendMessage(ChatColor.GOLD + "[ServerLocker] " + ChatColor.RED + "Error! Blacklist can't be empty.");
                            return true;
                        }
                        plugin.updateConfigAndVariables("blacklist", blacklist, null, sender.getUniqueId().toString(), sender.getName());
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.kickPlayer(ChatColor.GOLD + "[ServerLocker] " + ChatColor.RED + "The server is locked.");
                        }
                        sender.sendMessage(ChatColor.GOLD + "[ServerLocker] " + ChatColor.GREEN + "Server has been successfully locked." + ChatColor.BLUE + " Lock type: blacklist");
                        plugin.getLogger().info("Server has been successfully locked by " + sender.getName() + " UUID: " + sender.getUniqueId() + " Lock type: " + plugin.getLockType());
                        return true;
                    } else if (Objects.equals(args[1], "add")) {
                        if (!Objects.equals(plugin.getLockType(), "blacklist")) {
                            sender.sendMessage(ChatColor.GOLD + "[ServerLocker] " + ChatColor.RED + "Error! Lock type must be set to blacklist in order to add something to it.");
                            return true;
                        }
                        List<String> blacklist = plugin.getPlayerList();
                        int i = 0;
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (isAffected(player, sender)) {
                                if (!blacklist.contains(player.getAddress().toString())) {
                                    blacklist.add(player.getAddress().toString());
                                    i++;
                                }
                            }
                        }
                        if (i == 0) {
                            sender.sendMessage(ChatColor.GOLD + "[ServerLocker] " + ChatColor.RED + "Error! No new players were added to the blacklist.");
                            return true;
                        }
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.kickPlayer(ChatColor.GOLD + "[ServerLocker] " + ChatColor.RED + "The server is locked.");
                        }
                        sender.sendMessage(ChatColor.GOLD + "[ServerLocker] " + ChatColor.GREEN + "Successfully added " + ChatColor.AQUA + i + ChatColor.GREEN + "players to the blacklist.");
                        plugin.getLogger().info("New blacklisted players added by " + sender.getName() + " UUID: " + sender.getUniqueId());
                        return true;
                    }
                }
            }
            sendHelpMessage(sender);
            return true;
        } else if (commandSender instanceof ConsoleCommandSender) {
            ConsoleCommandSender sender = (ConsoleCommandSender) commandSender;
            if (args.length == 1) {
                switch (args[0]) {
                    case "all":
                        plugin.updateConfigAndVariables("all", null, null, null, null);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (isAffected(player)) {
                                player.kickPlayer(ChatColor.GOLD + "[ServerLocker] " + ChatColor.RED + "The server is locked.");
                            }
                        }
                        sender.sendMessage("[ServerLocker] " + "Server has been successfully locked." + " Lock type: all");
                        return true;
                    case "whitelist":
                        List<String> whitelist = new ArrayList<>();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            whitelist.add(player.getUniqueId().toString());
                        }
                        plugin.updateConfigAndVariables("whitelist", whitelist, null, null, null);
                        sender.sendMessage("[ServerLocker] " + "Server has been successfully locked." + " Lock type: whitelist");
                        return true;
                }
            } else if (args.length == 2) {
                if (Objects.equals(args[0], "permission")) {
                    plugin.updateConfigAndVariables("permission", null, args[1], null, null);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (isAffected(player))
                            player.kickPlayer(ChatColor.GOLD + "[ServerLocker] " + ChatColor.RED + "The server is locked.");
                    }
                    sender.sendMessage("[ServerLocker] " + "Server has been successfully locked." + " Lock type: permission, Permission: " + args[1]);
                    return true;
                } else if (Objects.equals(args[0], "blacklist")) {
                    if (Objects.equals(args[1], "new")) {
                        List<String> blacklist = new ArrayList<>();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (isAffected(player)) blacklist.add(player.getAddress().toString());
                        }
                        if (blacklist.size() == 0) {
                            sender.sendMessage("[ServerLocker] " + "Error! Blacklist can't be empty.");
                            return true;
                        }
                        plugin.updateConfigAndVariables("blacklist", blacklist, null, null, null);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.kickPlayer("[ServerLocker] " + "The server is locked.");
                        }
                        sender.sendMessage("[ServerLocker] " + "Server has been successfully locked." + " Lock type: blacklist");
                        return true;
                    } else if (Objects.equals(args[1], "add")) {
                        if (!Objects.equals(plugin.getLockType(), "blacklist")) {
                            sender.sendMessage("[ServerLocker] " + "Error! Lock type must be set to blacklist in order to add something to it.");
                            return true;
                        }
                        List<String> blacklist = plugin.getPlayerList();
                        int i = 0;
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (isAffected(player)) {
                                if (!blacklist.contains(player.getAddress().toString())) {
                                    blacklist.add(player.getAddress().toString());
                                    i++;
                                }
                            }
                        }
                        if (i == 0) {
                            sender.sendMessage("[ServerLocker] " + "Error! No new players were added to the blacklist.");
                            return true;
                        }
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.kickPlayer("[ServerLocker] " + "The server is locked.");
                        }
                        sender.sendMessage("[ServerLocker] " + "Successfully added " + i + "players to the blacklist.");
                        return true;
                    }
                }
            }
            sendHelpMessage(sender);
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> possibilities = new ArrayList<>();
        if (args.length == 1 && commandSender.hasPermission("serverlocker.serverlock")) {
            possibilities.add("all");
            possibilities.add("permission");
            possibilities.add("whitelist");
            possibilities.add("blacklist");
        } else if (args.length == 2 && commandSender.hasPermission("serverlocker.serverlock")) {
            possibilities.add("new");
            possibilities.add("add");
        }
        return possibilities;
    }

    private void sendHelpMessage(Player player) {
        player.sendMessage(ChatColor.GOLD + "[ServerLocker] /serverlock: ");
        player.sendMessage(ChatColor.GREEN + "/serverlock all" + ChatColor.AQUA + " - Kicks all players and locks the server for everyone.");
        player.sendMessage(ChatColor.GREEN + "/serverlock permission <permission>" + ChatColor.AQUA + " - Kicks all players without a specified permission and locks the server for every player that doesn't have it.");
        player.sendMessage(ChatColor.GREEN + "/serverlock whitelist" + ChatColor.AQUA + " - Locks the server for all players except the players that are currently on the server.");
        player.sendMessage(ChatColor.GREEN + "/serverlock blacklist new" + ChatColor.AQUA + " - Kicks all players and locks the server for them");
        player.sendMessage(ChatColor.GREEN + "/serverlock blacklist add" + ChatColor.AQUA + " - Adds all players that are currently on the server to a previously set blacklist");
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Players with the permission " + ChatColor.YELLOW + "serverlocker.immunity" + ChatColor.LIGHT_PURPLE + " will be completely ignored by the plugin.");
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Have in mind that depending on what is set in the plugin's config the command sender and server operators may not be affected.");
    }

    private void sendHelpMessage(ConsoleCommandSender sender) {
        sender.sendMessage("[ServerLocker] /serverlock: ");
        sender.sendMessage("/serverlock all" + " - Kicks all players and locks the server for everyone.");
        sender.sendMessage("/serverlock permission <permission>" + " - Kicks all players without a specified permission and locks the server for every player that doesn't have it.");
        sender.sendMessage("/serverlock whitelist" + " - Locks the server for all players except the players that are currently on the server.");
        sender.sendMessage("/serverlock blacklist new" + " - Kicks all players and locks the server for them");
        sender.sendMessage("/serverlock blacklist add" + " - Adds all players that are currently on the server to a previously set blacklist and kicks them.");
        sender.sendMessage("Players with the permission= " + "serverlocker.immunity" + " will be completely ignored by the plugin.");
        sender.sendMessage("Please have in mind that depending on what is set in the plugin's config the command sender and server operators may not be affected.");
    }

    private boolean isAffected(Player player, Player sender) {
        return !((player.isOp() && plugin.isIgnoreOperators()) || (Objects.equals(player, sender) && plugin.isIgnoreSender()) || player.hasPermission("serverlocker.immunity"));
    }

    private boolean isAffected(Player player) {
        return !((player.isOp() && plugin.isIgnoreOperators()) || player.hasPermission("serverlocker.immunity"));
    }
}
