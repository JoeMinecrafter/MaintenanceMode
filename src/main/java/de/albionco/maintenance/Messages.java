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

package de.albionco.maintenance;

/**
 * Simple class to store messages that will be used by the plugin.
 *
 * @author Connor Spencer Harries
 */
public class Messages {

    /**
     * Message that's displayed when the command is run without arguments or an invalid subcommand
     */
    public static final String HELP_MESSAGE = ChatColor.YELLOW + "MaintenanceMode by Albion";

    /**
     * Message that's displayed when the "help" subcommand is executed
     */
    public static final String HELP_USAGE = ChatColor.YELLOW + "Usage: /maintenance <enable|disable|list|add|remove|reload|help>";

    /**
     * Message that's displayed when the "reload" subcommand is executed and succeeds
     */
    public static final String CONFIG_RELOAD = ChatColor.GREEN + "Configuration has been reloaded :)";

    /**
     * Message that's displayed then the "reload" subcommand is executed and fails
     */
    public static final String CONFIG_RELOAD_ERR = ChatColor.RED + "Unable to reload configuration :(";

    /**
     * Message that's displayed when the invalid arguments are passed to a subcommand
     */
    public static final String HELP_INVALID = ChatColor.RED + "Invalid arguments provided, try this: /";

    /**
     * Message that's displayed when the "enable" subcommand is executed
     */
    public static final String MAINTENANCE_ENABLED = ChatColor.GREEN + "Maintenance mode has been enabled!";

    /**
     * Message that's displayed when the "enable" subcommand is executed but maintenance mode is already active
     */
    public static final String MAINTENANCE_ENABLED_ALREADY = ChatColor.RED + "Maintenance mode is already enabled!";

    /**
     * Message that's displayed when the "disable" subcommand is executed
     */
    public static final String MAINTENANCE_DISABLED = ChatColor.GREEN + "Maintenance mode has been disabled!";

    /**
     * Message that's displayed when the "disable" subcommand is executed but maintenance mode is already disabled
     */
    public static final String MAINTENANCE_DISABLED_ALREADY = ChatColor.RED + "Maintenance mode isn't enabled!";

    /**
     * Message that's displayed when the "add" subcommand is executed and succeeds
     */
    public static final String WHITELIST_ADD = ChatColor.GREEN + "Player added to the whitelist!";

    /**
     * Message that's displayed when the "add" subcommand is executed and fails
     */
    public static final String WHITELIST_ADD_EXIST = ChatColor.RED + "That player is already whitelisted!";

    /**
     * Message that's displayed when the "remove" subcommand is executed and succeeds
     */
    public static final String WHITELIST_DEL = ChatColor.GREEN + "Player removed from the whitelist!";

    /**
     * Message that's displayed when the "remove" subcommand is executed and fails
     */
    public static final String WHITELIST_DEL_EXIST = ChatColor.RED + "That player is not whitelisted!";

    /**
     * Stop people creating an instance of the class
     */
    private Messages() {

    }

    /**
     * I really hate typing out the code to change colours every time.
     * @param in input string
     * @return Bukkit/Bungee formatted String
     */
    public static String colour(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }
}
