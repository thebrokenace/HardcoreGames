package Kits.KitListeners.Kits.Utility;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Hermit {

    public static Location hermitTeleport(Location origin) {

        Location loc = origin;



        int maxY = 5;
        int minY = -5;
        int rangeY = (maxY - minY) + 1;
        int randY = (int) (Math.random() * rangeY) + minY;


        int maxX = 1000;
        int minX = -1000;
        int rangeX = (maxX - minX) + 1;
        int randX = (int) (Math.random() * rangeX) + minX;

        int maxZ = 1000;
        int minZ = -1000;
        int rangeZ = (maxZ - minZ) + 1;
        int randZ = (int) (Math.random() * rangeZ) + minZ;
        World world = loc.getWorld();


        Location rawLocation = new Location(world, loc.getX() + randX, loc.getY() + randY, loc.getZ() + randZ);
        int i = 0;




        while (((isSafeLocation(rawLocation)) || (rawLocation == loc))) {

            i = i + 1;
            if (i>100) {
                break;
            }
            maxY = 5;
            minY = -5;
            rangeY = (maxY - minY) + 1;
            randY = (int) (Math.random() * rangeY) + minY;


            maxX = 1000;
            minX = -1000;
            rangeX = (maxX - minX) + 1;
            randX = (int) (Math.random() * rangeX) + minX;

            maxZ = 1000;
            minZ = -1000;
            rangeZ = (maxZ - minZ) + 1;
            randZ = (int) (Math.random() * rangeZ) + minZ;
            world = loc.getWorld();
            if (world != null)

                rawLocation = new Location(world, loc.getX() + randX, loc.getWorld().getHighestBlockYAt(loc) + 1, loc.getZ() + randZ);
        }
        return rawLocation;
    }
    static Location getRandomSafeLocation(Location origin) {

        Location loc = origin;



        int maxY = 5;
        int minY = -5;
        int rangeY = (maxY - minY) + 1;
        int randY = (int) (Math.random() * rangeY) + minY;


        int maxX = 50;
        int minX = -50;
        int rangeX = (maxX - minX) + 1;
        int randX = (int) (Math.random() * rangeX) + minX;

        int maxZ = 50;
        int minZ = -50;
        int rangeZ = (maxZ - minZ) + 1;
        int randZ = (int) (Math.random() * rangeZ) + minZ;
        World world = loc.getWorld();


        Location rawLocation = new Location(world, loc.getX() + randX, loc.getY() + randY, loc.getZ() + randZ);
        int i = 0;




        while (((isSafeLocation(rawLocation)) || (rawLocation == loc))) {

            i = i + 1;
            if (i>100) {
                break;
            }
            maxY = 5;
            minY = -5;
            rangeY = (maxY - minY) + 1;
            randY = (int) (Math.random() * rangeY) + minY;


            maxX = 50;
            minX = -50;
            rangeX = (maxX - minX) + 1;
            randX = (int) (Math.random() * rangeX) + minX;

            maxZ = 50;
            minZ = -50;
            rangeZ = (maxZ - minZ) + 1;
            randZ = (int) (Math.random() * rangeZ) + minZ;
            world = loc.getWorld();
            if (world != null)

                rawLocation = new Location(world, loc.getX() + randX, loc.getWorld().getHighestBlockYAt(loc) + 1, loc.getZ() + randZ);
        }
        return rawLocation;
    }
    public static boolean isSafeLocation(Location location) {
        try {
            Block feet = location.getBlock();
            if (!feet.getType().isTransparent() && !feet.getLocation().add(0, 1, 0).getBlock().getType().isTransparent()) {
                return true; // not transparent (will suffocate)
            }
            Block head = feet.getRelative(BlockFace.UP);
            if (!head.getType().isTransparent()) {
                return true; // not transparent (will suffocate)
            }
            Block ground = feet.getRelative(BlockFace.DOWN);
            // returns if the ground is solid or not.
            return !ground.getType().isSolid();
        } catch (Exception err) {
        }
        return true;
    }
}
