package Kits.KitListeners.Kits.Vanity;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Peanut implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void move (PlayerMoveEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.PEANUT) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getGameMode() != GameMode.SPECTATOR) {
                        if (p != e.getPlayer()) {
                            if (!entityBehindPlayer(e.getPlayer(), p)) {
                                if (p.getLocation().distance(e.getPlayer().getLocation()) <= 25) {

                                    e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
                                    e.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 8));
                                    return;
                                }
                            }
                        }
                    }
                }
                e.getPlayer().removePotionEffect(PotionEffectType.SLOW);
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30, 4));
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 30, 0));
            }
        }

    }

    private static boolean entityBehindPlayer(Entity entity, Entity player) {
        Double yaw = 2*Math.PI-Math.PI*player.getLocation().getYaw()/180;
        Vector v = entity.getLocation().toVector().subtract(player.getLocation().toVector());
        Vector r = new Vector(Math.sin(yaw),0, Math.cos(yaw));
        float theta = r.angle(v);
        if (Math.PI/2<theta && theta<3*Math.PI/2) {
            return true;
        }
        return false;
    }
}
