package me.lorenzo0111.parkour.data.cache;

import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class ChallengeHandler {
    private static ChallengeHandler instance;
    private final Map<Player,Challenge> challenges = new HashMap<>();

    public void start(Player player, Challenge challenge) {
        challenges.put(player, challenge);
    }

    public Challenge end(Player player) {
        Challenge challenge = challenges.remove(player);
        challenge.setEnd(Instant.now());
        player.teleport(challenge.getParkour().getStart());
        return challenge;
    }

    public Challenge get(Player player) {
        return challenges.get(player);
    }

    public boolean inChallenge(Player player) {
        return challenges.containsKey(player);
    }

    public static ChallengeHandler getInstance() {
        if (instance == null) instance = new ChallengeHandler();
        return instance;
    }
}
