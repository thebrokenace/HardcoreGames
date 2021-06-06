package Kits.KitListeners.Kits.Vanity;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Blink implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    Map<UUID, Long> cooldownMap = new HashMap<>();

    @EventHandler
    public void onRightClick (PlayerInteractEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.BLINK) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR) {
                    if (e.getHand() == EquipmentSlot.HAND && e.getItem() != null && e.getItem().getType() == Material.REDSTONE_TORCH && e.getItem().getItemMeta() != null && ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Blink Torch")) {
                        Player p = e.getPlayer();
                        if (cooldownMap.containsKey(p.getUniqueId())) {
                            if (System.currentTimeMillis() - cooldownMap.get(p.getUniqueId()) >= 45000) {
                                if (teleport(p)) {
                                    cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());

                                    e.getItem().setAmount(e.getItem().getAmount() - 1);
                                }

                            } else {
                                p.sendMessage(ChatColor.RED + "You must wait " + (45 - Math.round((System.currentTimeMillis() - ((cooldownMap.get(p.getUniqueId()))))/1000f)) + " seconds to use this again!");
                            }
                        } else {
                            if (teleport(p)) {
                                cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                                e.getItem().setAmount(e.getItem().getAmount() - 1);
                            }
                        }

                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlace (BlockPlaceEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.BLINK) {
                if(e.getItemInHand().getType() == Material.REDSTONE_TORCH && e.getItemInHand().getItemMeta() != null && ChatColor.stripColor(e.getItemInHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Blink Torch")) {
                    e.setCancelled(true);
                }
            }
        }
    }
    public void drawLine(Location point1, Location point2, double space, Color color) {
        new BukkitRunnable() {
            int time = 0;
            @Override
            public void run() {
                World world = point1.getWorld();
                Validate.isTrue(point2.getWorld().

                        equals(world), "Lines cannot be in different worlds!");
                double distance = point1.distance(point2);
                org.bukkit.util.Vector p1 = point1.toVector();
                org.bukkit.util.Vector p2 = point2.toVector();
                Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
                double length = 0;
                for(;length<distance; p1.add(vector))

                {
                    Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1);

                    world.spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ(), 1, dustOptions);
                    length += space;
                }

                time++;
                if (time > 20*5) {
                    cancel();
                }
            }



        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);
    }
    public boolean teleport (Player p) {
        if (p.getLocation().getWorld() != null) {
            Location block = p.getTargetBlock(null, 100).getLocation();


            drawLine(p.getLocation(), block.clone().add(0,1,0), 0.5, Color.LIME);
            p.teleport(block.add(0,1,0));
            p.getLocation().getWorld().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 1);
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10f , 1);
            p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1200, 1));
            new BukkitRunnable() {
                int time = 0;
                @Override
                public void run() {
                    p.setFallDistance(-10000);
                    time++;
                    if (time > 20*10) {
                        cancel();
                    }
                }
            }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);
            return true;
        }
        return false;

    }

}
