package Kits.KitListeners.Kits.Utility;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import Util.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class Checkpoint implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    Map<UUID, Long> cooldownMap = new HashMap<>();
    Map<Block, UUID> placedPointLocation = new HashMap<>();
    List<Block> placedCheckPoint = new ArrayList<>();

    Map<UUID, Long> damageCooldown = new HashMap<>();

    @EventHandler
    public void onRightClick (PlayerInteractEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.CHECKPOINT) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR) {
                    if (e.getHand() == EquipmentSlot.HAND && e.getItem() != null && e.getItem().getType() == Material.STONE_BUTTON && e.getItem().getItemMeta() != null && ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Checkpoint Button")) {
                        Player p = e.getPlayer();

                        if (placedPointLocation.containsValue(e.getPlayer().getUniqueId())) {
                            if (!damageCooldown.containsKey(e.getPlayer().getUniqueId())) {
                                if (cooldownMap.containsKey(p.getUniqueId())) {

                                    if (System.currentTimeMillis() - cooldownMap.get(p.getUniqueId()) >= 60000) {
                                        cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                                        Sounds.checkPointTeleport(e.getPlayer().getLocation());
                                        e.getPlayer().teleport(getKey(placedPointLocation, e.getPlayer().getUniqueId()).getLocation());
                                        e.getPlayer().getLocation().getWorld().playEffect(e.getPlayer().getLocation(), Effect.ENDER_SIGNAL, 5);
                                        e.getPlayer().getLocation().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10f, 1f);


                                    } else {
                                        p.sendMessage(ChatColor.RED + "You must wait " + (60 - Math.round((System.currentTimeMillis() - ((cooldownMap.get(p.getUniqueId())))) / 1000f)) + " seconds to use this again!");
                                    }
                                } else {
                                    cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                                    Sounds.checkPointTeleport(e.getPlayer().getLocation());
                                    e.getPlayer().teleport(getKey(placedPointLocation, e.getPlayer().getUniqueId()).getLocation());
                                    e.getPlayer().getLocation().getWorld().playEffect(e.getPlayer().getLocation(), Effect.ENDER_SIGNAL, 5);
                                    e.getPlayer().getLocation().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10f, 1f);

                                }
                        } else {
                                if ((System.currentTimeMillis() - damageCooldown.get(e.getPlayer().getUniqueId())) > 10000) {
                                    if (cooldownMap.containsKey(p.getUniqueId())) {

                                        if (System.currentTimeMillis() - cooldownMap.get(p.getUniqueId()) >= 60000) {
                                            cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                                            Sounds.checkPointTeleport(e.getPlayer().getLocation());
                                            e.getPlayer().teleport(getKey(placedPointLocation, e.getPlayer().getUniqueId()).getLocation());
                                            e.getPlayer().getLocation().getWorld().playEffect(e.getPlayer().getLocation(), Effect.ENDER_SIGNAL, 5);
                                            e.getPlayer().getLocation().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10f, 1f);
                                            e.getPlayer().sendMessage(ChatColor.GREEN + "You were teleported to your Checkpoint!");


                                        } else {
                                            p.sendMessage(ChatColor.RED + "You must wait " + (60 - Math.round((System.currentTimeMillis() - ((cooldownMap.get(p.getUniqueId())))) / 1000f)) + " seconds to use this again!");
                                        }
                                    } else {
                                        cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                                        Sounds.checkPointTeleport(e.getPlayer().getLocation());
                                        e.getPlayer().teleport(getKey(placedPointLocation, e.getPlayer().getUniqueId()).getLocation());
                                        e.getPlayer().getLocation().getWorld().playEffect(e.getPlayer().getLocation(), Effect.ENDER_SIGNAL, 5);
                                        e.getPlayer().getLocation().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10f, 1f);
                                        e.getPlayer().sendMessage(ChatColor.GREEN + "You were teleported to your Checkpoint!");
                                    }
                                } else {
                                    e.getPlayer().sendMessage(ChatColor.RED +"You were damaged too recently, please wait before trying again!");
                                }
                            }
                    } else {
                            e.getPlayer().sendMessage(ChatColor.RED + "You must first place your Checkpoint before doing this!");
                        }

                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlace (BlockPlaceEvent e) {
        if (game.isStarted()) {

            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.CHECKPOINT) {
                if (e.getItemInHand().getItemMeta() != null && ChatColor.stripColor(e.getItemInHand().getItemMeta().getDisplayName()).equals("Checkpoint")) {
                    e.getBlock().setMetadata("checkpoint", new FixedMetadataValue(HardcoreGames.getInstance(), true));

                    Sounds.checkPointPlace(e.getPlayer());
                    placedCheckPoint.add(e.getBlock());
                    placedPointLocation.put(e.getBlock(), e.getPlayer().getUniqueId());

                    e.getPlayer().sendMessage(ChatColor.GREEN + "Placed Checkpoint!");

                }
                if (e.getItemInHand().getItemMeta() != null && ChatColor.stripColor(e.getItemInHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Checkpoint Button")) {
                    e.setCancelled(true);
                }
            } else {
                if (e.getItemInHand().getItemMeta() != null && ChatColor.stripColor(e.getItemInHand().getItemMeta().getDisplayName()).equals("Checkpoint")) {
                    e.setCancelled(true);
                }
                }
        }
    }

    public Block getKey(Map<Block, UUID> map, UUID value) {
        for (Map.Entry<Block, UUID> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @EventHandler
    public void onBreak (BlockBreakEvent e) {
        if (game.isStarted()) {
            if (placedCheckPoint.contains(e.getBlock()) && e.getBlock().hasMetadata("checkpoint")) {
                ItemStack stack = new ItemStack(Material.NETHER_BRICK_FENCE);
                ItemMeta meta = stack.getItemMeta();
                if (meta != null)
                meta.setDisplayName("Checkpoint");
                stack.setItemMeta(meta);
                placedCheckPoint.remove(e.getBlock());
                placedPointLocation.remove(e.getBlock());




                if (e.getBlock().getLocation().getWorld() != null)
                e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), stack);
                e.getBlock().setType(Material.AIR);
                e.getPlayer().sendMessage(ChatColor.RED + "Broke checkpoint");

                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage (EntityDamageEvent e) {
        if (game.isStarted()) {
            if (e.getEntity() instanceof Player) {
                Player check = (Player) e.getEntity();
                if (kitInfo.getPlayerKit(check) == Kits.CHECKPOINT) {
                    damageCooldown.put(check.getUniqueId(), System.currentTimeMillis());
                }
            }
        }
    }

}
