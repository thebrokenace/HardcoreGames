package Kits.KitListeners.Kits.Defense;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import Util.GamePhase;
import Util.Sounds;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Dwarf implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void onSneak (PlayerToggleSneakEvent e) {
        if (game.getPhase() == GamePhase.GAMESTARTED) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.DWARF) {
                if (e.isSneaking()) {
                    new BukkitRunnable() {
                        int time = 0;

                        @Override
                        public void run() {
                            if (!e.getPlayer().isSneaking() && time < 200) {
                                cancel();
                            }
                            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 4);
                            e.getPlayer().getLocation().getWorld().spawnParticle(Particle.REDSTONE, e.getPlayer().getLocation().add(0,2,0), 1, dustOptions);
                            if (e.getPlayer().isSneaking() && time <= 200) {
                                StringBuilder actionBarMessage = new StringBuilder();

                                double percent = (double) time / 2;
                                for (int i = 0; i < percent; i++) {
                                     actionBarMessage = new StringBuilder(ChatColor.YELLOW + "Charged: ");
                                    //Charged: [| | | | | | | | | | ]

                                    actionBarMessage.append(ChatColor.GREEN + "" + percent + "%");
                                }

                                if (percent < 100) {
                                    e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBarMessage.toString()));
                                } else {
                                    e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "FULLY CHARGED! Release shift to activate shockwave!"));
                                }
                            }

                            if (time == 40) {
                                //2 seconds charged
                                Sounds.chargeDwarf(e.getPlayer().getLocation());
                                e.getPlayer().sendMessage(ChatColor.GREEN + "20% Charged");
                            }
                            if (time == 80) {
                                Sounds.chargeDwarf(e.getPlayer().getLocation());
                                e.getPlayer().sendMessage(ChatColor.GREEN + "40% Charged");
                            }
                            if (time == 120) {
                                //5 seconds charged
                                Sounds.chargeDwarf(e.getPlayer().getLocation());
                                e.getPlayer().sendMessage(ChatColor.GREEN + "60% Charged");
                            }
                            if (time == 160) {
                                Sounds.chargeDwarf(e.getPlayer().getLocation());
                                e.getPlayer().sendMessage(ChatColor.GREEN + "80% Charged");
                            }
                            if (time == 200) {
                                Sounds.chargeDwarf(e.getPlayer().getLocation());
                                Sounds.chargeReadyDwarf(e.getPlayer().getLocation());
                                e.getPlayer().sendMessage(ChatColor.GREEN + "100% Charged");
                            }
                            if (time >= 200) {
                                if (!e.getPlayer().isSneaking()) {
                                    Sounds.shockwaveDwarf(e.getPlayer().getLocation());
                                    shockwave(e.getPlayer());
                                    cancel();
                                }
                            }

                            time++;
                        }
                    }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);
                }

            }
        }

    }

    public void shockwave(Player p) {
        p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
        p.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, p.getLocation(), 5);
        for (Entity e : p.getWorld().getNearbyEntities(p.getLocation(), 7, 7, 7)) {
            if (e != p) {
                Location loc = e.getLocation();
                Location ploc = p.getLocation();
                Location difference = ploc.subtract(loc);
                difference.add(0, 0, 0);
                e.setVelocity(difference.toVector().subtract(new Vector(0,2,0)).normalize().multiply(-3));
//                new BukkitRunnable() {
//                    int time = 0;
//                    @Override
//                    public void run() {
//                        e.setFallDistance(0);
//                        if (time > 5*20) {
//                            e.setFallDistance(0);
//                            cancel();
//                        }
//                        time++;
//                    }
//                }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);
            }
        }
    }
}
