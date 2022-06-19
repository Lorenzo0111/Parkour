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

import me.lorenzo0111.parkour.ParkourPlugin;
import me.lorenzo0111.parkour.data.cache.Challenge;
import me.lorenzo0111.parkour.data.cache.ChallengeHandler;
import me.lorenzo0111.parkour.data.flat.Parkour;
import me.lorenzo0111.parkour.data.sql.Time;
import me.lorenzo0111.parkour.utils.MovementHandler;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.Optional;

import static org.bukkit.event.EventPriority.HIGHEST;

public class MovementListener implements Listener {
    private final ParkourPlugin plugin;

    public MovementListener(ParkourPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(ignoreCancelled = true, priority = HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo() == null) return;
        if (!plugin.getChallenges().inChallenge(event.getPlayer()) && !plugin.getChallenges().isDelayed(event.getPlayer())) {
            Optional<Parkour> first = plugin.getParkours()
                    .getParkours()
                    .values()
                    .stream()
                    .filter(p -> MovementHandler.checkDistance(event.getTo(), p.getStart()))
                    .findFirst();

            if (first.isPresent()) {
                Challenge challenge = new Challenge(event.getPlayer(), first.get());
                plugin.getChallenges().start(event.getPlayer(), challenge);
                event.getPlayer().sendMessage(plugin.getMessages().getMessage("challenge.start").replace("{name}", first.get().getName()));
            }

            return;
        }

        Challenge challenge = plugin.getChallenges().get(event.getPlayer());
        if (challenge == null) return;

        if (challenge.getParkour().getCheckpoints().size() <= challenge.getCurrentCheckpoint()) {
            if (MovementHandler.checkDistance(event.getTo(), challenge.getParkour().getEnd())) {
                challenge = plugin.getChallenges().end(event.getPlayer());

                Time time = challenge.toTime();

                event.getPlayer().sendMessage(plugin.getMessages().getMessage("game.end")
                        .replace("{name}", challenge.getParkour().getName())
                        .replace("{time}", String.valueOf(time.time())));

                plugin.getDatabase().save(time);
            }

            return;
        }

        Location nextCheckpoint = challenge.getParkour().getCheckpoints().get(challenge.getCurrentCheckpoint()).location();
        if (nextCheckpoint != null && challenge.getCurrentCheckpoint() != challenge.getParkour().getCheckpoints().size() && MovementHandler.checkDistance(event.getTo(), nextCheckpoint)) {
            challenge.setCurrentCheckpoint(challenge.getCurrentCheckpoint() + 1);
            event.getPlayer().sendMessage(plugin.getMessages().getMessage("game.checkpoint")
                    .replace("{current}", String.valueOf(challenge.getCurrentCheckpoint()))
                    .replace("{total}", String.valueOf(challenge.getParkour().getCheckpoints().size())));
        }
    }

    @EventHandler
    public void onFly(PlayerToggleFlightEvent event) {
        if (event.isFlying() && plugin.getChallenges().inChallenge(event.getPlayer())) {
            plugin.getChallenges().end(event.getPlayer());
            event.getPlayer().sendMessage(plugin.getMessages().getMessage("game.fly"));
        }
    }

}
