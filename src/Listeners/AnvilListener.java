package Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilListener implements Listener {
    @EventHandler
    public void anvilEvent(PrepareAnvilEvent e) {
        ItemStack item = e.getResult();
        if (item != null) {
            //if (item.getItemMeta()!=null)
            ItemMeta meta = item.getItemMeta();
            if (meta!=null) {
                meta.setDisplayName(null);
                item.setItemMeta(meta);
            }
        }

        e.setResult(item);
    }
}
