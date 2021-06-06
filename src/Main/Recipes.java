package Main;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Recipes {
    Config config = new Config();

    public ShapelessRecipe normalSoup () {
        ShapelessRecipe normalSoup = new ShapelessRecipe(NamespacedKey.minecraft("easybrownstew"),new ItemStack(Material.MUSHROOM_STEW));
        normalSoup.addIngredient(1, Material.BROWN_MUSHROOM);
        normalSoup.addIngredient(1, Material.BOWL);
        return normalSoup;
    }

    public ShapelessRecipe redSoup () {
        ShapelessRecipe normalSoup = new ShapelessRecipe(NamespacedKey.minecraft("easyredstew"),new ItemStack(Material.MUSHROOM_STEW));
        normalSoup.addIngredient(1, Material.RED_MUSHROOM);
        normalSoup.addIngredient(1, Material.BOWL);
        return normalSoup;
    }

    public ShapelessRecipe appleSoup () {
        ShapelessRecipe normalSoup = new ShapelessRecipe(NamespacedKey.minecraft("applejuice"),changeName(new ItemStack(Material.MUSHROOM_STEW), ChatColor.GOLD + "Apple Juice"));
        normalSoup.addIngredient(3, Material.APPLE);
        normalSoup.addIngredient(1, Material.BOWL);
        return normalSoup;
    }
    public ShapelessRecipe cactusJuice () {
        ShapelessRecipe normalSoup = new ShapelessRecipe(NamespacedKey.minecraft("cactusjuice"),changeName(new ItemStack(Material.MUSHROOM_STEW), ChatColor.DARK_GREEN + "Cactus Juice"));
        normalSoup.addIngredient(1, Material.CACTUS);
        normalSoup.addIngredient(1, Material.BOWL);
        return normalSoup;
    }
    public ShapelessRecipe cocoaJuice () {
        ShapelessRecipe normalSoup = new ShapelessRecipe(NamespacedKey.minecraft("cocoasoup"),changeName(new ItemStack(Material.MUSHROOM_STEW), ChatColor.BLUE + "Chocolate Milk"));
        normalSoup.addIngredient(1, Material.COCOA_BEANS);
        normalSoup.addIngredient(1, Material.BOWL);
        return normalSoup;
    }
    public ShapelessRecipe beatSoup () {
        ShapelessRecipe normalSoup = new ShapelessRecipe(NamespacedKey.minecraft("beatsoup"),changeName(new ItemStack(Material.MUSHROOM_STEW), ChatColor.LIGHT_PURPLE + "Beetroot Soup"));
        normalSoup.addIngredient(1, Material.BEETROOT);
        normalSoup.addIngredient(1, Material.BOWL);
        return normalSoup;
    }

    public ShapelessRecipe caneSoup () {
        ShapelessRecipe normalSoup = new ShapelessRecipe(NamespacedKey.minecraft("canesoup"), changeName(new ItemStack(Material.MUSHROOM_STEW), ChatColor.GREEN + "Cane Juice"));
        normalSoup.addIngredient(3, Material.SUGAR_CANE);
        normalSoup.addIngredient(1, Material.BOWL);
        return normalSoup;
    }

    public ItemStack changeName (ItemStack i, String s) {
        ItemMeta meta = i.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(s);
        }
        i.setItemMeta(meta);
        return i;
    }

    public List<ShapelessRecipe> allSoups () {
        List<ShapelessRecipe> recipes = new ArrayList<>();

        if (config.getSoupStats()) {
            recipes.add(beatSoup());
            recipes.add(cocoaJuice());
            recipes.add(normalSoup());
            recipes.add(cactusJuice());
            recipes.add(redSoup());
            recipes.add(caneSoup());
            recipes.add(appleSoup());
            return recipes;
        } else {
            return recipes;
        }
    }
}
