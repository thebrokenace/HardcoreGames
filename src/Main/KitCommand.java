package Main;

import KitGUI.InventoryGUI;
import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Messages.Messages;
import Util.Game;
import Util.GamePhase;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.MAX_VALUE;

public class KitCommand implements CommandExecutor {
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    Game game = Game.getSharedGame();
    Messages messages = new Messages();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!((Player) commandSender).getGameMode().equals(GameMode.SPECTATOR)) {
            if (!game.isStarted() && game.getPhase() != GamePhase.WINNERDECIDED) {
                if (strings.length == 0) {
                    Player p = (Player) commandSender;
                    p.openInventory(InventoryGUI.playerUnlockedKits(p));
                    return false;
                }
                String kit = strings[0].toUpperCase();
                Player p = (Player) commandSender;
                if (returnKit(kit) != null) {
                    Kits kits = returnKit(kit);
                    if (kitInfo.isKitUnlocked(p, kits)) {
                        kitInfo.setPlayerKit(p, kits);
                        kit = kits.name().toLowerCase();
                        p.sendMessage(ChatColor.GREEN + "Selected the " + kit.substring(0, 1).toUpperCase() + kit.substring(1) + " kit!");
                    } else {
                        p.sendMessage(ChatColor.RED + "You have not unlocked this kit yet!");
                    }
                } else {

                    p.sendMessage(ChatColor.RED + "Not a valid kit!");
                }

                return false;
            } else {
                Player p = (Player) commandSender;
                p.sendMessage(ChatColor.AQUA + "You are currently a " + kitInfo.getKitNameFormatted(kitInfo.getPlayerKit(p), p) + "!");
                for (String se : KitInfo.kitDescription(kitInfo.getPlayerKit(p))) {
                p.sendMessage(ChatColor.GRAY + se);
                p.sendMessage(ChatColor.AQUA + "How to Use");
                p.sendMessage(ChatColor.GRAY + KitInfo.kitsHelp(kitInfo.getPlayerKit(p)).get(0));
                }
                return false;
            }
        }
        return false;
    }


    public Kits returnKit (String kit) {
        for (Kits kits : Kits.values()) {
            if (kits.toString().equalsIgnoreCase(kit)) {
                return kits;
            }
        }

        int distance = MAX_VALUE;
        String bestMatch = null;
        List<String> kitNames = new ArrayList<>();
        for (Kits see : Kits.values()) {
            kitNames.add(see.name());
        }
        for (String se : kitNames) {
            if (StringUtils.getLevenshteinDistance(se, kit) < distance) {
                bestMatch = se;
                distance = (StringUtils.getLevenshteinDistance(se, kit));
            }
        }
        if (distance >= 4) {
            return null;
        } else {
            return Kits.valueOf(bestMatch);
        }
    }
}
