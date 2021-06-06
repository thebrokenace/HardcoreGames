package Kits.KitListeners.Kits.Vanity;

import Kits.KitListeners.Kits.Defense.Chameleon;
import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import Util.GamePhase;
import Util.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Copycat implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void onKill (PlayerDeathEvent e) {
        if (game.isStarted() || game.getPhase() == GamePhase.WINNERDECIDED) {
            if (e.getEntity().getKiller() != null) {
                Player killer = e.getEntity().getKiller();
                if (kitInfo.copycats.containsKey(killer)) {
                    //take kit
                    kitInfo.setPlayerKit(killer, kitInfo.getPlayerKit(e.getEntity()));
                    //Bukkit.broadcastMessage(kitInfo.getPlayerKit(killer) + "killer kit");
                    //Bukkit.broadcastMessage(ChatColor.RED + kitInfo.getKitNameFormatted(kitInfo.getPlayerKit(killer), killer));
                    killer.sendMessage(ChatColor.AQUA + "You are now a " + kitInfo.getKitNameFormatted(kitInfo.getPlayerKit(e.getEntity()), killer) +"!");
                    if (kitInfo.getPlayerKit(killer) == Kits.CHAMELEON) {
                        Chameleon.applyNoNametag(killer);
                    } else {
                        Chameleon.removeNoNametagIfExists(killer);
                    }
                    if (kitInfo.getPlayerKit(killer) == Kits.SPY) {
                        Chameleon.addChameleonCounter(killer);
                    } else {
                        Chameleon.removeChameleonCounter(killer);
                    }
                    if (kitInfo.getPlayerKit(killer) == Kits.SCOUT) {
                        killer.setAllowFlight(true);
                    }

                    Sounds.copycatKitStole(killer.getLocation());

                }
            }
        }

    }
}
