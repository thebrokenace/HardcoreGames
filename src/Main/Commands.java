package Main;

import Feast.MainFeast;
import Feast.MiniFeast;
import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Messages.Scoreboard;
import Robots.Robot;
import Util.Game;
import Util.GamePhase;
import Util.Sounds;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Commands implements CommandExecutor {
    Game game = Game.getSharedGame();
    Robot robot = Robot.getSharedRobots();
    PlayerStats stats = PlayerStats.getStats();
    Config config = new Config();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.isOp() || commandSender instanceof ConsoleCommandSender) {
            Player pl = null;
            if (commandSender instanceof Player) {
                pl = (Player) commandSender;
            }
            if (strings[0].toLowerCase().equalsIgnoreCase("scoreboard")) {
                Scoreboard.fastBoard();
            }
            if (strings[0].toLowerCase().equalsIgnoreCase("botstoggle")) {
                config.setBotsStatus(!config.getBotsEnabled());
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Toggled Bots Status to " + config.getBotsEnabled());

                if (pl != null) {
                    pl.sendMessage(ChatColor.GREEN + "Toggled Bots Status to " + config.getBotsEnabled());
                }
            }
            if (strings[0].toLowerCase().equalsIgnoreCase("souptoggle")) {
                config.setSoupStatus(!config.getSoupStats());
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Toggled Soup Status to " + config.getSoupStats());

                if (pl != null) {
                    pl.sendMessage(ChatColor.GREEN + "Toggled Soup Status to " + config.getSoupStats());
                }
            }
            if (strings[0].toLowerCase().equalsIgnoreCase("reload")) {
                config.reload();
                stats.reloadStats();
            }
            if (strings[0].toLowerCase().equalsIgnoreCase("stats")) {
                Bukkit.broadcastMessage(config.graceTimer() + " hi " + config.minPlayers() + " min" + config.preGameTimer() + " sup");
                Bukkit.broadcastMessage(stats.totalKills.toString());

                Bukkit.broadcastMessage(ChatColor.GOLD + pl.getName() + "'s stats:\n");
                Bukkit.broadcastMessage(ChatColor.AQUA + "Total Kills:" + stats.getTotalKills(pl));
                Bukkit.broadcastMessage(ChatColor.AQUA + "Total Games:" + stats.getTotalGamesPlayed(pl));
                Bukkit.broadcastMessage(ChatColor.AQUA + "Total Wins:" + stats.getTotalWins(pl));
                for (Player pla : Bukkit.getOnlinePlayers()) {
                    Bukkit.broadcastMessage(ChatColor.AQUA + "Favorite Kit:" + stats.getMostFrequentlyUsedKit(pla));
                }
            }
            if (strings[0].toLowerCase().equalsIgnoreCase("addkill")) {
                stats.addKill(((Player) commandSender));
                Bukkit.broadcastMessage(stats.totalKills.toString());
            }
            if (strings[0].toLowerCase().equalsIgnoreCase("addwin")) {
                stats.addWinForPlayer(((Player) commandSender));
                Bukkit.broadcastMessage(stats.totalKills.toString());
            }
            if (strings[0].toLowerCase().equalsIgnoreCase("addgame")) {
                stats.addGame(((Player) commandSender));
                Bukkit.broadcastMessage(stats.totalKills.toString());
            }
            if (strings[0].toLowerCase().equalsIgnoreCase("addkit")) {
                Bukkit.broadcastMessage(strings[1]);
                Kits kit = returnKit(strings[1]);
                if (kit != null) {
                    PlayerStats.getStats().addKitToFrequent(pl, kit);
                    Bukkit.broadcastMessage(stats.mostFrequentKit.toString());
                    for (Player pla : Bukkit.getOnlinePlayers()) {
                        Bukkit.broadcastMessage(ChatColor.AQUA + "Favorite Kit:" + stats.getMostFrequentlyUsedKit(pla));
                    }
                }else {
                    Bukkit.broadcastMessage("invald");
                }
            }

            if (strings[0].toLowerCase().equalsIgnoreCase("disarm")) {
                for (NPC npc : robot.activeRobots()) {
                    npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HAND,null);
                }
            }
            if (strings[0].toLowerCase().equalsIgnoreCase("wander")) {
                robot.wander(((Player) commandSender).getLocation());
            }
            if (strings[0].toLowerCase().equalsIgnoreCase("spawnrobot")) {
                robot.spawnRobot(((Player) commandSender).getLocation());
            }
            if (strings[0].toLowerCase().equalsIgnoreCase("despawnbots")) {
                robot.despawnAllBots();
            }
            if (strings[0].toLowerCase().equalsIgnoreCase("freekits")) {
                //Bukkit.broadcastMessage("freekits is toglged " + "to " +                 + "k");
                if (game.allKitsFree()) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Toggled all kits to be unlocked!");
                } else {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Toggled all kits to be locked!");

                }
                }

            if (strings[0].toLowerCase().equalsIgnoreCase("soundtest")) {
                if (returnSound(strings[1].toLowerCase()) != null) {
                    if (strings.length < 3) {
                        Sounds.testSounds(((Player) commandSender), returnSound(strings[1].toLowerCase()));
                    } else {
                        Sounds.testSounds(((Player) commandSender), returnSound(strings[1].toLowerCase()),  Integer.parseInt(strings[2]), Integer.parseInt(strings[3]));

                    }
                }

            }



            if (strings[0].toLowerCase().equalsIgnoreCase("rewardkit")) {
                if (strings.length > 2) {
                    String player = strings[1];
                    String kit = strings[2];

                    UUID uuid = null;
                    Player p = Bukkit.getPlayer(player);
                    OfflinePlayer offlinePlayer = null;
                    if (p == null) {
                        offlinePlayer = Bukkit.getOfflinePlayer(player);
                        if (offlinePlayer.hasPlayedBefore()) {
                            uuid = offlinePlayer.getUniqueId();

                        }
                    } else {
                        uuid = p.getUniqueId();
                    }
//                    if (uuid != null) {
//                        Bukkit.broadcastMessage("uuid not null");
//                    } else {
//                        Bukkit.broadcastMessage("uuid is null");
//                    }




                    if (returnKit(kit) != null && uuid != null) {
                        //manually inputted kit here
                        //Bukkit.broadcastMessage("adding kit");
                        addKitFor24Hours(getUser(uuid), returnKit(kit));

                        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Just gave " + getUser(uuid).getUsername() + " the " + kit + " kit for 24 hours!" );


                    }
                } else {
                    if (strings.length == 2) {
                        //give player random kit for 24 hours
                        String player = strings[1];

                        UUID uuid = null;
                        Player p = Bukkit.getPlayer(player);
                        OfflinePlayer offlinePlayer = null;
                        if (p == null) {
                            offlinePlayer = Bukkit.getOfflinePlayer(player);
                            if (offlinePlayer.hasPlayedBefore()) {
                                uuid = offlinePlayer.getUniqueId();

                            }
                        } else {
                            uuid = p.getUniqueId();
                        }
//                        if (uuid != null) {
//                            Bukkit.broadcastMessage("uuid not null");
//                        } else {
//                            Bukkit.broadcastMessage("uuid is null");
//                        }




                        if (uuid != null) {
                            //manually inputted kit here
                            //Bukkit.broadcastMessage("adding kit");
                            Kits randomkit = randomLockedKit(getUser(uuid));
                            if (randomkit != null) {
                                addKitFor24Hours(getUser(uuid), randomkit);
                                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Just gave " + getUser(uuid).getUsername() + " the " + randomkit + " kit!");
                            } else {
                                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + getUser(uuid).getUsername() + " has unlocked every kit!");

                            }

                        }



                    }
                }

            }


            if (strings[0].toLowerCase().equals("start")) {
                Bukkit.broadcastMessage("Starting new game...");
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Starting new game...");

                game.startGame(game);

            }
            if (strings[0].toLowerCase().equals("restart")) {
                game.restartGame(game);
            }
            if (strings[0].toLowerCase().equals("phase")) {
                if (strings[1].toLowerCase().equals("pregame")) {
                    game.setPhase(GamePhase.PREGAME);
                }
                if (strings[1].toLowerCase().equals("grace")) {
                    game.setPhase(GamePhase.GRACE);
                }
                if (strings[1].toLowerCase().equals("gamestarted")) {
                    game.setPhase(GamePhase.GAMESTARTED);
                }
            }
            if (strings[0].toLowerCase().equals("data")) {
                Bukkit.broadcastMessage(game.getPhase().toString());
                Bukkit.broadcastMessage(game.getKills(((Player) commandSender).getUniqueId()) + " kills");
                Bukkit.broadcastMessage(game.playerCount() + "player count");
                Bukkit.broadcastMessage(game.loserCount() + "loser count");
                Bukkit.broadcastMessage(game.currentPlayersLeft() + "pl");
                Bukkit.broadcastMessage(game.currentPlayerList().toString());
                Bukkit.broadcastMessage(game.currentPlayersLeft() + "curent players left");

            }
            if (strings[0].toLowerCase().equals("kills")) {
                Bukkit.broadcastMessage(game.getKills(((Player) commandSender).getUniqueId()) + "");
            }

            if (strings[0].toLowerCase().equals("minifeast")) {
                MiniFeast.spawnFeast(game.getWorld(), 0);
            }
            if (strings[0].toLowerCase().equals("feast")) {
                MainFeast.spawnFeast(game.getWorld(), 0);
            }

        }
        return false;
    }

    public void addKitFor24Hours(User user, Kits kits) {
        KitInfo kitInfo = KitInfo.getSharedKitInfo();
        //Bukkit.broadcastMessage("added permission " + "hardcoregames." + kitInfo.getKitLowercase(kits));

        Node node = Node.builder("hardcoregames." + kitInfo.getKitLowercase(kits))
                .value(true)
                .expiry(Duration.ofHours(24))
                .build();

                    user.data().add(node);
                   HardcoreGames.getLuckPermsInstance().getUserManager().saveUser(user).thenRun(() -> {
            HardcoreGames.getLuckPermsInstance().getMessagingService().ifPresent(service -> {
                service.pushUserUpdate(user);
            });
        });
        //HardcoreGames.getLuckPermsInstance().runUpdateTask();
    }

    public Kits randomLockedKit (User p) {
        int pick;
        Kits kits = Kits.NONE;
        if (lockedKitsLP(p).size() != 0) {
            while (isKitUnlockedLP(p, kits)) {
                pick = new Random().nextInt(Kits.values().length);
                kits = Kits.values()[pick];
            }
            return kits;

         } else {
            return null;
        }

    }





    public boolean isKitUnlockedLP (User p, Kits kits) {
        if (kits == Kits.NONE) { return true; }
        return p.getCachedData().getPermissionData().checkPermission("hardcoregames." + getKitLowercaseLP(kits)).asBoolean();
    }

    public String getKitLowercaseLP (Kits kits) {
        return kits.toString().toLowerCase();
    }

    public List<Kits> unlockedKitsLP (User p) {
        List<Kits> unlocked = new ArrayList<>();
        for (Kits kits : Kits.values()) {
            if (isKitUnlockedLP(p, kits)) {
                unlocked.add(kits);
            }
        }
        return unlocked;
    }

    public List<Kits> lockedKitsLP (User p) {
        List<Kits> locked = new ArrayList<>();
        for (Kits kits : Kits.values()) {
            if (!isKitUnlockedLP(p, kits)) {
                locked.add(kits);
            }
        }
        return locked;
    }





    public Sound returnSound (String sound) {
        for (Sound sounds : Sound.values()) {
            if (sounds.toString().equalsIgnoreCase(sound)) {
                return sounds;
            }
        }
        return null;
    }



    public Kits returnKit (String kit) {
        for (Kits kits : Kits.values()) {
            if (kits.toString().equalsIgnoreCase(kit)) {
                return kits;
            }
        }
        return null;
    }

    public User getUser(UUID uniqueId) {
        UserManager userManager = HardcoreGames.getLuckPermsInstance().getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(uniqueId);

        return userFuture.join(); // ouch! (block until the User is loaded)
    }
}

