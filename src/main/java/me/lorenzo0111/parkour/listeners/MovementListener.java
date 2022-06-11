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
