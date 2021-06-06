package Kits.KitListeners.Kits.Utility;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import Util.Sounds;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class Cultivator implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void place (BlockPlaceEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.CULTIVATOR) {
                if (e.getBlockPlaced().getType().name().toLowerCase().contains("sapling") && !e.getBlockPlaced().getType().name().toLowerCase().contains("potted")) {
                        e.getBlock().setType(Material.AIR);
                        e.getBlockPlaced().getWorld().generateTree(e.getBlockPlaced().getLocation(), TreeType.TREE);
                        Sounds.cultivatorGrowTree(e.getPlayer().getLocation());
                }
                if (e.getPlayer().getInventory().getItemInMainHand().getType().name().toLowerCase().contains("seeds") || e.getPlayer().getInventory().getItemInMainHand().getType() == Material.POTATO || e.getPlayer().getInventory().getItemInMainHand().getType() == Material.CARROT) {
                    //Bukkit.broadcastMessage("seeds in name");
                    if (e.getBlock().getBlockData() instanceof Ageable) {
                        //Bukkit.broadcastMessage("placed seeds");
                        Ageable ageable = ((Ageable) e.getBlock().getBlockData());
                        ageable.setAge(ageable.getMaximumAge());
                        e.getBlock().setBlockData(ageable);
                        e.getBlock().getState().update();

                    }

                }


            }
        }

    }
    @EventHandler
    public void place (BlockBreakEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.CULTIVATOR) {
                if (e.getBlock().getType() == Material.WHEAT || e.getBlock().getType() == Material.CARROTS || e.getBlock().getType() == Material.POTATOES || e.getBlock().getType() == Material.BEETROOTS) {

                    if (e.getBlock().getType() == Material.WHEAT) {
                        e.setDropItems(false);
                        if (e.getBlock().getLocation().getWorld() != null)
                        e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.WHEAT));
                        e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.WHEAT));

                    }
//                    if (e.getBlock().getType() == Material.CARROTS) {
//                        if (e.getBlock().getLocation().getWorld() != null)
//                            e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.CARROT));
//                    }
//                    if (e.getBlock().getType() == Material.POTATOES) {
//                        if (e.getBlock().getLocation().getWorld() != null)
//                            e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.POTATO));
//                    }
//                    if (e.getBlock().getType() == Material.BEETROOTS) {
//                        if (e.getBlock().getLocation().getWorld() != null)
//                            e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.BEETROOT));
//                    }
                }


            }
        }

    }
}
