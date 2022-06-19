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

package me.lorenzo0111.parkour.data.flat;

import me.lorenzo0111.parkour.ParkourPlugin;
import me.lorenzo0111.parkour.hologram.Hologram;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Parkour {
    private final ParkourPlugin plugin;
    private final String name;
    private final Location start;
    private final Hologram hologram;
    private Hologram endHologram;
    private List<Checkpoint> checkpoints;
    private Location end;

    public Parkour(ParkourPlugin plugin, String name, Location start) {
        this.name = name;
        this.start = start;
        this.hologram = new Hologram(plugin,start, plugin.getMessages().getMessage("hologram.start")
                .replace("{name}", name));
        this.plugin = plugin;
        hologram.spawn();
    }

    public String getName() {
        return name;
    }

    public Location getStart() {
        return start;
    }

    public List<Checkpoint> getCheckpoints() {
        if (checkpoints == null) checkpoints = new ArrayList<>();

        return checkpoints;
    }

    public void setCheckpoints(List<Location> checkpoints) {
        List<Checkpoint> list = new ArrayList<>();

        for (int i = 0; i < checkpoints.size(); i++) {
            list.add(new Checkpoint(checkpoints.get(i), new Hologram(plugin,checkpoints.get(i), plugin.getMessages().getMessage("hologram.checkpoint")
                    .replace("{name}", name)
                    .replace("{count}", String.valueOf(i + 1)))));
        }

        this.checkpoints = list;
    }

    public Location getEnd() {
        return end;
    }

    public void setEnd(Location end) {
        this.end = end;

        this.endHologram = new Hologram(plugin,end, plugin.getMessages().getMessage("hologram.end")
                .replace("{name}", name));
        endHologram.spawn();
    }

    public List<Hologram> getHolograms() {
        List<Hologram> holograms = new ArrayList<>();
        holograms.add(hologram);
        if (endHologram != null) holograms.add(endHologram);
        if (checkpoints != null) holograms.addAll(checkpoints.stream().map(Checkpoint::hologram).toList());
        return holograms;
    }

    public ParkourPlugin getPlugin() {
        return plugin;
    }
}
