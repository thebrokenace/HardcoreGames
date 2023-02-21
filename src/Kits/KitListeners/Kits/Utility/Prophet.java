package Kits.KitListeners.Kits.Utility;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Prophet implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void onMove (PlayerMoveEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.PROPHET) {
                Block block = e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
                Block block1 = e.getPlayer().getLocation().getBlock();
                if (e.getPlayer().isSwimming()) { return; }
                if (block1.getType() == Material.WATER)  { return; }
                if (block.getType() == Material.WATER || block.getType() == Material.KELP_PLANT || block.getType() == Material.KELP) {
                    Material old = block.getType();
                    Material glass = Material.BLUE_STAINED_GLASS;

                    //e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 200, 2));
                    //e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 1));
                    e.getPlayer().sendBlockChange(block.getLocation(), glass.createBlockData());
                    Bukkit.getServer().getScheduler().runTaskLater(HardcoreGames.getInstance(), () ->  e.getPlayer().sendBlockChange(block.getLocation(), old.createBlockData()), 200);

                }
            }
        }
    }
}
