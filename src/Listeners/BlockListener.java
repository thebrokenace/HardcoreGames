package Listeners;

import Util.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {
    Game game = Game.getSharedGame();
    @EventHandler
    public void onPlace (BlockPlaceEvent e) {

        if (!game.isStarted()) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onBreak (BlockBreakEvent e) {
        if (!game.isStarted()) {
            e.setCancelled(true);
        }

    }


}
