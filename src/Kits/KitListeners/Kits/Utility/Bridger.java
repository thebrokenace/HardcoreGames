package Kits.KitListeners.Kits.Utility;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Bridger implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void onThrow (ProjectileLaunchEvent e) {
        if (game.isStarted()) {
            if (e.getEntity().getShooter() instanceof Player) {
                Player shooter = (Player) e.getEntity().getShooter();
                if (kitInfo.getPlayerKit(shooter) == Kits.BRIDGER) {
                    if (e.getEntity() instanceof Egg) {
                        if (isHoldingSuperBridgeEgg(shooter)) {
                            superBridge(e.getEntity());
                        } else {
                            bridge(e.getEntity());
                        }


                    }
                }
            }
        }

    }

    @EventHandler
    public void onHatch (PlayerEggThrowEvent e) {
        if (kitInfo.getPlayerKit(e.getPlayer() ) == Kits.BRIDGER) {
            e.setHatching(false);
        }
    }
    public void superBridge (Entity e) {
        e.setGlowing(true);
        e.setVelocity(e.getVelocity().multiply(2.5));
        BukkitTask task = new BukkitRunnable() {
            int time = 0;
            @Override
            public void run() {
                Block firstDown = e.getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);
                Block left = firstDown.getRelative(BlockFace.EAST);
                Block right = firstDown.getRelative(BlockFace.WEST);
                Block forward = firstDown.getRelative(BlockFace.NORTH);
                Block back = firstDown.getRelative(BlockFace.SOUTH);
                firstDown.setType(Material.PURPLE_WOOL);
                left.setType(Material.PURPLE_WOOL);
                right.setType(Material.PURPLE_WOOL);
                forward.setType(Material.PURPLE_WOOL);
                back.setType(Material.PURPLE_WOOL);



                e.getLocation().getWorld().playSound(e.getLocation(), Sound.ENTITY_EGG_THROW, 10f, 1f);

                if (e.isDead()) {
                    e.remove();
                    cancel();
                }
                if (e.isOnGround()) {
                    e.remove();
                    cancel();
                }
                if (time > 2000) {
                    cancel();
                }
                time++;
            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);
        game.taskID.add(task);
    }

    public void bridge (Entity e) {
        e.setGlowing(true);
        BukkitTask task = new BukkitRunnable() {
            int time = 0;
            @Override
            public void run() {
                Block firstDown = e.getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);

                firstDown.setType(Material.PURPLE_WOOL);



                e.getLocation().getWorld().playSound(e.getLocation(), Sound.ENTITY_EGG_THROW, 10f, 1f);

                if (e.isDead()) {
                    e.remove();
                    cancel();
                }
                if (e.isOnGround()) {
                    e.remove();
                    cancel();
                }
                if (time > 2000) {
                    cancel();
                }
                time++;
            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);
        game.taskID.add(task);
    }

    public Block getLeftBlock(Entity Observer, Block RelativeBlock, boolean right) {
        double Yaw = Math.toRadians(Observer.getLocation().getYaw());

        int lx = (int) ((int) RelativeBlock.getLocation().getX() + (Yaw * 1.0));
        int lz = (int) ((int) RelativeBlock.getLocation().getZ() + (Yaw * 1.0));

        int rx = (int) (RelativeBlock.getLocation().getX() + (Yaw * -1.0));
        int rz = (int) (RelativeBlock.getLocation().getZ() + (Yaw * -1.0));

        int y = (int) RelativeBlock.getLocation().getY();

        Block LeftBlock;
        Block RightBlock;

        LeftBlock = RelativeBlock.getRelative(lx, y, lz);
        RightBlock = RelativeBlock.getRelative(rx, y, rz);

        if (right) {
            return RightBlock;
        } else {
            return LeftBlock;
        }
    }

    public boolean isHoldingSuperBridgeEgg (Player p) {
        if (isSuperBridgeEgg(p.getInventory().getItemInMainHand()) || isSuperBridgeEgg(p.getInventory().getItemInOffHand())) {
            return true;
        }
        return false;
    }
    public boolean isSuperBridgeEgg(ItemStack i) {
        if (i.getType() == Material.EGG) {
            if (i.getItemMeta() != null && ChatColor.stripColor(i.getItemMeta().getDisplayName()).equals("Super Bridge Egg")) {
                return true;
            }
        }
        return false;
    }
    public boolean isHoldingBridgeEgg (Player p) {
        if (isBridgeEgg(p.getInventory().getItemInMainHand()) || isBridgeEgg(p.getInventory().getItemInOffHand())) {
            return true;
        }
        return false;
    }

    public boolean isBridgeEgg(ItemStack i) {
        if (i.getType() == Material.EGG) {
            if (i.getItemMeta() != null && ChatColor.stripColor(i.getItemMeta().getDisplayName()).equals("Bridge Egg")) {
                return true;
            }
        }
        return false;
    }
}
