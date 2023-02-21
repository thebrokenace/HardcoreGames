package Kits.KitListeners.Kits.Vanity;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Imposter implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();

//    @EventHandler
//    public void onKill (PlayerInteractEntityEvent e) {
//        //if (game.isStarted()) {
//        if (e.getRightClicked() instanceof Player) {
//                Player killer = e.getPlayer();
//                Player clicked = (Player)e.getRightClicked();
//                if (kitInfo.getPlayerKit(killer) == Kits.IMPOSTER) {
//
//                    try {
//                        // setskin for player skin
//                        if (HardcoreGames.getInstance().getSkinsRestorerAPI().getSkinName(e.getPlayer().getName()) == null || !HardcoreGames.getInstance().getSkinsRestorerAPI().getSkinName(e.getPlayer().getName()).equals(clicked.getName())) {
//                            HardcoreGames.getInstance().getSkinsRestorerAPI().setSkin(killer.getName(), clicked.getName());
//                            // Force skinrefresh for player
//                            HardcoreGames.getInstance().getSkinsRestorerAPI().applySkin(new PlayerWrapper(killer));
//                            e.getPlayer().getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, e.getPlayer().getLocation(), 40);
//                            e.getPlayer().getLocation().getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, e.getPlayer().getLocation(), 40);
//
//                            killer.sendMessage(ChatColor.GREEN + "You have disguised as " + clicked.getName() + "!");
//                        } else {
//                            killer.sendMessage(ChatColor.RED + "You are already disguised as " + clicked.getName() + "!");
//
//                        }
//                    } catch (SkinRequestException ex) {
//                        ex.printStackTrace();
//                    }                }
//
//            // }
//        }
//    }

    @EventHandler
    public void onKill (PlayerInteractEntityEvent e) {
        if (game.isStarted()) {
            if (e.getHand() == EquipmentSlot.HAND) {

                //if (game.isStarted()) {
                if (e.getRightClicked() instanceof Animals) {
                    Player killer = e.getPlayer();
                    if (kitInfo.getPlayerKit(killer) == Kits.IMPOSTER) {

                        Disguise disguise = DisguiseAPI.constructDisguise(e.getRightClicked());
                        if (DisguiseAPI.isDisguised(killer)) {
                            //Bukkit.broadcastMessage(DisguiseAPI.isDisguiseInUse(disguise) + "");
                            if (DisguiseAPI.getDisguise(killer).getDisguiseName().equals(disguise.getDisguiseName())) {
                                killer.sendMessage(ChatColor.RED + "You are already disguised as a " + e.getRightClicked().getName().toLowerCase().substring(0, 1).toUpperCase() + e.getRightClicked().getName().toLowerCase().substring(1) + "!");
                                return;
                            }
                        }
                        DisguiseAPI.setViewDisguiseToggled(killer, true);
                        DisguiseAPI.setActionBarShown(killer, false);
                        DisguiseAPI.disguiseEntity(e.getPlayer(), disguise);
                        disguiseEffect(killer.getLocation());
                        killer.sendMessage(ChatColor.GREEN + "You have disguised as a " + e.getRightClicked().getName().toLowerCase().substring(0, 1).toUpperCase() + e.getRightClicked().getName().toLowerCase().substring(1) + "!");


                    }
                }

                // }


                if (e.getRightClicked() instanceof Player) {
                    Player killer = e.getPlayer();
                    if (kitInfo.getPlayerKit(killer) == Kits.IMPOSTER) {

                        Disguise disguise = DisguiseAPI.constructDisguise(e.getRightClicked());
                        if (DisguiseAPI.isDisguised(killer)) {
                            if (DisguiseAPI.getDisguise(killer).getDisguiseName().equals(disguise.getDisguiseName())) {
                                killer.sendMessage(ChatColor.RED + "You are already disguised as " + e.getRightClicked().getName() + "!");
                                return;
                            }
                        }
                        DisguiseAPI.setViewDisguiseToggled(killer, false);
                        DisguiseAPI.setActionBarShown(killer, false);
                        DisguiseAPI.disguiseEntity(e.getPlayer(), disguise);
                        disguiseEffect(killer.getLocation());
                        killer.sendMessage(ChatColor.GREEN + "You have disguised as " + e.getRightClicked().getName() + "!");


                    }
                }
            }
        }
    }

    //SCP kit, move fast, player look at u, no fast

    @EventHandler
    public void onDamageRemoveDisguise (EntityDamageByEntityEvent e) {
        if (game.isStarted()) {
            if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
                if (kitInfo.getPlayerKit(e.getDamager()) == Kits.IMPOSTER || kitInfo.getPlayerKit(e.getEntity()) == Kits.IMPOSTER) {
                    if (DisguiseAPI.isDisguised(e.getEntity())) {
                        DisguiseAPI.undisguiseToAll(e.getEntity());
                        e.getEntity().sendMessage(ChatColor.RED + "Your disguise was removed!");
                        disguiseEffect(e.getEntity().getLocation());
                    }
                    if (DisguiseAPI.isDisguised(e.getDamager())) {
                        DisguiseAPI.undisguiseToAll(e.getDamager());
                        e.getDamager().sendMessage(ChatColor.RED + "Your disguise was removed!");
                        disguiseEffect(e.getEntity().getLocation());
                    }
                }

            }
        }
    }

//    @EventHandler
//    public void disguise (PlayerInteractEvent e) {
//        if (game.isStarted()) {
//            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.IMPOSTER) {
//                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
//                    if (isHoldingDisguiseKit(e.getPlayer())) {
//
//                        e.getPlayer().openInventory(disguiseKitGUI(e.getPlayer()));
//
//                    }
//                }
//            }
//        }
//    }

    List<Block> ventList = new ArrayList<>();
    @EventHandler
    public void onChunkLoad (ChunkLoadEvent e) {
        if (e.getWorld().equals(game.getWorld())) {
            if (!e.getChunk().getBlock(0, 0, 0).getBiome().name().toLowerCase().contains("ocean") && !e.getChunk().getBlock(0, 0, 0).getBiome().name().toLowerCase().contains("river")) {
                Random random = new Random();
                if (random.nextInt(2) == 0) {
                    if (ventList.size() <= 4000) {
                        Block b = e.getWorld().getHighestBlockAt(e.getChunk().getBlock(0, 0, 0).getLocation());
                        //Bukkit.broadcastMessage("made vent at " + b.getLocation().toString());
                        ventList.add(b);
                    }
                }
            }
        }
    }

    @EventHandler
    public void playerMoveEvent (PlayerMoveEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.IMPOSTER) {
                boolean vent = false;
                Block ventBlock = null;
                for (Block b : getNearbyBlocks(e.getPlayer().getLocation(), 5)) {
                    if (ventList.contains(b)) {
                        vent = true;
                        ventBlock = b;
                        break;
                    }
                }


                if (vent) {
                    if (e.getPlayer().getWorld().equals(game.getWorld())) {
                        e.getPlayer().sendBlockChange(ventBlock.getLocation(), Material.IRON_TRAPDOOR.createBlockData());
                        //Bukkit.broadcastMessage("Found vent");
                        if (e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).equals(ventBlock)) {
                            //Bukkit.broadcastMessage("touched vent block");
                            if (ventList.size() >= 2) {
                                Random rand = new Random();
                                Block randomElement = ventList.get(rand.nextInt(ventList.size()));
                                e.getPlayer().teleport(randomElement.getLocation().clone().add(0, 2, 1));
                                ventList.remove(ventBlock);
                                ventList.remove(randomElement);
                                e.getPlayer().sendBlockChange(ventBlock.getLocation().getWorld().getHighestBlockAt(ventBlock.getLocation()).getLocation(), Material.IRON_BLOCK.createBlockData());

                                e.getPlayer().sendBlockChange(randomElement.getLocation().getWorld().getHighestBlockAt(ventBlock.getLocation()).getLocation(), Material.IRON_BLOCK.createBlockData());
                            }
                        }
                    }
                }




            }
        }
    }
    public static List<Block> getNearbyBlocks(Location location, int radius) {
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
//    @EventHandler
//    public void onClick (InventoryClickEvent e) {
//        if (game.isStarted()) {
//            if (e.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&5&lDisguise Kit (Click to Disguise)"))) {
//               if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
//                   ItemMeta playerHeadMeta = e.getCurrentItem().getItemMeta();
//
//                   SkullMeta headData = (SkullMeta) playerHeadMeta;
//
//                   if (headData != null) {
//                       Player p = Bukkit.getPlayer(headData.getOwningPlayer().getName());
//                       if (HardcoreGames.getInstance().getSkinsRestorerAPI().getSkinName(e.getWhoClicked().getName()) == null || !HardcoreGames.getInstance().getSkinsRestorerAPI().getSkinName(e.getWhoClicked().getName()).equals(p.getName())) {
//                           e.getWhoClicked().closeInventory();
//                           e.getWhoClicked().sendMessage(ChatColor.GREEN + "You have disguised as " + p.getName() + "!");
//                           try {
//                               HardcoreGames.getInstance().getSkinsRestorerAPI().setSkin(e.getWhoClicked().getName(), p.getName());
//                               // Force skinrefresh for player
//                               HardcoreGames.getInstance().getSkinsRestorerAPI().applySkin(new PlayerWrapper(e.getWhoClicked()));
//                               e.getWhoClicked().getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, e.getWhoClicked().getLocation(), 40);
//                               e.getWhoClicked().getLocation().getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, e.getWhoClicked().getLocation(), 40);
//
//                           } catch (SkinRequestException e1) {
//                               e1.printStackTrace();
//                           }
//                           e.setCancelled(true);
//                           return;
//
//                       } else {
//                           e.getWhoClicked().sendMessage(ChatColor.RED + "You are already disguised as " + p.getName() + "!");
//                           e.getWhoClicked().closeInventory();
//                           e.setCancelled(true);
//                           return;
//                       }
//                   }
//               }
//
//               e.setCancelled(true);
//            }
//        }
//    }


//    @EventHandler
//    public void onClick (InventoryClickEvent e) {
//        if (game.isStarted()) {
//            if (e.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&5&lDisguise Kit (Click to Disguise)"))) {
//                if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
//
//
//                    e.setCancelled(true);
//                }
//            }
//        }
//    }
//    public Inventory disguiseKitGUI (Player p) {
//        Inventory kit = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', "&5&lDisguise Kit (Click to Disguise)"));
//
//        List<OfflinePlayer> players = new ArrayList<>();
//        for (UUID uuid : game.humansLeft()) {
//
//                players.add(Bukkit.getOfflinePlayer(uuid));
//
//        }
//
//        for (int i = 0; i < players.size(); i++) {
//            ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
//            ItemMeta playerHeadMeta = stack.getItemMeta();
//
//            if (playerHeadMeta != null) {
//                SkullMeta headData = (SkullMeta) playerHeadMeta;
//
//                headData.setOwningPlayer(players.get(i));
//                if (!(players.get(i) == Bukkit.getOfflinePlayer(p.getUniqueId()))) {
//                    playerHeadMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&l" + headData.getOwningPlayer().getName()));
//
//                    List<String> lore = new ArrayList<>();
//                    lore.add(ChatColor.translateAlternateColorCodes('&', "&6Disguise!"));
//                    playerHeadMeta.setLore(lore);
//                    stack.setItemMeta(playerHeadMeta);
//                } else {
//                    playerHeadMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&l" + "Clear Disguise"));
//
//                    stack.setItemMeta(playerHeadMeta);
//                }
//            }
//
//            kit.setItem(i, stack);
//        }
//        return kit;
//    }
//
//
//    public boolean isHoldingDisguiseKit (Player p) {
//
//        if (p.getInventory().getItemInMainHand().equals(disguiseKit())) {
//            return true;
//        } else {
//            return false;
//        }
//
//    }

    public void disguiseEffect (Location loc) {
        loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 40);
        loc.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 40);
        loc.getWorld().playSound(loc, Sound.BLOCK_FURNACE_FIRE_CRACKLE, 10f, 1f);
    }

    public static ItemStack disguiseKit () {
        ItemStack itemStack = new ItemStack(Material.COMPASS);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5&lDisguise Kit"));
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&a&oRight click this compass to disguise as other players!"));
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
