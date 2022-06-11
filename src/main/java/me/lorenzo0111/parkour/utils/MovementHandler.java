package me.lorenzo0111.parkour.utils;

import org.bukkit.Location;

public final class MovementHandler {

    public static boolean checkDistance(Location playerLocation, Location checkpointLocation) {
        double playerX = playerLocation.getX();
        double playerY = playerLocation.getY();
        double playerZ = playerLocation.getZ();

        double checkPointX = checkpointLocation.getX() + 0.5;
        double checkPointY = checkpointLocation.getY() + 0.5;
        double checkPointZ = checkpointLocation.getZ() + 0.5;

        double xDistance = Math.abs(playerX - checkPointX);
        double yDistance = Math.abs(playerY - checkPointY);
        double zDistance = Math.abs(playerZ - checkPointZ);

        return xDistance < 0.8 && yDistance < 0.8 && zDistance < 0.8;
    }
}
