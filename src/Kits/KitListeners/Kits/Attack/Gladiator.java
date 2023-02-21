package Kits.KitListeners.Kits.Attack;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import Util.GamePhase;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Gladiator implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    List<UUID> currentlyDueling = new ArrayList<>();
    Map<UUID, Long> cooldownMap = new HashMap<>();

    @EventHandler
    public void onRightClick (PlayerInteractEntityEvent e) {
        if (game.getPhase() == GamePhase.GAMESTARTED) {
            if (e.getRightClicked() instanceof Player) {
                if (!currentlyDueling.contains(e.getRightClicked().getUniqueId())) {
                    if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.GLADIATOR) {
                        Player p = e.getPlayer();
                        if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null && ChatColor.stripColor(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Gladiator Dueler")) {
                            if (cooldownMap.containsKey(p.getUniqueId())) {
                                if (System.currentTimeMillis() - cooldownMap.get(p.getUniqueId()) >= 90000) {
                                    cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                                    shadowGame(e.getPlayer(), (Player) e.getRightClicked());
//                            e.getClickedBlock().setType(Material.AIR);
//                            ItemStack button = new ItemStack(Material.STONE_BUTTON);
//                            ItemMeta meta = button.getItemMeta();
//                            if (meta != null)
//                            meta.setDisplayName("Gambler Button");
//                            button.setItemMeta(meta);
//                            if (e.getClickedBlock().getLocation().getWorld() != null)
//                             e.getClickedBlock().getLocation().getWorld().dropItemNaturally(e.getClickedBlock().getLocation(), button);
                                } else {
                                    p.sendMessage(ChatColor.RED + "You must wait " + (90 - Math.round((System.currentTimeMillis() - ((cooldownMap.get(p.getUniqueId())))) / 1000f)) + " seconds to use this again!");
                                }
                            } else {
                                cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                                shadowGame(e.getPlayer(), (Player) e.getRightClicked());
                            }

                        }


                    }
                }
            }
        }
    }

    public void shadowGame (Player challenger, Player victim) {
        Location oldChallengerLoc = challenger.getLocation().clone();
        Location oldVictimLoc = victim.getLocation().clone();
        challenger.sendMessage(ChatColor.GREEN + "You have challenged " + victim.getName() + " to the Shadow Game!");
        victim.sendMessage(ChatColor.RED + "You have been challenged by " + challenger.getName() + " to the Shadow Game!");
        Location arenaOrigin = challenger.getLocation().clone();
        arenaOrigin.setY(250);
        List<Block> arena = new ArrayList<>();
        for (Block b : cylinder(arenaOrigin, 30)) {
            arena.add(b);
            b.setType(Material.BEDROCK);
        }
        for (Block b : cylinder(arenaOrigin.clone().add(0,1,0), 30)) {
            arena.add(b);

            b.setType(Material.BEDROCK);
        }
        for (Block b : cylinder(arenaOrigin.clone().add(0,1,0), 29)) {
            arena.add(b);

            b.setType(Material.AIR);
        }
        for (Block b : cylinder(arenaOrigin.clone().add(0,2,0), 30)) {
            arena.add(b);

            b.setType(Material.BEDROCK);
        }
        for (Block b : cylinder(arenaOrigin.clone().add(0,2,0), 29)) {
            arena.add(b);

            b.setType(Material.AIR);
        }
        for (Block b : cylinder(arenaOrigin.clone().add(0,3,0), 30)) {
            arena.add(b);

            b.setType(Material.BEDROCK);
        }
        for (Block b : cylinder(arenaOrigin.clone().add(0,3,0), 29)) {
            arena.add(b);

            b.setType(Material.AIR);
        }
        for (Block b : cylinder(arenaOrigin.clone().add(0,4,0), 30)) {
            arena.add(b);

            b.setType(Material.BEDROCK);
        }
        for (Block b : cylinder(arenaOrigin.clone().add(0,4,0), 29)) {
            arena.add(b);

            b.setType(Material.AIR);
        }
        for (Block b : cylinder(arenaOrigin.clone().add(0,5,0), 30)) {
            arena.add(b);

            b.setType(Material.BEDROCK);
        }

        Location challengerStartingPoint = arenaOrigin.clone().add(0, 1, 15);
        Location victimStartingPoint = arenaOrigin.clone().subtract(0, 0, 15);
        victimStartingPoint.add(0, 1, 0);


        challenger.teleport(challengerStartingPoint);
        victim.teleport(victimStartingPoint);
        currentlyDueling.add(victim.getUniqueId());
        currentlyDueling.add(challenger.getUniqueId());

        challenger.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 700, 10));
        victim.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 700, 10));

        new BukkitRunnable() {
            int time = 35;

            @Override
            public void run() {
                if (time >= 30) {
                    challenger.sendTitle(String.valueOf(time - 30), "", 10, 10, 10);
                    victim.sendTitle(String.valueOf(time - 30), "", 10, 10, 10);
                    challenger.playSound(challenger.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10f, 1);
                    victim.playSound(victim.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10f, 1);


                    challenger.teleport(lookAt(challengerStartingPoint, victimStartingPoint));
                    victim.teleport(lookAt(victimStartingPoint, challengerStartingPoint));

                    if (time == 30) {
                        challenger.sendMessage(ChatColor.RED + "The Shadow Game has begun!!");
                        victim.sendMessage(ChatColor.RED + "The Shadow Game has begun!!");
                    }
                }

                if (time < 30) {
                    if (challenger.isDead() || !challenger.isOnline()) {
                        //victim won
                        victim.sendMessage(ChatColor.GREEN + "Congratulations! You won the Shadow Game!");
                        victim.playSound(victim.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10f, 1);
                        victim.teleport(oldVictimLoc);
                        currentlyDueling.remove(victim.getUniqueId());
                        currentlyDueling.remove(challenger.getUniqueId());
                        for (Block b : arena) {
                            b.setType(Material.AIR);
                        }
                        cancel();
                    }
                    if (victim.isDead() || !victim.isOnline()) {
                        //challenger won
                        challenger.sendMessage(ChatColor.GREEN + "Congratulations! You won the Shadow Game!");
                        challenger.playSound(challenger.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10f, 1);
                        challenger.teleport(oldChallengerLoc);
                        currentlyDueling.remove(victim.getUniqueId());
                        currentlyDueling.remove(challenger.getUniqueId());
                        for (Block b : arena) {
                            b.setType(Material.AIR);
                        }
                        cancel();
                    }

                }

                if (time <= 0) {
                    challenger.teleport(oldChallengerLoc);
                    victim.teleport(oldVictimLoc);
                    challenger.sendMessage(ChatColor.RED + "Time ran out!");
                    victim.sendMessage(ChatColor.RED + "Time ran out!");
                    currentlyDueling.remove(victim.getUniqueId());
                    currentlyDueling.remove(challenger.getUniqueId());
                    for (Block b : arena) {
                        b.setType(Material.AIR);
                    }
                    cancel();

                }

                time--;
            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 20L);
    }
    public Location lookAt(Location loc, Location lookat) {
        //Clone the loc to prevent applied changes to the input loc
        loc = loc.clone();

        // Values of change in distance (make it relative)
        double dx = lookat.getX() - loc.getX();
        double dy = lookat.getY() - loc.getY();
        double dz = lookat.getZ() - loc.getZ();

        // Set yaw
        if (dx != 0) {
            // Set yaw start value based on dx
            if (dx < 0) {
                loc.setYaw((float) (1.5 * Math.PI));
            } else {
                loc.setYaw((float) (0.5 * Math.PI));
            }
            loc.setYaw((float) loc.getYaw() - (float) Math.atan(dz / dx));
        } else if (dz < 0) {
            loc.setYaw((float) Math.PI);
        }

        // Get the distance from dx/dz
        double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

        // Set pitch
        loc.setPitch((float) -Math.atan(dy / dxz));

        // Set values, convert to degrees (invert the yaw since Bukkit uses a different yaw dimension format)
        loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
        loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);

        return loc;
    }
    public List<Block> cylinder(Location loc, int r) {
        List<Block> blocks = new ArrayList<>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        World w = loc.getWorld();
        int rSquared = r * r;
        for (int x = cx - r; x <= cx +r; x++) {
            for (int z = cz - r; z <= cz +r; z++) {
                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
                    blocks.add(w.getBlockAt(x, cy, z));
                }
            }
        }
        return blocks;
    }


}
