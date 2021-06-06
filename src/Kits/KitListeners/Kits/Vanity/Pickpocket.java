package Kits.KitListeners.Kits.Vanity;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import Util.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class Pickpocket implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    HashMap<UUID, Long> cooldownMap = new HashMap<UUID, Long>();

        @EventHandler
        public void onHit (EntityDamageByEntityEvent e) {
            if (!e.isCancelled()) {
                if (game.isStarted()) {
                    if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
                        Player pickpocket = (Player) e.getDamager();
                        Player hit = (Player) e.getEntity();
                        if (kitInfo.getPlayerKit(pickpocket) == Kits.PICKPOCKET) {
                            if (pickpocket.getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD) {
                                if (pickpocket.getInventory().getItemInMainHand().getItemMeta() != null && ChatColor.stripColor(pickpocket.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Pickpocket Stick")) {
                                    if (cooldownMap.containsKey(pickpocket.getUniqueId())) {
                                        if (System.currentTimeMillis() - cooldownMap.get(pickpocket.getUniqueId()) >= 20000) {
                                            cooldownMap.put(pickpocket.getUniqueId(), System.currentTimeMillis());
                                            pickpocketRun(hit);
                                            Sounds.pickpocket(pickpocket);
                                            Sounds.pickpocket(hit);
                                        } else {
                                            pickpocket.sendMessage(ChatColor.RED + "You must wait " + (20 - Math.round((System.currentTimeMillis() - ((cooldownMap.get(pickpocket.getUniqueId()))))/1000f)) + " seconds to use this again!");
                                        }
                                    } else {
                                        cooldownMap.put(pickpocket.getUniqueId(), System.currentTimeMillis());
                                        pickpocketRun(hit);
                                        Sounds.pickpocket(pickpocket);
                                        Sounds.pickpocket(hit);

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        public void pickpocketRun (Player hit) {


            ItemStack itemInHand = hit.getInventory().getItemInMainHand();
            Random random = new Random();
            int R = random.nextInt(27) + 9;
            ItemStack replaceItem = hit.getInventory().getItem(R);

            boolean clearinv = true;
            for (int i = 9; i < 36; i++) {
                ItemStack currentItem = hit.getInventory().getItem(i);
                if (currentItem != null) {
                    clearinv = false;
                    break;
                }
            }
            if (!clearinv) {
                while (replaceItem == null) {
                    R = random.nextInt(27) + 9;
                    replaceItem = hit.getInventory().getItem(R);


                }

                //now replaceitem isnt null
                hit.getInventory().setItem(R, itemInHand);
                hit.getInventory().setItemInMainHand(replaceItem);

            } else {
                //inv is clear
                R = random.nextInt(27) + 9;
                //pick random slot
                hit.getInventory().setItem(R, itemInHand);
                hit.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            }

        }



}
