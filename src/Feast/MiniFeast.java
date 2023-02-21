package Feast;

import Kits.KitListeners.Kits.Utility.Spy;
import Listeners.PlayerInteractListener;
import Main.HardcoreGames;
import Util.TeleportUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MiniFeast {

    public static Location randomMiniFeastArea (World world, double newSize) {
        if (world != null) {
            WorldBorder worldBorder = world.getWorldBorder();
            Location center = worldBorder.getCenter();
            if (newSize != 0) {
                return TeleportUtil.generateFeastLocation(center, (int) newSize);
            } else {
                return TeleportUtil.generateFeastLocation(center, (int) (worldBorder.getSize()*0.50));
            }

        } else {
            return null;
        }

    }

    public static void spawnFeast (World world, double newSize) {

                Location l = null;
                if (newSize != 0) {
                    l = randomMiniFeastArea(world, 0);
                } else {
                    l = randomMiniFeastArea(world, newSize);

                }

                //spawn feast here
                //create grass circle
                for (int i = 0; i < 16; i++) {
                    if (i % 2 == 0) {
                        cylinder(l.clone().add(0, i, 0), Material.GLOWSTONE, 4 + i);
                    } else {

                        cylinder(l.clone().add(0, i, 0), Material.QUARTZ_BLOCK, 4 + i);


                    }
                    if (i == 15) {


                        cylinder(l.clone().add(0, i, 0), Material.GRASS_BLOCK, 4 + i);
                        for (int p = 0; p < 15; p++) {
                            cylinder(l.clone().add(0, i + 1 + p, 0), Material.AIR, 4 + i);
                        }
                        dome(l.clone().add(0, i, 0), 4 + i);
                    }
                }

                Bukkit.broadcastMessage(ChatColor.AQUA + "A Mini-Feast has just spawned at X: " + l.getX() + " Y: " + l.getY() + " Z: " + l.getZ() + "!" + "\nAll compasses will now point at the Mini Feast for the next 10 seconds!");
                Location finalL = l;
                Spy.cancelTrack();
                PlayerInteractListener.cancelTrack();
                new BukkitRunnable() {
                    int time = 0;

                    @Override
                    public void run() {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.setCompassTarget(finalL);
                        }
                        if (time > 20 * 10) {
                            cancel();
                        }
                        time++;
                    }
                }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);


                Block center = l.clone().add(0, 16, 0).getBlock();
                center.setType(Material.GLOWSTONE);
                l.clone().add(0, 17, 0).getBlock().setType(Material.CHEST);
                Block block = l.clone().add(0, 17, 0).getBlock();


                Chest chest = (Chest) block.getState();


                Block northChest = center.getRelative(BlockFace.NORTH);
                Block westChest = center.getRelative(BlockFace.WEST);
                Block eastChest = center.getRelative(BlockFace.EAST);
                Block southChest = center.getRelative(BlockFace.SOUTH);

                northChest.setType(Material.CHEST);
                westChest.setType(Material.CHEST);
                eastChest.setType(Material.CHEST);
                southChest.setType(Material.CHEST);

                List<Block> orient = new ArrayList<>();
                orient.add(northChest);
                orient.add(westChest);
                orient.add(eastChest);
                orient.add(southChest);
                for (Block b : orient) {
                    if (relativeToBlockandBlock(center, b) != null) {

                        if (setBlock(b, Material.CHEST, relativeToBlockandBlock(center, b)) != null) {
                            b.setBlockData(setBlock(b, Material.CHEST, relativeToBlockandBlock(center, b)));
                            b.getState().update();
                        }


                    }
                }


                List<Chest> miniFeastChests = new ArrayList<>();

                Chest north = (Chest) northChest.getState();
                Chest west = (Chest) westChest.getState();
                Chest east = (Chest) eastChest.getState();
                Chest south = (Chest) southChest.getState();

                miniFeastChests.add(chest);
                miniFeastChests.add(north);
                miniFeastChests.add(west);
                miniFeastChests.add(east);
                miniFeastChests.add(south);


                for (Chest chests : miniFeastChests) {
                    fillChests(chests);
                }


            }



    public static BlockData setBlock(Block block, Material material, BlockFace blockFace) {
        block.setType(material);
        BlockData blockData = block.getBlockData();
        if (blockData instanceof Directional) {
            ((Directional) blockData).setFacing(blockFace);
            return blockData;
        }
        if (blockData instanceof Orientable) {
            ((Orientable) blockData).setAxis(convertBlockFaceToAxis(blockFace));
            return blockData;

        }
        if (blockData instanceof Rotatable) {
            ((Rotatable) blockData).setRotation(blockFace);
            return blockData;

        }
        return null;
    }
    private static Axis convertBlockFaceToAxis(BlockFace face) {
        switch (face) {
            case NORTH:
            case SOUTH:
                return Axis.Z;
            case EAST:
            case WEST:
                return Axis.X;
            case UP:
            case DOWN:
                return Axis.Y;
            default:
                return Axis.X;
        }
    }
    public static BlockFace relativeToBlockandBlock (Block origin, Block direction) {
        int directX = direction.getX();
        int directZ = direction.getZ();

        int originX = origin.getX();
        int originZ = origin.getZ();

        //d.x > o.x (d more right than origin) && d.z < o.z (d more up than origin) (north east basically)
        //d.x < o.x (d more left than origin) && d.z > o.z (d more down than origin) (south west basically)
        //only want to output NESW
        //check only one line
        //d.x  == o.x (same vertically positioned block) && d.z < o.z (d directly above origin) (north)/(south)
        //d.z == o.z (same horizonatally positioned block) && d.x > o.x (d directly right of origin) (east/west)

        if (directX == originX && directZ < originZ) {
            //north
            //Bukkit.broadcastMessage("north");
            return BlockFace.NORTH;
        }
        if (directX == originX && directZ > originZ) {
            //south
            //Bukkit.broadcastMessage("south");
            return BlockFace.SOUTH;

        }
        if (directZ == originZ && directX > originX) {
            //east
            //Bukkit.broadcastMessage("east");
            return BlockFace.EAST;
        }
        if (directZ == originZ && directX < originX) {
            //west
           // Bukkit.broadcastMessage("west");
            return BlockFace.WEST;

        }

        return null;



    }




    public static void fillChests (Chest chest) {
        chest.setCustomName(ChatColor.RED + "Mini Feast");
        chest.update();
        addRandomItems(chest);

    }

    public static void addRandomItems (Chest chest) {
        Random rand = new Random();
        int bound = 9;
        int amountOfItems = rand.nextInt(bound) + 5; //9-14
        for (int i = 0; i < amountOfItems; i++) {
            Random slot = new Random();
            int slotbound = 27;
            int pickslot = slot.nextInt(slotbound);

            Random rarity = new Random();

            float raritytier = rarity.nextFloat(); //0.00-1.00

            float legChance = 0.10f;
            float epicChance = 0.20f;
            float rareChance = 0.30f;
            float commonChance = 0.40f;

            int tier = 0;
            //0.10
            if (raritytier <= legChance) {
                tier = 3;
                //0.10 + 0.20 = 0.30
            } else if (raritytier <= epicChance + legChance) {
                tier = 2;
                //0.30 + 0.30 = 0.60
            } else if (raritytier <= rareChance + epicChance) {
                tier = 1;
                //0.60 + 0.40 = 1.00
            } else if (raritytier <= commonChance + rareChance) {
                tier = 0;

            }

            //common at 40%, rare at 30%, epic at 20%, leg at 10%

            //common rare epic legendary



            //make 30 items for loottable. then divide into 7-8 items per group for 4 groups
            //with varying percentages to get each rarity

            //for example, roll 0-3, if u get 0 (most common) u can only get first 7 items from loottable at same chance

            //chest.getBlockInventory().addItem(randomLoot(tier));
            //Bukkit.broadcastMessage(randomLoot(tier).toString());
            chest.getBlockInventory().setItem(pickslot, randomLoot(tier));
        }
    }

    public static ItemStack randomLoot (int i) {
    if (i == 0) {
        Random random = new Random();
        int j = random.nextInt(7);
        switch (j) {
            //common 0
            case 0:
                return itemStackMaker(Material.COOKED_BEEF, 2);
            case 1:
                return itemStackMaker(Material.IRON_INGOT, 2);
            case 2:
                return itemStackMaker(Material.COOKED_CHICKEN, 2);
            case 3:
                return itemStackMaker(Material.BOWL, 6);
            case 4:
                return itemStackMaker(Material.OAK_PLANKS, 16);
            case 5:
                return itemStackMaker(Material.BROWN_MUSHROOM, 3);
            case 6:
                return itemStackMaker(Material.RED_MUSHROOM, 3);

        }
    }
        if (i == 1) {
            Random random = new Random();
            int j = random.nextInt(9);
            switch (j) {
                //rare 1
                case 0: return itemStackMaker(Material.IRON_SWORD, 1);
                case 1: return itemStackMaker(Material.MUSHROOM_STEW, 1);
                case 2: return getPotionItemStack(PotionType.WATER_BREATHING, true, false);
                case 3: return itemStackMaker(Material.BOW, 1);
                case 4: return itemStackMaker(Material.ARROW, 5);
                case 5: return itemStackMaker(Material.WATER_BUCKET, 1);
                case 6: return itemStackMaker(Material.FLINT_AND_STEEL, 1);
                case 7: return itemStackMaker(Material.COBWEB, 5);
                case 8: return kitRefill();
            }
        }
        if (i == 2) {
            Random random = new Random();
            int j = random.nextInt(14);
            switch (j) {
                //epic 2
                case 0: return itemStackMaker(Material.ENDER_PEARL, 1);
                case 1: return getPotionItemStack(PotionType.STRENGTH, false, false);
                case 2: return getPotionItemStack(PotionType.SPEED, false, false);
                case 3: return getPotionItemStack(PotionType.POISON, false, false);
                case 4: return getPotionItemStack(PotionType.WEAKNESS, false, false);
                case 5: return getPotionItemStack(PotionType.SLOWNESS, false, false);
                case 6: return getPotionItemStack(PotionType.FIRE_RESISTANCE, false, false);
                case 7: return getPotionItemStack(PotionType.INSTANT_DAMAGE, false, false);
                case 8: return getPotionItemStack(PotionType.JUMP, false, false);
                case 9: return itemStackMaker(Material.IRON_HELMET, 1);
                case 10: return itemStackMaker(Material.IRON_CHESTPLATE, 1);
                case 11: return itemStackMaker(Material.IRON_LEGGINGS, 1);
                case 12: return itemStackMaker(Material.IRON_BOOTS, 1);
                case 13: return itemStackMaker(Material.IRON_SWORD, 1);

            }
        }
        if (i == 3) {
            Random random = new Random();
            int j = random.nextInt(9);
            switch (j) {
                //leg 3
                case 0: return getPotionItemStack(PotionType.SPEED, false, true);
                case 1: return getPotionItemStack(PotionType.STRENGTH, true, false);
                case 2: return getPotionItemStack(PotionType.INSTANT_DAMAGE, false, true);
                case 3: return itemStackMaker(Material.TNT, 1);
                case 4: return getPotionItemStack(PotionType.REGEN, false, false);
                case 5: return getPotionItemStack(PotionType.SPEED, true, false);
                case 6: return itemStackMaker(Material.DIAMOND, 1);
                case 7: return itemStackMaker(Material.ENDER_PEARL, 3);
                case 8: return kitRefill();

            }




        }
        //Bukkit.broadcastMessage("retruning dirt");
        return new ItemStack(Material.DIRT);
    }

    public static ItemStack kitRefill () {
        ItemStack item = new ItemStack(Material.SNOWBALL);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("Kit Refill");
        }
        item.setItemMeta(meta);
        return item;
    }

    public static void dome (Location location, int rad) {
        Location centerPos = location;
        Location centerLoc = location;
        centerPos.setX(centerPos.getX() + 0.5);
        centerPos.setY(centerPos.getY() + 0.5);
        centerPos.setZ(centerPos.getZ() + 0.5);
        final int radius = rad;
        final ArrayList<Block> blocks = new ArrayList<Block>();
        for (int i = -radius; i <= radius; ++i) {
            for (int j = 0; j <= radius; ++j) {
                for (int k = -radius; k <= radius; ++k) {
                    if ((int)Math.sqrt(i * i + j * j + k * k) == radius) {
                        blocks.add(centerLoc.getWorld().getBlockAt((int)(i + centerLoc.getX()), (int)(j + centerLoc.getY()), (int)(k + centerLoc.getZ())));
                    }
                }
            }
        }
        ArrayList<Block> clone = (ArrayList<Block>) blocks.clone();
        new BukkitRunnable() {
            public void run() {
                for (int j = 0; j < 2; j++) {
                    if (blocks.size() > 0) {
                        blocks.get(0).setType(Material.GLASS);

                        blocks.remove(0);
                        if (!blocks.isEmpty()) {
                            blocks.get(blocks.size() - 1).setType(Material.GLASS);
                            blocks.remove(blocks.size() - 1);
                        }
                    } else {
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 0L);

        for (Block b : clone) {
            b.setType(Material.GLOWSTONE);
        }

    }


    public static ItemStack itemStackMaker (Material mat, int amount) {
        ItemStack item = new ItemStack(mat);
        item.setAmount(amount);
        return item;
    }

    public static ItemStack getPotionItemStack(PotionType type, boolean extend, boolean upgraded){
        ItemStack potion = new ItemStack(Material.SPLASH_POTION, 1);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        if (meta != null) {
            meta.setBasePotionData(new PotionData(type, extend, upgraded));
        }
        potion.setItemMeta(meta);
        return potion;
    }

    public boolean isWithinBorder (Location loc) {
        if (loc.getWorld() != null) {
            return loc.getWorld().getWorldBorder().isInside(loc);
        }
    return false;
    }
    public static void miniFeast (Location loc) {

    }



    public static void cylinder(Location loc, Material mat, int r) {
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        World w = loc.getWorld();
        int rSquared = r * r;
        for (int x = cx - r; x <= cx +r; x++) {
            for (int z = cz - r; z <= cz +r; z++) {
                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
                    w.getBlockAt(x, cy, z).setType(mat);
                }
            }
        }
    }
}
