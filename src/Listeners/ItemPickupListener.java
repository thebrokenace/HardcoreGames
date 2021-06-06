package Listeners;

import Util.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class ItemPickupListener implements Listener {
    Game game = Game.getSharedGame();
    @EventHandler
    public void itemPickup (EntityPickupItemEvent e) {
        if (!game.isStarted()) {
            e.setCancelled(true);
        }
    }
}
