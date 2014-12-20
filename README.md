MaintenanceMode
===============

A simple maintenance mode plugin for BungeeCord and Bukkit servers.

### Permissions
The only permission is `maintenance.toggle` which grants access to every subcommand.

### Commands

There is only one command, however it has subcommands which provide the actual functionality. 
The command is `maintenance` but it is worth noting that the aliases `mm` and `mmw` are also available for usage.

### Subcommands

|Command|Description|
|-------|-----------|
|enable|Enable maintenance mode|
|disable|Disable maintenance mode|
|add|Add a player to the whitelist|
|remove|Remove a player from the whitelist|
|list|List all whitelisted players|
|reload|Reload the configuration file|
|help|Display all subcommands|
