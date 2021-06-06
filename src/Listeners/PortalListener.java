package Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.world.PortalCreateEvent;

public class PortalListener implements Listener {
    @EventHandler
    public void onPortal (PlayerPortalEvent e ) {
        e.setCancelled(true);
    }
    @EventHandler
    public void portal (PortalCreateEvent e) {
        e.setCancelled(true);
    }
}
