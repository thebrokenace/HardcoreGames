package Kits.KitListeners.Kits.Vanity;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import Util.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public class Burrower implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();

    @EventHandler
    public void onUse (PlayerInteractEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.BURROWER) {
                if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.EGG) {
                    if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null && ChatColor.stripColor(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Burrower Panic Room")) {
                            e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
                            createRoom(e.getPlayer());
                        }
                    }
                }
            }
        }
    }


    public void createRoom (Player p) {
        Sounds.burrowRoom(p.getLocation());
        Location origin = p.getLocation();
        Location startingPoint = origin.clone().subtract(0, 20, 0);
        if (startingPoint.getY() <= 6) {
            startingPoint.setY(7);
        }
        for (int i = 0; i < 5; i++) {
                for (Block b : getNearbyBlocksY(startingPoint.clone().add(0, i, 0), 5)) {
                    if (i != 4 && i != 0) {
                        b.setType(Material.AIR);
                    } else {
                        b.setType(Material.OAK_PLANKS);
                    }
                }

        }

        for (Block b : getNearbyBlocks(startingPoint.clone(), 6)) {
            if (b.getType() != Material.BEDROCK && b.getType() != Material.AIR) {
                b.setType(Material.OAK_PLANKS);
            }
        }
//        for (int x = -7; x <= 7; x++) {
//            for (int z = -7; z <= 7; z++) {
//                for (int y = -3; y <= 3; y++) {
//                    Block block = startingPoint.clone().add(0,3,0).getBlock().getRelative(x, y, z);
//                    block.setType(Material.RED_WOOL);
//                }
//            }
//        }
        //pasteHollowCube(startingPoint.clone().add(0,3,0), 5, 5, 5, Material.RED_WOOL);
        startingPoint.getBlock().setType(Material.GLOWSTONE);
        Block block = startingPoint.getBlock().getRelative(BlockFace.UP);

        BlockData pebble = Material.STONE_BUTTON.createBlockData("[face=floor]");
        block.setBlockData(pebble);

        startingPoint.getBlock().getRelative(BlockFace.UP).setMetadata("panicescape", new FixedMetadataValue(HardcoreGames.getInstance(), this));

        p.teleport(startingPoint.clone().add(0, 2,0));




    }
//    public static void pasteHollowCube(Location loc, int w, int h, int l, Material type){
//
//        for(int i = 0; i < w;i++){
//            for(int j = 0; j < h;j++){
//                for(int k = 0;k < l;k++){
//
//                    Block bl = loc.getBlock().getRelative(i, j, k);
//
//                    if(i == 0 || j == 0 || k  == 0){
//                        if(i == w-1 || j == h-1 || k == l-1){
//                            bl.setType(type);
//                        }
//                    }
//
//                }
//            }
//        }
//
//    }
    public static List<Block> getNearbyBlocksY(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {

                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {

                    blocks.add(location.getWorld().getBlockAt(x, location.getBlockY(), z));
                }

        }
        return blocks;
    }


    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }
    @EventHandler
    public void pressedButton (PlayerInteractEvent e) {
        if (game.isStarted()) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.STONE_BUTTON) {
                   if (e.getClickedBlock().hasMetadata("panicescape")) {
                        e.getPlayer().teleport(new Location(e.getPlayer().getWorld(),e.getPlayer().getLocation().getX(), e.getPlayer().getLocation().getWorld().getHighestBlockAt(e.getPlayer().getLocation()).getLocation().getY() + 2, e.getPlayer().getLocation().getZ()));
                        e.getClickedBlock().removeMetadata("panicescape", HardcoreGames.getInstance());
                        //e.getClickedBlock().getLocation().getWorld().createExplosion(e.getClickedBlock().getLocation(), 8, true);
                        e.getClickedBlock().setType(Material.AIR);

                   }
                }
            }
        }
    }
}
