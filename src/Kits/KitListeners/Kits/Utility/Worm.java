package Kits.KitListeners.Kits.Utility;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import Util.Sounds;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Worm implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();

    @EventHandler
    public void onLookBlock (PlayerInteractEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.WORM) {
                if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.DIRT) {
                        if (e.getClickedBlock().getLocation().getWorld() != null)
                        e.getClickedBlock().getLocation().getWorld().dropItemNaturally(e.getClickedBlock().getLocation(), new ItemStack(Material.DIRT));
                        e.getClickedBlock().setType(Material.AIR);
                        if (e.getPlayer().getFoodLevel() != 20) {
                            if (e.getPlayer().getFoodLevel() >= 19) {
                                e.getPlayer().setFoodLevel(20);
                            } else {
                                e.getPlayer().setFoodLevel(e.getPlayer().getFoodLevel() + 1);
                                Sounds.wormEat(e.getPlayer());
                            }
                        }
                        }
                }
            }
        }
    }
}
