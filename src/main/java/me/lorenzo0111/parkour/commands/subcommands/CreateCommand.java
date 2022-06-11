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

package me.lorenzo0111.parkour.commands.subcommands;

import me.lorenzo0111.parkour.commands.SubCommand;
import me.lorenzo0111.parkour.data.flat.MessagesFile;
import me.lorenzo0111.parkour.data.flat.Parkour;
import me.lorenzo0111.parkour.data.flat.ParkourFile;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class CreateCommand extends SubCommand {

    @Override
    public void perform(Player sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(MessagesFile.getInstance().getMessage("errors.usage").replace("{usage}", "/parkour create {name}"));
            return;
        }

        String name = args[0];
        Location start = sender.getLocation();

        if (ParkourFile.getInstance().getParkour(name) != null) {
            sender.sendMessage(MessagesFile.getInstance().getMessage("errors.exists").replace("{name}", name));
            return;
        }

        Parkour parkour = new Parkour(name,start);
        ParkourFile.getInstance().add(parkour);

        sender.sendMessage(MessagesFile.getInstance().getMessage("commands.create").replace("{name}", name));
    }

    @Override
    public @Nullable String permission() {
        return "parkour.admin.create";
    }

}
