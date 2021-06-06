package Kits.KitListeners.Kits.Defense;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import Util.GamePhase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Madman implements Listener {
    static Game game = Game.getSharedGame();
    static KitInfo kitInfo = KitInfo.getSharedKitInfo();
    static Map<UUID, Long> timeinMadmanPrescence = new HashMap<>();
    static List<UUID> inMadManRange = new ArrayList<>();

        public static void madman () {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player pl : Bukkit.getOnlinePlayers()) {
                        if (game.getPhase() == GamePhase.GAMESTARTED) {
                            if (kitInfo.getPlayerKit(pl) == Kits.MADMAN) {
                                //10 sec in presence = confusion 1 weakness 1 20 sec = conf 3 weak 2 slowness 1
                                Player madman = pl;
                                if (pl.getLocation().getWorld() != null)
                                    for (Entity entity : pl.getLocation().getWorld().getNearbyEntities(pl.getLocation(), 20, 20, 20)) {
                                        if (entity instanceof Player) {
                                            Player p = (Player) entity;

                                            if (p != madman) {
                                                if (!p.isOp()) {
                                                    if (p.getGameMode().equals(GameMode.SURVIVAL)) {


                                                        if (kitInfo.getPlayerKit(p) != Kits.MADMAN) {
                                                            if (!timeinMadmanPrescence.containsKey(p.getUniqueId())) {
                                                                Random rand = new Random();
                                                                int ran = rand.nextInt(2);
                                                                if (ran == 0) {
                                                                    p.sendMessage(ChatColor.GRAY + "An overbearing sense of insanity overcomes you...\nYou can feel the aura of a Madman...");
                                                                } else {
                                                                    p.sendMessage(ChatColor.GRAY + "You hear maniacal laughs and your senses start to fade...\nYou can feel the aura of a Madman...");

                                                                }
                                                            }
                                                            timeinMadmanPrescence.put(p.getUniqueId(), timeinMadmanPrescence.getOrDefault(p.getUniqueId(), 0L) + 1);

                                                            long time = timeinMadmanPrescence.getOrDefault(p.getUniqueId(), 0L);
                                                            if ((time >= 5) && (time <= 10)) {
                                                                p.getActivePotionEffects().removeIf(pot -> pot.getType().equals(PotionEffectType.CONFUSION));
                                                                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 3));

                                                            }
                                                            if ((time > 10) && (time < 20)) {
                                                                p.getActivePotionEffects().removeIf(pot -> pot.getType().equals(PotionEffectType.CONFUSION));
                                                                p.getActivePotionEffects().removeIf(pot -> pot.getType().equals(PotionEffectType.SLOW));

                                                                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 6));
                                                                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 0));

                                                                //p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 30, 0));

                                                            }
                                                            if ((time > 20)) {
                                                                p.getActivePotionEffects().removeIf(pot -> pot.getType().equals(PotionEffectType.CONFUSION));
                                                                //p.getActivePotionEffects().removeIf(pot -> pot.getType().equals(PotionEffectType.WEAKNESS));
                                                                //p.getActivePotionEffects().removeIf(pot -> pot.getType().equals(PotionEffectType.BLINDNESS));
                                                                p.getActivePotionEffects().removeIf(pot -> pot.getType().equals(PotionEffectType.SLOW));

                                                                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 9));
                                                                //p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 30, 0));
                                                                //p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
                                                                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 1));
                                                            }

                                                        }

                                                    }
                                                }


                                            }
                                        }
                                    }

                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    if (kitInfo.getPlayerKit(p) != Kits.MADMAN) {
                                        if (madman.getLocation().distance(p.getLocation()) >= 20) {
                                            //Bukkit.broadcastMessage(p.getDisplayName() + "is not in range of madman");

                                            if (inMadManRange.contains(p.getUniqueId())) {
                                                p.getActivePotionEffects().removeIf(pot -> pot.getType().equals(PotionEffectType.CONFUSION));
                                                //p.getActivePotionEffects().removeIf(pot -> pot.getType().equals(PotionEffectType.WEAKNESS));
                                                p.getActivePotionEffects().removeIf(pot -> pot.getType().equals(PotionEffectType.BLINDNESS));
                                                p.getActivePotionEffects().removeIf(pot -> pot.getType().equals(PotionEffectType.SLOW));
                                                inMadManRange.remove(p.getUniqueId());
                                            }
                                            timeinMadmanPrescence.remove(p.getUniqueId());

                                        }
                                    }

                                }


                            }
                        }
                    }






                }
                }.runTaskTimer(HardcoreGames.getInstance(), 0L, 20L);
            }
    }

