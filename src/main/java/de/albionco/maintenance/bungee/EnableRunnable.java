/*
 * Copyright (c) 2015 Connor Spencer Harries
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

import de.albionco.maintenance.Messages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import org.apache.commons.lang3.time.DurationFormatUtils;

import static de.albionco.maintenance.Messages.MAINTENANCE_ENABLED;

/**
 * Created by Connor Harries on 06/01/2015.
 *
 * @author Connor Spencer Harries
 */
public class EnableRunnable implements Runnable {

    private final BungeePlugin parent;
    private final CommandSender sender;

    public EnableRunnable(BungeePlugin parent, CommandSender sender) {
        this.parent = parent;
        this.sender = sender;
    }

    @Override
    public void run() {
        int loops = parent.getCountdown();
        ProxyServer.getInstance().broadcast(Messages.colour(format(parent.getCountdownMessage(), loops)));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i = loops - 1; i > 0; i--) {
            if(parent.getAlertTimes().contains(i)) {
                ProxyServer.getInstance().broadcast(Messages.colour(format(parent.getCountdownMessage(), i)));
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(i == 1) {
                parent.kick(null);
                parent.setEnabled(true);
                sender.sendMessage(MAINTENANCE_ENABLED);
            }
        }
    }

    private String format(String message, int seconds) {
        return message.replace("{{ TIME }}", DurationFormatUtils.formatDurationWords(seconds * 1000, true, false)).replace("{{ SECONDS }}", String.valueOf(seconds));
    }
}
