package Kits.KitListeners.Kits.Attack;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import Util.Sounds;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Cannibal implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void onHit (EntityDamageByEntityEvent e) {
        if (!e.isCancelled()) {
            if (game.isStarted()) {
                if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
                    Player cannibal = (Player) e.getDamager();
                    Player hit = (Player) e.getEntity();
                    if (kitInfo.getPlayerKit(cannibal) == Kits.CANNIBAL) {
                        Sounds.cannibalPunch(cannibal.getLocation());
                        cannibal.setFoodLevel(cannibal.getFoodLevel()+2);
                        hit.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 200, 1));
                    }
                }
            }
        }
    }
}
