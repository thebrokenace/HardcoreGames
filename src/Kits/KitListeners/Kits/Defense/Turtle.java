package Kits.KitListeners.Kits.Defense;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import Util.Sounds;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

public class Turtle implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onTakeDamage (EntityDamageByEntityEvent e) {
        if (game.isStarted()) {
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                if (kitInfo.getPlayerKit(p) == Kits.TURTLE) {
                    if (p.isSneaking()) {
                        e.setDamage(0.20 * e.getDamage());
                        Sounds.turtleHitShift(p.getLocation());

                    }
                }
            }

            if (e.getDamager() instanceof Player) {
                Player p = (Player) e.getDamager();
                if (kitInfo.getPlayerKit(p) == Kits.TURTLE) {
                    if (p.isSneaking()) {
                        e.setCancelled(true);
                    }
                }
            }
        }
     }


     @EventHandler
    public void onShoot (EntityShootBowEvent e) {
         if (e.getEntity() instanceof Player) {
             Player p = (Player) e.getEntity();
             if (kitInfo.getPlayerKit(p) == Kits.TURTLE) {
                 if (p.isSneaking()) {
                     e.setConsumeItem(false);
                     e.setCancelled(true);
                 }
             }
         }
     }
}
