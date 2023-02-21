package Kits.KitListeners.Kits.Utility;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import Util.Sounds;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Lumberjack implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();

    @EventHandler
    public void  blockBreakTree (BlockBreakEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.LUMBERJACK) {
                if (e.getPlayer().getInventory().getItemInMainHand().getType().name().toLowerCase().contains("axe")) {
                    if (e.getBlock().getType().name().toLowerCase().contains("log")) {
                        for (Block b : getConnectedblocks(e.getBlock())) {

                                if (b.getLocation().getWorld() != null)
                                    b.getLocation().getWorld().dropItemNaturally(b.getLocation(), new ItemStack(b.getType()));
                                b.setType(Material.AIR);
                            Sounds.lumberBreakTree(e.getPlayer().getLocation());

                        }
                        applyDamage(e.getPlayer().getInventory().getItemInMainHand(), -10, e.getPlayer());

                    }
                }
            }
        }
    }
    public void applyDamage(ItemStack is, int damage, Player p) {
        ItemMeta im = is.getItemMeta();

        if(im instanceof Damageable) {
            Damageable itemdmg = (Damageable) im;
            itemdmg.setDamage(itemdmg.getDamage() - damage);
            is.setItemMeta((ItemMeta) itemdmg);

            if (itemdmg.getDamage() >= is.getType().getMaxDurability()) {
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 10, 1);
                p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
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
            if (b.getType() == block.getType() || b.getType().name().toLowerCase().contains("leaves")) {
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
}
