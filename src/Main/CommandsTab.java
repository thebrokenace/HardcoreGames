package Main;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class CommandsTab implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> string = new ArrayList<>();
        string.add("botstoggle");
        string.add("souptoggle");
        string.add("reload");
        string.add("stats");
        string.add("addkill");
        string.add("addwin");
        string.add("addgame");
        string.add("addkit");
        string.add("disarm");
        string.add("wander");
        string.add("spawnrobot");
        string.add("despawnbots");
        string.add("freekits");
        string.add("soundtest");
        string.add("rewardkit");
        string.add("start");
        string.add("restart");
        string.add("data");
        string.add("kills");
        string.add("minifeast");
        string.add("feast");
        if (strings[0].equalsIgnoreCase("soundtest")) {
            List<String > sounds = new ArrayList<>();
            for (Sound so : Sound.values()) {
                sounds.add(so.name());
            }
            return StringUtil.copyPartialMatches(strings[1], sounds, new ArrayList<>());
        }
        return StringUtil.copyPartialMatches(strings[0], string, new ArrayList<>());
    }
}
