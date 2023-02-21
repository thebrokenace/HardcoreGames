package Kits.KitListeners.Kits.Vanity;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class Vulture implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();

    HashMap<UUID, UUID> betMap = new HashMap<UUID, UUID>();
    List<UUID> betOnAlready = new ArrayList<>();


    @EventHandler
    public void onRightClickPicker (PlayerInteractEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.VULTURE) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (isHoldingPicker(e.getPlayer())) {
                        if (!betMap.containsKey(e.getPlayer().getUniqueId())) {
                            e.getPlayer().openInventory(vultureBetPickerGUI(e.getPlayer()));
                        } else {
                            e.getPlayer().sendMessage(ChatColor.RED + "You have already placed your bet!");
                        }
                    }
                }
            }
        }
    }

    public Inventory vultureBetPickerGUI (Player p) {
        Inventory vulture = Bukkit.createInventory(null, 54, vultureBetPickerGUIName());

        List<OfflinePlayer> players = new ArrayList<>();
        for (UUID uuid : game.humansLeft()) {
            if (!p.getUniqueId().equals(uuid) && !p.isDead() && !betOnAlready.contains(uuid)) {
                players.add(Bukkit.getOfflinePlayer(uuid));
            }
        }

        for (int i = 0; i < players.size(); i++) {
            ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta playerHeadMeta = stack.getItemMeta();

            if (playerHeadMeta != null) {
                SkullMeta headData = (SkullMeta) playerHeadMeta;

                headData.setOwningPlayer(players.get(i));
                playerHeadMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&l" + headData.getOwningPlayer().getName()));

                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.translateAlternateColorCodes('&', "&6Place Bet"));
                playerHeadMeta.setLore(lore);
                stack.setItemMeta(playerHeadMeta);
            }

            vulture.setItem(i, stack);
        }


        return vulture;
    }

    @EventHandler
    public void onPick (InventoryClickEvent e) {

        if (e.getView().getTitle().equals(vultureBetPickerGUIName())) {
            if (kitInfo.getPlayerKit(e.getWhoClicked()) == Kits.VULTURE) {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                    ItemMeta playerHeadMeta = e.getCurrentItem().getItemMeta();

                    SkullMeta headData = (SkullMeta) playerHeadMeta;

                    if (headData != null) {
                        Player p = Bukkit.getPlayer(headData.getOwningPlayer().getName());
                        if (!betOnAlready.contains(p.getUniqueId())) {
                            e.getWhoClicked().sendMessage(ChatColor.GREEN + "You bet on " + headData.getOwningPlayer().getName() + "!");
                            betMap.put(e.getWhoClicked().getUniqueId(), p.getUniqueId());
                            e.getWhoClicked().closeInventory();
                            betOnAlready.add(p.getUniqueId());
                        } else {
                            e.getWhoClicked().sendMessage(ChatColor.RED + "This player is bet on already!");

                        }
                        e.setCancelled(true);
                    }

                }


            }
        }
    }

    public String vultureBetPickerGUIName () {
        return ChatColor.translateAlternateColorCodes('&', "&6&lVulture Death Bet Picker");
    }



    @EventHandler
    public void onDeath (PlayerDeathEvent e) {
        if (game.isStarted()) {
            if (betMap.containsValue(e.getEntity().getUniqueId())) {

                Player vulture = Bukkit.getPlayer(getKeyByValue(betMap, e.getEntity().getUniqueId()));
                if (kitInfo.getPlayerKit(vulture) == Kits.VULTURE) {
                    Player victim = e.getEntity();
                    if (vulture == null) {
                        return;
                    }
                    vulture.sendMessage(ChatColor.GREEN + "Your bet was correct! You have received " + victim.getName() + "'s items!");

                    List<ItemStack> victimDrops = e.getDrops();
                    ItemStack[] items = new ItemStack[victimDrops.size()];
                    items = victimDrops.toArray(items);
                    Map<Integer, ItemStack> map = vulture.getInventory().addItem(items);
                    for (ItemStack item : map.values()) {
                        vulture.getWorld().dropItemNaturally(vulture.getLocation(), item);
                    }

                    e.getDrops().clear();
                }
            }
        }
    }

    @EventHandler
    public void updateLog (PlayerDeathEvent e) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (kitInfo.getPlayerKit(p) == Kits.VULTURE) {
                updateLogVulture(p, e.getEntity().getName() + " has died at " + Math.round(e.getEntity().getLocation().getX()) + ", " + Math.round(e.getEntity().getLocation().getY()) + ", " + Math.round(e.getEntity().getLocation().getZ()) + " from " + e.getEntity().getLastDamageCause().getCause().toString().toLowerCase().replace("_", " "));
            }
        }
    }

    public UUID getKeyByValue(Map<UUID, UUID> map, UUID value) {
        for (Map.Entry<UUID, UUID> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public boolean isHoldingPicker (Player p) {
        if (isPicker(p.getInventory().getItemInMainHand())) {
            return true;
        }
        return false;
    }

    public boolean isPicker (ItemStack i) {
        if (i.getItemMeta() != null && ChatColor.stripColor(i.getItemMeta().getDisplayName()).equals("Death Bet Picker")) {
            return true;
        } else {
            return false;
        }

    }

    public static ItemStack pickerCompass () {
        ItemStack stack = new ItemStack(Material.COMPASS);
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&d&oRight click and place a bet"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&d&oon the next player to die to"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&d&oget all of their stuff!"));
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lDeath Bet Picker"));
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;

    }

    public static ItemStack deathLog () {
        ItemStack writtenBook = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta meta = writtenBook.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5&lDeath Log"));
        writtenBook.setItemMeta(meta);
        BookMeta bookMeta = (BookMeta) writtenBook.getItemMeta();
        bookMeta.setTitle("Death Log");
        bookMeta.setAuthor("Vulture");
        List<String> pages = new ArrayList<>();
        pages.add("All recent player deaths will be stored here.");
        bookMeta.setPages(pages);

        writtenBook.setItemMeta(bookMeta);
        return writtenBook;
    }

    public void updateLogVulture (Player p, String s) {
        if (kitInfo.getPlayerKit(p) == Kits.VULTURE) {
            ItemStack i = (containsDeathLog(p,s));
        }
    }

    public ItemStack containsDeathLog (Player p, String s) {
        if (kitInfo.getPlayerKit(p) == Kits.VULTURE) {
            for (int il = 0; il < p.getInventory().getContents().length; il++) {
                ItemStack i = p.getInventory().getItem(il);
                if (i != null) {
                    if (i.getType() == Material.WRITTEN_BOOK && i.getItemMeta() != null && ChatColor.stripColor(i.getItemMeta().getDisplayName()).equals("Death Log")) {
                        p.getInventory().setItem(il, updateLog(i, s));
                        return i;
                    }
                }
            }
        }
        return null;
    }

    public ItemStack updateLog (ItemStack i, String s) {
        if (i.getItemMeta() != null && ChatColor.stripColor(i.getItemMeta().getDisplayName()).equals("Death Log") && i.getType() == Material.WRITTEN_BOOK) {
            ItemStack writtenBook = i;
            BookMeta bookMeta = (BookMeta) writtenBook.getItemMeta();
            bookMeta.setTitle("Death Log");
            bookMeta.setAuthor("Vulture");
            List<String> pages = new ArrayList<>(bookMeta.getPages());
            pages.add(s);
            bookMeta.setPages(pages);

            writtenBook.setItemMeta(bookMeta);
            return writtenBook;
        }
        return null;

    }
}
