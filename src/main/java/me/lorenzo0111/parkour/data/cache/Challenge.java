package me.lorenzo0111.parkour.data.cache;

import me.lorenzo0111.parkour.data.flat.Parkour;
import me.lorenzo0111.parkour.data.sql.Time;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;

public class Challenge {
    private final Player player;
    private final Parkour parkour;
    private final Instant start;
    private int currentCheckpoint;
    private Instant end;

    public Challenge(Player player, Parkour parkour) {
        this.player = player;
        this.parkour = parkour;
        this.start = Instant.now();
        this.currentCheckpoint = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public Parkour getParkour() {
        return parkour;
    }

    public Instant getStart() {
        return start;
    }

    public int getCurrentCheckpoint() {
        return currentCheckpoint;
    }

    public void setCurrentCheckpoint(int currentCheckpoint) {
        this.currentCheckpoint = currentCheckpoint;
    }

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public Time toTime() {
        if (end == null) end = Instant.now();

        return new Time(parkour.getName(),player.getUniqueId(), Duration.between(start, end).getSeconds());
    }
}
