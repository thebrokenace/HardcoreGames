package Kits.KitListeners.Kits.Defense;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import Util.GamePhase;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Chameleon implements Listener {
    static Game game = Game.getSharedGame();
    static KitInfo kitInfo = KitInfo.getSharedKitInfo();
    static String teamName = "NoNameTagTeam";
    static String spyCounterTeamName = "spychameleon";
    static Map<UUID, Location> locationMap = new HashMap<>();
    static Map<UUID, Boolean> doShow = new HashMap<>();

    public static void removeNoNametagIfExists(Player p) {
        Team NoNameTagTeam = null;
        ScoreboardManager sbm = Bukkit.getServer().getScoreboardManager();
        if (sbm != null && sbm.getMainScoreboard().getTeam(teamName) != null) {
            //Bukkit.broadcastMessage("found scoreboard chameleon");
            NoNameTagTeam = sbm.getMainScoreboard().getTeam(teamName);
        }
        if (NoNameTagTeam != null) {
            ///Bukkit.broadcastMessage("removing " + p.getName());
            NoNameTagTeam.removeEntry(p.getName());
        }

    }


    public static void camouflage () {
      BukkitTask task =  new BukkitRunnable() {
            @Override
            public void run() {
                if (game.getPhase() == GamePhase.GAMESTARTED) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (kitInfo.getPlayerKit(p) == Kits.CHAMELEON) {
                            //Bukkit.broadcastMessage(e.getPlayer().getVelocity().toString()) ;
                            if (!doShow.containsKey(p.getUniqueId())) {

                                if (locationMap.containsKey(p.getUniqueId())) {

                                    if (locationMap.get(p.getUniqueId()).distance(p.getLocation().clone().add(0.01, 0.00,0.00)) > 0.5) {

                                        p.setInvisible(false);
                                        locationMap.put(p.getUniqueId(), p.getLocation());
                                    } else {
                                        p.setInvisible(true);
                                        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.LIME, 1);
                                        p.getLocation().getWorld().spawnParticle(Particle.REDSTONE, p.getLocation().clone().add(0,2,0),1, dustOptions);
                                    }

                                } else {
                                    if (p.getLocation().getWorld() != null && game.getWorld().equals(p.getLocation().getWorld())) {
                                        locationMap.put(p.getUniqueId(), p.getLocation());
                                    }

                                }
                            } else {
                                p.setInvisible(false);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L,10L);
      game.taskID.add(task);
    }


    public static void applyNoNametag(Player p) {
        Team NoNameTagTeam = null;



        ScoreboardManager sbm = Bukkit.getServer().getScoreboardManager();

        if (sbm != null && sbm.getMainScoreboard().getTeam(teamName) == null)
            sbm.getMainScoreboard().registerNewTeam(teamName);
        if (sbm != null) {
            NoNameTagTeam = sbm.getMainScoreboard().getTeam(teamName);
        }
        if (NoNameTagTeam != null) {
            NoNameTagTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }
        if (NoNameTagTeam != null) {
            NoNameTagTeam.addEntry(p.getName());
        }


        Team letSpySee = null;

        if (sbm != null && sbm.getMainScoreboard().getTeam(spyCounterTeamName) == null)
            sbm.getMainScoreboard().registerNewTeam(spyCounterTeamName);
        if (sbm != null) {
            letSpySee = sbm.getMainScoreboard().getTeam(spyCounterTeamName);
        }
        if (letSpySee != null) {
            letSpySee.setCanSeeFriendlyInvisibles(true);
            letSpySee.addEntry(p.getName());
            //letSpySee.setAllowFriendlyFire(true);
            //NoNameTagTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }
//        if (NoNameTagTeam != null) {
//            NoNameTagTeam.addEntry(p.getName());
//        }


    }
    public static void addChameleonCounter (Player p) {
        if (kitInfo.getPlayerKit(p) == Kits.SPY) {
            ScoreboardManager sbm = Bukkit.getScoreboardManager();
            Team letSpySee = null;

            if (sbm != null && sbm.getMainScoreboard().getTeam(spyCounterTeamName) == null)
                sbm.getMainScoreboard().registerNewTeam(spyCounterTeamName);
            if (sbm != null) {
                letSpySee = sbm.getMainScoreboard().getTeam(spyCounterTeamName);
            }
            if (letSpySee != null) {
                letSpySee.addEntry(p.getName());

            }
        }
    }

    public static void removeChameleonCounter (Player p) {
            ScoreboardManager sbm = Bukkit.getScoreboardManager();
            Team letSpySee = null;

            if (sbm != null && sbm.getMainScoreboard().getTeam(spyCounterTeamName) == null)
                sbm.getMainScoreboard().registerNewTeam(spyCounterTeamName);
            if (sbm != null) {
                letSpySee = sbm.getMainScoreboard().getTeam(spyCounterTeamName);
            }
            if (letSpySee != null) {
                letSpySee.removeEntry(p.getName());

            }

    }

    @EventHandler
    public void onHit (EntityDamageByEntityEvent e) {
        if (game.isStarted()) {
            if (e.getEntity() instanceof Player) {
                Player cham = (Player) e.getEntity();
                if (kitInfo.getPlayerKit(cham) == Kits.CHAMELEON) {
                    new BukkitRunnable() {
                        int time = 0;
                        @Override
                        public void run() {
                            doShow.put(cham.getUniqueId(), true);

                            if (time > 3) {
                                doShow.remove(cham.getUniqueId());
                                cancel();

                            }
                            time++;
                        }
                    }.runTaskTimer(HardcoreGames.getInstance(), 0L, 20L);
                }
            }

            if (e.getDamager() instanceof Player) {
                Player cham = (Player) e.getDamager();
                if (kitInfo.getPlayerKit(cham) == Kits.CHAMELEON) {
                    new BukkitRunnable() {
                        int time = 0;
                        @Override
                        public void run() {
                            doShow.put(cham.getUniqueId(), true);

                            if (time > 3) {
                                doShow.remove(cham.getUniqueId());
                                cancel();

                            }
                            time++;
                        }
                    }.runTaskTimer(HardcoreGames.getInstance(), 0L, 20L);
                }
            }
        }
    }



}
