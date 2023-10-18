package veksiak.serverlocker.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Help implements CommandExecutor {

    private final List<String> helpMessages;

    public Help() {
        this.helpMessages = new ArrayList<>();
        Collections.addAll(this.helpMessages,
                "§6[ServerLocker] Commands: ",
                "§a/serverlock all §b- Kicks all players and locks the server for everyone.",
                "§a/serverlock permission <permission> §b- Kicks all players without a specified permission and locks the server for every player that doesn't have it.",
                "§cWarning! Always use real permissions when setting the permission server lock.",
                "§a/serverlock whitelist §b- Locks the server for all players except the players that are currently on the server.",
                "§a/serverlock blacklist new §b- Kicks all players and locks the server for them.",
                "§a/serverlock blacklist add §b- Adds all players that are currently on the server to a previously set blacklist.",
                "§a/serverunlock §b- Unlocks the server by lifting the current lock.",
                "§a/lockstatus §b- Displays the status of the server lock.",
                "§dPlayers with the permission §eserverlocker.immunity §dwill be completely ignored by the plugin.",
                "§dHave in mind that depending on what is set in the plugin's config the command sender and server operators may not be affected.");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("serverlocker.help")) {
            commandSender.sendMessage(ChatColor.GOLD + "[ServerLocker] " + ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }
        this.sendMessage(commandSender);
        return true;
    }

    private void sendMessage(CommandSender commandSender) {
        for (String string : this.helpMessages) {
            commandSender.sendMessage((commandSender instanceof Player ? string : string.replaceAll("§[0-9a-f]", "")));
        }
    }
}
