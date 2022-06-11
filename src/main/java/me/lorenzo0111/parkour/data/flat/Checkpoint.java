package me.lorenzo0111.parkour.data.flat;

import me.lorenzo0111.parkour.hologram.Hologram;
import org.bukkit.Location;

public record Checkpoint(Location location, Hologram hologram) {

    public Checkpoint {
        hologram.spawn();
    }


}
