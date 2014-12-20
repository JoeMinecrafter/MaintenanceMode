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

package de.albionco.maintenance.bungee.command;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import de.albionco.maintenance.bungee.BungeePlugin;
import de.albionco.maintenance.bungee.event.WhitelistUpdateEvent;
import de.albionco.maintenance.bungee.event.WhitelistUpdateEvent.Operation;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.List;

import static de.albionco.maintenance.Messages.*;

/**
 * Handle all of MaintenanceModes BungeeCord commands
 *
 * @author Connor Spencer Harries
 */
public class CommandMaintenance extends Command implements TabExecutor {

    /**
     * Store the parent plugin
     */
    private final BungeePlugin parent;

    /**
     * Create a new instance of the class
     * @param parent parent {@link de.albionco.maintenance.bungee.BungeePlugin}
     */
    public CommandMaintenance(BungeePlugin parent) {
        super("maintenance", "maintenance.toggle", "mm", "mmw");
        this.parent = parent;
    }

    /**
     * Handle the various subcommands associated with the plugin
     *
     * @param sender command sender
     * @param args command arguments
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args != null && args.length > 0) {
            String sub = args[0];

            switch (sub.toLowerCase()) {
                case "enable":
                    if (parent.getEnabled()) {
                        sender.sendMessage(MAINTENANCE_ENABLED_ALREADY);
                        break;
                    }

                    parent.kick(null);
                    parent.setEnabled(true);
                    sender.sendMessage(MAINTENANCE_ENABLED);
                    break;
                case "disable":
                    if (!parent.getEnabled()) {
                        sender.sendMessage(MAINTENANCE_DISABLED_ALREADY);
                        break;
                    }

                    parent.setEnabled(false);
                    sender.sendMessage(MAINTENANCE_DISABLED);
                    break;
                case "add":
                case "remove":
                    if (args.length > 1) {
                        String name = args[1];
                        switch (sub) {
                            case "add":
                                if (parent.getWhitelist().contains(name)) {
                                    sender.sendMessage(WHITELIST_ADD_EXIST);
                                } else {
                                    parent.getWhitelist().add(name);

                                    WhitelistUpdateEvent event = new WhitelistUpdateEvent(name, Operation.ADD);
                                    ProxyServer.getInstance().getPluginManager().callEvent(event);

                                    sender.sendMessage(WHITELIST_ADD);
                                }
                                break;
                            case "remove":
                                if (!parent.getWhitelist().contains(name)) {
                                    sender.sendMessage(WHITELIST_DEL_EXIST);
                                } else {
                                    parent.getWhitelist().remove(name);

                                    WhitelistUpdateEvent event = new WhitelistUpdateEvent(name, Operation.REMOVE);
                                    ProxyServer.getInstance().getPluginManager().callEvent(event);

                                    sender.sendMessage(WHITELIST_DEL);
                                }
                                break;
                            default:
                                sender.sendMessage(HELP_INVALID + "maintenance <add|remove> <username>");
                                break;
                        }
                        break;
                    } else {
                        sender.sendMessage(HELP_INVALID + "maintenance <add|remove> <username>");
                    }
                    break;
                case "list":
                    Joiner joiner = Joiner.on(", ");
                    String joined = joiner.join(parent.getWhitelist());
                    sender.sendMessage(ChatColor.GREEN + "Whitelist: " + ChatColor.WHITE + (joined.equals("") ? "No players added to whitelist" : joined));
                    break;
                case "reload":
                    if (parent.reload()) {
                        sender.sendMessage(CONFIG_RELOAD);
                    } else {
                        sender.sendMessage(CONFIG_RELOAD_ERR);
                    }
                    break;
                case "help":
                    sender.sendMessage(HELP_USAGE);
                    break;
                default:
                    sender.sendMessage(HELP_MESSAGE);
                    break;
            }
        } else {
            sender.sendMessage(HELP_MESSAGE);
        }
    }

    /**
     * Add tab completion to commands
     *
     * @param sender command sender
     * @param args arguments
     * @return list of potential tab results
     */
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
            if(sub.equals("add")) {
                String search = args[1].toLowerCase();
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                    if (player.getName().toLowerCase().startsWith(search)) {
                        if(!parent.getWhitelist().contains(player.getName())) {
                            commands.add(player.getName());
                        }
                    }
                }
            } else if (sub.equals("remove")) {
                String search = args[1].toLowerCase();
                for(String name : parent.getWhitelist()) {
                    if(name.toLowerCase().startsWith(search)) {
                        commands.add(name);
                    }
                }
            }
        }

        return commands;
    }
}
