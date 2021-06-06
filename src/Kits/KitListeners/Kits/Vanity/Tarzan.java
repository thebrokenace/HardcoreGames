package Kits.KitListeners.Kits.Vanity;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Tarzan implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.TARZAN) {
                if (e.getFrom().getBlock().getRelative(BlockFace.DOWN).getType().name().toLowerCase().contains("leaves")) {
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 1));
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20, 1));
                }
            }
        }
    }


    @EventHandler
    public void onFall(EntityDamageEvent e) {
        if (game.isStarted()) {
            if (e.getEntity() instanceof Player) {
                if (kitInfo.getPlayerKit((Player) e.getEntity()) == Kits.TARZAN) {
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                       // Bukkit.broadcastMessage("tarzan feel on leaf");
                        if (((Player) e.getEntity()).getLocation().getBlock().getRelative(BlockFace.DOWN).getType().name().toLowerCase().contains("leaves")) {
                            //Bukkit.broadcastMessage("leafed detected");

                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
