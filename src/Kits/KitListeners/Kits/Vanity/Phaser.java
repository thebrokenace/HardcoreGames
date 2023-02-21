package Kits.KitListeners.Kits.Vanity;

import Kits.KitTools.KitInfo;
import Main.HardcoreGames;
import Util.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Phaser implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    List<Block> blocks = new ArrayList<>();
    @EventHandler
    public void onInteractPhaser (PlayerInteractEvent e) {

    }

    @EventHandler
    public void moveEvent (PlayerMoveEvent e) {
        if (game.isStarted()) {
           // if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.PHASER) {

                for (Block b : getNearbyBlocks(e.getPlayer().getLocation(), 1)) {
                    if (b.getLocation().getY() >= e.getPlayer().getLocation().getY()) {
                        if (!blocks.contains(b)) {
                            convertBlock(b, e.getPlayer());
                        }

                    }
                }
            }
       // }
    }
    public List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    if (location.getWorld().getBlockAt(x, y, z).getType().isSolid()) {
                        blocks.add(location.getWorld().getBlockAt(x, y, z));
                    }
                }
            }
        }
        return blocks;
    }
    public void convertBlock(Block b, Player p) {
        blocks.add(b);
        Material mat = b.getType();
        if (b.getLocation().getWorld() != null) {

            FallingBlock fallingBlock = b.getLocation().getWorld().spawnFallingBlock(b.getLocation().clone().add(0.5, 0, 0.5), b.getBlockData());
            fallingBlock.setInvulnerable(true);
            fallingBlock.setGravity(false);
            fallingBlock.setDropItem(false);
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl != p) {
//                    PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(fallingBlock.getEntityId());
//                    ((CraftPlayer) pl).getHandle().playerConnection.sendPacket(packet);
                }
            }

            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl != p) {
                    pl.sendBlockChange(b.getLocation(), mat.createBlockData());

                }
            }


            //b.setType(Material.AIR);


            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl != p) {
                    Bukkit.broadcastMessage("send packet to " + pl.getName() + " at " + b.getLocation().toString() + " to material" + mat.toString());
                    pl.sendBlockChange(b.getLocation(), Material.STONE.createBlockData());
                }
            }


            new BukkitRunnable() {
                int time = 0;

                @Override
                public void run() {
                    if (time > 3) {
                        fallingBlock.remove();
                        b.setType(mat);
                        blocks.remove(b);

                        cancel();
                    }
                    time++;
                }
            }.runTaskTimer(HardcoreGames.getInstance(), 0L, 20L);

        }
    }
}
