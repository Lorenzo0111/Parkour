/*
 * This file is part of Parkour, licensed under the MIT License.
 *
 *  Copyright (c) Lorenzo0111
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

package me.lorenzo0111.parkour.commands;

import me.lorenzo0111.parkour.ParkourPlugin;
import me.lorenzo0111.parkour.commands.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkourCommand implements TabExecutor {
    private final ParkourPlugin plugin;
    private final Map<String, SubCommand> commands = new HashMap<>();

    public ParkourCommand(ParkourPlugin plugin) {
        this.plugin = plugin;
        
        commands.put("reload", new ReloadCommand(plugin));
        commands.put("create", new CreateCommand(plugin));
        commands.put("checkpoint", new CheckpointCommand(plugin));
        commands.put("end", new EndCommand(plugin));
        commands.put("delete", new DeleteCommand(plugin));
        commands.put("teleport", new TeleportCommand(plugin));
        commands.put("top", new TopCommand(plugin));
        commands.put("info", new InfoCommand(plugin));
        commands.put("stats", new StatsCommand(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessages().getMessage("errors.player"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(plugin.getMessages().getMessage("errors.usage").replace("{usage}", "/parkour <create|checkpoint|end|delete|top|info|stats>"));
            return true;
        }

        if (!commands.containsKey(args[0])) {
            sender.sendMessage(plugin.getMessages().getMessage("errors.usage").replace("{usage}", "/parkour <create|checkpoint|end|delete|top|info|stats>"));
            return true;
        }

        SubCommand subCommand = commands.get(args[0]);
        if (subCommand.permission() != null && !sender.hasPermission(subCommand.permission())) {
            sender.sendMessage(plugin.getMessages().getMessage("errors.no-permission"));
            return true;
        }

        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, newArgs.length);
        subCommand.perform((Player) sender, newArgs);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();

        if (args.length != 1) {

            SubCommand subCommand = commands.get(args[0]);
            if (subCommand != null) {
                return subCommand.complete();
            }

            return tab;
        }

        for (String s : commands.keySet()) {
            if (s.startsWith(args[0])) {
                tab.add(s);
            }
        }

        return tab;
    }

}
