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

import me.lorenzo0111.parkour.hologram.Hologram;
import me.lorenzo0111.parkour.hologram.HologramManager;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkourFile extends AbstractFile {
    private static ParkourFile instance;
    private Map<String, Parkour> parkours;

    public ParkourFile(File dataFolder) throws IOException {
        super(new File(dataFolder, "parkour.yml"));

        instance = this;
    }

    @Override
    public void reloadAndSave() {
        save();
        reload();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void reload() {
        super.reload();

        parkours = new HashMap<>();
        for (String parkour : this.getConfig().getKeys(false)) {
            ConfigurationSection section = this.getConfig().getConfigurationSection(parkour);
            if (section == null) continue;

            Parkour p = new Parkour(parkour, section.getLocation("start"));

            if (section.contains("checkpoints")) {
                p.setCheckpoints((List<Location>) section.getList("checkpoints"));
            }

            if (section.contains("end")) {
                p.setEnd(section.getLocation("end"));
            }

            parkours.put(parkour, p);
        }
    }

    @Override
    public void save() {
        this.resetConfig();

        if (this.parkours != null) {
            for (String parkour : this.parkours.keySet()) {
                Parkour p = this.parkours.get(parkour);

                ConfigurationSection section = this.getConfig().createSection(parkour);
                section.set("start", p.getStart());

                if (p.getCheckpoints() != null) {
                    section.set("checkpoints", p.getCheckpoints().stream().map(Checkpoint::location).toList());
                }

                if (p.getEnd() != null) {
                    section.set("end", p.getEnd());
                }

                this.getConfig().set(parkour, section);
            }
        }

        super.save();
    }

    public static ParkourFile getInstance() {
        return instance;
    }

    public void add(Parkour parkour) {
        this.parkours.put(parkour.getName(), parkour);
    }

    public void remove(String parkour) {
        Parkour p = this.parkours.remove(parkour);

        for (Hologram hologram : p.getHolograms()) {
            HologramManager.getInstance().remove(hologram);
        }
    }

    public Parkour getParkour(String parkour) {
        return this.parkours.get(parkour);
    }

    public Map<String, Parkour> getParkours() {
        return parkours;
    }
}
