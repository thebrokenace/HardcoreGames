package Kits.KitListeners.Kits.Attack;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import Util.GamePhase;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Thor implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    Map<UUID, Long> cooldownMap = new HashMap<>();

    @EventHandler
    public void onRightClick (PlayerInteractEvent e) {
        if (game.getPhase() == GamePhase.GAMESTARTED) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.THOR) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (e.getHand() == EquipmentSlot.HAND && e.getItem() != null && e.getItem().getType() == Material.WOODEN_AXE && e.getItem().getItemMeta() != null && ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Thor's Hammer")) {
                        Player p = e.getPlayer();
                        if (cooldownMap.containsKey(p.getUniqueId())) {
                            if (System.currentTimeMillis() - cooldownMap.get(p.getUniqueId()) >= 5000) {
                                cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                                if (e.getClickedBlock() != null)
                                    e.getPlayer().getWorld().spigot().strikeLightning(e.getClickedBlock().getLocation(), true);

                                for (Entity ent : e.getPlayer().getLocation().getWorld().getNearbyEntities(e.getClickedBlock().getLocation(), 20,20,20)) {
                                    if (ent instanceof Player) {
                                        ((Player) ent).playSound(ent.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 10, 10f);
                                    }
                                }


                            } else {
                                p.sendMessage(ChatColor.RED + "You must wait " + (5 - Math.round((System.currentTimeMillis() - ((cooldownMap.get(p.getUniqueId()))))/1000f)) + " seconds to use this again!");
                            }
                        } else {
                            cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                            if (e.getClickedBlock() != null)
                               e.getPlayer().getWorld().spigot().strikeLightning(e.getClickedBlock().getLocation(), true);
                            for (Entity ent : e.getPlayer().getLocation().getWorld().getNearbyEntities(e.getClickedBlock().getLocation(), 20,20,20)) {
                                if (ent instanceof Player) {
                                    ((Player) ent).playSound(ent.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 10, 10f);
                                }
                            }
                        }

                    }
                }
            }
        }
    }
    @EventHandler
    public void lightning (EntityDamageByEntityEvent e) {
        if (game.isStarted()) {
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                if (kitInfo.getPlayerKit(p) == Kits.THOR) {
                    if (e.getDamager() instanceof LightningStrike) {
                        p.setFireTicks(0);
                        e.setDamage(0.3*e.getDamage());
                    }
                }
            }
        }
    }
}
