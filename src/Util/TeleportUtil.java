package Util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class TeleportUtil {

    public static HashSet<Material> bad_blocks = new HashSet<>();

    static{
        bad_blocks.add(Material.LAVA);
        bad_blocks.add(Material.FIRE);
        bad_blocks.add(Material.CACTUS);
        bad_blocks.add(Material.WATER);
        bad_blocks.add(Material.MAGMA_BLOCK);
    }

    public static Location generateLocation(Entity player, boolean hermit, int radius){
        //Generate Random Location
        Random random = new Random(System.currentTimeMillis());


        int x = 0;
        int z = 0;
        int y = 0;

        if (hermit) {
            x = random.nextInt(1000) + 1000;
            z = random.nextInt(1000) + 1000;
            y = 150;
        } else {
            x = random.nextInt(radius);
            z = random.nextInt(radius);
            y = 150;
        }


        Location randomLocation = new Location(player.getWorld(), x, y, z);
        if (randomLocation.getWorld() != null)
        y = randomLocation.getWorld().getHighestBlockYAt(randomLocation);
        randomLocation.setY(y + 1);

        return randomLocation;
    }

    public static Location findSafeLocation(Entity player, boolean hermit){
        int radius = 60;
        Location randomLocation = generateLocation(player, hermit, radius);
        int maxAttempts = 5000;
        int attempts = 0;
        while (attempts < maxAttempts){
            if (attempts > 2000) {
                if (attempts % 5 == 0) {
                    radius++;
                }
            }
            //Keep looking for a safe location
            randomLocation = generateLocation(player, hermit, radius);
            if (!isLocationSafe(randomLocation)) {
                attempts++;
            } else {
                maxAttempts = 0;
                return randomLocation;
            }

        }
        return randomLocation;
    }

    private static boolean isLocationSafe(final Location location) {

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        Block block = location.getWorld().getBlockAt(x, y, z);
        Block below = location.getWorld().getBlockAt(x, y - 1, z);
        Block above = location.getWorld().getBlockAt(x, y + 1, z);
//
//        if (block.getBiome().equals(Biome.OCEAN)) {
//            return true;
//        }
        Biome biome = block.getBiome();
        List<Biome> badBiomes = new ArrayList<>();
        badBiomes.add(Biome.OCEAN);
        badBiomes.add(Biome.COLD_OCEAN);
        badBiomes.add(Biome.DEEP_COLD_OCEAN);
        badBiomes.add(Biome.DEEP_FROZEN_OCEAN);
        badBiomes.add(Biome.DEEP_LUKEWARM_OCEAN);
        badBiomes.add(Biome.DEEP_OCEAN);
        badBiomes.add(Biome.DEEP_WARM_OCEAN);
        badBiomes.add(Biome.FROZEN_OCEAN);
        badBiomes.add(Biome.LUKEWARM_OCEAN);
        badBiomes.add(Biome.WARM_OCEAN);
        if (badBiomes.contains(biome)) {
            return false;
        }

            return (!bad_blocks.contains(above.getType()))
                    && (!bad_blocks.contains(block.getType()))
                    && (!bad_blocks.contains(below.getType()))
                    && (!above.getType().isSolid())
                    && (!block.getType().isSolid())
                    && (below.getType().isSolid());


    }
//    // Bad blocks, or bad biome
//    private boolean badBlock(Block block, int x, int z, World world, Biome biomes) {
//        for (Material currentBlock : bad_blocks) //Check Block
//            if (block.getType().equals(currentBlock))
//                return true;
//        //Check Biomes
//
//            if (block.getBiome().equals(biomes))
//                return true;
//        return false;
//        //FALSE MEANS NO BAD BLOCKS/BIOME WHERE FOUND!
//    }


    public static Location generateFeastLocation(Location location, int radius){
        //Generate Random Location
        Random random = new Random(System.currentTimeMillis());

        int x = 0;
        int z = 0;
        int y = 0;

            x = random.nextInt(radius - 30);
            z = random.nextInt(radius - 30);
            y = 150;



        Location randomLocation = new Location(location.getWorld(), x, y, z);
        y = randomLocation.getWorld().getHighestBlockYAt(randomLocation);
        randomLocation.setY(y);

        return randomLocation;
    }

    public static Location findSafeFeastLocation(Location location, int newSize){

        Location randomLocation = generateFeastLocation(location, newSize);

        while (!isLocationSafe(randomLocation)){
            //Keep looking for a safe location
            randomLocation = generateFeastLocation(location, newSize);
        }
        return randomLocation;
    }
}
