package Kits.KitListeners.Kits.Utility;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import Util.GamePhase;
import Util.Sounds;
import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Hacker implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    HashMap<UUID, Long> cooldownMap = new HashMap<UUID, Long>();
    public static List<Player> currentObfuscated = new ArrayList<>();
    @EventHandler
    public void onInteract (PlayerInteractEvent e) {
        if (game.getPhase() == GamePhase.GAMESTARTED) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.HACKER) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR) {
                    if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.GHAST_TEAR) {
                        List<Player> obfuscated = new ArrayList<>();
                        e.getPlayer().getInventory().getItemInMainHand().setAmount((e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1));
                        e.getPlayer().sendMessage(ChatColor.GREEN + "Obfuscation started!");
                        if (e.getPlayer().getLocation().getWorld() != null)
                            for (Entity entity : e.getPlayer().getLocation().getWorld().getNearbyEntities(e.getPlayer().getLocation(), 400, 400, 400)) {
                                if (entity instanceof Player) {

                                    Player obfuscate = (Player) entity;
                                    if (obfuscate != e.getPlayer()) {
                                        obfuscated.add(obfuscate);
                                        currentObfuscated.add(obfuscate);
                                        Sounds.obfuscation(obfuscate);
                                        obfuscate.sendMessage(ChatColor.RED + "Your compass has been obfuscated by an Hacker!");
                                    }
                                }
                            }
                        new BukkitRunnable() {
                            int time = 0;

                            @Override
                            public void run() {

                                for (Player p : obfuscated) {

                                    Location loc = p.getLocation();
                                    int radius = 2;
                                    double y = ((double) time) / 20;
                                    double x = radius * Math.cos(time);
                                    double z = radius * Math.sin(time);
                                    Location l = new Location(p.getWorld(), (float) (loc.getX() + x), (float) (loc.getY() + y), (float) (loc.getZ() + z));
                                    p.setCompassTarget(l);


                                }

                                time++;
                                if (time >= 200) {
                                    for (Player p : obfuscated) {
                                        p.sendMessage(ChatColor.GREEN + "Your compass is now working again!");
                                        currentObfuscated.remove(p);
                                    }
                                    e.getPlayer().sendMessage(ChatColor.RED + "Obfuscation wore off!");
                                    cancel();
                                }

                            }
                        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);


                    }

                    if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.BLUE_DYE && e.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null && ChatColor.stripColor(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Kit Disabler")) {
                        Player p = e.getPlayer();
                        if (cooldownMap.containsKey(p.getUniqueId())) {
                            if (System.currentTimeMillis() - cooldownMap.get(p.getUniqueId()) >= 120000) {
                                cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                                disableKits(e.getPlayer());
                            } else {
                                p.sendMessage(ChatColor.RED + "You must wait " + (120 - Math.round((System.currentTimeMillis() - ((cooldownMap.get(p.getUniqueId())))) / 1000f)) + " seconds to use this again!");
                            }
                        } else {
                            cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                            disableKits(e.getPlayer());
                        }


                    }


                }

            }
        }
    }
    @EventHandler
    public void invClick (InventoryClickEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit((Player) e.getWhoClicked()) == Kits.HACKER) {
                if (e.getCurrentItem() != null)
                    if (e.getCurrentItem().getType() == Material.BLUE_DYE && e.getCurrentItem().getItemMeta() != null && ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Kit Disabler")) {
                        e.setCancelled(true);
                    }
            }
        }
    }


    public void disableKits (Player p) {
        p.sendMessage(ChatColor.GREEN + "You have disabled all kit abilities of players around you!");
        Map<Player, Kits> kitList = new HashMap<>();
        for (Entity e : p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 100, 100, 100)) {
            if (e instanceof Player) {
                Player pl = (Player) e;
                if (p != pl) {
                    kitList.put(pl, kitInfo.getPlayerKit(pl));
                    Sounds.obfuscation(pl);

                    if (kitInfo.getPlayerKit(pl) == Kits.SCOUT || kitInfo.getPlayerKit(pl) == Kits.PHANTOM) {
                        pl.setAllowFlight(false);
                    }

                    if (kitInfo.getPlayerKit(pl) == Kits.IMPOSTER) {
                        if (DisguiseAPI.isDisguised(pl)) {
                            DisguiseAPI.undisguiseToAll(pl);
                            pl.sendMessage(ChatColor.RED + "Your disguise was removed!");
                            disguiseEffect(pl.getLocation());
                        }
                    }
                    kitInfo.setPlayerKit(pl, Kits.NONE);



                    pl.sendMessage(ChatColor.RED + "Your kit was disabled temporarily by an Hacker!");

                }

            }
        }
        new BukkitRunnable() {
            int time = 0;
            @Override
            public void run() {



                if (time > 15) {
                    p.sendMessage(ChatColor.RED + "Kit disabler wore off!");
                    for (Player player : kitList.keySet()) {
                        player.sendMessage(ChatColor.GREEN + "You regained your kit abilities!");

                        kitInfo.setPlayerKit(player, kitList.get(player));
                        if (kitInfo.getPlayerKit(player) == Kits.SCOUT || kitInfo.getPlayerKit(player) == Kits.PHANTOM) {
                            player.setAllowFlight(true);
                        }
                    }

                    cancel();
                }
                time++;
            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 20L);
    }

    public void disguiseEffect (Location loc) {
        loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 40);
        loc.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 40);
        loc.getWorld().playSound(loc, Sound.BLOCK_FURNACE_FIRE_CRACKLE, 10f, 1f);
    }
    public static List<Player> getCurrentObfuscated () {
        return currentObfuscated;
    }
}
