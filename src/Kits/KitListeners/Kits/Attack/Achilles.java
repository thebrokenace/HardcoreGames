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

public class Achilles implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();

    @EventHandler
    public void entityDamage (EntityDamageByEntityEvent e) {
        if (game.isStarted()) {
            if (e.getEntity() instanceof Player) {

                Player p = (Player) e.getEntity();
                if (kitInfo.getPlayerKit(p) == Kits.ACHILLES) {

                    if (e.getDamager() instanceof Player) {

                        Player hit = (Player) e.getDamager();
                        if (hit.getInventory().getItemInMainHand().getType().name().toLowerCase().contains("wooden") || hit.getInventory().getItemInMainHand().getType() == Material.STICK) {
                            Sounds.achillesHitGood(hit.getLocation());
                            if (hit.getInventory().getItemInMainHand().getType() == Material.WOODEN_SWORD) {
                                e.setDamage(7);
                            } else {
                                e.setDamage(4);
                            }
                        } else {
                            Sounds.achillesHitBad(hit.getLocation());
                            e.setDamage(0.4 * e.getDamage());
                        }
                    } else {
                        e.setDamage(0.4 * e.getDamage());

                    }


                }
            }

        }
    }
}
