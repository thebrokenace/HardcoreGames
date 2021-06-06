package Kits.KitListeners.Kits.Vanity;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import Util.GamePhase;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Gambler implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();

    Map<UUID, Long> cooldownMap = new HashMap<>();

    @EventHandler
    public void onRightClick (PlayerInteractEvent e) {
        if (game.getPhase() == GamePhase.GAMESTARTED) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.GAMBLER) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    // if (e.getClickedBlock() != null && e.getClickedBlock().hasMetadata("gamblerbutton")) {
                    if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.STONE_BUTTON){
                        Player p = e.getPlayer();
                    if (cooldownMap.containsKey(p.getUniqueId())) {
                        if (System.currentTimeMillis() - cooldownMap.get(p.getUniqueId()) >= 10000) {
                            cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                            gamble(p);
//                            e.getClickedBlock().setType(Material.AIR);
//                            ItemStack button = new ItemStack(Material.STONE_BUTTON);
//                            ItemMeta meta = button.getItemMeta();
//                            if (meta != null)
//                            meta.setDisplayName("Gambler Button");
//                            button.setItemMeta(meta);
//                            if (e.getClickedBlock().getLocation().getWorld() != null)
//                             e.getClickedBlock().getLocation().getWorld().dropItemNaturally(e.getClickedBlock().getLocation(), button);
                        } else {
                            p.sendMessage(ChatColor.RED + "You must wait " + (10 - Math.round((System.currentTimeMillis() - ((cooldownMap.get(p.getUniqueId())))) / 1000f)) + " seconds to use this again!");
                        }
                    } else {
                        cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                        gamble(p);
//                        e.getClickedBlock().setType(Material.AIR);
//                        ItemStack button = new ItemStack(Material.STONE_BUTTON);
//                        ItemMeta meta = button.getItemMeta();
//                        if (meta != null)
//                            meta.setDisplayName("Gambler Button");
//                        button.setItemMeta(meta);
//                        if (e.getClickedBlock().getLocation().getWorld() != null)
//                            e.getClickedBlock().getLocation().getWorld().dropItemNaturally(e.getClickedBlock().getLocation(), button);
                    }

                    //  }
                }
                }
            }
        }
    }
//    @EventHandler
//    public void placeBlock (BlockPlaceEvent e) {
//        if (game.isStarted()) {
//                if (e.getItemInHand().getItemMeta() != null && e.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("Gambler Button")) {
//                    e.getBlockPlaced().setMetadata("gamblerbutton", new FixedMetadataValue(HardcoreGames.getInstance(), true));
//                }
//
//        }
//
//    }

//    @EventHandler
//    public void breakButton (BlockBreakEvent e) {
//        if (game.isStarted()) {
//
//            if (e.getBlock().hasMetadata("gamblerbutton")) {
//                 e.getBlock().removeMetadata("gamblerbutton", HardcoreGames.getInstance());
//                ItemStack button = new ItemStack(Material.STONE_BUTTON);
//                ItemMeta meta = button.getItemMeta();
//                if (meta != null)
//                meta.setDisplayName("Gambler Button");
//                button.setItemMeta(meta);
//                if (e.getBlock().getLocation().getWorld() != null)
//                 e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), button);
//                 e.getBlock().setType(Material.AIR);
//                 e.setCancelled(true);
//            }
//
//
//        }
//
//    }

    public void gamble (Player p) {
        //Bukkit.broadcastMessage("ran gamble func");
        Random rand = new Random();
        int bound = 2;
        String message = "";
        if (rand.nextInt(bound) == 1) {
            //good
            Random randGood = new Random();
            int boundGood = 8;
            int random = randGood.nextInt(boundGood);
            message = ChatColor.GREEN + "Good luck! ";
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10f, 1);
                //Bukkit.broadcastMessage("good");
            switch (random) {
                case 0:
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*10, 2));
                    p.sendMessage(message+"You got Speed 2 for 10 seconds!");
                    return;
                case 1:
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*10, 2));
                    p.sendMessage(message+"You got Strength 2 for 10 seconds!");
                    return;
                case 2:
                    p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20*20, 2));
                    p.sendMessage(message+"You got Leaping 2 for 10 seconds!");
                    return;
                case 3:
                    p.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
                    p.sendMessage(message+"You got an Enderpearl!");
                    return;
                case 4:
                    p.getInventory().addItem(new ItemStack(Material.COOKED_CHICKEN));
                    p.getInventory().addItem(new ItemStack(Material.COOKED_CHICKEN));
                    p.getInventory().addItem(new ItemStack(Material.COOKED_CHICKEN));
                    p.getInventory().addItem(new ItemStack(Material.COOKED_CHICKEN));
                    p.sendMessage(message+"You got some fried chicken!");
                    return;
                case 5:
                    p.getInventory().addItem(new ItemStack(Material.MUSHROOM_STEW));
                    p.sendMessage(message + "You got some soup!");
                    return;
                case 6:
                    p.setLevel(p.getLevel()+5);
                    p.sendMessage(message+"You gained some XP!");
                    return;
                case 7:
                    kitInfo.setPlayerKit(p, Surprise.getRandomKit());
                    p.sendMessage(message+"Your kit was rerolled!");
                    p.sendMessage(ChatColor.AQUA + "You are now a " + kitInfo.getKitNameFormatted(kitInfo.getPlayerKit(p), p) + "!");
                    for (ItemStack i : kitInfo.getKitDrops(kitInfo.getPlayerKit(p))) {
                        p.getInventory().addItem(i);
                    }
                    return;
            }

        } else {
            //bad
            //Bukkit.broadcastMessage("bad");

            Random randBad = new Random();
            int boundBad = 9;
            int random = randBad.nextInt(boundBad);
            message = ChatColor.RED + "Bad luck! ";
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_FALL, 10f, 1);
            switch (random) {
                case 0:
                    p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 20*20, 2));
                    p.sendMessage(message + "You got starvation!");
                    return;
                case 1:
                    p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 30*20, 1));
                    p.sendMessage(message+"You got weakness!");
                    return;
                case 2:
                    p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10*20,2));
                    p.sendMessage(message + "You got poison!");
                    return;
                case 3:
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 30*20, 1));
                    p.sendMessage(message+"You got fatigue!");
                    return;
                case 4:
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*20, 1));
                    p.sendMessage(message+"You got slowness!");
                    return;
                case 5:
                    p.setFireTicks(100);
                    p.sendMessage(message+"You were engulfed in fire!");
                    return;
                case 6:
                    p.setLevel(0);
                    p.sendMessage(message+"You lost all of your XP!");
                    return;
                case 7:
                    if (p.getLocation().getWorld() != null)
                    p.getLocation().getWorld().strikeLightning(p.getLocation());
                    p.sendMessage(message+"You were struck by lightning!");
                    return;
                case 8:
                    kitInfo.setPlayerKit(p, Kits.NONE);
                    p.sendMessage(message+"Your kit was removed!");
                    p.sendMessage(ChatColor.AQUA + "You are now a " + kitInfo.getKitNameFormatted(kitInfo.getPlayerKit(p), p) + "!");
//                    for (ItemStack i : kitInfo.getKitDrops(kitInfo.getPlayerKit(p))) {
//                        p.getInventory().addItem(i);
//                    }
                    return;
                }

            }
        }
        //good
        //strength speed leaping luck, heal
        //bad
        //engulfed on fire, spawn mobs, lightning, weakness, instant damage, tnt




}
