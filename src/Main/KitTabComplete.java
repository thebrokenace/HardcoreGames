package Main;

import Kits.KitTools.KitInfo;
import Util.Game;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class KitTabComplete implements TabCompleter {
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    Game game = Game.getSharedGame();
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (game.isStarted()) {
            return null;
        } else {

            return StringUtil.copyPartialMatches(strings[0], kitInfo.getNamesUnlockedKits((Player) commandSender), new ArrayList<>());


        }

    }
}
