package Kits.KitListeners.Kits.Utility;

import Kits.KitListeners.KitUtils.Renderer;
import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

public class Radar implements Listener {
    static Game game = Game.getSharedGame();
    static KitInfo kitInfo = KitInfo.getSharedKitInfo();
    public static void startRadar () {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (kitInfo.getPlayerKit(p) == Kits.RADAR) {

                    }
                }
            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 20L);

    }
    @EventHandler
    public void onMapInitialize(MapInitializeEvent e) {
        if (game.isStarted()) {
            MapView mapView = e.getMap();
            mapView.setScale(MapView.Scale.CLOSEST);
            mapView.getRenderers().clear();
            mapView.addRenderer(new Renderer());
        }
    }
}
