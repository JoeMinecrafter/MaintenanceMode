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

package de.albionco.maintenance.bukkit;

import de.albionco.maintenance.Messages;
import de.albionco.maintenance.bukkit.command.CommandMaintenance;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static de.albionco.maintenance.Messages.colour;

/**
 * Created by Connor Harries on 20/12/2014.
 *
 * @author Connor Spencer Harries
 */
public class BukkitPlugin extends JavaPlugin implements Listener {

    /**
     * Store the list of whitelisted players
     */
    private List<String> whitelist;

    /**
     * Store the message players are shown when kicked
     */
    private String message_kick;

    /**
     * Store the message_motd that will be displayed when maintenance mode is active
     */
    private String message_motd;

    /**
     * Store whether maintenance mode is enabled or not
     */
    private boolean enabled;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        whitelist = getConfig().getStringList("whitelist");
        enabled = getConfig().getBoolean("enabled");
        message_motd = getConfig().getString("messages.message_motd", "&c&lMaintenance Mode");
        message_kick = getConfig().getString("messages.kick", "&cThe server is in maintenance mode, sorry for any inconvenience.");

        message_motd = colour(message_motd);
        message_kick = colour(message_kick);

        PluginCommand command = getCommand("maintenance");

        CommandMaintenance cmd = new CommandMaintenance(this);
        command.setExecutor(cmd);
        command.setTabCompleter(cmd);

        Bukkit.getServer().getPluginManager().registerEvents(new ServerListener(this), this);
    }

    @Override
    public void onDisable() {
        try {
            getConfig().set("enabled", enabled);
            getConfig().set("whitelist", whitelist);

            getConfig().save(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            getLogger().severe("Unable to save configuration values");
        }
    }

    /**
     * Kick a {@link org.bukkit.entity.Player} from the server
     * @param kick null to kick all players
     */
    public void kick(Player kick) {
        if(kick == null) {
            boolean skip = (whitelist == null || whitelist.size() < 1);
            for(Player player : Bukkit.getOnlinePlayers()) {
                if(skip) {
                    player.kickPlayer(getKickMessage());
                } else {
                    if (!player.hasPermission("maintenance.bypass")) {
                        if (!whitelist.contains(player.getName())) {
                            player.kickPlayer(getKickMessage());
                        }
                    }
                }
            }
        } else {
            kick.kickPlayer(getKickMessage());
        }
    }

    /**
     * Get whether maintenance mode is enabled
     * @return {@link #enabled}
     */
    public boolean getEnabled() {
        return this.enabled;
    }

    /**
     * Get the maintenance mode motd
     * @return {@link #message_motd}
     */
    public String getMotd() {
        return this.message_motd;
    }

    /**
     * Get the message players will be kicked with when maintenance mode is active
     * @return {@link #message_kick}
     */
    public String getKickMessage() {
        return this.message_kick;
    }

    /**
     * Get the list of whitelisted players
     * @return {@link #whitelist}
     */
    public List<String> getWhitelist() {
        return whitelist;
    }


    public void setMaintenanceEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Reload our configuration file
     */
    public void reload() {
        reloadConfig();
    }
}
