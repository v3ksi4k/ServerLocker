# ServerLocker
This plugin is used to lock a minecraft server from a certain group of players joining. It can be used to organize events, or to restrict player access in emergency situations.

## Commands:
  * serverlocker - Displays a help message that contains the command list and some important informations about the plugin.
  * serverlock:
    * all - Kicks all players and locks the server for everyone.
    * permission [permission] - Kicks all players without a specified permission and locks the server for every player that doesn't have it.
    *  whitelist - Locks the server for all players except the players that are currently on the server.
    * blacklist new/add - Kicks all players and locks the server for them./Adds all players that are currently on the server to a previously set blacklist.
  * serverunlock -  Unlocks the server by lifting the current lock.
  * lockstatus - Displays the status of the server lock.

## Permissions:
  * serverlocker.help - Allows the user to view the plugin's commands.
  * serverlocker.serverlock - Allows the user to lock the server using the /serverlock command.
  * serverlocker.serverunlock - Allows the user to unlock the server using the /serverunlock command.
  * serverlocker.lockstatus - Allows the user to check the server's lock status.
  * serverlocker.immunity - Makes the user immune to all server locks.

## The config.yml file:
Server locks can also be set in the config.yml file, by editing the value of "LockType".

Remember that everytime you modify something in the file, the server will need to be restarted for the changes to apply.

The config file looks similar to this (Any changes to the server lock itself happen in the "Lock" path):

![image](https://github.com/v3ksi4k/ServerLocker/assets/147096926/311c4772-a148-4534-9784-2d6a3d34c44c)

### Some lock types require their own additional arguments **(Failing to provide them may reset the config or/and set an emergency lock of type "all")**:
  * permission [Permission] _permission_ - All players without the *permission* permission won't be able to join the server.
  > e.g. Permission: serverlocker.serverlock
  * whitelist [PlayerList] _a list of UUID's_ - Only players with UUID's from the list will be able to join the server.
  > e.g. PlayerList: ['UUID1', 'UUID2', 'UUID3']
  * blacklist [PlayerList] _a list of UUID's_ - Players with UUID's from the list won't be able to join the server.
  > e.g. PlayerList: ['UUID1', 'UUID2', 'UUID3']
  
### Lock types that don't require arguments:
  * all - Locks the server for everyone.
  * none - No server lock.
