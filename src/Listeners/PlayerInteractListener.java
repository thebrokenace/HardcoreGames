package Listeners;

import Kits.KitListeners.Kits.Utility.Spy;
import Main.Config;
import Main.HardcoreGames;
import Messages.Messages;
import Util.Game;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityStatus;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Double.MAX_VALUE;

public class PlayerInteractListener implements Listener {
    Messages messages = new Messages();
    Game game = Game.getSharedGame();
    public static HashMap<Player, Entity> trackMap = new HashMap<>();
    Config config = new Config();
    @EventHandler
    public void onInteract (PlayerInteractEvent e) {
        //check for soup heal
        if (config.getSoupStats()) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (e.getItem() != null && e.getItem().getType() == Material.MUSHROOM_STEW) {
                    if (e.getPlayer().getHealth() >= 20 && e.getPlayer().getFoodLevel() >= 20) {
                        return;
                    }
                    CraftPlayer cPlayer = ((CraftPlayer) e.getPlayer());
                    PacketPlayOutEntityStatus eat = new PacketPlayOutEntityStatus(cPlayer.getHandle(), (byte) 9);
                    cPlayer.getHandle().playerConnection.sendPacket(eat);


                    if (e.getPlayer().getHealth() == 20) {
                        e.getPlayer().setFoodLevel(e.getPlayer().getFoodLevel() + 3);
                    } else {
                        if (e.getPlayer().getHealth() >= 14) {
                            e.getPlayer().setHealth(20);
                        } else {
                            e.getPlayer().setHealth(e.getPlayer().getHealth() + 6);
                        }

                    }

                    if (e.getHand() == EquipmentSlot.OFF_HAND) {
                        e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.BOWL));
                    }
                    if (e.getHand() == EquipmentSlot.HAND) {
                        e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.BOWL));
                    }
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EAT, 10f, 1f);
                }
            }
        }
    }

    @EventHandler
    public void onTrack (PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getItem() != null && e.getItem().getType() == Material.COMPASS) {
                ItemMeta meta = e.getItem().getItemMeta();
                if (meta != null) {
                    if (ChatColor.stripColor(meta.getDisplayName()).equalsIgnoreCase("Spy Compass")) {
                        return;
                    }
                }
                if (nearestPlayer(e.getPlayer()) != null) {
                    if (trackMap.getOrDefault(e.getPlayer(), null) != nearestPlayer(e.getPlayer())) {
                        Spy.trackMap.remove(e.getPlayer());
                        track(e.getPlayer(), nearestPlayer(e.getPlayer()));
                    }
                    //if (trackMap.getOrDefault(e.getPlayer(), null) != nearestPlayer(e.getPlayer())) {
                        e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messages.trackMessage(nearestPlayer(e.getPlayer()))));
                    //}
                } else {
                    e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messages.failedTrack()));

                }
            }

            }
    }

    //fix ASAP, makes new runnable on each right click
    public void track (Player tracker, Entity victim) {

            //Bukkit.broadcastMessage("started new runnable");
            trackMap.put(tracker, victim);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!trackMap.containsKey(tracker)) {
                        cancel();
                    }

                    if (trackMap.get(tracker) == victim) {
                        tracker.setCompassTarget(victim.getLocation());
                    } else {
                        //Bukkit.broadcastMessage("Cancelled " + tracker.getName() + " tracking " + victim.getName());
                        cancel();
                    }
                }
            }.runTaskTimer(HardcoreGames.getInstance(), 0L, 10L);

    }


    public Entity nearestPlayer (Entity p) {
        if (p.getLocation().getWorld() == null ) { return null; }
        List<Entity> candidates = new ArrayList<>();
        for (Entity e : p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 500, 500, 500)) {

            if (e instanceof Player) {
                if ((Player) e != p) {
                    if (!((Player) e).isOp()) {
                        if (((Player) e).getGameMode().equals(GameMode.SURVIVAL)) {
                            if (!e.hasMetadata("standnpc")) {

                                candidates.add((Player) e);
                            }
                        }
                    }
                }
            }
            if (e instanceof NPC) {
                if (!e.hasMetadata("standnpc")) {
                    candidates.add(e);
                }
            }
        }
        double tempDistance = MAX_VALUE;
        Entity closest = null;
        for (Entity near : candidates) {
                double distance = near.getLocation().distance(p.getLocation());
                if (distance < tempDistance) {
                    tempDistance = distance;
                    closest = near;

                }


        }
        return closest;

    }

    public static  void cancelTrack () {
        trackMap.clear();
    }


//    @EventHandler
//    public void onLeftClick (PlayerInteractEvent e) {
//
//        if (game.isStarted()) {
//            if (!game.getWorld().getWorldBorder().isInside(e.getPlayer().getLocation())) {
//                if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
//                    if (game.getWorld().getWorldBorder().getSize() > 300) {
//                        if (e.getClickedBlock() != null) {
//                            e.getClickedBlock().setType(Material.AIR);
//                        }
//                    }
//                }
//            }
//        }
//    }
}
