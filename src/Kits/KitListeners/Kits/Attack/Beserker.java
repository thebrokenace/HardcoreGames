package Kits.KitListeners.Kits.Attack;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import Util.Sounds;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Beserker implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void onKill (PlayerDeathEvent e) {
        if (game.isStarted()) {
            if (e.getEntity().getKiller() != null) {
                Player killer = e.getEntity().getKiller();
                if (kitInfo.getPlayerKit(killer) == Kits.BESERKER) {
                    //speed 2 strength 1 on kill
                    killer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, 2));
                    killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, 2));
                    Sounds.beserkerKill(killer);
                }
            }
        }

        if (game.isStarted()) {
            if (e.getEntity().getKiller() != null) {
                if (e.getEntity().hasMetadata("NPC")) {
                    Player killer = e.getEntity();
                    if (kitInfo.getPlayerKit(killer) == Kits.BESERKER) {
                        //speed 2 strength 1 on kill
                        killer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 2));
                        killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));
                        Sounds.beserkerKill(killer);
                    }
                }
            }
        }

    }
}
