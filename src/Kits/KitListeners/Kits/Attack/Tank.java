package Kits.KitListeners.Kits.Attack;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import Util.GamePhase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Tank implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void onKill (PlayerDeathEvent e) {
        if (game.isStarted() || game.getPhase() == GamePhase.WINNERDECIDED) {
            if (e.getEntity().getKiller() != null) {
                Player killer = e.getEntity().getKiller();
                if (kitInfo.getPlayerKit(killer) == Kits.TANK) {
                    if (e.getEntity().getLocation().getWorld() != null)

                    e.getEntity().getLocation().getWorld().createExplosion(e.getEntity().getLocation(), 5, false);

                }
            }
        }

    }

    @EventHandler
    public void damageExplosion (EntityDamageEvent e) {
        if (game.isStarted()) {
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                if (kitInfo.getPlayerKit(p) == Kits.TANK) {
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
                        e.setCancelled(true);
                    }
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
