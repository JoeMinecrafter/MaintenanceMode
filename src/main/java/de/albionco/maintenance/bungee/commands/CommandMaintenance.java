/*
 * Copyright (c) 2014 Connor Spencer Harries
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.albionco.maintenance.bungee.commands;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import de.albionco.maintenance.bungee.BungeePlugin;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Connor Harries on 19/12/2014.
 *
 * @author Connor Spencer Harries
 */
public class CommandMaintenance extends Command implements TabExecutor {

    private static final String CONFIG_RELOAD = ChatColor.GREEN + "Configuration has been reloaded :)";
    private static final String CONFIG_RELOAD_ERR = ChatColor.RED + "Unable to reload configuration :(";
    private final String INVALID_ARGS = ChatColor.RED + "Invalid arguments provided, /";
    private final String MAINTENANCE_ENABLE = ChatColor.GREEN + "Maintenance mode has been enabled!";
    private final String MAINTENANCE_ENABLED = ChatColor.RED + "Maintenance mode is already enabled!";
    private final String MAINTENANCE_DISABLE = ChatColor.GREEN + "Maintenance mode has been disabled!";
    private final String MAINTENANCE_DISABLED = ChatColor.RED + "Maintenance mode isn't enabled!";
    private final String WHITELIST_ADD = ChatColor.GREEN + "Player added to the whitelist!";
    private final String WHITELIST_DEL = ChatColor.GREEN + "Player removed from the whitelist!";
    private final String WHITELIST_EXIST = ChatColor.RED + "That player is already whitelisted!";
    private final String WHITELIST_NOT_EXIST = ChatColor.RED + "That player is not whitelisted!";

    @Getter
    private final BungeePlugin Parent;

    public CommandMaintenance(BungeePlugin parent) {
        super("maintenance", "maintenance.toggle", "mm", "mmw");
        this.Parent = parent;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args != null && args.length > 0) {
            String sub = args[0];

            switch (sub.toLowerCase()) {
                case "enable":
                    if (getParent().getEnabled()) {
                        sender.sendMessage(MAINTENANCE_ENABLED);
                        break;
                    }

                    List<String> players = getParent().getWhitelist();
                    for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                        String uuid = player.getUniqueId().toString().replace("-", "");
                        if (!players.contains(uuid)) {
                            player.disconnect(ChatColor.RED + "The server has entered " + ChatColor.BOLD + "maintenance " + ChatColor.RED + "mode!");
                        }
                    }

                    getParent().setEnabled(true);
                    sender.sendMessage(MAINTENANCE_ENABLE);
                    break;
                case "disable":
                    if (!getParent().getEnabled()) {
                        sender.sendMessage(MAINTENANCE_DISABLED);
                        break;
                    }

                    getParent().setEnabled(false);
                    sender.sendMessage(MAINTENANCE_DISABLE);
                    break;
                case "add":
                case "remove":
                    if (args.length > 1) {
                        String uuid = args[1];
                        if (sub.equals("add")) {
                            if (getParent().getWhitelist().contains(uuid)) {
                                sender.sendMessage(WHITELIST_EXIST);
                            } else {
                                getParent().getWhitelist().add(uuid);
                                sender.sendMessage(WHITELIST_ADD);
                            }
                        } else if (sub.equals("remove")) {
                            if (!getParent().getWhitelist().contains(uuid)) {
                                sender.sendMessage(WHITELIST_NOT_EXIST);
                            } else {
                                getParent().getWhitelist().remove(uuid);
                                sender.sendMessage(WHITELIST_DEL);
                            }
                        } else {
                            sender.sendMessage(INVALID_ARGS + "maintenance <add|remove> <username>");
                        }
                        break;
                    } else {
                        sender.sendMessage(INVALID_ARGS + "maintenance <add|remove> <username>");
                    }
                    break;
                case "list":
                    Joiner joiner = Joiner.on(", ");
                    String joined = joiner.join(getParent().getWhitelist());
                    sender.sendMessage(ChatColor.GREEN + "Whitelist: " + ChatColor.WHITE + (joined.equals("") ? "No players added to whitelist" : joined));
                    break;
                case "reload":
                    if (getParent().reload(true)) {
                        sender.sendMessage(CONFIG_RELOAD);
                    } else {
                        sender.sendMessage(CONFIG_RELOAD_ERR);
                    }
                    break;
                case "help":
                    sender.sendMessage(ChatColor.YELLOW + "Usage: /maintenance <enable|disable|list|add|remove|reload|help>");
                    break;
                default:
                    sender.sendMessage(ChatColor.YELLOW + "MaintenanceMode by Albion");
                    break;
            }

        } else {
            sender.sendMessage(ChatColor.YELLOW + "MaintenanceMode by Albion");
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if(args.length < 1 || args.length > 2) {
            return ImmutableSet.of();
        }

        List<String> commands = new ArrayList<>();

        if(args.length == 1) {
            String search = args[0].toLowerCase();
            if("list".startsWith(search)) {
                commands.add("list");
            }

            if("add".startsWith(search)) {
                commands.add("add");
            }

            if("remove".startsWith(search)) {
                commands.add("remove");
            }

            if("help".startsWith(search)) {
                commands.add("help");
            }

            if("enable".startsWith(search)) {
                commands.add("enable");
            }

            if("disable".startsWith(search)) {
                commands.add("disable");
            }

            if("reload".startsWith(search)) {
                commands.add("reload");
            }
        } else {
            String sub = args[0].toLowerCase();
            if(sub.equals("add") || sub.equals("remove")) {
                String search = args[1].toLowerCase();
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                    if (player.getName().startsWith(search)) {
                        commands.add(player.getName());
                    }
                }
            }
        }

        return commands;
    }
}
