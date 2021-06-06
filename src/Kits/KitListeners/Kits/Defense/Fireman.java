package Kits.KitListeners.Kits.Defense;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class Fireman implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void damageExplosion (EntityDamageEvent e) {
        if (game.isStarted()) {
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                if (kitInfo.getPlayerKit(p) == Kits.FIREMAN) {
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)) {
                        p.setFireTicks(0);
                        e.setCancelled(true);
                    }
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
                        p.setFireTicks(0);
                        e.setCancelled(true);
                    }

                }
            }
        }
    }

    @EventHandler
    public void lightning (EntityDamageByEntityEvent e) {
        if (game.isStarted()) {
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                if (kitInfo.getPlayerKit(p) == Kits.FIREMAN) {
                    if (e.getDamager() instanceof LightningStrike) {
                        //Bukkit.broadcastMessage("stuck by lighting");
                        p.setFireTicks(0);
                        e.setDamage(0);
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
