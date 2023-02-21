package Feast;

import Kits.KitListeners.Kits.Utility.Spy;
import Listeners.PlayerInteractListener;
import Main.HardcoreGames;
import Util.TeleportUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static KitGUI.InventoryGUI.glowItem;

public class MainFeast implements Listener {

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.getInventory() instanceof EnchantingInventory) {
            EnchantingInventory inv = (EnchantingInventory) e.getInventory();

            ItemStack i = new ItemStack(Material.LAPIS_LAZULI);
            i.setAmount(20);
            inv.setItem(1, i);

        }
    }
    @EventHandler
    public void onClose (InventoryCloseEvent e) {
        if (e.getInventory() instanceof EnchantingInventory) {
            EnchantingInventory einv = (EnchantingInventory) e.getInventory();
            ItemStack i = new ItemStack(Material.AIR);
            einv.setItem(1, i);

        }
    }
    @EventHandler
    public void onClick (InventoryClickEvent e) {
        if (e.getInventory() instanceof EnchantingInventory) {
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.LAPIS_LAZULI) {
                e.setCancelled(true);
            }
        }

    }
    public static Location randomMiniFeastArea (World world, double newSize) {
        if (world != null) {
            WorldBorder worldBorder = world.getWorldBorder();
            Location center = worldBorder.getCenter();
            if (newSize != 0) {
                return TeleportUtil.generateFeastLocation(center, (int) newSize);
            } else {
                return TeleportUtil.generateFeastLocation(center, (int) (worldBorder.getSize() * 0.50));
            }

        } else {
            return null;
        }
    }
    public static void spawnFeast (World world, double size) {



        Location l = world.getWorldBorder().getCenter();

        if (l.getWorld() != null)
        l.setY(70);




        //spawn feast here
        //Bukkit.broadcastMessage(l.toString() + "feast location");
        //create grass circle
        for (int i = 0; i < 32; i++) {
            if (i % 2 == 0) {
                cylinder(l.clone().add(0, i, 0), Material.GLOWSTONE, 16+i);
            } else {

                cylinder(l.clone().add(0, i, 0), Material.QUARTZ_BLOCK, 16+i);


            }
            if (i == 31) {


                cylinder(l.clone().add(0, i, 0), Material.GRASS_BLOCK, 16+i);
                for (int p =0; p < 31; p++) {
                    cylinder(l.clone().add(0, i+1+p,0), Material.AIR, 16+i);
                }
                dome(l.clone().add(0,i,0), 16+i);
            }
        }

        Bukkit.broadcastMessage(ChatColor.AQUA + "The Feast has just spawned at X: " + l.getX() + " Y: " + l.getY() + " Z: " + l.getZ() + "!!!" + "\nAll compasses will now point at the Feast for the next 30 seconds!\nThe Feast will contain diamond items, rare gear, potions, food, soup, kit refills, and much, much more!" );
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
                if (time > 30*10) {
                    cancel();
                }
                time ++;
            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);


        Location bl = l.clone().add(0,32,0);
        List<Chest> FeastChests = new ArrayList<>();
        for (Location chestLoc : circle(bl, 7, true)) {
            Block b = chestLoc.getBlock();
            b.setType(Material.CHEST);
            Chest chest = (Chest) b.getState();
            FeastChests.add(chest);
        }
        for (Location chestLoc : circle(bl, 6, false)) {
            Block b = chestLoc.getBlock();
            b.setType(Material.QUARTZ_PILLAR);
        }
        for (Location chestLoc : circle(bl.clone().add(0,1,0), 6, true)) {
            Block b = chestLoc.getBlock();
            b.setType(Material.CHEST);
            Chest chest = (Chest) b.getState();
            FeastChests.add(chest);
        }
        for (Location chestLoc : circle(bl.clone().add(0,1,0), 5, false)) {
            Block b = chestLoc.getBlock();
            b.setType(Material.QUARTZ_PILLAR);
        }
        for (Location chestLoc : circle(bl.clone().add(0,2,0), 5, true)) {
            Block b = chestLoc.getBlock();
            b.setType(Material.CHEST);
            Chest chest = (Chest) b.getState();
            FeastChests.add(chest);
        }
        for (Location chestLoc : circle(bl.clone().add(0,2,0), 4, false)) {
            Block b = chestLoc.getBlock();
            b.setType(Material.QUARTZ_PILLAR);
        }

        //miniFeastChests.add(chest);
//        miniFeastChests.add(north);
//        miniFeastChests.add(west);
//        miniFeastChests.add(east);
//        miniFeastChests.add(south);

        for (Chest chests : FeastChests) {
            fillChests(chests);
        }



        Block center = bl.clone().add(0,2,0).getBlock();
        center.setType(Material.GLOWSTONE);
        Block table = center.getRelative(BlockFace.UP);
        table.setType(Material.ENCHANTING_TABLE);



//        EnchantingTable etable = (EnchantingTable) table;
//        etable.setMetadata("feast", new FixedMetadataValue(HardcoreGames.getInstance(), true));

        for (Block b : getNearbyBlocks(table.getLocation(), 2)) {
            b.setType(Material.BOOKSHELF);
        }
        for (Block b : getNearbyBlocks(table.getLocation(), 1)) {
            b.setType(Material.AIR);
        }
        table.setType(Material.ENCHANTING_TABLE);


        //Chest chest = (Chest) block.getState();



            }




    public static Set<Location> circle(Location location, int radius, boolean hollow){
        Set<Location> blocks = new HashSet<Location>();
        World world = location.getWorld();
        int X = location.getBlockX();
        int Y = location.getBlockY();
        int Z = location.getBlockZ();
        int radiusSquared = radius * radius;

        if(hollow){
            for (int x = X - radius; x <= X + radius; x++) {
                for (int z = Z - radius; z <= Z + radius; z++) {
                    if ((X - x) * (X - x) + (Z - z) * (Z - z) <= radiusSquared) {
                        Location block = new Location(world, x, Y, z);
                        blocks.add(block);
                    }
                }
            }
            return makeHollow(blocks, false);
        } else {
            for (int x = X - radius; x <= X + radius; x++) {
                for (int z = Z - radius; z <= Z + radius; z++) {
                    if ((X - x) * (X - x) + (Z - z) * (Z - z) <= radiusSquared) {
                        Location block = new Location(world, x, Y, z);
                        blocks.add(block);
                    }
                }
            }
            return blocks;
        }
    }
    private static Set<Location> makeHollow(Set<Location> blocks, boolean sphere){
        Set<Location> edge = new HashSet<Location>();
        if(!sphere){
            for(Location l : blocks){
                World w = l.getWorld();
                int X = l.getBlockX();
                int Y = l.getBlockY();
                int Z = l.getBlockZ();
                Location front = new Location(w, X + 1, Y, Z);
                Location back = new Location(w, X - 1, Y, Z);
                Location left = new Location(w, X, Y, Z + 1);
                Location right = new Location(w, X, Y, Z - 1);
                if(!(blocks.contains(front) && blocks.contains(back) && blocks.contains(left) && blocks.contains(right))){
                    edge.add(l);
                }
            }
            return edge;
        } else {
            for(Location l : blocks){
                World w = l.getWorld();
                int X = l.getBlockX();
                int Y = l.getBlockY();
                int Z = l.getBlockZ();
                Location front = new Location(w, X + 1, Y, Z);
                Location back = new Location(w, X - 1, Y, Z);
                Location left = new Location(w, X, Y, Z + 1);
                Location right = new Location(w, X, Y, Z - 1);
                Location top = new Location(w, X, Y + 1, Z);
                Location bottom = new Location(w, X, Y - 1, Z);
                if(!(blocks.contains(front) && blocks.contains(back) && blocks.contains(left) && blocks.contains(right) && blocks.contains(top) && blocks.contains(bottom))){
                    edge.add(l);
                }
            }
            return edge;
        }
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, location.getBlockY(), z));
                }

        }
        return blocks;
    }
    public static void fillChests (Chest chest) {
        chest.setCustomName(ChatColor.DARK_RED + "Feast");
        chest.update();
        addRandomItems(chest);

    }

    public static void addRandomItems (Chest chest) {
        Random rand = new Random();
        int bound = 15;
        int amountOfItems = rand.nextInt(bound) + 5; //9-14
        for (int i = 0; i < amountOfItems; i++) {
            Random slot = new Random();
            int slotbound = 27;
            int pickslot = slot.nextInt(slotbound);

            Random rarity = new Random();

            float raritytier = rarity.nextFloat(); //0.00-1.00

            float legChance = 0.10f;
            float epicChance = 0.40f;
            float rareChance = 0.30f;
            float commonChance = 0.20f;

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
                    return itemStackMaker(Material.COOKED_BEEF, 32);
                case 1:
                    return itemStackMaker(Material.IRON_INGOT, 16);
                case 2:
                    return itemStackMaker(Material.COOKED_CHICKEN, 16);
                case 3:
                    return itemStackMaker(Material.BOWL, 16);
                case 4:
                    return itemStackMaker(Material.BROWN_MUSHROOM, 16);
                case 5:
                    return itemStackMaker(Material.RED_MUSHROOM, 16);
                case 6:
                    return kitRefill();

            }
        }
        if (i == 1) {
            Random random = new Random();
            int j = random.nextInt(12);
            switch (j) {
                //rare 1
                case 0: return itemStackMaker(Material.IRON_SWORD, 1);
                case 1: return itemStackMaker(Material.MUSHROOM_STEW, 1);
                case 2: return getPotionItemStack(PotionType.WATER_BREATHING, true, false);
                case 3: return itemStackMaker(Material.BOW, 1);
                case 4: return itemStackMaker(Material.ARROW, 32);
                case 5: return itemStackMaker(Material.WATER_BUCKET, 1);
                case 6: return itemStackMaker(Material.FLINT_AND_STEEL, 1);
                case 7: return itemStackMaker(Material.COBWEB, 32);
                case 8: return kitRefill();
                case 9: return itemStackMaker(Material.SPECTRAL_ARROW, 32);
                case 10: return itemStackMaker(Material.LAVA_BUCKET, 1);
                case 11: return itemStackMaker(Material.MUSHROOM_STEW, 1);

            }
        }
        if (i == 2) {
            Random random = new Random();
            int j = random.nextInt(18);
            switch (j) {
                //epic 2
                case 0: return itemStackMaker(Material.ENDER_PEARL, 16);
                case 1: return getPotionItemStack(PotionType.STRENGTH, true, false);
                case 2: return getPotionItemStack(PotionType.SPEED, true, false);
                case 3: return getPotionItemStack(PotionType.POISON, true, false);
                case 4: return getPotionItemStack(PotionType.WEAKNESS, true, false);
                case 5: return getPotionItemStack(PotionType.SLOWNESS, true, false);
                case 6: return getPotionItemStack(PotionType.FIRE_RESISTANCE, true, false);
                case 7: return getPotionItemStack(PotionType.INSTANT_DAMAGE, false, true);
                case 8: return getPotionItemStack(PotionType.JUMP, true, false);
                case 9: return kitRefill();
                case 10: return itemStackMaker(Material.DIAMOND_SWORD, 1);
                case 11: return itemStackMaker(Material.DIAMOND_HELMET, 1);
                case 12: return itemStackMaker(Material.DIAMOND_CHESTPLATE, 1);
                case 13: return itemStackMaker(Material.DIAMOND_LEGGINGS, 1);
                case 14: return itemStackMaker(Material.DIAMOND_BOOTS,1);
                case 15: return itemStackMaker(Material.EXPERIENCE_BOTTLE, 16);
                case 16: return itemStackMaker(Material.DIAMOND_AXE, 1);
                case 17: return itemStackMaker(Material.GOLDEN_APPLE, 6);

            }
        }
        if (i == 3) {
            Random random = new Random();
            int j = random.nextInt(10);
            switch (j) {
                //leg 3
                case 0: return getPotionItemStack(PotionType.SPEED, false, true);
                case 1: return getPotionItemStack(PotionType.STRENGTH, false, true);
                case 2: return getPotionItemStack(PotionType.INSTANT_DAMAGE, false, true);
                case 3: return itemStackMaker(Material.TNT, 32);
                case 4: return getPotionItemStack(PotionType.REGEN, false, true);
                case 5: return getPotionItemStack(PotionType.SPEED, true, false);
                case 6: return kitRefill();
                case 7: return getPotionItemStack(PotionType.INVISIBILITY, false, false);
                case 8: return itemStackMaker(Material.END_CRYSTAL, 10);
                case 9: return itemStackMaker(Material.OBSIDIAN, 10);
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
        glowItem(item);
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
