package Kits.KitListeners.Kits.Defense;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import Util.Sounds;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

public class Neo implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void onProjectileHit (ProjectileLaunchEvent e) {
        if (game.isStarted()) {
            new BukkitRunnable() {
                int time = 20*20;
                @Override
                public void run() {
                    if (e.getEntity().isDead()) {
                        cancel();
                    }
                    if (e.getEntity().isOnGround()) {
                        cancel();
                    }
                    if (time <= 0) {
                        e.getEntity().remove();
                        cancel();
                    }
                    if (e.getEntity().getLocation().getWorld() != null)
                    for (Entity entity : e.getEntity().getLocation().getWorld().getNearbyEntities(e.getEntity().getLocation(), 2, 2, 2)) {
                        if (entity instanceof Player) {
                            Entity p = entity;
                            if(kitInfo.getPlayerKit(p)==Kits.NEO)

                            {
                                if (e.getEntity().getShooter() != p) {
                                    //Bukkit.broadcastMessage("neo hit by projectile with velocity" + e.getEntity().getVelocity());
                                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.LIME, 4);
                                    e.getEntity().getLocation().getWorld().spawnParticle(Particle.REDSTONE, e.getEntity().getLocation(), 1, dustOptions);
                                    e.getEntity().setVelocity(e.getEntity().getVelocity().multiply(-1));
                                    e.getEntity().setShooter((ProjectileSource) p);
                                    e.getEntity().setMetadata("deflected", new FixedMetadataValue(HardcoreGames.getInstance(), true));
                                    Sounds.neoDeflect(p.getLocation());
                                }
                            }
                        }
                    }
                    time--;

                }



            }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);

        }
    }


}
