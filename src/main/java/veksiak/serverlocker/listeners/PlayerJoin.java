package veksiak.serverlocker.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import veksiak.serverlocker.ServerLocker;

import java.util.Objects;

public final class PlayerJoin implements Listener {
    private final ServerLocker plugin;

    public PlayerJoin(ServerLocker plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        //Check if the player that tries to join the server should be affected by the plugin.
        if (!((player.isOp() && plugin.isIgnoreOperators()) || (Objects.equals(player.getUniqueId().toString(), plugin.getSender()) && plugin.isIgnoreSender()) || player.hasPermission("serverlocker.immunity"))) {
            switch (plugin.getLockType()) {
                case "all": {
                    player.kickPlayer(ChatColor.GOLD + "[ServerLocker] " + ChatColor.RED + "The server is locked.");
                    break;
                }
                case "permission": {
                    if (!player.hasPermission(plugin.getPermission()))
                        player.kickPlayer(ChatColor.GOLD + "[ServerLocker] " + ChatColor.RED + "The server is locked.");
                    break;
                }
                case "whitelist": {
                    if (!plugin.getPlayerList().contains(player.getUniqueId().toString()))
                        player.kickPlayer(ChatColor.GOLD + "[ServerLocker] " + ChatColor.RED + "The server is locked.");
                    break;
                }
                case "blacklist": {
                    if (plugin.getPlayerList().contains(player.getAddress().toString()))
                        player.kickPlayer(ChatColor.GOLD + "[ServerLocker] " + ChatColor.RED + "The server is locked.");
                    break;
                }
            }
        }
    }
}
