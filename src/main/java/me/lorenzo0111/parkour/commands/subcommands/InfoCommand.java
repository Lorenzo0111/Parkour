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

import me.lorenzo0111.parkour.ParkourPlugin;
import me.lorenzo0111.parkour.commands.SubCommand;
import me.lorenzo0111.parkour.data.flat.Checkpoint;
import me.lorenzo0111.parkour.data.flat.Parkour;
import me.lorenzo0111.parkour.data.flat.gui.GuiFile;
import me.lorenzo0111.parkour.data.flat.gui.GuiItem;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InfoCommand extends SubCommand {

    public InfoCommand(ParkourPlugin plugin) {
        super(plugin);
    }

    @Override
    public void perform(Player sender, String[] args) {
        Parkour parkour = fromArgs(sender, "info {name}", args);
        if (parkour == null) return;

        GuiFile file = GuiFile.get("info")
                .registerPlaceholder("name", parkour.getName())
                .bind('S', "start", Map.of("x", String.valueOf(parkour.getStart().getBlockX()), "y", String.valueOf(parkour.getStart().getBlockY()), "z", String.valueOf(parkour.getStart().getBlockZ())))
                .bind('E', "end", Map.of("x", String.valueOf(parkour.getEnd().getBlockX()), "y", String.valueOf(parkour.getEnd().getBlockY()), "z", String.valueOf(parkour.getEnd().getBlockZ())));

        List<GuiItem> items = new ArrayList<>();

        for (int i = 0; i < parkour.getCheckpoints().size(); i++) {
            Checkpoint checkpoint = parkour.getCheckpoints().get(i);

            String name = String.valueOf(i + 1);

            items.add(new GuiItem(file, "checkpoint")
                    .registerPlaceholder("name", name)
                    .registerPlaceholder("x", String.valueOf(checkpoint.location().getBlockX()))
                    .registerPlaceholder("y", String.valueOf(checkpoint.location().getBlockY()))
                    .registerPlaceholder("z", String.valueOf(checkpoint.location().getBlockZ()))
                    .onClick((c) -> sender.teleport(checkpoint.location()))
            );
        }

        file.setItems(items).toGUI(sender).show();
    }

    @Override
    public @Nullable String permission() {
        return null;
    }

    @Override
    public List<String> complete() {
        return new ArrayList<>(plugin.getParkours().getParkours().keySet());
    }

}
