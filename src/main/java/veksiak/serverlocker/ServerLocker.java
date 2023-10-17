package veksiak.serverlocker;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import veksiak.serverlocker.commands.LockStatus;
import veksiak.serverlocker.commands.ServerLock;
import veksiak.serverlocker.commands.ServerUnlock;
import veksiak.serverlocker.commands.Help;
import veksiak.serverlocker.listeners.PlayerJoin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ServerLocker extends JavaPlugin {
    private final String[] possibleLockTypesArray = {"none", "all", "permission", "whitelist", "blacklist"}; //Used to check if lockType is valid.
    private final FileConfiguration config = getConfig();
    private List<String> possibleLockTypes; //It is created using possibleLockTypesArray to use the contains() method.
    private String lockType;
    private boolean ignoreOperators;
    private String sender;
    private String senderName;
    private boolean ignoreSender;
    private List<String> playerList = new ArrayList<>();
    private String permission;

    @Override
    public void onEnable() {
        setupConfig();
        getVariablesAndFormatConfig();
        registerCommands();
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getLogger().info("Plugin successfully enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin successfully disabled.");
    }

    private void registerCommands() {
        this.getCommand("serverlock").setExecutor(new ServerLock(this));
        this.getCommand("serverunlock").setExecutor(new ServerUnlock(this));
        this.getCommand("lockstatus").setExecutor(new LockStatus(this));
        this.getCommand("serverlocker").setExecutor(new Help());
    }

    private void setupConfig() {
        saveDefaultConfig();
        possibleLockTypes = Arrays.asList(possibleLockTypesArray);
        List<String> comments = new ArrayList<>();
        comments.add("It is not recommended to edit the server lock using the config. If you locked yourself out of the server, you can unlock it by using /serverunlock command in the server's console.");
        comments.add("Available options: none, permission, whitelist, blacklist, all.");
        comments.add("Warning! Always use real permissions when setting the permission server lock.");
        comments.add("After choosing the lock type you need to provide certain arguments for it to work.");
        comments.add("Required arguments: none - none, permission - [Permission] the permission that should be required to join the server,");
        comments.add("whitelist - [PlayerList] a list of player UUID's, blacklist - [PlayerList] a list of player UUID's.");
        comments.add("Providing invalid arguments for a certain lock type will cause the plugin to reset it.");
        config.addDefault("IgnoreSender", true);
        config.addDefault("IgnoreOperators", false);
        config.addDefault("Lock.LockType", "none");
        config.options().setHeader(comments);
        config.options().copyDefaults(true);
        saveConfig();
    }


     /*  Method used to format the config by removing redundant entries, throw warnings if needed and setting emergency locks after detecting invalid config content. */
    private void getVariablesAndFormatConfig() {
        if (config.getString("Lock.LockType") == null) {
            updateConfigAndVariables("none", null, null, null, null);
        } else {
            switch (config.getString("Lock.LockType")) {
                case "permission":
                    if (config.getString("Lock.Permission") == null) {
                        //If invalid data is provided in the config file, the server will get locked for safety reasons.
                        updateConfigAndVariables("all", null, null, null, null);
                        getLogger().warning("No required arguments provided for lock type: permission. For safety reasons the lock type will be set to: all");
                        break;
                    }
                    permission = config.getString("Lock.Permission");
                    updateConfigAndVariables("permission", null, permission, null, null);
                    getLogger().info("Detected active server lock. Type: permission.");
                    break;
                case "whitelist":
                    if (config.getStringList("Lock.PlayerList").size() == 0) {
                        // Setting unnecessary data to null stops the creation of redundant entries in the config.
                        updateConfigAndVariables("all", null, null, null, null);
                        getLogger().warning("No required arguments provided for lock type: whitelist. For safety reasons the lock type will be set to: all");
                        break;
                    }
                    playerList = config.getStringList("Lock.PlayerList");
                    updateConfigAndVariables("whitelist", playerList, null, null, null);
                    getLogger().info("Detected active server lock. Type: whitelist.");
                    break;
                case "blacklist":
                    if (config.getStringList("Lock.PlayerList").size() == 0) {
                        updateConfigAndVariables("all", null, null, null, null);
                        getLogger().warning("No required arguments provided for lock type: whitelist. For safety reasons the lock type will be set to: all");
                        break;
                    }
                    playerList = config.getStringList("Lock.PlayerList");
                    updateConfigAndVariables("blacklist", playerList, null, null, null);
                    getLogger().info("Detected active server lock. Type: blacklist.");
                    break;
                case "all":
                    updateConfigAndVariables("all", null, null, null ,null);
                    getLogger().info("Detected active server lock. Type: all.");
                    break;
            }
        }
        if (!possibleLockTypes.contains(config.getString("Lock.LockType"))) {
            getLogger().warning("Invalid lock type detected. For safety reasons it will be changed to: all");
            updateConfigAndVariables("all", null, null, null, null);
            lockType = "all";
        } else {
            lockType = config.getString("Lock.LockType");
        }
        playerList = config.getStringList("Lock.PlayerList");
        permission = config.getString("Lock.Permission");
        ignoreSender = config.getBoolean("IgnoreSender");
        ignoreOperators = config.getBoolean("IgnoreOperators");
        sender = config.getString("Lock.Sender");
        senderName = config.getString("Lock.SenderName");
        getLogger().info("Config successfully loaded.");
    }

    /* Used to update all variables in the config and the plugin */
    public void updateConfigAndVariables(String lockType, List<String> playerList, String permission, String sender, String sendername) {
        this.lockType = lockType;
        this.playerList = playerList;
        this.permission = permission;
        this.sender = sender;
        this.senderName = sendername;
        config.set("Lock.LockType", lockType);
        config.set("Lock.PlayerList", playerList);
        config.set("Lock.Permission", permission);
        config.set("Lock.Sender", sender);
        config.set("Lock.SenderName", sendername);
        saveConfig();
    }
    /* Getters */
    public String getLockType() {
        return lockType;
    }

    public List<String> getPlayerList() {
        return playerList;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isIgnoreSender() {
        return ignoreSender;
    }

    public boolean isIgnoreOperators() {
        return ignoreOperators;
    }

    public String getSender() {
        return sender;
    }

    public String getSenderName() {
        return senderName;
    }
}
