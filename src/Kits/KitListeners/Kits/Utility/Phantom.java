package Kits.KitListeners.Kits.Utility;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import Util.GamePhase;
import Util.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class Phantom implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    HashMap<UUID, Long> cooldownMap = new HashMap<UUID, Long>();

    @EventHandler
    public void onRightClick (PlayerInteractEvent e) {
        if (game.getPhase() == GamePhase.GAMESTARTED) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.PHANTOM) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR) {
                    if (e.getHand() == EquipmentSlot.HAND && e.getItem() != null && e.getItem().getType() == Material.FEATHER && e.getItem().getItemMeta() != null && ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Phantom Flight")) {
                        Player p = e.getPlayer();
                        if (cooldownMap.containsKey(p.getUniqueId())) {
                            if (System.currentTimeMillis() - cooldownMap.get(p.getUniqueId()) >= 120000) {
                                cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                                flight(e.getPlayer(), e.getItem());


                            } else {
                                p.sendMessage(ChatColor.RED + "You must wait " + (120 - Math.round((System.currentTimeMillis() - ((cooldownMap.get(p.getUniqueId()))))/1000f)) + " seconds to use this again!");
                            }
                        } else {
                            cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                            flight(e.getPlayer(), e.getItem());
                        }

                    }
                }
            }
        }
    }
    @EventHandler
    public void onDrop (PlayerDropItemEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.PHANTOM) {
                if (e.getItemDrop().getItemStack().getType() == Material.FEATHER && e.getItemDrop().getItemStack().getItemMeta() != null && e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase("Phantom Flight")) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void invClick (InventoryClickEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit((Player) e.getWhoClicked()) == Kits.PHANTOM) {
                if (e.getCurrentItem() != null)
                if (e.getCurrentItem().getType() == Material.FEATHER && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Phantom Flight")) {
                    e.setCancelled(true);
                }
            }
        }
    }



    public void flight (Player p, ItemStack itemStack) {
        Sounds.phantomFly(p.getLocation());
        itemStack.addUnsafeEnchantment(Enchantment.SOUL_SPEED, 3);
        new BukkitRunnable() {
            int time = 0;
            @Override
            public void run() {
                p.setAllowFlight(true);
                p.setFlying(true);
                p.setFlySpeed(0.2f);
                if (time >= 3) {
                    p.setAllowFlight(false);
                    p.setFlying(false);
                    p.setFlySpeed(0f);

                    itemStack.removeEnchantment(Enchantment.SOUL_SPEED);
                    new BukkitRunnable() {
                        int seconds = 0;
                        @Override
                        public void run() {
                            seconds++;
                            p.setFallDistance(-10000);
                            if (seconds >= 100) {
                                p.setFallDistance(0);
                                cancel();
                            }
                        }
                    }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);

                    cancel();
                }
                time++;
            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 20L);

    }


}
