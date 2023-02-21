package Kits.KitListeners.Kits.Utility;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Portal implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();

    HashMap<UUID, Integer> portalsPlaced = new HashMap<UUID, Integer>();
    HashMap<UUID, List<Block>> placedPortals = new HashMap<UUID, List<Block>>();
    HashMap<UUID, Long> portalCooldown = new HashMap<>();
    @EventHandler
    public void onPlace (BlockPlaceEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.PORTAL) {
                if (isHoldingPortal(e.getPlayer())) {
                    if (portalsPlaced.getOrDefault(e.getPlayer().getUniqueId(), 0) < 2) {
                        placePortal(e.getBlockPlaced().getLocation(), e.getPlayer().getUniqueId());
                        e.getItemInHand().setAmount(e.getItemInHand().getAmount()-1);
                        portalsPlaced.put(e.getPlayer().getUniqueId(), portalsPlaced.getOrDefault(e.getPlayer().getUniqueId(), 0) + 1);
                    } else {
                        e.getPlayer().sendMessage(ChatColor.RED + "You have already placed two portals!");
                        e.setCancelled(true);
                    }

                }
            }
        }

    }

    public void placePortal (Location location, UUID uuid) {
        location.getBlock().setType(Material.AIR);
        location.getBlock().setType(Material.END_PORTAL);
        List<Block> defaultPortals = new ArrayList<>();
        List<Block> portals = placedPortals.getOrDefault(uuid, defaultPortals);
        portals.add(location.getBlock());
        placedPortals.put(uuid, portals);
        location.getBlock().setMetadata("portal", new FixedMetadataValue(HardcoreGames.getInstance(), uuid));

    }

    @EventHandler
    public void onPortalClick (PlayerInteractEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.PORTAL) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (e.getClickedBlock() != null) {
                        if (isPortalBlock(e.getClickedBlock())) {
                            if (e.getClickedBlock().getMetadata("portal").get(0).value() == e.getPlayer().getUniqueId()) {
                                e.getClickedBlock().setType(Material.AIR);
                                e.getClickedBlock().removeMetadata("portal", HardcoreGames.getInstance());
                                e.getClickedBlock().getLocation().getWorld().dropItemNaturally(e.getClickedBlock().getLocation(), portalBlock());
                                portalsPlaced.put(e.getPlayer().getUniqueId(), portalsPlaced.getOrDefault(e.getPlayer().getUniqueId(),0)-1);
                                List<Block> blocks = placedPortals.get(e.getPlayer().getUniqueId());
                                blocks.remove(e.getClickedBlock());
                                placedPortals.put(e.getPlayer().getUniqueId(), blocks);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onGoPortal (PlayerMoveEvent e) {
        if (game.isStarted()) {
            if (isPortalBlock(e.getPlayer().getLocation().getBlock())) {
                if (portalCooldown.containsKey(e.getPlayer().getUniqueId())) {

                    if (System.currentTimeMillis() - portalCooldown.get(e.getPlayer().getUniqueId()) >= 5000) {
                        UUID uuid = (UUID) e.getPlayer().getLocation().getBlock().getMetadata("portal").get(0).value();
                        if (getOtherValue(placedPortals.get(uuid), e.getPlayer().getLocation().getBlock()) != null) {
                            Location otherLocation = getOtherValue(placedPortals.get(uuid), e.getPlayer().getLocation().getBlock()).getLocation();
                            otherLocation.add(1, 0, 1);
                            e.getPlayer().teleport(otherLocation);
                            e.getPlayer().playEffect(e.getPlayer().getLocation(), Effect.ENDER_SIGNAL, 10);
                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10f, 1f);

                        } else {
                            if (getOtherValue(placedPortals.get(uuid), e.getPlayer().getLocation().getBlock()) == null) {

                                e.getPlayer().sendMessage(ChatColor.RED + "This Portal doesn't have an exit!");
                            }
                        }
                    } else {
                        e.getPlayer().sendMessage(ChatColor.RED + "Please wait " + (5 - Math.round((System.currentTimeMillis() - ((portalCooldown.get(e.getPlayer().getUniqueId()))))/1000f)) + " seconds before re-entering this portal.");
                    }
                } else {
                    portalCooldown.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
                    UUID uuid = (UUID) e.getPlayer().getLocation().getBlock().getMetadata("portal").get(0).value();
                    if (getOtherValue(placedPortals.get(uuid), e.getPlayer().getLocation().getBlock()) != null) {
                        Location otherLocation = getOtherValue(placedPortals.get(uuid), e.getPlayer().getLocation().getBlock()).getLocation();
                        otherLocation.add(5, 0, 0);
                        e.getPlayer().teleport(otherLocation);
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10f, 1f);
                    } else {
                        e.getPlayer().sendMessage(ChatColor.RED + "This Portal doesn't have an exit!");
                    }


                }


            }
        }
    }

    public Block getOtherValue (List<Block> b, Block be) {
        Block blo = null;
        if (b.size() != 2) {
            return null;
        }
        for (Block bl : b) {
            if (!bl.equals(be)) {
                blo = bl;
            }
        }
        return blo;
    }

    public boolean isHoldingPortal (Player p) {
        if (isPortalItemStack(p.getInventory().getItemInMainHand())) {
            return true;
        }
        return false;
    }
    public boolean isPortalItemStack (ItemStack i) {
        if (i.getItemMeta() != null && ChatColor.stripColor(i.getItemMeta().getDisplayName()).equals("Portal Block")) {
            return true;
        } else {
            return false;
        }

    }
    public boolean isPortalBlock (Block b) {
        if (b.hasMetadata("portal")) {
            return true;
        }
        return false;
    }

    public ItemStack portalBlock () {
        ItemStack stack = new ItemStack(Material.PURPLE_WOOL);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5&lPortal Block"));
        stack.setItemMeta(meta);
        return glowItem(stack);
    }

    public ItemStack glowItem(ItemStack item) {
        ItemMeta itemStackMeta = item.getItemMeta();
        itemStackMeta.addEnchant(Enchantment.LURE, 0, true);
        itemStackMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        item.setItemMeta(itemStackMeta);
        return item;
    }
}
