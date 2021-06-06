package Kits.KitListeners.Kits.Utility;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import Util.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Launcher implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void onPlace (BlockPlaceEvent e) {
        if (game.isStarted()) {
                if (isLaunchpadItem(e.getItemInHand())) {
                    if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.LAUNCHER) {
                        e.getBlockPlaced().setMetadata("launchpad", new FixedMetadataValue(HardcoreGames.getInstance(), true));
                    } else {
                        e.getBlockPlaced().setMetadata("launchpaddrop", new FixedMetadataValue(HardcoreGames.getInstance(), true));

                    }
                }

        }

    }

    @EventHandler
    public void onJump (PlayerMoveEvent e) {
        if (game.isStarted()) {
            if (isLaunchpadBlock(e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN))) {
                Sounds.launcherLauncher(e.getPlayer());
                    //no fall damage
                    e.getPlayer().setVelocity(e.getPlayer().getVelocity().add(new Vector(0,4,0)));
                    new BukkitRunnable() {
                        int time = 0;
                        @Override
                        public void run() {
                            e.getPlayer().setFallDistance(-9999);
                            if (time >= 10*20) {
                                cancel();
                            }
                            if (e.getPlayer().isOnGround()) {
                                cancel();
                            }

                            time++;
                        }
                    }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);

            }
        }
    }

    @EventHandler
    public void onBreak (BlockBreakEvent e) {
        if (game.isStarted()) {
            if (isLaunchpadBlock(e.getBlock()) || e.getBlock().hasMetadata("launchpaddrop")) {
                e.getBlock().removeMetadata("launchpad", HardcoreGames.getInstance());
                e.getBlock().removeMetadata("launchpaddrop", HardcoreGames.getInstance());

                e.setCancelled(true);
                e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), launchpad());
                e.getBlock().setType(Material.AIR);
            }
        }
    }


    public boolean isLaunchpadBlock(Block b) {
        if (b.hasMetadata("launchpad")) {
            if (b.getType() == Material.SPONGE) {
                return true;
            }
        }

        return false;
    }

    public boolean isLaunchpadItem (ItemStack i) {
        if (i.getType() == Material.SPONGE) {
            return i.getItemMeta() != null && ChatColor.stripColor(i.getItemMeta().getDisplayName()).equals("Launchpad");
        }
        return false;
    }
    public ItemStack launchpad () {
        ItemStack stack = new ItemStack(Material.SPONGE);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lLaunchpad"));
        stack.setItemMeta(meta);
        return stack;
    }
}
