package Kits.KitListeners.Kits.Vanity;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Jumper implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void teleport (PlayerTeleportEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.JUMPER) {
                if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
                    if (e.getTo() != null)
                    e.getPlayer().teleport(e.getTo());
                    e.setCancelled(true);
                }
            }
        }
    }
}
