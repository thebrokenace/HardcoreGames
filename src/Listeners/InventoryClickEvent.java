package Listeners;

import Feast.MiniFeast;
import KitGUI.InventoryGUI;
import Kits.KitTools.KitInfo;
import Util.Game;
import Util.GamePhase;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class InventoryClickEvent implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void invClick (org.bukkit.event.inventory.InventoryClickEvent e) {
        if (!game.isStarted()) {
            if (!((Player) e.getWhoClicked()).isOp()) {
                e.setCancelled(true);
            }
        }  else {
            if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals("Kit Refill") && e.getCurrentItem().getType() == Material.SNOWBALL) {
                Player p = (Player) e.getWhoClicked();
                if (kitInfo.getKitDrops(kitInfo.getPlayerKit(p)).size() != 0) {

                    Random random = new Random(System.currentTimeMillis());
                    int refillAmount = kitInfo.getKitDrops(kitInfo.getPlayerKit(p)).size();
                    int pick = random.nextInt(refillAmount);
                    ItemStack refill = kitInfo.getKitDrops(kitInfo.getPlayerKit(p)).get(pick);
                    refill.setAmount(1);
                    e.setCurrentItem(refill);

                } else {
                    //no item kit
                    ItemStack refill = MiniFeast.randomLoot(3);
                    e.setCurrentItem(refill);

                }
            }
        }

    }


    @EventHandler
    public void onClickKitSelector (org.bukkit.event.inventory.InventoryClickEvent e) {
        if (game.getPhase() == GamePhase.PREGAME) {
            if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null && ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equals("Kit Selector")) {
                e.setCancelled(true);
                e.getWhoClicked().openInventory(InventoryGUI.playerUnlockedKits((Player) e.getWhoClicked()));
            }
        }
    }
}
