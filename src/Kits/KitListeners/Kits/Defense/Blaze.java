package Kits.KitListeners.Kits.Defense;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import Util.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Blaze implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    Map<UUID, Long> cooldownMap = new HashMap<>();
    Map<UUID, Boolean> isBlazing = new HashMap<>();

    @EventHandler
    public void onRightClick (PlayerInteractEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.BLAZE) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR) {
                    if (e.getHand() == EquipmentSlot.HAND && e.getItem() != null && e.getItem().getType() == Material.BLAZE_POWDER && e.getItem().getItemMeta() != null && ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Blaze Run")) {
                        Player p = e.getPlayer();
                        if (cooldownMap.containsKey(p.getUniqueId())) {
                            if (System.currentTimeMillis() - cooldownMap.get(p.getUniqueId()) >= 60000) {
                                cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                                blazeRun(p);
                                p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() -1);


                            } else {
                                p.sendMessage(ChatColor.RED + "You must wait " + (60 - Math.round((System.currentTimeMillis() - ((cooldownMap.get(p.getUniqueId()))))/1000f)) + " seconds to use this again!");
                            }
                        } else {
                            cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                            blazeRun(p);
                            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() -1);

                        }

                    }
                }
            }
        }
    }

    public void blazeRun (Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 160, 2));
        Sounds.blazeStart(p.getLocation());
        new BukkitRunnable() {
            int time = 0;
            @Override
            public void run() {
                p.setFireTicks(0);
                isBlazing.put(p.getUniqueId(), true);
                if (p.getLocation().getBlock().getType() == Material.AIR) {
                    p.getLocation().getBlock().setType(Material.FIRE);
                }
                if (time > 160) {
                    p.setFireTicks(0);
                    isBlazing.remove(p.getUniqueId(), true);
                    cancel();
                }
                time++;


            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);
    }

    @EventHandler
    public void blazeDamage (EntityDamageEvent e) {
        if (game.isStarted()) {
            if (e.getEntity() instanceof Player) {
                Player blaze = (Player) e.getEntity();
                if (kitInfo.getPlayerKit(blaze) == Kits.BLAZE) {
                    if (isBlazing.getOrDefault(blaze.getUniqueId(),false)) {
                        if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE)) {
                            e.setCancelled(true);
                        }
                        if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
