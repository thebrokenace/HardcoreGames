package Main;

import Messages.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements CommandExecutor {
    Messages messages = new Messages();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {

            Player p = (Player) commandSender;
            if (args.length < 1) {
                p.sendMessage(ChatColor.GREEN + "Usage: /help (feast/kits/soup)");
                return false;
            } else {
                if (args[0].toLowerCase().equals("feast")) {
                    p.sendMessage(messages.help(args[0]));
                    return true;
                }
                if (args[0].toLowerCase().equals("soup")) {
                    p.sendMessage(messages.help(args[0]));

                    return true;
                }
                if (args[0].toLowerCase().equals("kits")) {
                    p.sendMessage(messages.help(args[0]));
                    return true;
                }

                p.sendMessage(ChatColor.RED + "Unknown option! Please type /help feast, /help kits, or /help soup.");
            }
        }
        return false;
    }
}
