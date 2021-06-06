package Util;

import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public class PregenerateRandomLocation {
    Map<Entity, Location> locationMap = new HashMap<>();
    Map<Entity, Location> hermitMap = new HashMap<>();


    private PregenerateRandomLocation() { } // make your constructor private, so the only war
    // to access "application" is through singleton pattern

    private static PregenerateRandomLocation _pregen;

    public static PregenerateRandomLocation get_pregen()
    {
        if (_pregen == null)
            _pregen = new PregenerateRandomLocation();
        return _pregen;
    }


    public Map<Entity, Location> getLocationMap() {
        return this.locationMap;
    }

    public Map<Entity, Location> getHermitMap() {
        return this.hermitMap;
    }

    public void putLocation(Entity e, boolean hermit) {
        if (hermit) {
            hermitMap.put(e, TeleportUtil.findSafeLocation(e, true));
        } else {
            locationMap.put(e, TeleportUtil.findSafeLocation(e, false));

        }
    }

    public Location getHermitLocation(Entity e) {
        return getHermitMap().getOrDefault(e, null);
    }

    public Location getEntityLocation(Entity e) {
        return getLocationMap().getOrDefault(e, null);
    }

    public void clearMap() {
        this.locationMap.clear();
        this.hermitMap.clear();
    }
}
