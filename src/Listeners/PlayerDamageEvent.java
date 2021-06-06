package Listeners;

import Util.Game;
import Util.GamePhase;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageEvent implements Listener {
    Game game = Game.getSharedGame();
    @EventHandler
    public void onEntityDamage (EntityDamageEvent e) {
        //check for phase before allowing hit, don't allow any entity hit till timer goes off

        if (game.getPhase() == GamePhase.PREGAME || game.getPhase() == GamePhase.WINNERDECIDED) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onEntityByOtherDamage (EntityDamageByEntityEvent e) {
        if (game.getPhase() == GamePhase.GRACE) {
            if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
                e.setCancelled(true);
            }
            if (e.getEntity() instanceof Player && e.getDamager() instanceof Projectile) {
               if(((Projectile) e.getDamager()).getShooter() instanceof Player) {
                   e.setCancelled(true);
               }
            }
        }
    }

//    @EventHandler
//    public void onShoot (EntityShootBowEvent e) {
//        if (!game.isStarted() || game.getPhase() == GamePhase.GRACE) {
//            if (e.getEntity() instanceof Player) {
//                e.setCancelled(true);
//            }
//        }
//    }
}
