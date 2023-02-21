package Listeners;

import Util.Game;
import Util.GamePhase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemPickupListener implements Listener {
    Game game = Game.getSharedGame();
    @EventHandler
    public void itemPickup (EntityPickupItemEvent e) {
        if (!game.isStarted()) {
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void itemDrop (PlayerDropItemEvent e) {
        if (game.getPhase() == GamePhase.WINNERDECIDED || game.getPhase() == GamePhase.PREGAME) {
            e.setCancelled(true);
        }
    }

}
