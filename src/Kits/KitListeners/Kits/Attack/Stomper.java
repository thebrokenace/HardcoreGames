package Kits.KitListeners.Kits.Attack;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import Util.GamePhase;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class Stomper implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();

    @EventHandler
    public void onFallDamage (EntityDamageEvent e) {
        if (game.isStarted()) {
            if (e.getEntity() instanceof Player) {
                Player stomper = (Player)e.getEntity();
                if (kitInfo.getPlayerKit(stomper) == Kits.STOMPER) {
                    if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                        if (game.getPhase() == GamePhase.GAMESTARTED) {
                            if (stomper.getLocation().getWorld() != null)
                                for (Entity entity : stomper.getLocation().getWorld().getNearbyEntities(stomper.getLocation(), 2.5, 2.5, 2.5)) {
                                    if (entity instanceof LivingEntity) {
                                        LivingEntity victim = (LivingEntity) entity;
                                        if (victim != e.getEntity()) {
                                            victim.damage(e.getDamage(), e.getEntity());
                                            if (victim.getLocation().getWorld() != null)
                                                victim.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, victim.getLocation(), 3);
                                            victim.getLocation().getWorld().playSound(victim.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
                                        }
                                    }
                                }
                        }
                        if (e.getDamage() > 4) {
                            e.setDamage(4);
                        }


                    }

                }

            }

        }

    }
}
