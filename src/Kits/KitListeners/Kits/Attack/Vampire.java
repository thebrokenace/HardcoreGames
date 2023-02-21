package Kits.KitListeners.Kits.Attack;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import Util.Sounds;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Vampire implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();

    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        if (game.isStarted()) {
            if (e.getEntity().getKiller() != null) {
                Player killer = e.getEntity().getKiller();
                if (kitInfo.getPlayerKit(killer) == Kits.VAMPIRE) {
                    //heal 5 on kill
//                    if (killer.getHealth() <= 10) {
//                        killer.setHealth(killer.getHealth() + 10);
//                    } else {
                        killer.setHealth(20);
                        Sounds.vampireHeal(killer);
                    //}
                }
            }
        }

    }
}
