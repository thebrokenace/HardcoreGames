package Kits.KitListeners.Kits.Defense;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import Util.GamePhase;
import Util.Sounds;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class Demoman implements Listener {
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    Game game = Game.getSharedGame();
    @EventHandler
    public void entityMoveBlock (PlayerMoveEvent e) {
        if (game.isStarted() && game.getPhase() != GamePhase.GRACE) {
                Player p = e.getPlayer();
                if (kitInfo.getPlayerKit(p) != Kits.DEMOMAN) {
                    if (e.getTo() != null)
                    if (e.getTo().getBlock().hasMetadata("bomb") && e.getTo().getBlock().getType() == Material.STONE_PRESSURE_PLATE && e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.GRAVEL) {
                        e.getTo().getBlock().removeMetadata("bomb", HardcoreGames.getInstance());

                        if (e.getTo().getWorld() != null)
                        e.getTo().getWorld().createExplosion(e.getTo(), 5, true);
                    }

                }
            }
        }



    @EventHandler
    public void blockPlace (BlockPlaceEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.DEMOMAN) {
                if (e.getBlockPlaced().getType() == Material.STONE_PRESSURE_PLATE) {
                    if (e.getBlockPlaced().getRelative(BlockFace.DOWN).getType() == Material.GRAVEL) {
                        Sounds.demomanRig(e.getPlayer());
                        e.getBlockPlaced().setMetadata("bomb", new FixedMetadataValue(HardcoreGames.getInstance(), true));
                    }
                }
            }
        }
    }

    @EventHandler
    public void damageExplosion (EntityDamageEvent e) {
        if (game.isStarted()) {
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                if (kitInfo.getPlayerKit(p) == Kits.DEMOMAN) {
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {

                        e.setDamage(0.5*e.getDamage());
                    }
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {

                        e.setDamage(0.5*e.getDamage());
                    }
                }
            }
        }
    }

//    @EventHandler
//    public void playerInteract (PlayerInteractEvent e) {
//        if (game.isStarted()) {
//            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.DEMOMAN) {
//                if (e.getAction() == Action.RIGHT_CLICK_AIR) {
//                    if (e.getItem() != null)
//                    if (e.getItem().getType() == Material.STICK) {
//                        for (Block b : getNearbyBlocks(e.getPlayer().getLocation(), 10)) {
//                            if (b.hasMetadata("bomb")  && b.getType() == Material.STONE_PRESSURE_PLATE && b.getRelative(BlockFace.DOWN).getType() == Material.GRAVEL) {
//                                b.removeMetadata("bomb", HardcoreGames.getInstance());
//                                b.getWorld().createExplosion(b.getLocation(), 5, true);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

//    public static List<Block> getNearbyBlocks(Location location, int radius) {
//        List<Block> blocks = new ArrayList<Block>();
//        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
//            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
//                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
//                    if (location.getWorld() != null)
//                    blocks.add(location.getWorld().getBlockAt(x, y, z));
//                }
//            }
//        }
//        return blocks;
//    }
}
