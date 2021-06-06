package Kits.KitListeners.Kits.Utility;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Poseidon implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void onMove (PlayerMoveEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.POSEIDON) {
                if (e.getPlayer().getLocation().getBlock().getType() == Material.WATER) {
                    Player p = e.getPlayer();
                    //p.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 30, 1));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 30, 99));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 30, 0));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30, 0));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 30, 1));

                }
            }
        }
    }
}
