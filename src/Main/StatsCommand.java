package Main;

import Messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {
    PlayerStats stats = PlayerStats.getStats();
    Messages messages = new Messages();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {

            Player p = (Player) commandSender;
            if (args.length < 1) {
                p.sendMessage(messages.statsMessageCommand(p));
                return false;
            } else {
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target != null) {
                    p.sendMessage(messages.statsMessageCommand(target));
                    return true;
                } else {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (offlinePlayer.hasPlayedBefore()) {
                        p.sendMessage(messages.statsMessageCommand(offlinePlayer));
                        return true;
                    } else {

                        p.sendMessage(ChatColor.RED + "Invalid player!");
                        return false;
                    }
                }

            }
        }
        return false;
    }
}
