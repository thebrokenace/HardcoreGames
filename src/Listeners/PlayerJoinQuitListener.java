package Listeners;

import Kits.KitListeners.Kits.Defense.Chameleon;
import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Messages.Messages;
import Util.Game;
import Util.GamePhase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.UUID;

public class PlayerJoinQuitListener implements Listener {
    Game game = Game.getSharedGame();
    Messages message = new Messages();
    HashMap<UUID, Boolean> wasKicked = new HashMap<UUID, Boolean>();
    @EventHandler
    public void onJoin (PlayerLoginEvent e) {
            //if in losers list, kick on entry (or force spectator)
        //Bukkit.broadcastMessage(game.getPhase().toString());
        if (game.getPhase() == GamePhase.WINNERDECIDED) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, message.pleaseWait());

        }

        if (game.isStarted()) {

            if (game.getLosers().contains(e.getPlayer().getUniqueId())) {
                //e.disallow(PlayerLoginEvent.Result.KICK_OTHER, message.kickMessage(e.getPlayer()));
                //e.getPlayer().kickPlayer(message.kickMessage(game, e.getPlayer()));
                KitInfo.getSharedKitInfo().setPlayerKit(e.getPlayer(), Kits.NONE);
                e.getPlayer().setGameMode(GameMode.SPECTATOR);
            } else {
                //e.disallow(PlayerLoginEvent.Result.KICK_OTHER, message.alreadyStarted());
                KitInfo.getSharedKitInfo().setPlayerKit(e.getPlayer(), Kits.NONE);
                e.getPlayer().setGameMode(GameMode.SPECTATOR);
            }

        } else {
            //if it is pregame


            if (!game.getPlayers().contains(e.getPlayer().getUniqueId())) {
                if (!e.getPlayer().hasPermission("hungergames.bypass") && !e.getPlayer().isOp()) {
                    game.addPlayer(e.getPlayer().getUniqueId());
                }
            }

        }
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e) {
        if (e.getPlayer().isDead()) {
            e.getPlayer().spigot().respawn();
        }

        if (e.getPlayer().hasPermission("hardcoregames.greenname")) {
            e.getPlayer().setDisplayName(ChatColor.GREEN + e.getPlayer().getName());
        }

        if (e.getPlayer().hasPermission("hardcoregames.voted")) {
            e.getPlayer().setDisplayName(ChatColor.YELLOW + e.getPlayer().getName());
        }

        if (!game.isStarted()) {

            Bukkit.getServer().getScheduler().runTaskLater(HardcoreGames.getInstance(), () -> e.getPlayer().sendMessage(message.welcome(e.getPlayer())), 1);



            e.getPlayer().getInventory().clear();
            e.getPlayer().setHealth(20);
            e.getPlayer().setFireTicks(0);
            e.getPlayer().setFoodLevel(20);
            for(PotionEffect effect:e.getPlayer().getActivePotionEffects()){
                e.getPlayer().removePotionEffect(effect.getType());
            }            e.getPlayer().setInvisible(false);
            e.getPlayer().setGameMode(GameMode.SURVIVAL);
            e.getPlayer().setInvulnerable(false);
            e.getPlayer().setLevel(0);
            e.getPlayer().setExp(0);
            if (!e.getPlayer().isOp()) {
                e.getPlayer().setFlying(false);
                e.getPlayer().setAllowFlight(false);

            }

            Chameleon.removeNoNametagIfExists(e.getPlayer());
            Chameleon.removeChameleonCounter(e.getPlayer());

            World world = Bukkit.getWorld("hungergames");
            if (world != null) {
                e.getPlayer().teleport(world.getSpawnLocation());
            }
        } else  {

            if (game.getLosers().contains(e.getPlayer().getUniqueId())) {
                //e.disallow(PlayerLoginEvent.Result.KICK_OTHER, message.kickMessage(e.getPlayer()));
                //e.getPlayer().kickPlayer(message.kickMessage(game, e.getPlayer()));
                e.getPlayer().getInventory().clear();
                e.getPlayer().setHealth(20);
                e.getPlayer().setFireTicks(0);
                e.getPlayer().setFoodLevel(20);
                e.getPlayer().getActivePotionEffects().clear();
                e.getPlayer().setInvisible(false);
                KitInfo.getSharedKitInfo().setPlayerKit(e.getPlayer(), Kits.NONE);
                e.getPlayer().setGameMode(GameMode.SPECTATOR);
                e.setJoinMessage(null);

                e.getPlayer().setInvulnerable(false);
//                if (!e.getPlayer().isOp()) {
//                    e.getPlayer().setFlying(false);
//                    e.getPlayer().setAllowFlight(false);
//
//                }
                e.getPlayer().sendMessage(message.kickMessage(e.getPlayer()));

                Chameleon.removeNoNametagIfExists(e.getPlayer());
                Chameleon.removeChameleonCounter(e.getPlayer());

                World world = Bukkit.getWorld("hungergames");
                if (world != null) {
                    e.getPlayer().teleport(world.getSpawnLocation());
                }
            } else {
                //e.disallow(PlayerLoginEvent.Result.KICK_OTHER, message.alreadyStarted());
                e.getPlayer().getInventory().clear();
                e.getPlayer().setHealth(20);
                e.getPlayer().setFireTicks(0);
                e.getPlayer().setFoodLevel(20);
                e.getPlayer().getActivePotionEffects().clear();
                e.getPlayer().setInvisible(false);
                e.getPlayer().setInvulnerable(false);
                KitInfo.getSharedKitInfo().setPlayerKit(e.getPlayer(), Kits.NONE);
                e.getPlayer().setGameMode(GameMode.SPECTATOR);
                e.getPlayer().sendMessage(message.kickMessage(e.getPlayer()));
                e.setJoinMessage(null);

//                if (!e.getPlayer().isOp()) {
//                    e.getPlayer().setFlying(false);
//                    e.getPlayer().setAllowFlight(false);
//
//                }
                Chameleon.removeNoNametagIfExists(e.getPlayer());
                Chameleon.removeChameleonCounter(e.getPlayer());

                World world = Bukkit.getWorld("hungergames");
                if (world != null) {
                    e.getPlayer().teleport(world.getSpawnLocation());
                }
            }

        }

    }

    @EventHandler
    public void onQuit (PlayerQuitEvent e) {
        if (!wasKicked.containsKey(e.getPlayer().getUniqueId())) {
            //add to losers list
            if (game.isStarted()) {
                game.addLoser(e.getPlayer().getUniqueId());
                message.sendToAll(message.deathFormattedMessage(e.getPlayer(), e.getPlayer()));
                Bukkit.broadcastMessage(message.peopleLeftMessage());
//            for (ItemStack i :  e.getPlayer().getInventory().getContents()) {
//                if (i != null) {
//                    if (e.getPlayer().getLocation().getWorld() != null) {
//                        e.getPlayer().getLocation().getWorld().dropItemNaturally(e.getPlayer().getLocation(), i);
//                    }
//                }
//            }

                e.getPlayer().setHealth(0);
//            Bukkit.broadcastMessage(game.currentPlayersLeft() + "hi" + game.currentPlayerList().size() + " size player");
//            if ((game.currentPlayersLeft()-1 == 1) && (game.currentPlayerList().size()-1 == 1)) {
//                if (game.setValidWinner()) {
//                    Bukkit.broadcastMessage(ChatColor.AQUA + "Congratulations " + game.getWinner().getName() + " for winning!!");
//                } else  {
//                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED+ "Error occurred when trying to set winner");
//
//                }
//            }

            } else {
                if (game.getPlayers().contains(e.getPlayer().getUniqueId())) {
                    game.removePlayer(e.getPlayer().getUniqueId());
                }
            }
        }

    }

    @EventHandler
    public void kickedEvent(PlayerKickEvent e) {
        wasKicked.put(e.getPlayer().getUniqueId(), true);
    }
}
