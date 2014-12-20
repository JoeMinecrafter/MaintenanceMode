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

package de.albionco.maintenance.bungee;

import de.albionco.maintenance.Configuration;
import de.albionco.maintenance.bungee.commands.CommandMaintenance;
import lombok.Getter;
import lombok.Setter;
import net.cubespace.Yamler.Config.InvalidConfigurationException;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Connor Harries on 19/12/2014.
 *
 * @author Connor Spencer Harries
 */
public class BungeePlugin extends Plugin implements Listener {

    @Setter
    @Getter
    private Boolean Enabled;

    @Getter
    @Setter
    private Configuration Config;

    @Getter
    @Setter
    private ServerPing Response;

    @Getter
    @Setter
    private List<String> Whitelist = new ArrayList<>();

    @Getter
    @Setter
    private String Message = "";

    @Override
    public void onEnable() {
        Configuration config = new Configuration(getDataFolder());

        try {
            config.init();
            getLogger().info("Loaded configuration values");
        } catch (InvalidConfigurationException e) {
            getLogger().warning("Unable to load configuration, quitting");
            return;
        }

        setConfig(config);
        reload(false);

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CommandMaintenance(this));
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
    }

    @Override
    public void onDisable() {
        getConfig().setEnabled(this.getEnabled());
        getConfig().setWhitelist(getWhitelist());

        try {
            getConfig().save();
        } catch (InvalidConfigurationException e) {
            // ignored, not much we can do
        }
    }

    @EventHandler
    public void ping(ProxyPingEvent event) {
        if (this.getEnabled()) {
            event.setResponse(getResponse());
        }
    }

    @EventHandler
    public void join(PreLoginEvent event) {
        if(this.getEnabled()) {
            String name = event.getConnection().getName();
            if(!getWhitelist().contains(name)) {
                event.setCancelled(true);
                event.setCancelReason(colour(getMessage()));
            }
        }
    }

    public boolean reload(boolean rl) {
        try {
            if(rl) {
                getConfig().reload();
            }

            setWhitelist(getConfig().getWhitelist());
            this.setEnabled(getConfig().getEnabled());
            setMessage(getConfig().getMessage());

            ServerPing.Protocol protocol = new ServerPing.Protocol("Maintenance", Short.MAX_VALUE);
            ServerPing.Players players = new ServerPing.Players(0, 0, null);
            Favicon icon = null;
            ServerPing ping = new ServerPing(protocol, players, colour(getMessage()), icon);
            setResponse(ping);

            return true;
        } catch (InvalidConfigurationException e) {
            return false;
        }
    }

    private String colour(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }
}
