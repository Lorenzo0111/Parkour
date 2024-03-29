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
import me.lorenzo0111.parkour.data.flat.Parkour;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SubCommand {
    protected final ParkourPlugin plugin;

    protected SubCommand(ParkourPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract void perform(Player sender, String[] args);

    public abstract @Nullable String permission();

    public List<String> complete() {
        return null;
    }

    public Parkour fromArgs(Player sender, String usage, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(plugin.getMessages().getMessage("errors.usage").replace("{usage}", "/parkour " + usage));
            return null;
        }

        Parkour parkour = plugin.getParkours().getParkour(args[0]);
        if (parkour == null) {
            sender.sendMessage(plugin.getMessages().getMessage("errors.not-exists").replace("{name}", args[0]));
            return null;
        }

        return parkour;
    }
}