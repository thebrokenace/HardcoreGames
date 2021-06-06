package Main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class HelpTabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> options = new ArrayList<>();
        options.add("feast");
        options.add("soup");
        options.add("kits");

        return StringUtil.copyPartialMatches(strings[0], options, new ArrayList<>());
    }
}
