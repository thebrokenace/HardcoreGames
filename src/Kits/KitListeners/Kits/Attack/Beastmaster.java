package Kits.KitListeners.Kits.Attack;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class Beastmaster implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void onKill (PlayerDeathEvent e) {
        if (game.isStarted()) {
            if (e.getEntity().getKiller() != null) {
                Player killer = e.getEntity().getKiller();
                if (kitInfo.getPlayerKit(killer) == Kits.BEASTMASTER) {
                    killer.getInventory().addItem(new ItemStack(Material.WOLF_SPAWN_EGG));

                }
            }
        }

    }
}
