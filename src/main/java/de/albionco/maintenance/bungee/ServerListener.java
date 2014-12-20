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

import de.albionco.maintenance.bungee.event.WhitelistUpdateEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;


/**
 * Listener class to handle all events, including custom ones.
 *
 * @author Connor Spencer Harries
 */
@SuppressWarnings("unused")
public class ServerListener implements Listener {

    /**
     * Store the parent plugin
     */
    private final BungeePlugin parent;

    /**
     * Create a new instance of the class
     *
     * @param parent parent plugin
     */
    public ServerListener(BungeePlugin parent) {
        this.parent = parent;
    }

    @EventHandler
    public void ping(ProxyPingEvent event) {
        if (parent.getEnabled()) {
            event.setResponse(parent.getPing());
        }
    }

    @EventHandler
    public void join(PreLoginEvent event) {
        if(parent.getEnabled()) {
            String name = event.getConnection().getName();
            if(!parent.getWhitelist().contains(name)) {
                event.setCancelled(true);
                event.setCancelReason(parent.getKickMessage());
            }
        }
    }

    @EventHandler
    public void update(WhitelistUpdateEvent event) {
        if(parent.getEnabled()) {
            if (event.getOperation() == WhitelistUpdateEvent.Operation.REMOVE) {
                ProxiedPlayer player;

                if ((player = ProxyServer.getInstance().getPlayer(event.getName())) != null) {
                    if(!player.hasPermission("maintenance.bypass")) {
                        parent.kick(player);
                    }
                }
            }
        }
    }

}
