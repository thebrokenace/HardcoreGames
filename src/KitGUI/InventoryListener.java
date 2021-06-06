package KitGUI;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void inventoryClick (InventoryClickEvent e) {
        if (e.getView().getTitle().equals(InventoryGUI.unlockedKitsName)) {
            ItemStack item = e.getCurrentItem();
            if (item != null) {
                if (item.getItemMeta() != null) {
                    Player p = (Player) e.getWhoClicked();
                    String kit = ChatColor.stripColor(item.getItemMeta().getDisplayName().toUpperCase());
                    Kits kits = returnKit(kit);

                    if (kits != null) {
                        kitInfo.setPlayerKit(p, kits);
                        p.openInventory(InventoryGUI.playerUnlockedKits(p));
                        p.sendMessage(ChatColor.GREEN + "Selected the " + kitInfo.getKitUnformatted(kits) + " kit!");
                        Sounds.playMenuClick(p);
                    } else {
                        if (kit.equalsIgnoreCase("Next Page")) {
                         //   Bukkit.broadcastMessage("Clicked next page");
                            p.openInventory(InventoryGUI.playerUnlockedKits2(p));
                            Sounds.nextPage(p);

                        }
                        if (kit.equalsIgnoreCase("Show Locked Kits")) {
                         //   Bukkit.broadcastMessage("Clicked locked kits");
                            p.openInventory(InventoryGUI.playerLockedKits(p));
                            Sounds.playMenuClick(p);


                        }
                    }
                }
            }



        }
        if (e.getView().getTitle().equals(InventoryGUI.unlockedKitsName2)) {
            ItemStack item = e.getCurrentItem();
            if (item != null) {
                if (item.getItemMeta() != null) {
                    Player p = (Player) e.getWhoClicked();
                    String kit = ChatColor.stripColor(item.getItemMeta().getDisplayName().toUpperCase());
                    Kits kits = returnKit(kit);

                    if (kits != null) {
                        kitInfo.setPlayerKit(p, kits);
                        p.openInventory(InventoryGUI.playerUnlockedKits2(p));
                        p.sendMessage(ChatColor.GREEN + "Selected the " + kitInfo.getKitUnformatted(kits) + " kit!");
                        Sounds.playMenuClick(p);

                    } else {
                        if (kit.equalsIgnoreCase("Previous Page")) {
                      //      Bukkit.broadcastMessage("Clicked previous page");
                            p.openInventory(InventoryGUI.playerUnlockedKits(p));
                            Sounds.nextPage(p);

                        }
                        if (kit.equalsIgnoreCase("Show Locked Kits")) {
                       //     Bukkit.broadcastMessage("Clicked locked kits");
                            p.openInventory(InventoryGUI.playerLockedKits(p));
                            Sounds.playMenuClick(p);


                        }
                    }
                }
            }



        }



        if (e.getView().getTitle().equals(InventoryGUI.lockedKitsName)) {
            ItemStack item = e.getCurrentItem();
            if (item != null) {
                if (item.getItemMeta() != null) {
                    Player p = (Player) e.getWhoClicked();
                    String kit = ChatColor.stripColor(item.getItemMeta().getDisplayName().toUpperCase());
                    Kits kits = returnKit(kit);

                    if (kits != null && e.getSlot() < 44) {
                        p.sendMessage(ChatColor.RED + "You have not unlocked this kit yet!");
                        Sounds.playMenuClick(p);

                    }

                        if (kit.equalsIgnoreCase("Next Page")) {
                     //       Bukkit.broadcastMessage("Clicked next page (locked)");
                            p.openInventory(InventoryGUI.playerLockedKits2(p));
                            Sounds.nextPage(p);

                        }
                        if (kit.equalsIgnoreCase("Show Unlocked Kits")) {
                      //      Bukkit.broadcastMessage("Clicked unlocked kits");
                            p.openInventory(InventoryGUI.playerUnlockedKits(p));
                            Sounds.playMenuClick(p);


                        }
                    }
                }
            }
        if (e.getView().getTitle().equals(InventoryGUI.lockedKitsName2)) {
            ItemStack item = e.getCurrentItem();
            if (item != null) {
                if (item.getItemMeta() != null) {
                    Player p = (Player) e.getWhoClicked();
                    String kit = ChatColor.stripColor(item.getItemMeta().getDisplayName().toUpperCase());
                    Kits kits = returnKit(kit);

                    if (kits != null && e.getSlot() < 44) {
                        p.sendMessage(ChatColor.RED + "You have not unlocked this kit yet!");
                        Sounds.playMenuClick(p);

                    }
                    if (kit.equalsIgnoreCase("Previous Page")) {
                       // Bukkit.broadcastMessage("Clicked previous page (locked)");
                        p.openInventory(InventoryGUI.playerLockedKits(p));
                        Sounds.nextPage(p);

                    }
                    if (kit.equalsIgnoreCase("Show Unlocked Kits")) {
                       // Bukkit.broadcastMessage("Clicked unlocked kits");
                        p.openInventory(InventoryGUI.playerUnlockedKits(p));
                        Sounds.playMenuClick(p);


                    }
                }
            }
        }


        }














    public Kits returnKit (String kit) {
        for (Kits kits : Kits.values()) {
            if (kits.toString().equalsIgnoreCase(kit)) {
                return kits;
            }
        }
        return null;
    }
}
