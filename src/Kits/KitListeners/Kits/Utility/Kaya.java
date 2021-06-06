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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Kaya implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void onMove (PlayerMoveEvent e) {
        if (game.isStarted()) {
            Player p = e.getPlayer();
            if (kitInfo.getPlayerKit(p) != Kits.KAYA) {
                if (e.getTo() != null)
                    if (e.getTo().getBlock().getRelative(BlockFace.DOWN).hasMetadata("kaya") && e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.GRASS_BLOCK) {
                       // Bukkit.broadcastMessage("walked on kaya block");
                        e.getTo().getBlock().getRelative(BlockFace.DOWN).removeMetadata("kaya", HardcoreGames.getInstance());
                        Block kaya = e.getTo().getBlock().getRelative(BlockFace.DOWN);
                        for (Block b : getConnectedblocks(kaya)) {
                            b.removeMetadata("kaya", HardcoreGames.getInstance());
                            b.setType(Material.AIR);
                        }


                        if (e.getTo().getWorld() != null)
                            e.getTo().getWorld().getBlockAt(e.getTo()).getRelative(BlockFace.DOWN).setType(Material.AIR);
                    }

            }
        }
    }

    @EventHandler
    public void blockPlace (BlockPlaceEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.KAYA) {
                if (e.getBlockPlaced().getType() == Material.GRASS_BLOCK) {
                    if (e.getItemInHand().getItemMeta() != null && ChatColor.stripColor(e.getItemInHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Kaya Grass Block"))
                        e.getBlockPlaced().setMetadata("kaya", new FixedMetadataValue(HardcoreGames.getInstance(), true));
                        Sounds.kayaTrap(e.getPlayer());
                }
            }
        }
    }
    private void getConnectedblocks(Block block, Set<Block> results, List<Block> todo) {
        //Here I collect all blocks that are directly connected to variable 'block'.
        //(Shouldn't be more than 6, because a block has 6 sides)

        //Loop through all block faces (All 6 sides around the block)
        for (BlockFace face : faces) {
            Block b = block.getRelative(face);
            //Check if they're both of the same type
            if (b.hasMetadata("kaya")) {
                //Add the block if it wasn't added already
                if (results.add(b)) {

                    //Add this block to the list of blocks that are yet to be done.
                    todo.add(b);
                }
            }
        }
    }
    public Set<Block> getConnectedblocks(Block block) {
        Set<Block> set = new HashSet<>();
        LinkedList<Block> list = new LinkedList<>();

        //Add the current block to the list of blocks that are yet to be done
        list.add(block);

        //Execute this method for each block in the 'todo' list
        while((block = list.poll()) != null) {
            getConnectedblocks(block, set, list);
        }
        return set;
    }
    private static final BlockFace[] faces = {
            BlockFace.DOWN,
            BlockFace.UP,
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    };
    @EventHandler
    public void blockBreak  (BlockBreakEvent e) {
        if (game.isStarted()) {
            if (e.getBlock().hasMetadata("kaya")) {
                e.getBlock().removeMetadata("kaya", HardcoreGames.getInstance());
            }
        }
    }
}
