package Listeners;

import Kits.KitListeners.Kits.Defense.Chameleon;
import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Messages.Messages;
import Util.Game;
import Util.GamePhase;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class DeathListener implements Listener {
    Game game = Game.getSharedGame();
    Messages message = new Messages();

    HashMap<UUID, Location> deathmap = new HashMap<>();
    @EventHandler
    public void onDeath(PlayerDeathEvent e) throws IOException {
            //check if player is in game and phase is here
            if (game.isStarted()) {
                if (e.getEntity().hasMetadata("NPC")) {
                    return;
                }
                if (e.getEntity().getGameMode() != GameMode.SURVIVAL) {
                    return;
                }

                Player p = e.getEntity();
                if (p.getKiller() != null) {

                    game.addKill(p.getKiller().getUniqueId());
                }
                game.addLoser(p.getUniqueId());



//                for (UUID uuid : game.currentPlayerList()) {
//                }


                if (game.currentPlayersLeft() == 1 && game.currentPlayerList().size() == 1) {

                    if (game.setValidWinner()) {
                        Bukkit.getScheduler().runTask(HardcoreGames.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                //p.kickPlayer(message.kickMessage(p));
                                if (game.getLosers().contains(e.getEntity().getUniqueId())) {
                                    //e.disallow(PlayerLoginEvent.Result.KICK_OTHER, message.kickMessage(e.getPlayer()));
                                    //e.getPlayer().kickPlayer(message.kickMessage(game, e.getPlayer()));
                                    Location deathLoc = e.getEntity().getLocation().clone();

                                    e.getEntity().spigot().respawn();
                                    e.getEntity().teleport(deathLoc);

                                    e.getEntity().getInventory().clear();
                                    e.getEntity().setHealth(20);
                                    e.getEntity().setFireTicks(0);
                                    e.getEntity().setFoodLevel(20);
                                    e.getEntity().getActivePotionEffects().clear();
                                    e.getEntity().setInvisible(false);
                                    KitInfo.getSharedKitInfo().setPlayerKit(e.getEntity(), Kits.NONE);
                                    e.getEntity().setGameMode(GameMode.SPECTATOR);
                                    //e.setJoinMessage(null);

                                    e.getEntity().setInvulnerable(true);
//                if (!e.getPlayer().isOp()) {
//                    e.getPlayer().setFlying(false);
//                    e.getPlayer().setAllowFlight(false);
//
//                }
                                    e.getEntity().sendMessage(message.kickMessage(e.getEntity()));

                                    Chameleon.removeNoNametagIfExists(e.getEntity());
                                    Chameleon.removeChameleonCounter(e.getEntity());

//                                    World world = Bukkit.getWorld("hungergames");
//                                    if (world != null) {
//                                        e.getEntity().teleport(world.getSpawnLocation());
//                                    }
                            }
                        }});


                        game.setPhase(GamePhase.WINNERDECIDED);
                        return;
                    } else  {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.RED+ "Error occurred when trying to set winner");

                    }
                }
                Bukkit.getScheduler().runTask(HardcoreGames.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        /*e.getEntity().spigot().respawn();
                        p.kickPlayer(message.kickMessage(p));*/
                        if (game.getLosers().contains(e.getEntity().getUniqueId())) {
                            //e.disallow(PlayerLoginEvent.Result.KICK_OTHER, message.kickMessage(e.getPlayer()));
                            //e.getPlayer().kickPlayer(message.kickMessage(game, e.getPlayer()));
                            Location deathLoc = e.getEntity().getLocation().clone();
                            e.getEntity().spigot().respawn();
                            e.getEntity().teleport(deathLoc);
                            e.getEntity().getInventory().clear();
                            e.getEntity().setHealth(20);
                            e.getEntity().setFireTicks(0);
                            e.getEntity().setFoodLevel(20);
                            e.getEntity().getActivePotionEffects().clear();
                            e.getEntity().setInvisible(false);
                            KitInfo.getSharedKitInfo().setPlayerKit(e.getEntity(), Kits.NONE);
                            e.getEntity().setGameMode(GameMode.SPECTATOR);
                            //e.setJoinMessage(null);

                            deathmap.put(p.getUniqueId(), deathLoc);


                            e.getEntity().setInvulnerable(false);
//                if (!e.getPlayer().isOp()) {
//                    e.getPlayer().setFlying(false);
//                    e.getPlayer().setAllowFlight(false);
//
//                }
                            e.getEntity().sendMessage(message.kickMessage(e.getEntity()));

                            Chameleon.removeNoNametagIfExists(e.getEntity());
                            Chameleon.removeChameleonCounter(e.getEntity());

                            World world = Bukkit.getWorld("hungergames");
//                            if (world != null) {
//                                e.getEntity().teleport(world.getSpawnLocation());
//                            }
                    }
                }});

            }
            if (!e.getEntity().getWorld().getName().equals("hungergames")) {
                World world = Bukkit.getWorld("hungergames");
                if (world != null)
                    e.getEntity().teleport(world.getSpawnLocation());

            }
    }


    @EventHandler
    public void onRespawn (PlayerRespawnEvent e) {
        if (!e.getPlayer().getWorld().getName().equals("hungergames")) {

                World world = Bukkit.getWorld("hungergames");
                if (world != null)
                e.getPlayer().teleport(world.getSpawnLocation());


        }
        if (game.getLosers().contains(e.getPlayer().getUniqueId())) {
            if (deathmap.containsKey(e.getPlayer().getUniqueId())) {
                e.getPlayer().teleport(deathmap.get(e.getPlayer().getUniqueId()));
            }



        }

        }
//    @EventHandler
//    public void onPlayerMove(PlayerMoveEvent e)
//    {
//        if (e.getPlayer().getLocation().getWorld() != null)
//        if (!e.getPlayer().getLocation().getWorld().getName().equals("hungergames"))
//        {
//            World world = Bukkit.getWorld("hungergames");
//            if (world != null) {
//                e.getPlayer().teleport(world.getSpawnLocation());
//            }
//
//        }
//    }
}
