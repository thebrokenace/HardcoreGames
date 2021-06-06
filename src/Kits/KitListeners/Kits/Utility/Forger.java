package Kits.KitListeners.Kits.Utility;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import Util.Sounds;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class Forger implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void breakBlock (BlockBreakEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.FORGER) {
                if (e.getBlock().getType().name().toLowerCase().contains("ore")) {
                    if (e.getPlayer().getInventory().getItemInMainHand().getType().name().toLowerCase().contains("pickaxe")) {
                        Player player = e.getPlayer();

                        if (e.isCancelled()) {
                            // Check if some other plugin cancelled the event, if so, do nothing
                            //Log.logToConsole("Event is already cancelled");
                            return;
                        }

                        Block block = e.getBlock(); // Get the block being broken
                        Material drop = Material.AIR; // The material to drop
                        int dropAmount = 1; // The amount to drop
                        ItemStack hand = e.getPlayer().getInventory().getItemInMainHand(); // Item in the main hand
                        boolean allowAutoSmelt = true; // We'll use this to decide if we want to allow auto-smelting

                        // Check the block being broken, if it isn't gold ore or iron ore, return out
                        switch (e.getBlock().getType()) {
                            case GOLD_ORE:
                                //Log.logToConsole("Block at location is Gold Ore");
                                drop = Material.GOLD_INGOT;
                                break;
                            case IRON_ORE:
                                //Log.logToConsole("Block at location is Iron Ore");
                                drop = Material.IRON_INGOT;
                                break;
                            case ANCIENT_DEBRIS:
                                drop = Material.NETHERITE_SCRAP;
                                break;
                            default:
                                // It isn't gold or iron ore, exit out
                                return;
                        }


                        // Check if the player is holding a pickaxe
                        boolean hasPickaxe = false;
                        switch (hand.getType()) {
                            case DIAMOND_PICKAXE:
                            case GOLDEN_PICKAXE:
                            case IRON_PICKAXE:
                            case STONE_PICKAXE:
                            case WOODEN_PICKAXE:
                            case NETHERITE_PICKAXE:
                                hasPickaxe = true;
                                // Log.debugToConsole("Tool in hand is pickaxe");
                                break;
                            default:
                                //	Log.debugToConsole("Tool in hand is not pickaxe");
                                break;
                        }


                        if (block.getDrops(hand).isEmpty()) {
                            // There are no drops
                            allowAutoSmelt = false;
                        }

                        if (allowAutoSmelt) {

                            // Check if the pickaxe has fortune, if so, get a random amount to get

                            if (hand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                                Random rand = new Random();
                                dropAmount = rand.nextInt(hand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 1) + 1;
                            }

                            if (hand.containsEnchantment(Enchantment.SILK_TOUCH)) {


                                switch (e.getBlock().getType()) {
                                    case GOLD_ORE:
                                        drop = Material.GOLD_ORE;

                                        break;
                                    case IRON_ORE:
                                        drop = Material.IRON_ORE;

                                        break;
                                    case ANCIENT_DEBRIS:
                                        drop = Material.ANCIENT_DEBRIS;

                                        break;
                                    default:
                                        //System.out.println("Some other item");
                                        break;

                                }

                                //return;
                            }

                            // Clear the block
                            block.setType(Material.AIR); // Sets the block to an AIR block

                            // Damage the pickaxe manually as we're cancelling the events, so otherwise
                            // it never takes damage.
                            ItemMeta handMeta = hand.getItemMeta();
                            if (handMeta instanceof Damageable) {
                                Damageable d = (Damageable) handMeta; // Create the damageable (make sure its org.bukkit.inventory.meta.Damageable and not Entity.Damageable)
                                d.setDamage(d.getDamage() + 4); // Set the damage
                                hand.setItemMeta(handMeta); // Set the meta back to the itemstack
                            }

                            boolean dropItem = false; // Default to false

                            block.setType(Material.AIR); // Clear the block


                            // auto-pickup is off, so drop the item
                            dropItem = true;


                            // drop the item
                            block.getWorld().dropItemNaturally(block.getLocation().add(0.2D, 0.2D, 0.2D), new ItemStack(drop, dropAmount)); // Drops the Item
                            Sounds.mineVein(e.getPlayer());

                            e.setCancelled(true); // Cancels the event which stops the block from naturally breaking

                        }

                    }
                }
            }
        }
    }
    }

