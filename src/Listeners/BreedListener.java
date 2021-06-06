package Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

public class BreedListener implements Listener {
    @EventHandler
    public void onBreed (EntityBreedEvent e) {
        e.setCancelled(true);
    }
}
