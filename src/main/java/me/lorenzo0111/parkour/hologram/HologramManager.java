package me.lorenzo0111.parkour.hologram;

import me.lorenzo0111.parkour.utils.Reloadable;

import java.util.ArrayList;
import java.util.List;

public class HologramManager implements Reloadable {
    private static HologramManager instance;
    private final List<Hologram> holograms = new ArrayList<>();

    @Override
    public void reload() {
        holograms.forEach(Hologram::despawn);
        holograms.clear();
    }

    public void register(Hologram hologram) {
        holograms.add(hologram);
    }

    public void remove(Hologram hologram) {
        holograms.remove(hologram);
        hologram.despawn();
    }

    public static HologramManager getInstance() {
        if (instance == null) {
            instance = new HologramManager();
        }
        return instance;
    }

}
