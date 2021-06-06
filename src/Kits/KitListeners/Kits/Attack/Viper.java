package Kits.KitListeners.Kits.Attack;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Viper implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void onHit (EntityDamageByEntityEvent e) {
        if (!e.isCancelled()) {
            if (game.isStarted()) {
                if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
                    LivingEntity ninja = (LivingEntity) e.getDamager();
                    LivingEntity hit = (LivingEntity) e.getEntity();
                    if (kitInfo.getPlayerKit(ninja) == Kits.VIPER) {
                        Random rand = new Random();
                        float float_random=rand.nextFloat();
                        if (float_random <= 0.25) {
                            hit.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10*20, 1));
                        }
                    }
                }
            }
        }
    }
}
