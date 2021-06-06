package Messages;

import Kits.KitTools.KitInfo;
import Main.HardcoreGames;
import Main.PlayerStats;
import Util.Game;
import Util.GamePhase;
import Util.GameTimer;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import sun.plugin2.message.Message;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class Scoreboard implements Listener {

    static Game game = Game.getSharedGame();
    static KitInfo kitInfo = KitInfo.getSharedKitInfo();

    static HashMap<UUID, org.bukkit.scoreboard.Scoreboard> scoreboardHashMap = new HashMap<UUID, org.bukkit.scoreboard.Scoreboard>();

    private static final Map<UUID, FastBoard> boards = new HashMap<>();

    static int elysiumsAnimation = 0;

    public static void fastBoard () {
        final int[] ticks = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                ticks[0]++;

                if (game.getPhase() == GamePhase.PREGAME) {
//                    for (Player p : Bukkit.getOnlinePlayers()) {
//
//                        if (!boards.containsKey(p.getUniqueId())) {
//                            FastBoard board = new FastBoard(p);
//                            boards.put(p.getUniqueId(), board);
//
//
//
//
//                        }
//
//                    }

//                    for (Player p : Bukkit.getOnlinePlayers()) {
//                        FastBoard board = boards.get(p.getUniqueId());
//                        updateBoard(board, ticks[0]);
//
//                    }
                    for (FastBoard board : boards.values()) {
                        updateBoard(board, ticks[0]);
                    }
                } else {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        FastBoard board = boards.remove(player.getUniqueId());

                        if (board != null) {
                            board.delete();
                        }
                    }
                    cancel();
                }

                if (ticks[0] >= 100) {
                    ticks[0] = 0;
                }
            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        FastBoard board = new FastBoard(player);

        //board.updateTitle(ChatColor.RED + "FastBoard");

        boards.put(player.getUniqueId(), board);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        FastBoard board = boards.remove(player.getUniqueId());

        if (board != null) {
            board.delete();
        }
    }

    static int gamesAnimation = 0;
    static int killsAnimation = 0;
    static int winsAnimation = 0;

    static int waitingAnimation = 0;
    static int ipAnimation = 0;


    private static void updateBoard(FastBoard board, int ticks) {

        List<String> gamesLines = new ArrayList<>();
        gamesLines.add("§d§c§lTotal Games");
        gamesLines.add("§d§c§l§f§lT§c§lotal Games");
        gamesLines.add("§d§c§lT§f§lo§c§ltal Games");
        gamesLines.add("§d§c§lTo§f§lt§c§lal Games");
        gamesLines.add("§d§c§lTot§f§la§c§ll Games");
        gamesLines.add("§d§c§lTota§f§ll §c§lGames");
        gamesLines.add("§d§c§lTotal §f§lG§c§lames");
        gamesLines.add("§d§c§lTotal G§f§la§c§lmes");
        gamesLines.add("§d§c§lTotal Ga§f§lm§c§les");
        gamesLines.add("§d§c§lTotal Gam§f§le§c§ls");
        gamesLines.add("§d§c§lTotal Game§f§ls§c§l");

//#FF8181
        List<String> format = new ArrayList<>();
        for (String s : gamesLines) {
            format.add(Messages.format(s.replace("§f", "#FF8181")));
        }
        gamesLines = format;

        if (ticks % 8 == 0) {
            gamesAnimation++;
        }
        if (gamesAnimation >= gamesLines.size()) {
            gamesAnimation = 0;
        }


        List<String> killsLines = new ArrayList<>();
        killsLines.add("§d§d§lTotal Kills");
        killsLines.add("§d§d§l§f§lT§d§lotal Kills");
        killsLines.add("§d§d§lT§f§lo§d§ltal Kills");
        killsLines.add("§d§d§lTo§f§lt§d§lal Kills");
        killsLines.add("§d§d§lTot§f§la§d§ll Kills");
        killsLines.add("§d§d§lTota§f§ll §d§lKills");
        killsLines.add("§d§d§lTotal §f§lK§d§lills");
        killsLines.add("§d§d§lTotal K§f§li§d§llls");
        killsLines.add("§d§d§lTotal Ki§f§ll§d§lls");
        killsLines.add("§d§d§lTotal Kil§f§ll§d§ls");
        killsLines.add("§d§d§lTotal Kill§f§ls§d§l");

        List<String> formatkills = new ArrayList<>();
        for (String s : killsLines) {
            formatkills.add(Messages.format(s.replace("§f", "#FF83FF")));
        }
        killsLines = formatkills;

        if (ticks % 8 == 0) {
            killsAnimation++;
        }
        if (killsAnimation >= killsLines.size()) {
            killsAnimation = 0;
        }


        //8AFFFF

        List<String> winsLines = new ArrayList<>();
        winsLines.add("§b§b§lTotal Wins");
        winsLines.add("§b§b§l§f§lT§b§lotal Wins");
        winsLines.add("§b§b§lT§f§lo§b§ltal Wins");
        winsLines.add("§b§b§lTo§f§lt§b§lal Wins");
        winsLines.add("§b§b§lTot§f§la§b§ll Wins");
        winsLines.add("§b§b§lTota§f§ll §b§lWins");
        winsLines.add("§b§b§lTotal §f§lW§b§lins");
        winsLines.add("§b§b§lTotal W§f§li§b§lns");
        winsLines.add("§b§b§lTotal Wi§f§ln§b§ls");
        winsLines.add("§b§b§lTotal Win§f§ls§b§l");

        List<String> formatwins = new ArrayList<>();
        for (String s : winsLines) {
            formatwins.add(Messages.format(s.replace("§f", "#C9FFFF")));
        }
        winsLines = formatwins;

        if (ticks % 8 == 0) {
            winsAnimation++;
        }
        if (winsAnimation >= winsLines.size()) {
            winsAnimation = 0;
        }


//
        List<String> waitingLines = new ArrayList<>();
        waitingLines.add("Waiting");
        waitingLines.add("Waiting.");
        waitingLines.add("Waiting..");
        waitingLines.add("Waiting...");


        if (ticks % 20 == 0) {
            waitingAnimation++;
        }
        if (waitingAnimation >= waitingLines.size()) {
            waitingAnimation = 0;
        }

        List<String> ipLines = new ArrayList<>();
        ipLines.add("§1§6§lhg.elysiums.net");
        ipLines.add("§1§6§e§lh§6§lg.elysiums.net");
        ipLines.add("§1§6§lh§e§lg§6§l.elysiums.net");
        ipLines.add("§1§6§lhg§e§l.§6§lelysiums.net");
        ipLines.add("§1§6§lhg.§e§le§6§llysiums.net");
        ipLines.add("§1§6§lhg.e§e§ll§6§lysiums.net");
        ipLines.add("§1§6§lhg.el§e§ly§6§lsiums.net");
        ipLines.add("§1§6§lhg.ely§e§ls§6§liums.net");
        ipLines.add("§1§6§lhg.elys§e§li§6§lums.net");
        ipLines.add("§1§6§lhg.elysi§e§lu§6§lms.net");
        ipLines.add("§1§6§lhg.elysiu§e§lm§6§ls.net");
        ipLines.add("§1§6§lhg.elysium§e§ls§6§l.net");
        ipLines.add("§1§6§lhg.elysiums§e§l.§6§lnet");
        ipLines.add("§1§6§lhg.elysiums.§e§ln§6§let");
        ipLines.add("§1§6§lhg.elysiums.n§e§le§6§lt");
        ipLines.add("§1§6§lhg.elysiums.ne§e§lt§6");





        if (ticks % 5 == 0) {
            ipAnimation++;
        }
        if (ipAnimation >= ipLines.size()) {
            ipAnimation = 0;
        }






        List<String> elysiumsLines = Messages.elysiumsTagAnimation();


        if (ticks % 4 == 0) {
            elysiumsAnimation++;
        }
        if (elysiumsAnimation >= elysiumsLines.size()) {
            elysiumsAnimation = 0;
        }

        if (!board.isDeleted()) {
            board.updateTitle(Messages.format(elysiumsLines.get(elysiumsAnimation) + " &e&lHunger Games"));
        }

        String time = "";
        if (GameTimer.time.equals("") || GameTimer.time.equals("0")) {
            time = waitingLines.get(waitingAnimation);
        } else {

            time = secondsToString(Integer.parseInt(GameTimer.time));
        }



        Collection<String> strings = new ArrayList<>();
        strings.add("");
        strings.add(gamesLines.get(gamesAnimation));
        strings.add("§c§c • §7" + PlayerStats.getStats().getTotalGamesPlayed(board.getPlayer()));
        strings.add("");
        strings.add(killsLines.get(killsAnimation));
        strings.add("§9§d • §7" + PlayerStats.getStats().getTotalKills(board.getPlayer()));
        strings.add("");
        strings.add(winsLines.get(winsAnimation));
        strings.add("§6§b • §7"+ PlayerStats.getStats().getTotalWins(board.getPlayer()));
        strings.add("");
        strings.add("§3§f§lPlayers: " + Bukkit.getOnlinePlayers().size() + "/" + "16");
        strings.add("");
        strings.add("§5§a§lStarting in: §f" + time);
        strings.add("");
        strings.add(ipLines.get(ipAnimation));


        if (!board.isDeleted()) {
            board.updateLines(strings);
        }
    }
//    @EventHandler
//    public void onJoin (PlayerJoinEvent e) {
//        Player p = e.getPlayer();
//        if (!boards.containsKey(e.getPlayer().getUniqueId())) {
//            FastBoard board = new FastBoard(p);
//            boards.put(p.getUniqueId(), board);
//        } else {
//            FastBoard board = new FastBoard(p);
//            boards.put(p.getUniqueId(), board);
//        }
//    }
//    @EventHandler
//    public void onJoin (PlayerQuitEvent e) {
//        Player p = e.getPlayer();
//        if (boards.containsKey(e.getPlayer().getUniqueId())) {
//            FastBoard board = boards.get(p.getUniqueId());
//            board.delete();
//        }
//    }
    private static String secondsToString(int pTime) {
        return String.format("%02d:%02d", pTime / 60, pTime % 60);
    }
}
