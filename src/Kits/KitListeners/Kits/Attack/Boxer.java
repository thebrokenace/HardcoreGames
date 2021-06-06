package Kits.KitListeners.Kits.Attack;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import Util.Sounds;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Boxer implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();

    @EventHandler
    public void onHit (EntityDamageByEntityEvent e) {
        if (!e.isCancelled()) {
            if (game.isStarted()) {
                if (e.getDamager() instanceof Player) {
                    Player boxer = (Player) e.getDamager();
                    if (kitInfo.getPlayerKit(boxer) == Kits.BOXER) {
                        if (boxer.getInventory().getItemInMainHand().getType() == Material.AIR) {
                            Sounds.boxerFist(boxer.getLocation());
                            e.setDamage(3.5);
                        }
                    }
                }
                if (e.getEntity() instanceof Player) {
                    Player boxer = (Player) e.getEntity();
                    if (kitInfo.getPlayerKit(boxer) == Kits.BOXER) {
                        e.setDamage(e.getDamage()-1);
                    }
                }
            }
        }
    }
}
