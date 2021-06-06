package Kits.KitListeners.Kits.Utility;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import Util.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class Switcher implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void projectileThrow (ProjectileLaunchEvent e) {
        if (game.isStarted()) {
            if (e.getEntity().getShooter() instanceof Player) {
                Player p = (Player) e.getEntity().getShooter();
                if (e.getEntity() instanceof Snowball) {
                    if (kitInfo.getPlayerKit(p) == Kits.SWITCHER) {
                        if (p.getInventory().getItemInMainHand().getType() == Material.SNOWBALL) {
                            if (p.getInventory().getItemInMainHand().getItemMeta() != null && ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Switcher Snowball")) {
                                e.getEntity().setMetadata("switcherball", new FixedMetadataValue(HardcoreGames.getInstance(), true));
                            }
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void projectileHit (ProjectileHitEvent e) {
        if (game.isStarted()) {
            if (e.getEntity().getShooter() instanceof Player) {
                if (e.getHitEntity() instanceof Player) {
                    Entity p = (Entity) e.getEntity().getShooter();
                    if (e.getEntity() instanceof Snowball) {
                        if (kitInfo.getPlayerKit(p) == Kits.SWITCHER) {
                            if (e.getEntity().hasMetadata("switcherball")) {
                                Location temp = e.getHitEntity().getLocation();
                                e.getHitEntity().teleport(p.getLocation());
                                p.teleport(temp);
                                Sounds.switcherSwitch((Player)p);
                                Sounds.switcherSwitch((Player)e.getHitEntity());

                            }

                        }
                    }
                }
            }
        }
    }
}
