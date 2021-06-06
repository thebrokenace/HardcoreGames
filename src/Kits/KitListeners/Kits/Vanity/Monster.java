package Kits.KitListeners.Kits.Vanity;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;

public class Monster implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void onTarget (EntityTargetEvent e) {
        if (game.isStarted()) {
            if (e.getTarget() instanceof Player) {
                Entity p = e.getTarget();
                if (kitInfo.getPlayerKit(p) == Kits.MONSTER) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDamage (EntityDamageByEntityEvent e) {
        if (game.isStarted()) {
            if (e.getDamager() instanceof Player) {
                if (e.getEntity() instanceof LivingEntity) {
                    Entity monster = e.getDamager();
                    if (kitInfo.getPlayerKit(monster) == Kits.MONSTER) {
                        if (monster.getLocation().getWorld() != null)
                        for (Entity ent : monster.getLocation().getWorld().getNearbyEntities(monster.getLocation(), 5, 5 ,5)) {
                            if (ent instanceof org.bukkit.entity.Monster) {
                                ((org.bukkit.entity.Monster) ent).setTarget((LivingEntity) e.getEntity());
                            }
                        }
                    }
                }
            }
        }
    }
}
