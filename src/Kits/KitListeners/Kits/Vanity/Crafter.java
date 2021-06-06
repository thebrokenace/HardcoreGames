package Kits.KitListeners.Kits.Vanity;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import com.shanebeestudios.vf.api.machine.Furnace;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Crafter implements Listener {
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    Game game = Game.getSharedGame();
    static Map<UUID, Furnace> furnaceMap = new HashMap<>();
    @EventHandler
    public void onRightClick (PlayerInteractEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.CRAFTER) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

                    if (e.getItem() != null && e.getItem().getType() == Material.CRAFTING_TABLE && e.getItem().getItemMeta() != null && ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Crafter's Crafting Table")) {

                        e.getPlayer().openWorkbench(null, true);


                    }

                    if (e.getItem() != null && e.getItem().getType() == Material.FURNACE && e.getItem().getItemMeta() != null && ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Crafter's Furnace")) {
                        if (furnaceMap.containsKey(e.getPlayer().getUniqueId())) {
                            Furnace furnace = furnaceMap.get(e.getPlayer().getUniqueId());
                            furnace.openInventory(e.getPlayer());
                            furnaceMap.put(e.getPlayer().getUniqueId(), furnace);

                        } else {


                            Furnace furnace = HardcoreGames.getInstance().getFurnaceManager().createFurnace("&b" + e.getPlayer().getName() + "'s Furnace");

                            //Inventory furnace = Bukkit.createInventory(e.getPlayer(), CraftInventoryFurnace);
                            //e.getPlayer().openInventory(furnace);
                            furnace.openInventory(e.getPlayer());

                            furnaceMap.put(e.getPlayer().getUniqueId(), furnace);

                        }
                        //Inventory inventory = furnace.getInventory();


                    }




                    }
            }


        }
    }

    @EventHandler
    public void onDeath (PlayerDeathEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getEntity()) == Kits.CRAFTER) {
                if (furnaceMap.containsKey(e.getEntity().getUniqueId())) {
                    Furnace furnace = furnaceMap.get(e.getEntity().getUniqueId());
                    List<ItemStack> drops = new ArrayList<>();
                    drops.add(furnace.getFuel());
                    drops.add(furnace.getInput());
                    drops.add(furnace.getOutput());
                    e.getDrops().addAll(drops);
                    furnaceMap.remove(e.getEntity().getUniqueId());
                }
            }
        }
    }


    @EventHandler
    public void onPlace (BlockPlaceEvent e) {
        if (e.getItemInHand().getType() == Material.CRAFTING_TABLE && e.getItemInHand().getItemMeta() != null && ChatColor.stripColor(e.getItemInHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Crafter's Crafting Table")) {
            e.setCancelled(true);
        }
        if (e.getItemInHand().getType() == Material.FURNACE && e.getItemInHand().getItemMeta() != null && ChatColor.stripColor(e.getItemInHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Crafter's Furnace")) {
            e.setCancelled(true);
        }
    }



    public static void clearfurnaces () {
        furnaceMap.clear();

    }

}
