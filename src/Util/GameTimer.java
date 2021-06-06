package Util;

import Kits.KitListeners.Kits.Attack.Archer;
import Kits.KitListeners.Kits.Attack.Werewolf;
import Kits.KitListeners.Kits.Defense.Chameleon;
import Kits.KitListeners.Kits.Defense.Madman;
import Kits.KitListeners.Kits.Utility.Spy;
import Kits.KitListeners.Kits.Vanity.Stand;
import Kits.KitListeners.Kits.Vanity.Surprise;
import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.Config;
import Main.HardcoreGames;
import Main.PlayerStats;
import Messages.Messages;
import Robots.Robot;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Inventory;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import Messages.Scoreboard;

import java.util.Random;

public class GameTimer {
    static Game game = Game.getSharedGame();
    static Messages messages = new Messages();
    static KitInfo kitInfo = KitInfo.getSharedKitInfo();
    static Robot robot = Robot.getSharedRobots();
    static Config config = new Config();
    public static String time = "";
    public static void startTimer() {
     BukkitTask task =   new BukkitRunnable() {
            int seconds = 0;
             int botcount = config.botCount();
             boolean startedSpawning = false;

            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size() <= 0) {
                    return;
                }
                checkInvalidWorld();
                pregenerateTeleportLocations();
                //Game.clearTeams();
//want an algorithm that spawns x bots per x seconds until 5 seconds are left while timer is going
                if (!startedSpawning) {
                    startedSpawning = checkBotSettings(botcount);
                }

                if (isEnoughPlayers()) {
                    Bukkit.broadcastMessage(ChatColor.GREEN + "Beginning game...");
                    startGame();
                    cancel();
                } else {
                    if (seconds % 30 == 0) {
                        time = "";
                        Bukkit.broadcastMessage(ChatColor.RED + "Not enough players! Waiting for more to join.");
                    }
                }

                seconds++;

            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 20L);
        game.taskID.add(task);
    }
   static boolean tier1 = false;
   static boolean tier2 = false;
    static boolean tier3 = false;
    static boolean tier4 = false;
    public static void startGame () {
     BukkitTask task =   new BukkitRunnable() {
            int seconds = Queue.tier1Time();

            @Override
            public void run() {
                if (seconds <= 30) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        messages.sendTitleMessage(p, String.valueOf(seconds), ChatColor.GREEN, "");
                    }
                    Sounds.countDownEnoughPlayers();
                }
                time = String.valueOf(seconds);
                if (!isEnoughPlayers()) {
                    Bukkit.broadcastMessage(ChatColor.RED + "Not enough players! Waiting for more to join.");
                    Sounds.countDownFailed();
                    time = "";
                    startTimer();
                    tier1 = false;
                    tier2 = false;
                    tier3 = false;
                    tier4 = false;
                    cancel();

                }
                pregenerateTeleportLocations();
                checkInvalidWorld();
                if (tier1 && !Queue.tier1(Bukkit.getOnlinePlayers().size())) {
                    Bukkit.broadcastMessage(ChatColor.BLUE + "Tier 1 tuned false");

                    tier1 = false;
                    //seconds = Queue.tier1Time();
                    Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 10f, 1f));
                }

                if (tier2 && !Queue.tier2(Bukkit.getOnlinePlayers().size())) {
                    Bukkit.broadcastMessage(ChatColor.BLUE + "Tier 2 tuned false");

                    tier2 = false;
                    seconds = Queue.tier1Time();
                    Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 10f, 1f));
                }

                if (tier3 && !Queue.tier3(Bukkit.getOnlinePlayers().size())) {
                    Bukkit.broadcastMessage(ChatColor.BLUE + "Tier 3 tuned false");

                    tier3 = false;
                    seconds = Queue.tier2Time();
                    Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 10f, 1f));
                }

                if (tier4 && !Queue.tier4(Bukkit.getOnlinePlayers().size())) {
                    Bukkit.broadcastMessage(ChatColor.BLUE + "Tier 4 tuned false");

                    tier4 = false;
                    seconds = Queue.tier3Time();
                    Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 10f, 1f));
                }


                if (!tier1 && Queue.tier1(Bukkit.getOnlinePlayers().size())) {
                    Bukkit.broadcastMessage(ChatColor.BLUE + "Tier 1 tuned true");
                    tier1 = true;
                    seconds = Queue.timeLeft(Bukkit.getOnlinePlayers().size());
                    Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10f, 1f));
                }

                if (!tier2 && Queue.tier2(Bukkit.getOnlinePlayers().size())) {
                    Bukkit.broadcastMessage(ChatColor.BLUE + "Tier 2 tuned true");

                    tier2 = true;
                    seconds = Queue.timeLeft(Bukkit.getOnlinePlayers().size());
                    Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10f, 1f));
                }

                if (!tier3 && Queue.tier3(Bukkit.getOnlinePlayers().size())) {
                    Bukkit.broadcastMessage(ChatColor.BLUE + "Tier 3 tuned true");

                    tier3 = true;
                    seconds = Queue.timeLeft(Bukkit.getOnlinePlayers().size());
                    Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10f, 1f));
                }

                if (!tier4 && Queue.tier4(Bukkit.getOnlinePlayers().size())) {
                    Bukkit.broadcastMessage(ChatColor.BLUE + "Tier 4 tuned true");

                    tier4 = true;
                    seconds = Queue.timeLeft(Bukkit.getOnlinePlayers().size());
                    Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10f, 1f));
                }






                seconds--;
                if (seconds < 0) {
                    startedGrace();
                    cancel();
                }

            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 20L);
     game.taskID.add(task);
    }

    public static void startedGrace () {
        Sounds.lastCountDown();
        game.setPhase(GamePhase.GRACE);
        validatePlayers();
        giveKitsPlayers();
        teleportBotsAndPlayers();
        activatePassiveAbilities();

        Bukkit.broadcastMessage(ChatColor.AQUA + "Grace period has started!\nYou have 3 minutes to gather materials before PVP is enabled!");

        startGracePeriod();

        robot.graceRobots();
    }

    public static void activatePassiveAbilities () {
        Werewolf.giveWolfPowers();
        Madman.madman();
        Archer.regenerateArrows();
        Chameleon.camouflage();
        Spy.spySeeKit();
        Stand.giveStands();

    }
    public static void pregenerateTeleportLocations () {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (kitInfo.getPlayerKit(p) == Kits.HERMIT) {
                        if (!PregenerateRandomLocation.get_pregen().getHermitMap().containsKey(p)) {
                            PregenerateRandomLocation.get_pregen().putLocation(p, true);
                        }
                    }
                    if (!PregenerateRandomLocation.get_pregen().getLocationMap().containsKey(p)) {

                        PregenerateRandomLocation.get_pregen().putLocation(p, false);
                    }
                }
                for (NPC npc : robot.activeRobots()) {
                    if (kitInfo.getPlayerKit(npc.getEntity()) == Kits.HERMIT) {
                        if (!PregenerateRandomLocation.get_pregen().getHermitMap().containsKey(npc.getEntity())) {
                            PregenerateRandomLocation.get_pregen().putLocation(npc.getEntity(), true);
                        }
                    }
                    if (!PregenerateRandomLocation.get_pregen().getLocationMap().containsKey(npc.getEntity())) {
                        PregenerateRandomLocation.get_pregen().putLocation(npc.getEntity(), false);
                    }
                }
            }
        }.runTaskAsynchronously(HardcoreGames.getInstance());

    }

    public static void teleportBotsAndPlayers () {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (kitInfo.getPlayerKit(p) == Kits.HERMIT) {
                if (!PregenerateRandomLocation.get_pregen().getHermitMap().containsKey(p)) {


                    PregenerateRandomLocation.get_pregen().putLocation(p, true);

                }

                p.teleport(PregenerateRandomLocation.get_pregen().getHermitLocation(p));
            } else {
                if (!PregenerateRandomLocation.get_pregen().getLocationMap().containsKey(p)) {

                    PregenerateRandomLocation.get_pregen().putLocation(p, false);

                }

                p.teleport(PregenerateRandomLocation.get_pregen().getEntityLocation(p));


            }
        }
        for (NPC npc : robot.activeRobots()) {
            Entity p = npc.getEntity();
            if (kitInfo.getPlayerKit(npc.getEntity()) == Kits.HERMIT) {
                if (!PregenerateRandomLocation.get_pregen().getHermitMap().containsKey(p)) {


                    PregenerateRandomLocation.get_pregen().putLocation(p, true);
                }

                p.teleport(PregenerateRandomLocation.get_pregen().getHermitLocation(p));
            } else {
                if (!PregenerateRandomLocation.get_pregen().getLocationMap().containsKey(p)) {



                    PregenerateRandomLocation.get_pregen().putLocation(p, false);
                }
                p.teleport(PregenerateRandomLocation.get_pregen().getEntityLocation(p));

            }

        }

     }
    public static void giveKitsPlayers () {
        for (Player p : Bukkit.getOnlinePlayers()) {


            if (kitInfo.getPlayerKit(p) != Kits.NONE) {
                PlayerStats.getStats().addKitToFrequent(p, kitInfo.getPlayerKit(p));
            }
            if (kitInfo.getPlayerKit(p) == Kits.SURPRISE) {

                Kits randomKit = Surprise.getRandomKit();
                kitInfo.surprises.put(p, randomKit);
                kitInfo.setPlayerKit(p, randomKit);
                p.sendMessage(ChatColor.AQUA + "You are now a " + kitInfo.getKitNameFormatted(randomKit, p) + "!");

            }

            if (kitInfo.getPlayerKit(p) == Kits.CHAMELEON) {
                Chameleon.applyNoNametag(p);
            }
            if (kitInfo.getPlayerKit(p) == Kits.SPY) {
                Chameleon.addChameleonCounter(p);
            }
            if (kitInfo.getPlayerKit(p) == Kits.SCOUT) {
                p.setAllowFlight(true);
            }

            if (kitInfo.getPlayerKit(p) == Kits.COPYCAT) {
                kitInfo.copycats.put(p, Kits.COPYCAT);
            }

            for (ItemStack i : kitInfo.getKitDrops(kitInfo.getPlayerKit(p))) {
                p.getInventory().addItem(i);
            }


        }

    }

    public static void validatePlayers () {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setHealth(20);
            p.setFoodLevel(20);
            p.setSaturation(20);
            p.setFireTicks(0);
            p.getInventory().clear();
            p.setInvisible(false);
            if (!p.isOp()) {
                p.setFlying(false);
                p.setAllowFlight(false);

            }
            p.getInventory().addItem(new ItemStack(Material.COMPASS));

        }
}
    public static void startGracePeriod() {
        for (NPC npc : robot.activeRobots()) {
            ItemStack[] npcInventory = new ItemStack[kitInfo.getKitDrops(kitInfo.getPlayerKit(npc.getEntity())).size()];
            for (int i = 0; i < kitInfo.getKitDrops(kitInfo.getPlayerKit(npc.getEntity())).size(); i++) {
                npcInventory[i] = kitInfo.getKitDrops(kitInfo.getPlayerKit(npc.getEntity())).get(i);
            }

            npc.getOrAddTrait(Inventory.class).setContents(npcInventory);
        }

      BukkitTask task =  new BukkitRunnable() {
            int seconds = config.graceTimer();
            @Override
            public void run() {

                World worldhg = Bukkit.getWorld("hungergames");
                if (worldhg != null) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getLocation().getWorld() != worldhg) {
                            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Player " + p.getName() +  " is in invalid world");
                            p.teleport(worldhg.getSpawnLocation());
                        }
                    }
                }

                if (seconds == 60) {
                Bukkit.broadcastMessage(ChatColor.AQUA + "60 seconds left until PVP is enabled!");
                }
                if (seconds <= 10) {
                    Sounds.countDownPVP();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        messages.sendTitleMessage(p, String.valueOf(seconds), ChatColor.RED, "");

                    }
                }
                seconds--;


                if (seconds < 0) {
                    Sounds.gameStartedSound();





                    Bukkit.broadcastMessage(ChatColor.RED + "PVP is now enabled!");
                    game.setPhase(GamePhase.GAMESTARTED);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        PlayerStats.getStats().addGame(p);
                    }

                    //set npcs to target players and other npcs
                    robot.robotsFight();


                    ForceFieldHandler.pvpEnabled();
                    cancel();
                }
            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 20L);
        game.taskID.add(task);
    }

    public static boolean checkBotSettings (int botcount) {
        if (Bukkit.getOnlinePlayers().size() != 0) {
            if (config.getBotsEnabled()) {

                // Bukkit.broadcastMessage("started spawning");
                startSpawning(botcount);
                return true;
            }
        }
        return false;

    }

    public static void checkInvalidWorld () {
        World worldhg = Bukkit.getWorld("hungergames");
        if (worldhg != null) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getLocation().getWorld() != worldhg) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Player " + p.getName() +  " is in invalid world");
                    p.teleport(worldhg.getSpawnLocation());
                }
            }
        }
    }

    //change playercount to humans
    public static boolean isEnoughPlayers() {

        if (!config.getBotsCountAsPlayers()) {
            return game.validPlayersOnline().size() >= config.minPlayers();
        } else {
            return game.playerCount() >= config.minPlayers();
        }
    }


    public static void startSpawning (int botcount) {
                Random random = new Random();
                int maxinterval = config.maxIntervalBot();
                int mininterval = config.minIntervalBot();
                int interval = random.nextInt(maxinterval) + mininterval;
                //Bukkit.broadcastMessage("trying to spawning bots");
//                Bukkit.broadcastMessage("new recursion just started spawning bots");
//                Bukkit.broadcastMessage("bot count this time is" + botcount);

                BukkitTask task = new BukkitRunnable() {

                    int bots = botcount;

                    @Override
                    public void run() {
                        if (config.getBotsEnabled()) {

                            if (Bukkit.getOnlinePlayers().size() != 0) {
                                if (game.isStarted()) {
                                    cancel();
                                    return;
                                }
                                if (bots <= 0) {
                                    cancel();
                                    return;
                                }

                                robot.spawnRobot(game.getWorld().getSpawnLocation());
                                //Bukkit.broadcastMessage("just spawned a bot");
                                bots--;
                                startSpawning(bots);
                                cancel();

                            }
                        }
                    }
                }.runTaskTimer(HardcoreGames.getInstance(), interval, interval);
                game.taskID.add(task);

            }



}
