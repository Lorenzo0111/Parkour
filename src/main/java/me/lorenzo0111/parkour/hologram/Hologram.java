package me.lorenzo0111.parkour.hologram;

import me.lorenzo0111.parkour.ParkourPlugin;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class Hologram {
    private final Location location;
    private final String text;
    private ArmorStand entity;

    public Hologram(Location location, String text) {
        this.location = location.clone().add(0, ParkourPlugin.getInstance().getConfig().getDouble("hologram.height"),0);
        this.text = text;

        HologramManager.getInstance().register(this);
    }

    public void spawn() {
        entity = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        entity.setCustomName(text);
        entity.setVisible(false);
        entity.setCustomNameVisible(true);
        entity.setGravity(false);
    }

    public void setText(String text) {
        entity.setCustomName(text);
    }

    public void despawn() {
        if(entity != null) entity.remove();
    }
}
