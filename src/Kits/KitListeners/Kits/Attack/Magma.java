package Kits.KitListeners.Kits.Attack;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

public class Magma implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void onHit (EntityDamageByEntityEvent e) {
        if (!e.isCancelled()) {
            if (game.isStarted()) {
                if (e.getEntity() instanceof Player) {
                    Player ninja = (Player) e.getDamager();
                    Entity hit = e.getEntity();
                    if (kitInfo.getPlayerKit(ninja) == Kits.MAGMA) {
                        Random rand = new Random();
                        float float_random=rand.nextFloat();
                        if (float_random <= 0.25) {
                            hit.setFireTicks(300);
                        }
                    }
                }
            }
        }
    }
}
