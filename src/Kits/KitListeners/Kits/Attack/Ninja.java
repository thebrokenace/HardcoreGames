package Kits.KitListeners.Kits.Attack;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import Util.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;
import java.util.UUID;

public class Ninja implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    HashMap<UUID, UUID> lastHit = new HashMap<>();
    HashMap<UUID, Long> timeLastHit = new HashMap<>();
    HashMap<UUID, Long> cooldownBeforeTeleport = new HashMap<>();
    @EventHandler
    public void shift (PlayerToggleSneakEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.NINJA) {
                if (e.isSneaking()) {

                    if (lastHit.containsKey(e.getPlayer().getUniqueId())) {
                        if (cooldownBeforeTeleport.containsKey(e.getPlayer().getUniqueId())) {
                            if ((System.currentTimeMillis() - cooldownBeforeTeleport.get(e.getPlayer().getUniqueId())) <= 8000) {
                                e.getPlayer().sendMessage(ChatColor.RED + "Please wait before trying again.");

                                return;
                            }
                            if (System.currentTimeMillis() - timeLastHit.get(e.getPlayer().getUniqueId()) <= 10000) {
                                Player pl = Bukkit.getPlayer(lastHit.get(e.getPlayer().getUniqueId()));
                                if (pl != null) {
                                    e.getPlayer().teleport(pl);
                                    Sounds.playNinjaWoosh(pl);
                                    pl.sendMessage(ChatColor.RED + "A Ninja has just teleported behind you!");
                                }
                                lastHit.remove(e.getPlayer().getUniqueId());
                                timeLastHit.remove(e.getPlayer().getUniqueId());
                                cooldownBeforeTeleport.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());

                            }

                        } else if (!cooldownBeforeTeleport.containsKey(e.getPlayer().getUniqueId())) {
                            if (System.currentTimeMillis() - timeLastHit.get(e.getPlayer().getUniqueId()) <= 10000) {
                                Player pl = Bukkit.getPlayer(lastHit.get(e.getPlayer().getUniqueId()));
                                if (pl != null) {
                                    e.getPlayer().teleport(pl);
                                    Sounds.playNinjaWoosh(pl);
                                    pl.sendMessage(ChatColor.RED + "A Ninja has just teleported behind you!");
                                }
                                lastHit.remove(e.getPlayer().getUniqueId());
                                timeLastHit.remove(e.getPlayer().getUniqueId());

                                cooldownBeforeTeleport.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());

                            }
                        } else {
                            e.getPlayer().sendMessage(ChatColor.RED + "Please wait before trying again.");
                        }
                    }

                }
            }
        }
    }

    @EventHandler
    public void onHit (EntityDamageByEntityEvent e) {
        if (!e.isCancelled()) {
            if (game.isStarted()) {
                if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
                    Player ninja = (Player) e.getDamager();
                    Player hit = (Player) e.getEntity();
                    if (kitInfo.getPlayerKit(ninja) == Kits.NINJA) {
                        lastHit.put(ninja.getUniqueId(), hit.getUniqueId());
                        timeLastHit.put(ninja.getUniqueId(), System.currentTimeMillis());
                    }
                }
            }
        }
    }
}
