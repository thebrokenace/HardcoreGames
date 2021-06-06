package Kits.KitTools;

import Main.HardcoreGames;
import Util.Game;
import net.luckperms.api.model.user.User;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class KitInfo {
    List<ItemStack> kitDrops = new ArrayList<>();
    public Map<Entity, Kits> surprises = new HashMap<>();
    public Map<Entity, Kits> copycats = new HashMap<>();
    HashMap<UUID, Kits> kitMap = new HashMap<>();
    Kit kit = new Kit();

    private KitInfo() { } // make your constructor private, so the only war
    // to access "application" is through singleton pattern

    private static KitInfo _kit;

    public static KitInfo getSharedKitInfo()
    {
        if (_kit == null)
            _kit = new KitInfo();
        return _kit;
    }
//    public static void setKitDrops(List<ItemStack> kitDrops) {
//        kitDrops = kit.drops()
//    }

    public List<ItemStack> getKitDrops(Kits kits) {

        return kit.drops(kits);
    }

    public Kits getPlayerKit (Entity p) {
        return kitMap.getOrDefault(p.getUniqueId(), Kits.NONE);
    }

    public void setPlayerKit (Entity p, Kits kit) {
        kitMap.put(p.getUniqueId(), kit);
    }

    public List<Kits> getAllKits () {
        return new ArrayList<>(Arrays.asList(Kits.values()));
    }

    public List<String> getKitNames () {
        List<String> strings = new ArrayList<>();
        for (Kits kit : getAllKits()) {
            String str = kit.toString().toLowerCase();
            strings.add(str.substring(0, 1).toUpperCase() + str.substring(1));
        }
        return strings;
    }

    public String getKitNameFormatted (Kits kits, Entity p) {
        String str = kits.toString().toLowerCase();
        str = str.substring(0, 1).toUpperCase() + str.substring(1);
        if (copycats.containsKey(p)) {
            if (getPlayerKit(p) == Kits.COPYCAT) {
                return "Copycat";
            }
            return "Copycat " + str;

        }
        if (surprises.containsKey(p)) {
            return "Surprise " + str;
        }


        return str;

    }

    public static Material kitIcon (Kits kits) {
        switch (kits) {
            case BOXER: return Material.BELL;
            case NONE: return Material.BARRIER;
            case PYRO:return Material.FLINT_AND_STEEL;
            case SPY: return Material.COMPASS;
            case SWITCHER: return Material.SNOWBALL;
            case SURPRISE: return Material.PLAYER_HEAD;
            case COPYCAT: return Material.PAINTING;
            case KAYA: return Material.GRASS_BLOCK;
            case TURTLE: return Material.TURTLE_HELMET;
            case DEMOMAN: return Material.TNT;
            case THOR: return Material.WOODEN_AXE;
            case PHANTOM: return Material.FEATHER;
            case BLINK: return Material.REDSTONE_TORCH;
            case CHAMELEON: return Material.LIME_DYE;
            case MINER: return Material.STONE_PICKAXE;
            case NINJA: return Material.LEATHER_CHESTPLATE;
            case SCOUT: return Material.IRON_BOOTS;
            case ANCHOR: return Material.CHAIN;
            case STOMPER: return Material.DIAMOND_BOOTS;
            case JUMPER: return Material.ENDER_PEARL;
            case FIREMAN: return Material.FIRE_CHARGE;
            case BURROWER: return Material.EGG;
            case CULTIVATOR: return Material.OAK_SAPLING;
            case LUMBERJACK: return Material.IRON_AXE;
            case HACKER: return Material.COMPASS;
            case PICKPOCKET: return Material.BLAZE_ROD;
            case BEASTMASTER: return Material.BONE;
            case NEO: return Material.ARROW;
            case WORM: return Material.LEAD;
            case POSEIDON: return Material.TRIDENT;
            case CANNIBAL: return Material.MUTTON;
            case TANK: return Material.GREEN_DYE;
            case HERMIT: return Material.MUSHROOM_STEW;
            case MAGMA: return Material.MAGMA_CREAM;
            case WEREWOLF: return Material.CLOCK;
            case MONSTER: return Material.ZOMBIE_HEAD;
            case DWARF: return Material.PLAYER_HEAD;
            case URGAL: return Material.SPLASH_POTION;
            case VIPER: return Material.ROTTEN_FLESH;
            case ARCHER: return Material.BOW;
            case FORGER: return Material.IRON_PICKAXE;
            case GRANDPA: return Material.STICK;
            case VAMPIRE: return Material.REDSTONE;
            case ACHILLES: return Material.WOODEN_SWORD;
            case BESERKER: return Material.IRON_SWORD;
            case MADMAN: return Material.SKELETON_SKULL;
            case CHECKPOINT: return Material.NETHER_BRICK_FENCE;
            case GRAPPLER:return Material.FISHING_ROD;
            case TARZAN: return Material.VINE;
            case BLAZE: return Material.BLAZE_POWDER;
            case HADES: return Material.ZOMBIE_HEAD;
            case ACROBAT: return Material.LEATHER_BOOTS;
            case GAMBLER: return Material.STONE_BUTTON;
            case EVOKER: return Material.ENCHANTED_BOOK;
            case PROPHET: return Material.WATER_BUCKET;
            case CRAFTER: return Material.CRAFTING_TABLE;
            case RADAR: return Material.MAP;
            case XRAY: return Material.GLASS;
            //case PHASER: return Material.GLASS;
            case GLADIATOR: return Material.IRON_INGOT;
            case WISP: return Material.DIAMOND;
            case ENGINEER: return Material.PISTON_HEAD;
            case STAND: return Material.KELP;
            case LAUNCHER: return Material.SPONGE;
        }
        return Material.AIR;
    }
/*

           case GAMBLER: desc.add("You get a special button...");
                          desc.add("Will it help you or will it harm you?");
                          return desc;
            case ACROBAT: desc.add("Turn solid blocks unsolid at will!");
                return desc;
            case HADES: desc.add("Summon an army of");
                    desc.add(" the undead to aid you!");
                return desc;
            case BLAZE: desc.add("Go blazingly fast and make a trail of fire");
                        desc.add("behind you to burn your enemies!");
                return desc;
            case TARZAN: desc.add("Confound your enemies by hopping from");
                         desc.add("tree to tree without detection!");
                return desc;
            case MADMAN: desc.add("The perfect anti-team kit!");
                         desc.add("Create an aura that confuses, blinds,");
                         desc.add("and weakens your enemies!");
                return desc;
            case ACHILLES: desc.add("This kit makes you nearly invincible");
                           desc.add("with one fatal weakness!");
                return desc;
            case VAMPIRE: desc.add("Steal your enemy's health after");
                          desc.add("killing them!");
                return desc;
            case GRANDPA: desc.add("Get a Knockback II stick on spawn!");
                return desc;
            case FORGER: desc.add("Instantly smelt all ores upon mining!");
                return desc;
            case ARCHER: desc.add("Spawn with a Bow and 10 free Arrows!");
                return desc;
            case VIPER: desc.add("Poison your enemies while fighting them!");
                return desc;
            case URGAL: desc.add("Spawn with 2 free Strength potions!");
                return desc;
            case DWARF: desc.add("Hold shift to charge up your shockwave");
                        desc.add("and release to send enemies flying away!");
                return desc;
            case MONSTER: desc.add("Monsters are your friends...");
                return desc;
            case WEREWOLF: desc.add("Become a fearsome monster when the moon comes out!");
                return desc;
            case LUMBERJACK: desc.add("Spawn with a Wooden Axe,");
                    desc.add(" mine all trees instantly!");
                return desc;
            case CULTIVATOR: desc.add("Grow saplings and all crops instantly!");
                return desc;
            case BURROWER: desc.add("Create a Panic room in an instant!");
                return desc;
            case FIREMAN: desc.add("Become immune to Lightning");
                    desc.add(" and all Fire damage!");
                return desc;
            case JUMPER: desc.add("Spawn with 5 ");
                    desc.add("damage-free Enderpearls!");
                return desc;
            case STOMPER: desc.add("Take limited fall damage!");
                        desc.add("Jump on your enemies to");
                          desc.add("deliver the damage you");
                          desc.add("would have taken onto them!");
                return desc;
            case ANCHOR: desc.add("Take and deliver no knockback");
            desc.add("to other players!");
                return desc;
            case SCOUT: desc.add("Spawn with 2 Speed");
                        desc.add("and Leaping potions!");
                        desc.add("Take limited fall damage");
                        desc.add("while having speed effect!");
                return desc;
            case NINJA: desc.add("Surprise your enemies");
                    desc.add(" by shifting after hitting");
                    desc.add(" them within ten seconds,");
                        desc.add(" to teleport behind them!");
                return desc;
            case MINER: desc.add("Spawn with a Stone Pickaxe");
                    desc.add(" with Unbreaking III and");
                        desc.add("mending! Mine all ore");
                                desc.add(" veins instantly!");
                return desc;
            case CHAMELEON: desc.add("Have no nametag,");
            desc.add(" and turn invisible by");
            desc.add(" standing still!");
                return desc;
            case BLINK: desc.add("Teleport instantly in the direction");
                    desc.add(" you are pointing in!");
                return desc;
            case PHANTOM: desc.add("Take flight for a few");
                    desc.add("seconds to travel far away!");
                return desc;
            case THOR: desc.add("Spawn with a wooden axe,");
                    desc.add("right click a block");
                    desc.add("to strike Lightning!");
                return desc;
            case DEMOMAN: desc.add("Stone Pressure Plate on");
                desc.add(" top of Gravel = Big Boom");
                return desc;
            case TURTLE: desc.add("Shift to take limited damage, but be aware");
                        desc.add("that you can't attack while shifting!");
                return desc;
            case KAYA: desc.add("Trick and trap your");
                    desc.add(" enemies with fake Grass Blocks!");
                return desc;
            case COPYCAT: desc.add("Start with no kit, kill");
                desc.add("others and take their kit!");
                return desc;
            case SURPRISE: desc.add("Get a random kit,");
                    desc.add("even ones that you");
                    desc.add("haven't unlocked!");
                return desc;
            case SWITCHER: desc.add("Start with 10 Snowballs,");
                    desc.add("throw them at someone to");
                    desc.add("switch places!");
                return desc;
            case SPY: desc.add("Become a master of espionage");
                    desc.add("and learn everything about");
                      desc.add(" your nearby enemies!");
                return desc;
            case PYRO: desc.add("Spawn with a Flint");
                    desc.add("and Steel as well");
                    desc.add("as five Fireballs!");
                return desc;
            case NONE: desc.add("Nothing!");
                return desc;
            case BOXER: desc.add("Your fists do as much damage as");
                    desc.add("a stone sword and you take half");
                        desc.add("a heart less damage than normal!");
                return desc;
            case CHECKPOINT: desc.add("Teleport back to your");
                desc.add("checkpoint by placing");
                             desc.add("it and right clicking");
                desc.add(" your button!");
                return desc;
            case OBFUSCATOR: desc.add("Confuse your enemies by");
                    desc.add("temporarily disabling");
                             desc.add("all nearby enemy");
                                     desc.add("compasses, including Spies!");
                return desc;
            case PICKPOCKET: desc.add("Spawn with a Blaze Rod, ");
                    desc.add("which if you hit someone");
                             desc.add("with, will replace the item");
                desc.add(" in hand with a random item");
                             desc.add("from their inventory!");
                return desc;
            case BEASTMASTER: desc.add("Spawn with 3 Wolf Spawn Eggs");
                              desc.add("and 20 bones, get more");
                              desc.add("Wolf Eggs on kill!");
                return desc;
            case NEO: desc.add("Deflect all projectiles");
                    desc.add(" to the original shooter!");
                return desc;
            case TANK: desc.add("Become immune to explosion damage, and");
                       desc.add("make your enemies explode by killing them!");
                return desc;
            case WORM: desc.add("Instantly break Dirt blocks, which feeds you by one Food Bar each!");
                return desc;
            case MAGMA: desc.add("Set your enemies on");
                    desc.add(" fire by hitting them!");
                return desc;
            case HERMIT: desc.add("Spawn far away when Grace period starts!");
                return desc;
            case BESERKER: desc.add("Gain Strength II and");
                desc.add("Speed II on each kill!");
                return desc;
            case CANNIBAL: desc.add("Give your enemies hunger when hitting them,");
                           desc.add("and feed yourself by hitting others!");
                return desc;
            case GRAPPLER: desc.add("Spawn with a grappling hook");
                desc.add(" to get anywhere!");
                return desc;
            case POSEIDON: desc.add("Water is your natural habitat,");
                            desc.add("but it does make you hungry...");
                return desc;
*/

    public static List<String> kitDescription (Kits kits) {
        List<String> desc = new ArrayList<>();
        switch (kits) {
            case GAMBLER: desc.add("You get a special button... Will it help you or will it harm you?");
                          return desc;
            case ACROBAT: desc.add("Turn solid blocks unsolid at will! Hold shift to take half the fall damage!");
                return desc;
            case HADES: desc.add("Kill monsters to add them to your undead army!");
                return desc;
            case BLAZE: desc.add("Go blazingly fast and make a trail of fire behind you to burn your enemies!");
                return desc;
            case TARZAN: desc.add("Confound your enemies by hopping from tree to tree with added speed!");
                return desc;
            case MADMAN: desc.add("The perfect anti-team kit! Create an aura that confuses, blinds, and slows your enemies!");
                return desc;
            case ACHILLES: desc.add("This kit makes you nearly invincible with one fatal weakness!");
                return desc;
            case VAMPIRE: desc.add("Steal your enemy's health after killing them!");
                return desc;
            case GRANDPA: desc.add("Get a Knockback II stick on spawn!");
                return desc;
            case FORGER: desc.add("Instantly smelt all ores upon mining!");
                return desc;
            case ARCHER: desc.add("Spawn with a Bow and 10 free Arrows! Receive 2 extra Arrows every 2 minutes!");
                return desc;
            case VIPER: desc.add("Poison your enemies while fighting them!");
                return desc;
            case URGAL: desc.add("Spawn with 2 free Strength potions!");
                return desc;
            case DWARF: desc.add("Hold shift to charge up your shockwave and release to send enemies flying away!");
                return desc;
            case MONSTER: desc.add("Monsters are your friends...");
                return desc;
            case WEREWOLF: desc.add("Become a fearsome monster when the moon comes out!");
                return desc;
            case LUMBERJACK: desc.add("Spawn with a Wooden Axe, mine all trees instantly!");
                return desc;
            case CULTIVATOR: desc.add("Grow saplings and all crops instantly!");
                return desc;
            case BURROWER: desc.add("Create a Panic room in an instant!");
                return desc;
            case FIREMAN: desc.add("Become immune to Lightning and all Fire damage!");
                return desc;
            case JUMPER: desc.add("Spawn with 5 damage-free Enderpearls!");
                return desc;
            case STOMPER: desc.add("Take limited fall damage! Jump on your enemies to deliver the damage you would have taken onto them!");
                return desc;
            case ANCHOR: desc.add("Take and deliver no knockback to other players!");
                return desc;
            case SCOUT: desc.add("Spawn with 2 Speed and Leaping potions! Take limited fall damage while you have the Speed effect! Double Jump!");
                return desc;
            case NINJA: desc.add("Surprise your enemies by shifting within ten seconds after hitting them, to teleport behind them!");
                return desc;
            case MINER: desc.add("Spawn with a Stone Pickaxe with Unbreaking III and mending! Mine all ore veins instantly!");
                return desc;
            case CHAMELEON: desc.add("Turn invisible by standing still!");
                return desc;
            case BLINK: desc.add("Teleport instantly to the place you are looking at!");
                return desc;
            case PHANTOM: desc.add("Take flight for a few seconds to travel far away!");
                return desc;
            case THOR: desc.add("Spawn with a wooden axe, right click a block to strike Lightning!");
                return desc;
            case DEMOMAN: desc.add("Stone Pressure Plate on top of Gravel = Big Boom");
                return desc;
            case TURTLE: desc.add("Shift to take limited damage, but be aware that you can't attack while shifting!");
                return desc;
            case KAYA: desc.add("Trick and trap your enemies with fake Grass Blocks!");
                return desc;
            case COPYCAT: desc.add("Start with no kit, kill others and take their kit!");
                return desc;
            case SURPRISE: desc.add("Get a random kit, even ones that you haven't unlocked yet!");
                return desc;
            case SWITCHER: desc.add("Start with 10 Snowballs, throw them at someone to switch places!");
                return desc;
            case SPY: desc.add("Become a master of espionage and learn everything about your nearby enemies! Be able to see hidden Chameleons! Infinite Compass Range! See enemy kits and health!");
                return desc;
            case PYRO: desc.add("Spawn with a Flint and Steel as well as five Fireballs!");
                return desc;
            case NONE: desc.add("Nothing!");
                return desc;
            case BOXER: desc.add("Your fists do as much damage as a stone sword and you take half a heart less damage than normal!");
                return desc;
            case CHECKPOINT: desc.add("Teleport back to your checkpoint by placing it and right clicking your button!");
                return desc;
            case HACKER: desc.add("Confuse your enemies by temporarily disabling all nearby enemy compasses, including Spies! Disable all nearby enemy kit abilities!");
                return desc;
            case PICKPOCKET: desc.add("Spawn with a Blaze Rod, which if you hit someone with, will replace the item in hand with a random item from their inventory!");
                return desc;
            case BEASTMASTER: desc.add("Spawn with 3 Wolf Spawn Eggs and 20 bones, get extra Wolf Eggs on each kill!");
                return desc;
            case NEO: desc.add("Deflect all projectiles to the original shooter!");
                return desc;
            case TANK: desc.add("Become immune to explosion damage, and make your enemies explode by killing them!");
                return desc;
            case WORM: desc.add("Instantly break Dirt blocks, which feeds you by one Food Bar each!");
                return desc;
            case MAGMA: desc.add("Set your enemies on fire by hitting them!");
                return desc;
            case HERMIT: desc.add("Spawn far away when Grace period starts!");
                return desc;
            case BESERKER: desc.add("Gain Strength II and Speed II on each kill!");
                return desc;
            case CANNIBAL: desc.add("Give your enemies hunger when hitting them, and feed yourself by hitting others!");
                return desc;
            case GRAPPLER: desc.add("Spawn with a grappling hook to get anywhere!");
                return desc;
            case POSEIDON: desc.add("Water is your natural habitat, but it does make you hungry...");
                return desc;
            case EVOKER: desc.add("Summon your mighty fangs against your enemies!");
                return desc;
            case PROPHET: desc.add("Walk on water!");
                return desc;
            case CRAFTER: desc.add("Use your portable Crafting Table and Furnace as you please!");
                return desc;
            //case PHASER: desc.add("Walk through solid walls!");
            //return desc;
            case XRAY: desc.add("Turn on your X-Ray vision for easy mining!");
            return desc;
            case RADAR: desc.add("Have a minimap that scans for nearby players!");
            return desc;
            case GLADIATOR: desc.add("Right click players with your Dueler ingot to 1v1 them!");
            return desc;
            case WISP: desc.add("Spawn decoys of yourself to confuse your enemies!");
            return desc;
            case ENGINEER: desc.add("Place Sentry Guns down to shoot down your enemies! Upgrade your Sentries with your handy wrench!");
            return desc;
            case STAND: desc.add("Summon a mighty Stand to aid you in battle! Increase your attack range! Ride your stand for a portable boat!");
            return desc;
            case LAUNCHER: desc.add("Use launchpads to boost yourself and your friends!");
        }
        return desc;


    }

    public String getKitLowercase (Kits kits) {
        return kits.toString().toLowerCase();

    }

    public String getKitUnformatted (Kits kits) {
        String str = kits.toString().toLowerCase();
        str = str.substring(0, 1).toUpperCase() + str.substring(1);
        return str;

    }
    public boolean isKitUnlocked (Player p, Kits kits) {
        Game game = Game.getSharedGame();
        if (game.getAllKitsFree()) {
            return true;
        }
        if (kits == Kits.NONE) { return true; }
        User user = HardcoreGames.getLuckPermsInstance().getUserManager().getUser(p.getUniqueId());
        if (user != null) {
            return hasPermission(user,
            "hardcoregames." + getKitLowercase(kits));
        }
        return false;
    }
    public boolean hasPermission(User user, String permission) {
        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }
    public List<Kits> unlockedKits (Player p) {
        List<Kits> unlocked = new ArrayList<>();
        for (Kits kits : Kits.values()) {
            if (isKitUnlocked(p, kits)) {
                unlocked.add(kits);
            }
        }
        return unlocked;
    }

    public List<Kits> lockedKits (Player p) {
        List<Kits> locked = new ArrayList<>();
        for (Kits kits : Kits.values()) {
            if (!isKitUnlocked(p, kits)) {
                locked.add(kits);
            }
        }
        return locked;
    }

    public List<String> getNamesUnlockedKits (Player p) {
        List<String> unlockedKits = new ArrayList<>();
        for (Kits kits : unlockedKits(p)) {
            String str = kits.toString().toLowerCase();
            unlockedKits.add(str.substring(0, 1).toUpperCase() + str.substring(1));

        }
        return unlockedKits;
    }


    public void clearKits() {
        kitMap.clear();
        surprises.clear();
        copycats.clear();
    }
}
