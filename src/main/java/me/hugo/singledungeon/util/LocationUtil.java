package me.hugo.singledungeon.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public final class LocationUtil {

    public static Location center(Location location) {
        return new Location(location.getWorld(), location.getBlockX() + 0.5D, location.getY(), location.getBlockZ() + 0.5D, location.getYaw(), location.getPitch());
    }

    public static String getStringByLocation(Location loc, boolean center) {
        if (center) loc = center(loc);

        return loc.getWorld().getName() + " , " + loc.getX() + " , " + loc.getY() + " , " + loc.getZ() + " , " + loc.getPitch() + " , " + loc.getYaw();
    }

    public static String getStringByLocation(Location loc) {
        return loc.getWorld().getName() + " , " + loc.getX() + " , " + loc.getY() + " , " + loc.getZ() + " , " + loc.getPitch() + " , " + loc.getYaw();
    }

    public static Location getLocationByString(String key, Location def) {
        if (key == null) return def;

        String[] split = key.split(" , ");
        if (split.length == 6)
            return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[5]), Float.parseFloat(split[4]));

        return def;
    }

    public static Location getLocationByString(String key) {
        if (key == null) return null;

        String[] split = key.split(" , ");
        if (split.length == 6)
            return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[5]), Float.parseFloat(split[4]));

        return null;
    }

    public static Location getLocationByString(String key, World specificWorld) {
        if (key == null) return null;

        String[] split = key.split(" , ");
        if (split.length == 6)
            return new Location(specificWorld, Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[5]), Float.parseFloat(split[4]));

        return null;
    }

}
