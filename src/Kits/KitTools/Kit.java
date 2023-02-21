package Kits.KitTools;

import Kits.KitListeners.Kits.Vanity.Vulture;
import Util.SkullCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Kit {
    public List<ItemStack> drops(Kits kits) {
        List<ItemStack> kitDrops = new ArrayList<>();
        if (kits == Kits.GRANDPA) {
            ItemStack grandpaStick = new ItemStack(Material.STICK);
            ItemMeta meta = grandpaStick.getItemMeta();
            if (meta != null) {
                meta.addEnchant(Enchantment.KNOCKBACK, 2, false);
            }
            grandpaStick.setItemMeta(meta);

            kitDrops.add(grandpaStick);
            return kitDrops;

        }
        if (kits == Kits.ARCHER) {
            kitDrops.add(itemStackMaker(Material.BOW, "&a&lArcher's Bow", null, 0, 1));
            kitDrops.add(itemStackMaker(Material.ARROW, "&a&lArcher's Arrows", null, 0, 10));

        }
        if (kits == Kits.MINER) {

            ItemStack item = new ItemStack(itemStackMaker(Material.STONE_PICKAXE, "&c&lMiner's Pickaxe", Enchantment.DIG_SPEED, 5, 1));
            item.addEnchantment(Enchantment.MENDING, 1);
            kitDrops.add(item);
        }
        if (kits == Kits.FORGER) {
            kitDrops.add(itemStackMaker(Material.STONE_PICKAXE, "&c&lForger's Pickaxe", Enchantment.DIG_SPEED, 2, 1));
        }
        if (kits == Kits.SPY) {
            kitDrops.add(glowItem(itemStackMaker(Material.COMPASS, "&5&lSpy Compass", null, 0, 1)));
        }
        if (kits == Kits.SWITCHER) {
            kitDrops.add(glowItem(itemStackMaker(Material.SNOWBALL, "&b&lSwitcher Snowball", null, 0, 10)));
        }
        if (kits == Kits.BEASTMASTER) {
            kitDrops.add(itemStackMaker(Material.WOLF_SPAWN_EGG, "", null, 0, 3));
            kitDrops.add(itemStackMaker(Material.BONE, "", null, 0, 20));
        }
        if (kits == Kits.PYRO) {
            kitDrops.add(glowItem(itemStackMaker(Material.FLINT_AND_STEEL, "&6&lPyro's Firestarter", null, 0, 1)));
            kitDrops.add(glowItem(itemStackMaker(Material.FIRE_CHARGE, "&6&lFire Balls", null, 0, 5)));
        }
        if (kits == Kits.URGAL) {
            kitDrops.add(getPotionItemStack(PotionType.STRENGTH, 1, false, true, "Urgal Strength Potion"));
            kitDrops.add(getPotionItemStack(PotionType.STRENGTH, 1, false, true, "Urgal Strength Potion"));

        }
        if (kits == Kits.KAYA) {
            kitDrops.add(itemStackMaker(Material.GRASS_BLOCK, "&a&lKaya Grass Block", null, 0, 64));
            kitDrops.add(itemStackMaker(Material.GRASS_BLOCK, "&7&lDecoys", null, 0, 64));
        }
        if (kits == Kits.DEMOMAN) {
            kitDrops.add(itemStackMaker(Material.STONE_PRESSURE_PLATE, "&7&lDemoman Pressure Plate", null, 0, 2));
            kitDrops.add(itemStackMaker(Material.GRAVEL, "&7&lDemoman Gravel", null, 0, 2));
            //kitDrops.add(itemStackMaker(Material.STICK, "Demoman Detonator", null, 0, 1));
        }
        if (kits == Kits.JUMPER) {
            kitDrops.add(glowItem(itemStackMaker(Material.ENDER_PEARL, "&a&lJumper Pearls", null, 0, 5)));
        }
        if (kits == Kits.HACKER) {
            kitDrops.add(itemStackMaker(Material.GHAST_TEAR, "&1&lCompass Blocker", Enchantment.BINDING_CURSE, 1, 3));
            kitDrops.add(itemStackMaker(Material.BLUE_DYE, "&9&lKit Disabler", Enchantment.BINDING_CURSE, 1, 1));
        }
        if (kits == Kits.TURTLE) {
            kitDrops.add(itemStackMaker(Material.TURTLE_HELMET, "&2&lTurtle Hat", null, 0, 1));
        }
        if (kits == Kits.PICKPOCKET) {
            kitDrops.add(glowItem(itemStackMaker(Material.BLAZE_ROD, "&6&lPickpocket Stick", null, 0 , 1)));
        }
        if (kits == Kits.LUMBERJACK) {
            kitDrops.add(itemStackMaker(Material.WOODEN_AXE, "&5&lLumberjack Axe", null, 0, 1));
        }
        if (kits == Kits.BURROWER) {
            kitDrops.add(glowItem(itemStackMaker(Material.EGG, "&d&lBurrower Panic Room", null, 1, 1)));
        }
        if (kits == Kits.THOR) {
            kitDrops.add(glowItem(itemStackMaker(Material.WOODEN_AXE, "&a&lThor's Hammer", null, 0, 1)));
        }
        if (kits == Kits.SCOUT) {
            kitDrops.add(getPotionItemStack(PotionType.SPEED, 1, false, true, "Scout Speed Potion"));
            kitDrops.add(getPotionItemStack(PotionType.SPEED, 1, false, true, "Scout Speed Potion"));
            kitDrops.add(getPotionItemStack(PotionType.JUMP, 1, false, true, "Scout Jump Potion"));
            kitDrops.add(getPotionItemStack(PotionType.JUMP, 1, false, true, "Scout Jump Potion"));

        }
        if (kits == Kits.PHANTOM) {
            kitDrops.add(itemStackMaker(Material.FEATHER, "&b&lPhantom Flight", null, 0, 1));
        }
        if (kits == Kits.BLINK) {
            kitDrops.add(glowItem(itemStackMaker(Material.REDSTONE_TORCH, "&c&lBlink Torch", null, 0, 10)));
        }
        if (kits == Kits.CHECKPOINT) {
            kitDrops.add(glowItem(itemStackMaker(Material.STONE_BUTTON, "&4&lCheckpoint Button", null, 0, 1)));
            kitDrops.add(itemStackMaker(Material.NETHER_BRICK_FENCE, "&c&lCheckpoint", null, 0, 1));
        }
        if (kits == Kits.GRAPPLER) {
            kitDrops.add(glowItem(itemStackMaker(Material.FISHING_ROD, "&8&lGrappling Hook", Enchantment.DURABILITY, 3, 1)));
        }
        if (kits == Kits.BLAZE) {
            kitDrops.add(glowItem(itemStackMaker(Material.BLAZE_POWDER, "&6&lBlaze Run", null, 0, 5)));
        }
        if (kits == Kits.GAMBLER) {
            kitDrops.add(glowItem(itemStackMaker(Material.STONE_BUTTON, "&1&lGambler Button", null, 0, 1)));
        }
        if (kits == Kits.HADES) {
            kitDrops.add(itemStackMaker(Material.BONE, "Hades Summoner", null, 0, 1));
        }
        if (kits == Kits.ACROBAT) {
            kitDrops.add(glowItem(itemStackMaker(Material.LIGHT_GRAY_DYE, "&7&lAcrobat Tool", null, 0, 16)));
        }
        if (kits == Kits.FIREMAN)                 {
            kitDrops.add(new ItemStack(Material.WATER_BUCKET));
        }
        if (kits == Kits.EVOKER) {
            kitDrops.add(itemStackMaker(Material.ENCHANTED_BOOK, "&e&lEvoker Fangs", null, 0, 1));
        }
        if (kits == Kits.CRAFTER) {
            kitDrops.add(glowItem(itemStackMaker(Material.CRAFTING_TABLE, "&6&lCrafter's Crafting Table", null, 0, 1)));
            kitDrops.add(glowItem(itemStackMaker(Material.FURNACE, "&b&lCrafter's Furnace", null, 0, 1)));
        }
        if (kits == Kits.RADAR) {
            kitDrops.add(glowItem(itemStackMaker(Material.MAP, "&a&lRadar's Scanner", null, 0, 1)));
        }
        if (kits == Kits.XRAY) {
            kitDrops.add(glowItem(itemStackMaker(Material.EGG, "&a&lXray Egg", null, 0, 10)));

        }
        if (kits == Kits.GLADIATOR) {
            kitDrops.add(glowItem(itemStackMaker(Material.IRON_INGOT, "&1&lGladiator Dueler", null, 0, 1)));
        }
        if (kits == Kits.WISP) {
            kitDrops.add(glowItem(itemStackMaker(Material.DIAMOND, "&1&lWisp Decoy", null, 0, 5)));
        }

        if (kits == Kits.ENGINEER) {
            Random random = new Random();
            ItemStack item = null;
            if (random.nextInt(2) == 0) {
                item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDZkYjEzN2EzNTY3OWJlYWY3OTAwNzBkMGM5Yzk2YzkwNjc2MjYwZWJjMDBkZDJjNzAwNTYyYTA5OWRiMDdjMCJ9fX0=");
            } else {
                item = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmU3ZmVmMjUwZTZiM2ViNjM2MGM5NzdlN2M1YzQ4ZjljZGNhNTI5MGJjN2JkY2M5MGQ2MDc5MzNiN2QzYjBkNyJ9fX0==");

            }
            if (item.getItemMeta() != null) {
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&1&lSentry Gun"));
                item.setItemMeta(meta);
            }
            kitDrops.add(item);

            kitDrops.add(glowItem(itemStackMaker(Material.BLAZE_ROD, "&a&lWrench", null, 0, 1)));
        }

        if (kits == Kits.LAUNCHER) {
            kitDrops.add(itemStackMaker(Material.SPONGE, "&6&lLaunchpad", null, 0, 15));
        }

        if (kits == Kits.PORTAL) {
            kitDrops.add(glowItem(itemStackMaker(Material.PURPLE_WOOL, "&5&lPortal Block", null, 0, 2)));
        }
        if (kits == Kits.VULTURE) {
            kitDrops.add(Vulture.pickerCompass());
            kitDrops.add(Vulture.deathLog());
        }
        if (kits == Kits.PLAGUE) {
            kitDrops.add(itemStackMaker(Material.GREEN_DYE, "&2&lInfect Syringe", null, 0, 20));
            kitDrops.add(itemStackMaker(Material.ARROW, "&2&lInfection Arrows", null, 0, 5));

            ItemStack infectBow = new ItemStack(Material.BOW);
            infectBow.setDurability((short) ((short) Material.BOW.getMaxDurability() - 5));
            kitDrops.add(infectBow);

        }
        if (kits == Kits.IMPOSTER) {
            //kitDrops.add(Imposter.disguiseKit());
        }

        if (kits == Kits.BRIDGER) {
            kitDrops.add(itemStackMaker(Material.EGG, "&1&bBridge Egg",null,0,25));
            kitDrops.add(itemStackMaker(Material.EGG, "&1&bSuper Bridge Egg",null,0,5));

        }

        return kitDrops;
    }


    public static ItemStack applySkullTexture(String base) {

        return SkullCreator.itemFromBase64(base);
    }
    public static ItemStack glowItem(ItemStack item) {
        ItemMeta itemStackMeta = item.getItemMeta();
        itemStackMeta.addEnchant(Enchantment.LURE, 0, true);
        itemStackMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        item.setItemMeta(itemStackMeta);
        return item;
    }
//    public static ItemStack kiticon(Kits kits) {
//        switch (kits) {
//            case THOR: return iconMaker(Material.DIRT, "hi", "8383");
////            case DEMOMAN: return Material.TNT;
////            case TURTLE: return Material.TURTLE_HELMET;
////            case KAYA: return Material.GRASS_BLOCK;
////            case COPYCAT: return Material.PAINTING;
////            case SURPRISE: return Material.BIRCH_WOOD;
////            case SWITCHER: return Material.SNOWBALL;
////            case SPY: return Material.COMPASS;
////            case PYRO: return Material.FLINT_AND_STEEL;
////            case NONE: return Material.BARRIER;
////            case BOXER: return
//        }
//        return null;
//    }
    public static ItemStack iconMaker (Material material, String name, String id) {
        if (!id.equals("")) {
//          return  HDBHandler.hdbItem("");
            return new ItemStack(material);
        } else {
            return new ItemStack(material);
        }
    }

    public ItemStack itemStackMaker (Material material, String name, Enchantment enchantment, int level, int amount) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            if (enchantment != null && level != 0) {
                meta.addEnchant(enchantment, level, false);
            }


        }
        itemStack.setItemMeta(meta);
        itemStack.setAmount(amount);

        return itemStack;


    }
    public ItemStack getPotionItemStack(PotionType type, int level, boolean extend, boolean upgraded, String displayName){
        ItemStack potion = new ItemStack(Material.SPLASH_POTION, 1);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        if (meta != null) {
            meta.setBasePotionData(new PotionData(type, extend, upgraded));
            meta.setDisplayName(displayName);
        }
        potion.setItemMeta(meta);
        return potion;
    }
}
