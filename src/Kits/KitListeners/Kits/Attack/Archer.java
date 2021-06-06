package Kits.KitListeners.Kits.Attack;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Archer {
    static Game game = Game.getSharedGame();
    static KitInfo kitInfo = KitInfo.getSharedKitInfo();
    public static void regenerateArrows () {
      BukkitTask task =  new BukkitRunnable() {
          @Override
          public void run() {
              for (Player p : Bukkit.getOnlinePlayers()) {
                if (kitInfo.getPlayerKit(p) == Kits.ARCHER) {
                    p.getInventory().addItem(arrows());
                    p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 5f, 1f);
                    p.sendMessage(ChatColor.GREEN + "You just received 2 extra arrows!");
                }
              }
          }
      }.runTaskTimer(HardcoreGames.getInstance(), 60*20L, 60*20L);

      game.taskID.add(task);


    }

    public static ItemStack arrows() {
        ItemStack stack = new ItemStack(Material.ARROW);
        ItemMeta meta = stack.getItemMeta();
        if (meta != null)
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lArcher's Arrows"));
        stack.setItemMeta(meta);
        stack.setAmount(2);
        return stack;
    }

}
