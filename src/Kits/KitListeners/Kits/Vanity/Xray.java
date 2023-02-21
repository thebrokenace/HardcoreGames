package Kits.KitListeners.Kits.Vanity;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public class Xray implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    List<Block> currentlyXrayed = new ArrayList<>();
    @EventHandler
    public void onInteract (ProjectileLaunchEvent e) {
        if (game.isStarted()) {
            if (e.getEntity().getShooter() instanceof Player) {

                if (kitInfo.getPlayerKit((Player) e.getEntity().getShooter()) == Kits.XRAY) {
                    Player xray = (Player) e.getEntity().getShooter();
                    if (e.getEntity() instanceof Egg) {
                        xray.getInventory().getItemInMainHand();
                        if (xray.getInventory().getItemInMainHand().getItemMeta() != null && ChatColor.stripColor(xray.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Xray Egg")) {
                            e.getEntity().setMetadata("xray", new FixedMetadataValue(HardcoreGames.getInstance(), true));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onhit (ProjectileHitEvent e) {
        if (e.getHitBlock() != null) {
            if (game.isStarted()) {
                if (e.getEntity().getShooter() instanceof Player) {
                    Player xray = (Player) e.getEntity().getShooter();
                    if (kitInfo.getPlayerKit(xray) == Kits.XRAY) {
                        if (e.getEntity().hasMetadata("xray")) {
                            if (e.getEntity() instanceof Egg) {

                                Block origin = e.getHitBlock();
                                List<Block> oldBlock = new ArrayList<>();
                                for (Block b : (getNearbyBlocks(origin.getLocation(), 5))) {
                                    if (!b.getType().name().toLowerCase().contains("ore") && !b.isLiquid() && !(b.getType() == Material.AIR) && b.getType().isSolid()) {
                                        oldBlock.add(b);
                                    }
                                }
                                for (Block b : oldBlock) {
                                    Bukkit.getServer().getScheduler().runTaskLater(HardcoreGames.getInstance(), () -> xray.sendBlockChange(b.getLocation(), b.getType().createBlockData()), 600);

                                }
                                for (Block b : getNearbyBlocks(origin.getLocation(), 5)) {
                                    if (!b.getType().name().toLowerCase().contains("ore") && !b.isLiquid() && !(b.getType() == Material.AIR) && b.getType().isSolid()) {

                                        xray.sendBlockChange(b.getLocation(), Material.GLASS.createBlockData());
                                    }

                                }


                            }
                        }
                    }


                }
            }
        }
    }
    public List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }
}
