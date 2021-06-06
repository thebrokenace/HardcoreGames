package Kits.KitListeners.Kits.Attack;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Robots.Robot;
import Util.Game;
import Util.Sounds;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Werewolf implements Listener {
    static Game game = Game.getSharedGame();
    static KitInfo kitInfo = KitInfo.getSharedKitInfo();
    public static void giveWolfPowers () {
        if (game.isStarted()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (kitInfo.getPlayerKit(p) == Kits.WEREWOLF) {
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            if (p.getLocation().getWorld() != null) {
                                World world = p.getWorld();
                                if (day(world)) {
                                    return;
                                } else {
                                   for (Entity e : p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 50, 50 ,50)) {
                                        if (e instanceof Player) {
                                            if (!(((Player) e) == p)) {
                                                ((Player) e).sendMessage(ChatColor.GRAY + "You shiver as you hear the unmistakable howl of a Werewolf...");
                                            }
                                            Sounds.wolfNight((Player)e);

                                        }
                                    }

                                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 1));
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 0));
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 10));
                                }
                            }

                        }
                    }.runTaskTimer(HardcoreGames.getInstance(), 0L, 600L);
                }
            }


            for (NPC ent : Robot.getSharedRobots().activeRobots()) {
                LivingEntity p = (LivingEntity) ent.getEntity();
                if (kitInfo.getPlayerKit(p) == Kits.WEREWOLF) {
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            if (p.getLocation().getWorld() != null) {
                                World world = p.getWorld();
                                if (day(world)) {
                                    return;
                                } else {
                                    for (Entity e : p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 50, 50 ,50)) {
                                        if (e instanceof Player) {
                                            if (!(((Player) e) == p)) {
                                                ((Player) e).sendMessage(ChatColor.GRAY + "You shiver as you hear the unmistakable howl of a Werewolf...");
                                            }
                                            Sounds.wolfNight((Player) e);

                                        }
                                    }
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 1));
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 0));
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 4));
                                }
                            }

                        }
                    }.runTaskTimer(HardcoreGames.getInstance(), 0L, 600L);
                }
            }
        }
    }
    public static boolean day(World world) {
        long time = world.getTime();

        return time < 12300 || time > 23850;
    }
}
