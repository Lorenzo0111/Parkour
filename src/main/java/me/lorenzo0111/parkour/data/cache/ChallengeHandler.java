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

package me.lorenzo0111.parkour.data.cache;

import me.lorenzo0111.parkour.ParkourPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChallengeHandler {
    private final ParkourPlugin plugin;
    private final Map<Player, Challenge> challenges = new HashMap<>();
    private final List<Player> delay = new ArrayList<>();

    public ChallengeHandler(ParkourPlugin plugin) {
        this.plugin = plugin;
    }

    public void start(Player player, Challenge challenge) {
        challenges.put(player, challenge);
    }

    public Challenge end(Player player) {
        Challenge challenge = challenges.remove(player);
        challenge.setEnd(Instant.now());
        player.teleport(challenge.getParkour().getStart());
        delay(player);
        return challenge;
    }

    public Challenge get(Player player) {
        return challenges.get(player);
    }

    public boolean inChallenge(Player player) {
        return challenges.containsKey(player);
    }

    public void delay(Player player) {
        delay.add(player);
        Bukkit.getScheduler().runTaskLater(plugin, () -> delay.remove(player), 20L);
    }

    public boolean isDelayed(Player player) {
        return delay.contains(player);
    }
}
