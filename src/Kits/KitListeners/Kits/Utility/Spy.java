package Kits.KitListeners.Kits.Utility;

import Kits.KitListeners.KitUtils.BelowName;
import Kits.KitListeners.KitUtils.Nametag;
import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Listeners.PlayerInteractListener;
import Main.HardcoreGames;
import Messages.Messages;
import Util.Game;
import Util.GamePhase;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;
import org.bukkit.scoreboard.Scoreboard;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Double.MAX_VALUE;

public class Spy  implements Listener {
    Messages messages = new Messages();
    public static HashMap<Player, Entity> trackMap = new HashMap<>();
    static Game game = Game.getSharedGame();
    static KitInfo kitInfo = KitInfo.getSharedKitInfo();

    @EventHandler
    public void onSpyCompass (PlayerInteractEvent e) {
        if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.SPY) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (e.getItem() != null && e.getItem().getType() == Material.COMPASS) {
                    if (e.getItem().getItemMeta() != null) {
                        ItemMeta meta = e.getItem().getItemMeta();
                        if (ChatColor.stripColor(meta.getDisplayName()).equalsIgnoreCase("Spy Compass")) {


                            // if (game.getPhase() == GamePhase.GAMESTARTED) {
                            if (nearestPlayer(e.getPlayer()) != null) {
                                if (trackMap.getOrDefault(e.getPlayer(), null) != nearestPlayer(e.getPlayer())) {
                                    PlayerInteractListener.trackMap.remove(e.getPlayer());
                                    track(e.getPlayer(), nearestPlayer(e.getPlayer()));
                                }

                                List<Entity> nearestThree = nearestThree(e.getPlayer());
                                if(Hacker.getCurrentObfuscated().contains(e.getPlayer())) {
                                    e.getPlayer().sendMessage(messages.obfuscatedSpyTrack(nearestThree, e.getPlayer()));
                                    return;
                                }
                                e.getPlayer().sendMessage(messages.spyTrack(nearestThree, e.getPlayer()));
                                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messages.trackMessage(nearestPlayer(e.getPlayer()))));

                            } else {
                                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(messages.failedTrack()));

                            }
//                        } else {
//                            e.getPlayer().sendMessage(ChatColor.RED + "You cannot use this right now.");
//                        }
                        }
                    }
                }

            }
        }
    }




    public void track (Player tracker, Entity victim) {
        trackMap.put(tracker, victim);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!trackMap.containsKey(tracker)) {
                    cancel();
                }
                if (trackMap.get(tracker) == victim) {
                    tracker.setCompassTarget(victim.getLocation());
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 10L);

    }

    //add being able to see nametags with kits + health of others
//    public void changeNameTag (Player victim, Player spy) {
//        if (kitInfo.getPlayerKit(spy) == Kits.SPY) {
//
//
//        }
//    }

    public static void changeName(Player spy, Player p, String newName){
//        Nametag nametag = new Nametag(p);
//        nametag.setPrefix(newName);
//
//        nametag.build(spy);

        new Nametag(p).setPrefix(newName).build(spy);
        new BelowName(p).build(spy);



    }
    public static void changeNameSuf(Player spy, Player p, String newName){
//        Nametag nametag = new Nametag(p);
//        nametag.setPrefix(newName);
//
//        nametag.build(spy);

        new Nametag(p).setSuffix(newName).build(spy);
        new BelowName(p).build(spy);



    }
    public static void packetSpy (Player spy, Player victim) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = null;
        if (scoreboard.getTeam("spysee" ) == null) {
            team = scoreboard.registerNewTeam("spysee");
        } else {
            team = scoreboard.getTeam("spysee");

        }
        team.addEntry(spy.getName());
        team.addEntry(victim.getName());

        team.setAllowFriendlyFire(true);
        team.setCanSeeFriendlyInvisibles(false);

        final Objective o = scoreboard.registerNewObjective("health", "health", "health");
        o.setDisplayName(ChatColor.RED + "hi");
        o.setDisplaySlot(DisplaySlot.BELOW_NAME);




        //ScoreboardObjective obj = new ScoreboardObjective(o.setRenderType(););

        //ScoreboardObjective obj = scoreboard.registerObjective("health", IScoreboardCriteria.HEALTH, IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + "foot" + "\"}" ), IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);


        //PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective(2, obj);

        //PacketPlayOutScoreboardDisplayObjective display = new PacketPlayOutScoreboardDisplayObjective(2, obj);

        //((CraftPlayer) spy).getHandle().playerConnection.sendPacket(packet);


    }

    public static void spySeeKit () {
        final int[] time = {0};


        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                time[0]++;

                for (Player p : Bukkit.getOnlinePlayers()) {
                        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                        scoreboard.registerNewObjective(p.getName(), p.getName(), ChatColor.RED + "❤", RenderType.INTEGER);
                        Objective obj = scoreboard.getObjective(p.getName());
                        obj.setDisplaySlot(DisplaySlot.BELOW_NAME);





                    if (kitInfo.getPlayerKit(p) == Kits.SPY) {
                        if (game.getPhase() == GamePhase.GAMESTARTED) {
                            for (Player pl : Bukkit.getOnlinePlayers()) {

                                if (pl != p) {
                                    if (game.getPhase() == GamePhase.GAMESTARTED) {
                                        if (p.getLocation().distance(pl.getLocation()) <= 60) {
                                            //packetSpy(p, pl);


                                                changeName(p, pl, "&b" + kitInfo.getKitUnformatted(kitInfo.getPlayerKit(pl)));

                                        } else {

                                            changeName(p, pl, "");

                                        }


                                    }
                                }
                            }
                            for (Player pl : Bukkit.getOnlinePlayers()) {
                                if (pl != p) {

                                    obj.getScore(pl.getName()).setScore(((int) Math.round(pl.getHealth())));
                                }
                            }
                            p.setScoreboard(scoreboard);
                        }

                    }
                }

                if (time[0] > 100) {
                    time[0] = 0;
                }

            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);
        game.taskID.add(task);
    }


//    @EventHandler
//    public void talk (AsyncPlayerChatEvent e) {
//        //if (game.isStarted()) {
//            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.SPY) {
//                for (Player p : Bukkit.getOnlinePlayers()) {
//                    if (p != e.getPlayer()) {
//                    getHealth(e.getPlayer(), p);
//
//
//
//                    }
//                }
//            }
//       // }
//    }
//
//


    public Entity nearestPlayer (Entity p) {

        if (p.getLocation().getWorld() == null ) { return null; }
        List<Entity> candidates = new ArrayList<>();
        for (Entity e : p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 5000, 5000, 5000)) {

            if (e instanceof Player) {
                if ((Player) e != p) {
                    if (!((Player) e).isOp()) {
                        if (((Player) e).getGameMode().equals(GameMode.SURVIVAL)) {
                            if (!e.hasMetadata("standnpc")) {

                                candidates.add((Player) e);
                            }
                        }
                    }
                }
            }

            if (e instanceof NPC) {
                if (!e.hasMetadata("standnpc")) {

                    candidates.add(e);
                }

            }
        }
        double tempDistance = MAX_VALUE;
        Entity closest = null;
        for (Entity near : candidates) {

            double distance = near.getLocation().distance(p.getLocation());
            if (distance < tempDistance) {
                tempDistance = distance;
                closest = near;

            }

        }
        return closest;

    }

    public List<Entity> nearestThree (Player p) {
        List<Entity> players = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (p.getLocation().getWorld() == null) {
                return null;
            }
            List<Player> candidates = new ArrayList<>();
            for (Entity e : p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 5000, 5000, 5000)) {

                if (e instanceof Player) {
                    if ((Player) e != p) {
                        if (!players.contains((Player) e)) {

                                candidates.add((Player) e);

                        }
                    }
                }
            }
            double tempDistance = MAX_VALUE;
            Entity closest = null;
            for (Entity near : candidates) {
                double distance = near.getLocation().distance(p.getLocation());
                if (distance < tempDistance) {
                    tempDistance = distance;
                    closest = near;

                }

            }
            if (closest != null) {
                players.add(closest);
            }
        }

        return players;

    }
    public static  void cancelTrack () {
        trackMap.clear();
    }

}
