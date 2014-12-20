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

package de.albionco.maintenance.bungee.event;

import net.md_5.bungee.api.plugin.Event;

/**
 * Custom event that lets developers do things when the whitelist is edited
 *
 * @author Connor Spencer Harries
 */
public class WhitelistUpdateEvent extends Event {

    /**
     * Store the username
     */
    private final String name;

    /**
     * Store the {@link de.albionco.maintenance.bungee.event.WhitelistUpdateEvent.Operation}
     */
    private final Operation operation;

    /**
     * Create a new instance of the class
     *
     * @param name player name
     * @param operation operation type
     */
    public WhitelistUpdateEvent(String name, Operation operation) {
        this.name = name;
        this.operation = operation;
    }

    /**
     * Get the value of {@link #operation}
     *
     * @return {@link #operation}
     */
    public Operation getOperation() {
        return this.operation;
    }

    /**
     * Get the value of {@link #name}
     *
     * @return {@link #name}
     */
    public String getName() {
         return this.name;
    }

    /**
     * Let us know whether people are being added to or removed from the whitelist
     */
    public enum Operation {
        ADD,
        REMOVE
    }
}
