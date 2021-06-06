package Util;

import Feast.MainFeast;
import Feast.MiniFeast;
import Kits.KitTools.KitInfo;
import Main.Config;
import Main.HardcoreGames;
import Robots.Robot;
import net.citizensnpcs.npc.ai.speech.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ForceFieldHandler {
    static Game game = Game.getSharedGame();
    static Config config = new Config();
    public static void pvpEnabled () {
    BukkitTask task = new BukkitRunnable() {
            int time = 0;
            final Random rand = new Random(System.currentTimeMillis());
            //time before first time border closes
            //5-8 min before first border goes from 5000 -> 4000 radius
            final int minBound = config.getMinBorderTime(); //minimum : 4
            final int maxBound = config.getMaxBorderTime(); //max : 8
            //yields 4 by default
            //rand.nextInt(4) gives any number 0-3. adding 4 to it yields range 4-7.
            int randomTimeInMinutes = rand.nextInt(maxBound - minBound) + minBound;


            int timesBorderShrunk = 0;
            @Override
            public void run() {

                int minutes = time/60;
                World world = Bukkit.getWorld("hungergames");
                if (world != null) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getLocation().getWorld() != world) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Player " + p.getName() +  " is in invalid world");
                            p.teleport(world.getSpawnLocation());
                        }
                    }
                }

                if (randomTimeInMinutes == minutes) {
                    //Bukkit.broadcastMessage(ChatColor.GOLD  + "randominute"  + randomTimeInMinutes);
                    double newSize = (game.getWorld().getWorldBorder().getSize() * 0.60);

                    Bukkit.broadcastMessage(ChatColor.RED + "The Border is now shrinking to " + Math.round(newSize) + " blocks in width!");
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        List<Player> safe = new ArrayList<>();
                        for (Entity pl : game.getWorld().getNearbyEntities(game.getWorld().getSpawnLocation(), newSize/2, newSize/2, newSize/2)) {
                            if (pl instanceof Player) {
                                safe.add((Player) pl);
                            }
                        }
                        if (!safe.contains(p)) {
                            p.sendMessage(ChatColor.DARK_RED + "WARNING! You are currently in the danger zone for the Border!");
                        }
                    }
                    timesBorderShrunk++;

                    //game.getWorld().getWorldBorder().setWarningTime(10);
                    //algorithm for border close  / time takes:
                    //1000 blocks walked = 294 seconds
                    //3.4 blocks a second
                    //seconds for border close = blocks shrunk  / 3.4

                    //first iteration:
                    //5000 radius -> 3000 radius
                    //takes 882 seconds to complete
                    //second iteration:
                    //3000 radius -> ~1800 radius
                    //takes 529 seconds to complete
                    //third iteration:
                    //1800 -> ~1080 radius
                    //takes ~317 seconds to complete
                    //fourth iteration:
                    //1080 -> 648 radius
                    //takes 190 seconds to complete
                    //fifth
                    //648 -> 388 radius
                    //takes 113 seconds to complete
                    //on 6th iteration switch to flat rate model
                    //388 -> 100 radius
                    //30 seconds
                    //100 -> 30
                    randomTimeInMinutes = (rand.nextInt(maxBound - minBound) + minBound) + minutes;


                    if (5 >= timesBorderShrunk) {
                        game.getWorld().getWorldBorder().setSize(newSize, timeBorderClose((int) newSize));

                        if (timesBorderShrunk == 2) {
                            Random rand = new Random();
                            int minifeastbound = 2;
                            int minifeast = rand.nextInt(minifeastbound);
                            if (minifeast == 1) {
                                MiniFeast.spawnFeast(game.getWorld(), newSize);

                            }
                        }

                        if (timesBorderShrunk == 3) {
                            Random rand = new Random();
                            int minifeastbound = 2;
                            int minifeast = rand.nextInt(minifeastbound);
                            if (minifeast == 1) {
                                MiniFeast.spawnFeast(game.getWorld(), newSize);

                            }
                        }

                        if (timesBorderShrunk == 4) {
                            Random rand = new Random();
                            int minifeastbound = 2;
                            int minifeast = rand.nextInt(minifeastbound);
                            if (minifeast == 1) {
                                MiniFeast.spawnFeast(game.getWorld(), newSize);

                            }

                        }

                        if (timesBorderShrunk == 5) {
                            Random rand = new Random();
                            int minifeastbound = 2;
                            int minifeast = rand.nextInt(minifeastbound);
                            if (minifeast == 1) {
                                MiniFeast.spawnFeast(game.getWorld(), newSize);

                            }
                        }
                    }




//time = 0;
                            if (timesBorderShrunk == 6) {
                                MainFeast.spawnFeast(game.getWorld(), 100);

                                game.getWorld().getWorldBorder().setSize(100, timeBorderClose((int) 200));


                            }
                            if (timesBorderShrunk == 7) {
                                game.getWorld().getWorldBorder().setSize(30, timeBorderClose((int) 30));

                            }
                            if (timesBorderShrunk == 8) {
                                game.getWorld().getWorldBorder().setSize(10, timeBorderClose(10));
                            }
                            if (timesBorderShrunk == 9) {
                                game.getWorld().getWorldBorder().setSize(2, timeBorderClose(2));
                                Bukkit.broadcastMessage(ChatColor.RED + "SUDDEN DEATH WILL BEGIN SOON!");

                            }
                            if (timesBorderShrunk == 10) {
                                Bukkit.broadcastMessage(ChatColor.RED + "SUDDEN DEATH HAS STARTED!");

                                List<Player> players =  new ArrayList<>(Bukkit.getOnlinePlayers());

                                suddenDeath(players);
                            }
                    }




                //Bukkit.broadcastMessage(game.currentPlayersLeft() + "");
                if (game.getPhase() == GamePhase.WINNERDECIDED) {
                    cancel();
                }
                //Bukkit.broadcastMessage(game.humansLeft().size() + " huamns left");
                //check if only players left
                if (game.validPlayersOnline().size() < 1 && Robot.getSharedRobots().activeRobots().size() >= 1) { //npe
                    //robots on, no humans
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.kickPlayer(ChatColor.AQUA + "Please wait, a new game is starting soon.");
                    }
                    game.restartGame(game);
                    cancel();
                    return;

                }

                if (game.currentPlayersLeft() < 1 && game.getPhase() != GamePhase.WINNERDECIDED || game.getPhase() == GamePhase.PREGAME) {
                    game.setPhase(GamePhase.WINNERDECIDED);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.kickPlayer(ChatColor.AQUA + "Please wait, a new game is starting soon.");
                    }
                    game.restartGame(game);
                    cancel();
                    return;
                }
                if (game.currentPlayersLeft() == 1 && game.getPhase() != GamePhase.WINNERDECIDED || game.getPhase() == GamePhase.PREGAME) {
                    //game.setPhase(GamePhase.WINNERDECIDED);
                    try {
                        game.setValidWinner();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }

//                    for (Player p : Bukkit.getOnlinePlayers()) {
//                        p.kickPlayer(ChatColor.AQUA + "Please wait, a new game is starting soon.");
//                    }
//                    try {
//                        game.restartGame(game);
//                    } catch (IOException exception) {
//                        exception.printStackTrace();
//                    }
                    cancel();
                }





                time++;
            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 20L);
        game.taskID.add(task);

    }

    static public void suddenDeath (List<Player> remaining) {
        for (Player p : remaining) {
            if (p.getGameMode() == GameMode.SURVIVAL) {
                EnderDragon dragon = p.getWorld().spawn(p.getLocation(), EnderDragon.class);
                dragon.setHealth(200);
                dragon.setAI(true);
                dragon.setGlowing(true);
                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (game.isStarted()) {
                            Random random = new Random();
                            if (random.nextInt(7) == 0) {
                                p.getWorld().strikeLightning(p.getLocation());
                            }
                        } else {
                            cancel();
                        }
                    }
                }.runTaskTimer(HardcoreGames.getInstance(), 0L, 20L);
                game.taskID.add(task);
            }
        }
    }
    public static int timeBorderClose (int size) {
        return (int) (size/5.4);
    }
}
