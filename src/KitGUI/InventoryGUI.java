package KitGUI;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.PlayerStats;
import Messages.Messages;
import Util.Game;
import Util.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class InventoryGUI {
    static String unlockedKitsName = ChatColor.BLUE + "Your Kits";
    static String unlockedKitsName2 = ChatColor.BLUE + "Your Kits (Page 2)";

    static String lockedKitsName = ChatColor.RED + "Locked Kits";
    static String lockedKitsName2 = ChatColor.RED + "Locked Kits (Page 2)";

    static Game game = Game.getSharedGame();
    static KitInfo kitInfo = KitInfo.getSharedKitInfo();
    static PlayerStats stats = PlayerStats.getStats();
    static Messages messages = new Messages();
    public static Inventory playerUnlockedKits (Player p) {
        Inventory kitGUI = Bukkit.createInventory(null, 54, unlockedKitsName);

        ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = enderpearl.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lShow Locked Kits"));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        enderpearl.setItemMeta(meta);
        glowItem(enderpearl);
        kitGUI.setItem(49, enderpearl);
        kitGUI.setItem(45, playerHead(p));

        if (kitInfo.unlockedKits(p).size() < 45) {
            //if only needs 1 page
            for (int i = 0; i < 45; i++) {

                if(i >= kitInfo.unlockedKits(p).size()){
                    kitGUI.setItem(i, new ItemStack(Material.AIR));
                }else{
                    ItemStack item = formatItem(kitInfo.unlockedKits(p).get(i), p);

                    kitGUI.setItem(i, item);
                }




            }
        } else {
            //remove last slot for page markers
            for (int i = 0; i < 45; i++) {

                if(i >= kitInfo.unlockedKits(p).size()){
                    kitGUI.setItem(i, new ItemStack(Material.AIR));
                }else{
                    ItemStack item = formatItem(kitInfo.unlockedKits(p).get(i), p);

                    kitGUI.setItem(i, item);
                }

                ItemStack nextPage = new ItemStack(Material.SPECTRAL_ARROW);
                ItemMeta nextPageMeta = nextPage.getItemMeta();
                if (nextPageMeta != null) {
                    nextPageMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lNext Page"));
                    nextPageMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                }
                nextPage.setItemMeta(nextPageMeta);
                glowItem(nextPage);
                kitGUI.setItem(50, nextPage);
            }
            //make next page here
        }
        return kitGUI;
    }

    public static Inventory playerUnlockedKits2 (Player p) {
        Inventory kitGUI = Bukkit.createInventory(null, 54, unlockedKitsName2);

        ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = enderpearl.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lShow Locked Kits"));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        enderpearl.setItemMeta(meta);
        glowItem(enderpearl);
        kitGUI.setItem(49, enderpearl);
        kitGUI.setItem(45, playerHead(p));

            //remove last slot for page markers

            //own 45+ kits here, grab 45th kit and over
            for (int i = 0; i < 45; i++) {

                if(i + 45 >= kitInfo.unlockedKits(p).size()){
                    kitGUI.setItem(i, new ItemStack(Material.AIR));
                }else{
                    ItemStack item = formatItem(kitInfo.unlockedKits(p).get(i + 45), p);
                    kitGUI.setItem(i, item);
                }

                ItemStack nextPage = new ItemStack(Material.SPECTRAL_ARROW);
                ItemMeta nextPageMeta = nextPage.getItemMeta();
                if (nextPageMeta != null) {
                    nextPageMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lPrevious Page"));
                    nextPageMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                }
                nextPage.setItemMeta(nextPageMeta);
                glowItem(nextPage);
                kitGUI.setItem(48, nextPage);
            }
            //make next page here

        return kitGUI;
    }


    public static Inventory playerLockedKits (Player p) {
        Inventory kitGUI = Bukkit.createInventory(null, 54, lockedKitsName);

        ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = enderpearl.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&lShow Unlocked Kits"));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        enderpearl.setItemMeta(meta);
        glowItem(enderpearl);
        kitGUI.setItem(49, enderpearl);

        kitGUI.setItem(45, playerHead(p));
        if (kitInfo.lockedKits(p).size() < 45) {
            //if only needs 1 page
            for (int i = 0; i < 45; i++) {

                if(i >= kitInfo.lockedKits(p).size()){
                    kitGUI.setItem(i, new ItemStack(Material.AIR));
                }else{
                    ItemStack item = formatItem(kitInfo.lockedKits(p).get(i), p);

                    kitGUI.setItem(i, item);
                }


            }
        } else {
            //remove last slot for page markers
            for (int i = 0; i < 45; i++) {

                if(i >= kitInfo.lockedKits(p).size()){
                    kitGUI.setItem(i, new ItemStack(Material.AIR));
                }else{
                    ItemStack item = formatItem(kitInfo.lockedKits(p).get(i), p);

                    kitGUI.setItem(i, item);
                }

                ItemStack nextPage = new ItemStack(Material.SPECTRAL_ARROW);
                ItemMeta nextPageMeta = nextPage.getItemMeta();
                if (nextPageMeta != null) {
                    nextPageMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lNext Page"));
                    nextPageMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                }
                nextPage.setItemMeta(nextPageMeta);
                glowItem(nextPage);
                kitGUI.setItem(50, nextPage);
            }
            //make next page here
        }
        return kitGUI;
    }


    public static Inventory playerLockedKits2 (Player p) {
        Inventory kitGUI = Bukkit.createInventory(null, 54, lockedKitsName2);

        ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = enderpearl.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lShow Unlocked Kits"));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        enderpearl.setItemMeta(meta);
        glowItem(enderpearl);
        kitGUI.setItem(49, enderpearl);
        kitGUI.setItem(45, playerHead(p));

        //remove last slot for page markers

        //own 45+ kits here, grab 45th kit and over
        for (int i = 0; i < 45; i++) {

            if(i + 45 >= kitInfo.lockedKits(p).size()){
                kitGUI.setItem(i, new ItemStack(Material.AIR));
            }else{
                ItemStack item = formatItem(kitInfo.lockedKits(p).get(i + 45), p);
                kitGUI.setItem(i, item);
            }

            ItemStack nextPage = new ItemStack(Material.SPECTRAL_ARROW);
            ItemMeta nextPageMeta = nextPage.getItemMeta();
            if (nextPageMeta != null) {
                nextPageMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lPrevious Page"));
                nextPageMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            }
            nextPage.setItemMeta(nextPageMeta);
            glowItem(nextPage);
            kitGUI.setItem(48, nextPage);
        }
        //make next page here

        return kitGUI;
    }



    public static List<String> formatDescription (List<String> string) {
        List<String> placeholder = new ArrayList<>();
        if (string.size() != 0) {
            List<String> strings = justify(string.get(0), 30);
            return strings;
        }
        return placeholder;


    }
    private static List<String> justify(String s, int limit) {
        List<String> strings = new ArrayList<>();
        StringBuilder justifiedText = new StringBuilder();
        StringBuilder justifiedLine = new StringBuilder();
        String[] words = s.split(" ");
        for (int i = 0; i < words.length; i++) {
            justifiedLine.append(words[i]).append(" ");
            if (i+1 == words.length || justifiedLine.length() + words[i+1].length() > limit) {
                justifiedLine.deleteCharAt(justifiedLine.length() - 1);
                strings.add(justifiedLine.toString());
                //justifiedText.append(justifiedLine.toString()).append(System.lineSeparator());
                justifiedLine = new StringBuilder();
            }
        }
        return strings;
    }
    public static ItemStack playerHead (Player p) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null ){
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&l" + p.getName()));

            List<String> formattedDesc = new ArrayList<>(messages.statsMessageFormat(p));
            formattedDesc.add(ChatColor.translateAlternateColorCodes('&', "&7&l" + "Selected Kit: &6&l" + kitInfo.getKitUnformatted(kitInfo.getPlayerKit(p))));
            meta.setLore(formattedDesc);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        item.setItemMeta(meta);
        SkullMeta skullmeta = (SkullMeta)item.getItemMeta();
        skullmeta.setOwningPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()));
        item.setItemMeta(skullmeta);
        return item;

    }
    public static ItemStack formatItem (Kits kits, Player p) {
        ItemStack item = new ItemStack(KitInfo.kitIcon(kits));
        if (kits == Kits.SURPRISE) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTBkMmEzY2U0OTk5ZmVkMzMwZDNhNWQwYTllMjE4ZTM3ZjRmNTc3MTk4MDg2NTczOTZkODMyMjM5ZTEyIn19fQ==");

        }
        if (kits == Kits.DWARF) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNiYmY0OGI2Zjk5NTI5OGZmYzhkNTVkM2M4MTgzYWUxYWVmOGEyYTlkNDIxN2MzMmVmNjljODY4ODMyM2IifX19");
        }
        if (kits == Kits.TANK) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmVhZjViOTlmNzJkZjViNzI0NTkzZGI3MjQxYmMyMjY1NmQ3N2JhM2ZiMWZkYWFhNThmOWFjNTM0NDYyOTIifX19");
        }
        if (kits == Kits.TURTLE) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTlkNjcxMjU4MmQ2MGEwMDU4ZTRmYmVlMGU4OWRkMDg5ZmJhYmI4MGYwN2U0YTA4NzQ5MDRjOTFiYzQ4ZjA4YSJ9fX0=");
        }
        if (kits == Kits.COPYCAT) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2YxZTMwMGViYTAyYzg1ODRiMzc4MDc3NGQ5N2JlYzI5MDhjYzdhMmY0YTJjNzZhZDg5YmI3NDk0OWE0OWI2NSJ9fX0=");
        }
        if (kits == Kits.SPY) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjBlODQxYjY4ZmIyZmQ3N2UwNmE0YTc2Njc2MzkxN2IzNWIyY2QxZDg1ODM3Zjg3NDRkYWI1OWIyMGI2ZmU2NyJ9fX0=");

        }
        if (kits == Kits.HADES) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVlNmNjYTAyMWE5OTEzY2UwNmQyZTczNzc2YWE4Yjg4NDQ3YWZiODI0Y2Q2NTdmZjhjMmNiODhjZGYwYTNkNSJ9fX0=");
        }
        if (kits == Kits.BOXER) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTdiNDE2ZDM2Yjc1NjE3Mzk4ZDdiYjNjZThkYjhhNzRiZDVlMzE0MTgyNTM3NzhjN2E2ZmU4NTQ5ODY3MSJ9fX0=");
        }
        if (kits == Kits.GAMBLER) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWMzY2VjNjg3NjlmZTljOTcxMjkxZWRiN2VmOTZhNGUzYjYwNDYyY2ZkNWZiNWJhYTFjYmIzYTcxNTEzZTdiIn19fQ==");
        }
        if (kits == Kits.BLAZE) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDA4MGJiZWZjYTg3ZGMwZjM2NTM2YjY1MDg0MjVjZmM0Yjk1YmE2ZThmNWU2YTQ2ZmY5ZTljYjQ4OGE5ZWQifX19");
        }
        if (kits == Kits.GRANDPA) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2M4MmMwNTU3NTBlNDFhNDQwZTdjMjM4YjE2MWY1OGI2MjEyNDllNzgyNGY1YTBiNjEzNzRiOGQ4MTM4ZmU0ZCJ9fX0=");
        }
        if (kits == Kits.PHANTOM) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmZjZDBjZDNkOTU0M2NmYjE0MTA1ZTNiYWNiY2I0YTg1ZGI0MjA3NDE5MDQ0ODk4MGQwYmI4ZWQ1ZTYxMmQzZCJ9fX0=");
        }
        if (kits == Kits.THOR) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmE5ZjgzMzI5YTJlNDc1YTc1MzM1YjM5NDlhYTRkMDU0ZjlkZTQxM2JmYjI4YWE2MGRlMmU1MjU5ZWNhYWQxIn19fQ==");
        }
        if (kits == Kits.NINJA) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmY3MzMyNjk2MTRmZThhOWFlODlmNWM1ODI4NmFhNTNiOWUzODNmYzRkYzU3ZTM3ZWNiZmE1ZTkzYSJ9fX0=");
        }
        if (kits == Kits.BURROWER) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU1NWY5YTQ4YWNjNDJhMDFiODgxYWY3MjZmZmMxYWMxNzg3ODllMzI3NGNiMmMxY2RiMTJmNTdjIn19fQ==");
        }
        if (kits == Kits.MADMAN) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmYyZjQwMTU1ZWQ0ZDA0ZDEzMzA5YTc5NzJlOGJmODlkYTljMTM3YzA4ODE4OWQ3MDdiMzc3YjBkY2EzNmMxMyJ9fX0=");
        }
        if (kits == Kits.SCOUT) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZkYzk0ZDEzZDZhODg0MmUxNzU5NzNkNmE2Njc0M2UyYzIwYjMyZWM1NDNiNWRmNjJiN2QwNDhmODM3ZGFlYiJ9fX0=");
        }
        if (kits == Kits.DEMOMAN) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2ExM2MzZmE0Y2I2N2ZkZjNlNGU0ZjEyMzliYmZlZTVjYTdhYzdhNjhiNDhkYzRkYWFhZDJkODUwYmIxMWJlIn19fQ==");
        }
        if (kits == Kits.CHAMELEON) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDI2YmM4ZGNmOWE3ZDg0YzcxOWNlMTk1MTE4NjRmNTc4N2Y5YTEyNDA3OGUwMjIzNDQ5NWNkNDNhMjE2In19fQ==");
        }
        if (kits == Kits.JUMPER) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjYxMWI4MWI3ODRhNzc0NTlhOWY5ZDI1MDk0YjhiMmUzZDg5MDI4ZTFlN2JiYWJlOTE2NjVjZDJkYzY2NiJ9fX0=");
        }
        if (kits == Kits.VAMPIRE) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGQ0NDc1NmUwYjRlY2U4ZDc0NjI5NmEzZDVlMjk3ZTE0MTVmNGJhMTc2NDdmZmUyMjgzODUzODNkMTYxYTkifX19");
        }
        if (kits == Kits.WEREWOLF) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY4ZDQzMTI5MzliYjMxMTFmYWUyOGQ2NWQ5YTMxZTc3N2Y4ZjJjOWZjNDI3NTAxY2RhOGZmZTNiMzY3NjU4In19fQ==");
        }
        if (kits == Kits.ANCHOR) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTcwNTA5YTY0YzQ5NmNkZGJhNDEzNWU2NzEyYmIwNWQ5YWFkNDIwNTJhMThmZWVlZWQ3ZDQ0ZGEyYzNiZDBjYyJ9fX0=");
        }
        if (kits == Kits.FIREMAN) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2E3MjlkZGRkNDQ4OWFmY2E1NDc0MTM3MzFjZGYwYjQ4ZWIwYmViNGZiYTA4N2M0M2FlYzBjNThhNWNhNGIyNyJ9fX0=");
        }
        if (kits == Kits.PYRO) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmU1YmEzYTU0YzVhYjU4ZGJhNjM2ZmE5MGI4YjM1NWQ5MzQ1ODY1YzAxZjg3ZjYyYzUzN2FkM2FjZjRkNDEyIn19fQ==");
        }
        if (kits == Kits.GLADIATOR) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTQ3NmU3YTEzMTQxODhjOTczNjRkYTQ4YmNhYzkxYzU4NWNjZTA1OTMwMTI4ZTRmZGM1YTlmNTUxZjkyMmY1In19fQ==");
        }
        if (kits == Kits.PROPHET) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmU2MmM4N2Q4MmNjNzNmZDQyMjg4YjI3NGZhODllOGQ2M2EyNWI5NTJiZGZlMGU1ZGRmNGM5OGIxNDM3In19fQ==");

        }
        if (kits == Kits.HACKER) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2I4MDlhYzk5ODNjZTJlMjYxOTY1OTQ5NGU5NWE5OTRmYTZjOTEzNGY1OTFiOGE1NmY3NGU4NjZkMjk0YSJ9fX0=");
        }
        if (kits == Kits.ENGINEER) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGVlMDhiZTExYWRkODg4Mjk4ZjYyNTJjYzA5ZDY1ODVkNTQ2YjhiMTkxOGRhZGU0ZGE4YmMxMzIzZjExZDQifX19=");
        }
        if (kits == Kits.STAND) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTZkZDFhNmU1N2YwYjNmMjc4OGViNzRkZTUwMmRmZjYxMTQ2NzQ1MWM4ZjEwYTk0YWViZjczYjAyMjk4ZjE5MyJ9fX0=");
        }
        if (kits == Kits.VULTURE) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzBiYjc1OWIwZjcyMjNjYjcwN2E4YjU2NDAxNTVmZmM1NjdmNWNjZDhmODVhZjU0NGUxYzc2ZTFmMmY3NmQ5OCJ9fX0=");
        }
        if (kits == Kits.PLAGUE) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTg2NGQyNDVjZDQxMDljNGE2MjdiMTkzNGM3N2Q0N2I3NGM1ZWQxODQzY2ViZDU0YWU0NWNhMDFhNmFhNWQwMyJ9fX0=");
        }

        if (kits == Kits.IMPOSTER) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGM0ZDQ0YTBmZmUwMjM0OWU5OWRhMDYyOTIxMzA2MzExM2U2YmIzYWZjMjU5ZjQ2NjE4YzkwZWRjZTgzMDc4NiJ9fX0=");
        }

        if (kits == Kits.PEANUT) {
            item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTA0OWQ3NDk4MzA5NzAzZDVjN2U2OTdmZjFlYzc1MGE5YWMxY2ExZjNlY2MyOTA4OGQxMzYwOWM4YjY5OThiNiJ9fX0=");
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null ){
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&l" + kitInfo.getKitUnformatted(kits)));
            List<String> formattedDesc = new ArrayList<>();
            for (String s : formatDescription(KitInfo.kitDescription(kits))) {
                formattedDesc.add(ChatColor.translateAlternateColorCodes('&', "&7&l" + s));
            }
            if (kitInfo.getPlayerKit(p) == kits) {
                formattedDesc.add(ChatColor.translateAlternateColorCodes('&', "&6&lSELECTED"));
            }
            meta.setLore(formattedDesc);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        item.setItemMeta(meta);

        if (kitInfo.getPlayerKit(p) == kits) {
            glowItem(item);
        }
        return item;
    }


    public static ItemStack applySkullTexture(String base) {

        return SkullCreator.itemFromBase64(base);
    }


    public static void glowItem(ItemStack item) {
        ItemMeta itemStackMeta = item.getItemMeta();
        itemStackMeta.addEnchant(Enchantment.LURE, 0, true);
        itemStackMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        item.setItemMeta(itemStackMeta);
    }
//    public static Inventory lockedKits (Player p) {
//
//    }
}
