package Listeners;

import Util.Game;
import Util.GamePhase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;

public class MobListener implements Listener {
    Game game = Game.getSharedGame();
    @EventHandler
    public void explosionEvent(EntityExplodeEvent e) {
        if (game.getPhase() == GamePhase.PREGAME) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void mobTargetEvent(EntityTargetEvent e) {
        if (game.getPhase() == GamePhase.PREGAME) {
            e.setCancelled(true);
        }
    }
}
