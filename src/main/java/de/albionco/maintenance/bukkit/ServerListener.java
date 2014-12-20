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

import de.albionco.maintenance.bukkit.event.WhitelistUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.ServerListPingEvent;

/**
 * Created by Connor Harries on 20/12/2014.
 *
 * @author Connor Spencer Harries
 */
@SuppressWarnings("unused")
public class ServerListener implements Listener {

    /**
     * Store the parent plugin
     */
    private final BukkitPlugin parent;

    /**
     * Create a new instance of the class
     *
     * @param parent parent plugin
     */
    public ServerListener(BukkitPlugin parent) {
        this.parent = parent;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void ping(ServerListPingEvent event) {
        if(parent.getEnabled()) {
            event.setMaxPlayers(0);
            event.setMotd(parent.getMotd());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void join(PlayerLoginEvent event) {
        if(parent.getEnabled()) {
            String name = event.getPlayer().getName();
            if(!event.getPlayer().hasPermission("maintenance.bypass")) {
                if (!parent.getWhitelist().contains(name)) {
                    event.setKickMessage(parent.getKickMessage());
                    event.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
                }
            }
        }
    }

    @EventHandler
    public void update(WhitelistUpdateEvent event) {
        if(parent.getEnabled()) {
            if (event.getOperation() == WhitelistUpdateEvent.Operation.REMOVE) {
                Player player;

                if ((player = Bukkit.getPlayerExact(event.getName())) != null) {
                    if(!player.hasPermission("maintenance.bypass")) {
                        parent.kick(player);
                    }
                }
            }
        }
    }
}
