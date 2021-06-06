package Kits.KitListeners.Kits.Defense;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Anchor implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void onHit (EntityDamageByEntityEvent e) {
        if (game.isStarted()) {
            if (e.getEntity() instanceof Player) {
                Player anchor = (Player) e.getEntity();
                if (kitInfo.getPlayerKit(anchor) == Kits.ANCHOR) {
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            anchor.setVelocity(anchor.getVelocity().zero());
                            cancel();
                        }
                    }.runTaskTimer(HardcoreGames.getInstance(), 1L, 0L);
                }
            }
            if (e.getDamager() instanceof Player) {
                Player anchor = (Player) e.getDamager();
                Entity hit = e.getEntity();
                if (kitInfo.getPlayerKit(anchor) == Kits.ANCHOR) {
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            hit.setVelocity(hit.getVelocity().zero());
                            cancel();
                        }
                    }.runTaskTimer(HardcoreGames.getInstance(), 1L, 0L);
                }
            }
        }
    }
}
