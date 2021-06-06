package Kits.KitListeners.Kits.Attack;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import Util.GamePhase;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Pyro implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void fireball (PlayerInteractEvent e) {
        if (game.getPhase() == GamePhase.GAMESTARTED) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.PYRO) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR) {
                    e.getPlayer().getInventory().getItemInMainHand().getType();
                    if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.FIRE_CHARGE) {
                        e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount()-1);
                        e.getPlayer().launchProjectile(Fireball.class);
                    }
                }
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    e.getPlayer().getInventory().getItemInMainHand().getType();
                    if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.FIRE_CHARGE) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }





}
