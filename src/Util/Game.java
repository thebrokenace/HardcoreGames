package Util;

import Kits.KitListeners.Kits.Defense.Scout;
import Kits.KitListeners.Kits.Vanity.Crafter;
import Kits.KitTools.KitInfo;
import Main.Config;
import Main.HardcoreGames;
import Messages.Scoreboard;
import Robots.Robot;
import com.shanebeestudios.vf.api.machine.Furnace;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.*;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.io.IOException;
import java.util.*;

public class Game {
    List<UUID> players = new ArrayList<>();
    List<UUID> losers = new ArrayList<>();
    long timeStarted = System.currentTimeMillis();
    GamePhase phase = GamePhase.PREGAME;
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    Player winner;
    int forcefieldRadius = 5000;
    Map<UUID, Integer> kills = new HashMap<>();
    //boolean allKitsFree = false;
    Config config = new Config();

    public List<BukkitTask> taskID = new ArrayList<>();

    //default 5000 forcefield radius

    //0 is pregame (no build no break, waiting for players to join, pick kit), 1 is grace period of 5 min (break enabled
    // build enabled, players no longer can join, no pvp but still can die), 2 is when game starts (pvp enabled)

    private Game() { } // make your constructor private, so the only war
    // to access "application" is through singleton pattern

    private static Game _game;

    public static Game getSharedGame()
    {
        if (_game == null)
            _game = new Game();
        return _game;
    }

    public int getForcefieldRadius () {
        return forcefieldRadius;
    }

    public boolean reduceForcefieldRadius (World world, int decrement) {
        if (forcefieldRadius - decrement > 0) {
            forcefieldRadius = forcefieldRadius - decrement;
            //world.getWorldBorder().(forcefieldRadius);
            return true;
            //successfully reduced square radius of forcefield by such
        } else {
            //invalid decrement, failed to reduce
            return false;
        }
    }

    public World getWorld() {
        return Bukkit.getWorld("hungergames");
    }

    public List<UUID> validPlayersOnline () {
        List<UUID> validPlayers = new ArrayList<>();
        if (Bukkit.getOnlinePlayers().size() != 0) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.hasPermission("hungergames.bypass") && !p.isOp() && !p.getGameMode().equals(GameMode.SPECTATOR)) {
                    if (!_game.losers.contains(p.getUniqueId())) {
                        validPlayers.add(p.getUniqueId());
                    }
                }
            }
            return validPlayers;
        }
        return validPlayers;
    }


    public List<UUID> humansLeft () {
        if (_game.isStarted() || _game.getPhase() == GamePhase.WINNERDECIDED) {
            List<UUID> validPlayers = new ArrayList<>();
            if (Bukkit.getOnlinePlayers().size() != 0) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!p.hasPermission("hungergames.bypass") && !p.isOp() && !p.getGameMode().equals(GameMode.SPECTATOR)) {
                        if (!_game.losers.contains(p.getUniqueId())) {
                            validPlayers.add(p.getUniqueId());
                        }
                    }
                }
                return validPlayers;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    public List<UUID> currentPlayerList () {
        Robot robot = Robot.getSharedRobots();

        if (_game.isStarted() || _game.getPhase() == GamePhase.WINNERDECIDED) {
            List<UUID> validPlayers = new ArrayList<>();
            if (Bukkit.getOnlinePlayers().size() != 0) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!p.hasPermission("hungergames.bypass") && !p.isOp() && !p.getGameMode().equals(GameMode.SPECTATOR)) {
                        if (!_game.losers.contains(p.getUniqueId())) {
                            validPlayers.add(p.getUniqueId());
                        }
                    }
                }
                for (NPC npc : robot.activeRobots()) {
                    validPlayers.add(npc.getUniqueId());
                }
                return validPlayers;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    public boolean allKitsFree () {
        if (config.freeKits()) {
            //if enabled
            config.setFreeKits(false);
            return false;
        } else {
            //if not enabled
            config.setFreeKits(true);
            return true;
        }
    }

    public boolean getAllKitsFree () {
        return config.freeKits();
    }

    public int currentPlayersLeft () {
        Robot robot = Robot.getSharedRobots();

        if (_game.isStarted() || _game.getPhase() == GamePhase.WINNERDECIDED) {
            List<UUID> validPlayers = new ArrayList<>();
            if (Bukkit.getOnlinePlayers().size() != 0) {

                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!p.hasPermission("hungergames.bypass") && !p.isOp() && !p.getGameMode().equals(GameMode.SPECTATOR)) {
                        if (!_game.losers.contains(p.getUniqueId())) {

                            //Bukkit.broadcastMessage(p.getDisplayName() + " is valid plyer");

                            validPlayers.add(p.getUniqueId());
                        }
                    }
                }
                for (NPC npc : robot.activeRobots()) {
                    validPlayers.add(npc.getUniqueId());
                }
                return validPlayers.size();
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    public List<UUID> getLosers() {
        return losers;
    }

    public int loserCount() {
        return losers.size();
    }

    public void addLoser (UUID uuid) {
        losers.add(uuid);
    }

    public List<UUID> getPlayers () {
        return players;
    }
    public void addPlayer (UUID uuid) {
        players.add(uuid);
    }

    public void removePlayer (UUID uuid) {
        players.remove(uuid);
    }

    public boolean isInGame (UUID uuid) {
        return players.contains(uuid);
    }

    public void addKill (UUID uuid) {
      kills.put(uuid, kills.getOrDefault(uuid, 0) + 1);
    }

    public int getKills (UUID uuid) {
        return kills.getOrDefault(uuid, 0);
    }

    public boolean isStarted () {
        return phase != GamePhase.PREGAME && phase != GamePhase.WINNERDECIDED;
    }

    public long getTimeStarted() {
        return timeStarted;
    }

    public void setPhase (GamePhase p) {
       phase = p;

    }

    public GamePhase getPhase () {
        return phase;
    }

    public int playerCount () {
        return players.size();
    }

    public Player getWinner() {
        return winner;
    }

    public boolean setValidWinner() throws IOException {
        if (currentPlayersLeft() == 1 && currentPlayerList().size() == 1) {
            winner = Bukkit.getPlayer(currentPlayerList().get(0));
            if (winner != null && !winner.hasMetadata("NPC")) {
                Bukkit.getServer().getScheduler().runTaskLater(HardcoreGames.getInstance(), () -> {
                    try {
                        WinHandler.rewardWinner(winner);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }, 10);


            } else {
                winner = null;
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public static void clearTeams () {
        for (Player p : Bukkit.getOnlinePlayers()) {

            for (Team t : p.getScoreboard().getTeams()) {
               // Bukkit.broadcastMessage(t.getName());

                t.removeEntry(p.getName());
            }
            //p.getScoreboard().getTeams().clear();
        }
    }
    public void startGame (Game _game) {
        //Scoreboard.setupScoreBoard();
        Scoreboard.fastBoard();


        for (Furnace furance : HardcoreGames.getInstance().getFurnaceManager().getAllFurnaces()) {
            furance.setFuel(new ItemStack(Material.AIR));
            furance.setInput(new ItemStack(Material.AIR));
            //HardcoreGames.getInstance().getFurnaceManager().
            HardcoreGames.getInstance().getFurnaceManager().removeFurnaceFromConfig(furance,true);

        }




        Crafter.clearfurnaces();
        Robot.getSharedRobots().despawnAllBots();
        Robot.getSharedRobots().clearRobots();

        new BukkitRunnable() {
            @Override
            public void run() {

        WorldCreator wc = new WorldCreator("hungergames");
        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.NORMAL);
        World world = wc.createWorld();

        if (world != null) {
            world.getWorldBorder().setSize(5000);
            //world.getWorldBorder().setCenter(world.getSpawnLocation());
            world.setDifficulty(Difficulty.HARD);
            world.setTime(0);
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.DO_INSOMNIA, false);
            Bukkit.getWorlds().add(world);

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.teleport(world.getSpawnLocation());
            }

            _game.setPhase(GamePhase.PREGAME);
            GameTimer.startTimer();
            cancel();

        }
        cancel();
            }
        }.runTask(HardcoreGames.getInstance());


    }

    public void restartGame (Game _game) {
        endGame(_game);
        startGame(_game);
    }

    public void endGame (Game _game) {
        Crafter.clearfurnaces();
        PregenerateRandomLocation.get_pregen().clearMap();

        for (Furnace furance : HardcoreGames.getInstance().getFurnaceManager().getAllFurnaces()) {
//            Bukkit.broadcastMessage(furance.toString() + ChatColor.GREEN + "FURNACE");
//            Bukkit.getConsoleSender().sendMessage(furance.toString() + ChatColor.GREEN + "FRUACNe");
            furance.setFuel(new ItemStack(Material.AIR));
            furance.setInput(new ItemStack(Material.AIR));
            HardcoreGames.getInstance().getFurnaceManager().removeFurnaceFromConfig(furance,true);

        }




        Robot.getSharedRobots().despawnAllBots();
        Scout.jumpers.clear();
//        for (BukkitTask b : Bukkit.getScheduler().getPendingTasks()) {
//                b.cancel();
//        }

            for (BukkitTask task : taskID) {
                task.cancel();
            }
            taskID.clear();
            Bukkit.getScheduler().runTask(HardcoreGames.getInstance(), new Runnable() {
                @Override
                public void run() {

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!p.hasMetadata("NPC")) {
                            p.kickPlayer("Restarting game!");
                        }
                    }


                    ;
                }
            });







        new BukkitRunnable() {
            @Override
            public void run() {
                World world = Bukkit.getWorld("hungergames");
                if (world != null) {
                    Bukkit.getServer().unloadWorld( world, false);
                    Bukkit.getServer().getWorlds().remove(world);
                }
                if (world != null) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + world.getWorldFolder().getPath());
                    try {
                        FileUtils.deleteDirectory(world.getWorldFolder());
                        WorldUtils.deleteDirectory(world.getWorldFolder());

                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
                cancel();
            }
        }.runTask(HardcoreGames.getInstance());




        _game.phase = GamePhase.PREGAME;
        _game.winner = null;
        _game.kills.clear();
        _game.players.clear();
        _game.forcefieldRadius = 0;
        _game.losers.clear();
        _game.timeStarted = 0;

        Robot.getSharedRobots().despawnAllBots();
        Robot.getSharedRobots().clearRobots();


        kitInfo.clearKits();

    }



}
