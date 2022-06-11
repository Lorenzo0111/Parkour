package me.lorenzo0111.parkour.listeners;

import me.lorenzo0111.parkour.data.cache.Challenge;
import me.lorenzo0111.parkour.data.cache.ChallengeHandler;
import me.lorenzo0111.parkour.data.flat.MessagesFile;
import me.lorenzo0111.parkour.data.sql.SQLDatabase;
import me.lorenzo0111.parkour.data.sql.Time;
import me.lorenzo0111.parkour.utils.MovementHandler;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import static org.bukkit.event.EventPriority.HIGHEST;

public class MovementListener implements Listener {


    @EventHandler(ignoreCancelled = true, priority = HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo() == null) return;
        if (!ChallengeHandler.getInstance().inChallenge(event.getPlayer())) return;

        Challenge challenge = ChallengeHandler.getInstance().get(event.getPlayer());
        if (challenge == null) return;

        if (challenge.getParkour().getCheckpoints().size() <= challenge.getCurrentCheckpoint()) {
            if (MovementHandler.checkDistance(event.getTo(),challenge.getParkour().getEnd())) {
                challenge = ChallengeHandler.getInstance().end(event.getPlayer());

                Time time = challenge.toTime();

                event.getPlayer().sendMessage(MessagesFile.getInstance().getMessage("game.end")
                        .replace("{name}", challenge.getParkour().getName())
                        .replace("{time}", String.valueOf(time.time())));

                SQLDatabase.getInstance().save(time);
            }

            return;
        }

        Location nextCheckpoint = challenge.getParkour().getCheckpoints().get(challenge.getCurrentCheckpoint()).location();
        if (nextCheckpoint != null && challenge.getCurrentCheckpoint() != challenge.getParkour().getCheckpoints().size() && MovementHandler.checkDistance(event.getTo(), nextCheckpoint)) {
            challenge.setCurrentCheckpoint(challenge.getCurrentCheckpoint() + 1);
            event.getPlayer().sendMessage(MessagesFile.getInstance().getMessage("game.checkpoint")
                    .replace("{current}", String.valueOf(challenge.getCurrentCheckpoint()))
                    .replace("{total}", String.valueOf(challenge.getParkour().getCheckpoints().size())));
        }
    }

    @EventHandler
    public void onFly(PlayerToggleFlightEvent event) {
        if (event.isFlying() && ChallengeHandler.getInstance().inChallenge(event.getPlayer())) {
            ChallengeHandler.getInstance().end(event.getPlayer());
            event.getPlayer().sendMessage(MessagesFile.getInstance().getMessage("game.fly"));
        }
    }

}
