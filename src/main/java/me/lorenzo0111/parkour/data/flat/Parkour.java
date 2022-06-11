package me.lorenzo0111.parkour.data.flat;

import me.lorenzo0111.parkour.hologram.Hologram;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Parkour {
    private final String name;
    private final Location start;
    private final Hologram hologram;
    private Hologram endHologram;
    private List<Checkpoint> checkpoints;
    private Location end;

    public Parkour(String name, Location start) {
        this.name = name;
        this.start = start;
        this.hologram = new Hologram(start,  MessagesFile.getInstance().getMessage("hologram.start")
                .replace("{name}", name));
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
            list.add(new Checkpoint(checkpoints.get(i), new Hologram(checkpoints.get(i), MessagesFile.getInstance().getMessage("hologram.checkpoint")
                    .replace("{name}", name)
                    .replace("{count}", String.valueOf(i+1)))));
        }

        this.checkpoints = list;
    }

    public Location getEnd() {
        return end;
    }

    public void setEnd(Location end) {
        this.end = end;

        this.endHologram = new Hologram(end,  MessagesFile.getInstance().getMessage("hologram.end")
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
}
