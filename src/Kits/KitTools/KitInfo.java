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
            case PORTAL: return Material.PURPLE_WOOL;
            case VULTURE: return Material.COMPASS;
            case PEANUT: return Material.GREEN_WOOL;
            case IMPOSTER: return Material.RED_WOOL;
            case PLAGUE: return Material.GREEN_DYE;
            case BRIDGER: return Material.EGG;
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
    public static List<String> kitsHelp (Kits kits) {
        List<String> help = new ArrayList<>();
        switch (kits) {
            case PLAGUE: help.add("To use this kit, hit other players with your infection syringe to infect them. These infected players will infect other players, and when all alive players are infected with the Plague, you will be transformed into a powerful entity with many strong effects. Hitting an already infected player with the syringe will give them 30 seconds of poison.");
            return help;
            case VULTURE: help.add("To use this kit, bet on a player with your Death Bet Compass. If that player dies, you will receive all of their drops. Keep in mind, however, you can only bet once. You can also use the Death Log book to see everyone that died and the coordinates of where they died.");
            return help;
            case PORTAL: help.add("To use this kit, place both of your portals down to be able to go through them at will. You can right click a portal to drop the portal block and replace it.");
            return help;
            case LAUNCHER: help.add("To use this kit, simply place the sponges down. You can now stand on the Launchers to be boosted into the air to get to those hard-to-get places! You also will not take fall damage while launching.");
            return help;
            case STAND: help.add("To use this kit, simply click with your crosshair at an entity or player, and watch your Stand do the fighting for you! However, be aware that if your Stand is damaged, you will take damage as well.");
            return help;
            case BLAZE: help.add("To use this kit, right click your blaze powder to get Speed 2 and fire immunity. All blocks under your will instantly be ignited, so be sure to use it to escape people that might be chasing you!");
            return help;
            case PHANTOM: help.add("To use this kit, right click your Phantom Feather to be able to take flight for a few seconds!");
            return help;
            case ENGINEER: help.add("To use this kit, place down your Sentry Gun to build it. Collect iron ingot and hit your Sentry Gun with your wrench to upgrade it. Keep in mind that you can move your Sentry Gun by right clicking it to pick it up, but re-building it will start it at Level 1. Your Sentry can only shoot what it can see in range, but upgrading your Sentry will increase range, damage, and fire-rate. Be aware that if your Sentry takes any damage, it will be temporarily disabled.");
            return help;
            case WISP: help.add("To use this kit, right-click your diamond down on a block to spawn a decoy of yourself. These decoys will last a minute each, and will walk around randomly. They also will disappear if hit, and enemy compasses will point towards them.");
            return help;
            case GLADIATOR: help.add("To use this kit, right click a player during the PVP phase to challenge them to a one-on-one fight. You will be teleported to an arena where you can fight for 30 seconds, or until someone dies, before being teleported back. This is very useful for team situations and taking out a team one-by-one.");
            return help;
            case XRAY: help.add("To use this kit, throw your X-Ray Eggs at the ground to turn them into glass, to easily find ores all around you!");
            return help;
            case RADAR: help.add("To use this kit, right click your map to be able to see all nearby players in your vicinity, and their general location!");
            return help;
            case CRAFTER: help.add("To use this kit, right click your crafting table or furnace to have a portable furnace and crafting table at all times!");
            return help;
            case PROPHET: help.add("To use this kit, simply walk on water to be able to run on it like magic! The blocks under you are only visible to yourself. Keep in mind that you will also receive hunger effect while walking on water.");
            return help;
            case POSEIDON: help.add("To use this kit, try to stay near water whenever possible. You will gain PVP and movement advantages, so be sure to stay near water during fights!");
            return help;
            case GRAPPLER: help.add("To use this kit, right click your fishing rod to a block and pull back to boost to hard-to-reach places! You can also hook players, animals, and items.");
            return help;
            case CANNIBAL: help.add("To use this kit, gain hunger bars back by hitting other players. Hitting other players also gives them a hunger effect for 30 seconds.");
            return help;
            case BESERKER: help.add("To use this kit, kill other players to receive a powerful Speed II and Strength II effect for a few seconds. Use this to your advantage to take out big teams!");
            return help;
            case HERMIT: help.add("To use this kit, simply pick it before the game starts to be teleported far away from all other players! This will make you hard to find and able to get all the resources you need without worrying about other players.");
            return help;
            case MAGMA: help.add("To use this kit, hit other players to have a chance at lighting them on fire!");
            return  help;
            case WORM: help.add("To use this kit, instantly mine all Dirt blocks by clicking on them! Dirt blocks are considered food for yourself, so be sure to mine Dirt when you are hungry.");
            return help;
            case TANK: help.add("To use this kit, kill other players to create a massive explosion! You are also completely immune to explosion damage, so be sure to use this to your advantage when taking out big teams.");
            return help;
            case NEO: help.add("To use this kit, be sure to hunt down anyone that is known to use projectile items. All and any projectiles that are not shot by you that come into your range will be deflected back at the shooter at maximum speed!");
            return help;
            case BEASTMASTER: help.add("To use this kit, tame your wolves with bones, and gain more wolf eggs by killing other players!");
            return help;
            case PICKPOCKET: help.add("To use this kit, hit other players with your Blaze Rod Wand to replace the item in their hand with a random item from their inventory!");
            return help;
            case CHECKPOINT: help.add("To use this kit, place down your Checkpoint, then right click your button to teleport back to it at anytime! Be warned, however, other players can trap or break your Checkpoint, making it useless.");
            return help;
            case BOXER: help.add("To use this kit, punch other players with your bare fists for extra damage! Also enjoy limited incoming damage from other players!");
            return help;
            case NONE: help.add("You don't currently have a kit! Be sure to pick a kit before the game starts with /kit!");
            return help;
            case PYRO: help.add("To use this kit, right click your Fire Balls at your enemies to throw a Fire Ball at them! Use your Firestarter to gain a PVP advantage by lighting your enemies on fire!");
            return help;
            case SPY: help.add("To use this kit, use information on other players to your advantage! Right click your Spy Compass to see the nearest players and their exact distances from you, as well as their kit and health! Your compass also has unlimited range and can find any player, no matter how far away they are!");
            return help;
            case SWITCHER: help.add("To use this kit, throw your Switcher Snowballs at other players to switch positions with them! Trap other players by throwing a snowball at them, then jumping into lava!");
            return help;
            case SURPRISE: help.add("If you picked this kit, you will automatically have your kit set to a random kit, including ones you don't own!");
            return help;
            case COPYCAT: help.add("To use this kit, kill other players to steal their kits and their abilities! Be sure to pick up their kit items as they will now be useful to you!");
            return help;
            case KAYA: help.add("To use this kit, use your Kaya Grass Blocks to construct pitfall traps and tower traps. These Grass Blocks will instantly disappear when a non-Kaya walks on them, which allows elaborate traps to be constructed for an easy win! Use your decoys to trick others into thinking that the blocks they walk on are safe!");
            return help;
            case TURTLE: help.add("To use this kit, shift to take limited damage! However, you are unable to fight back in this state.");
            return help;
            case DEMOMAN: help.add("To use this kit, construct a landmine by placing any Stone Pressure Plate on top of any Gravel, and watch your enemies explode after walking on them!");
            return help;
            case THOR: help.add("To use this kit, simply right click the ground with your Thor's Axe to spawn lightning on your enemies! Perfect for taking out towers!");
            return help;
            case BLINK: help.add("To use this kit, simply look at the direction that you want to teleport in, and right click your Redstone Torches! Keep in mind that you will receive severe weakness after each teleport, so be careful!");
            return help;
            case CHAMELEON: help.add("To use this kit, you will automatically turn invisible in the PVP phase, as long as you stand still or don't take damage. Use this to your advantage and stand still when escaping chasers!");
            return help;
            case MINER: help.add("To use this kit, use your stone pickaxe to mine ores to instantly mine them! Grab resources with ease and go out to fight!");
            return help;
            case NINJA: help.add("To use this kit, hit other players and press shift to instantly teleport behind them! Use this to your advantage to catch your enemies by surprise!");
            return help;
            case SCOUT: help.add("To use this kit, double jump to reach hard-to-get places! You will also take limited fall damage while you have the Speed effect!");
            return help;
            case ANCHOR: help.add("To use this kit, you will no longer take knockback or deal knockback, so be sure to use this to your advantage!");
            return help;
            case STOMPER: help.add("To use this kit, jump down from tall locations onto players or mobs to deal significant damage! The maximum damage you can take from fall damage with this kit is 2 hearts, but for anyone that was stomped on, will take damage proportional to the amount that you fell.");
            return help;
            case JUMPER: help.add("To use this kit, use your damage-free enderpearls at spawn to get away as soon as possible! Be sure to kill endermen to get more enderpearls!");
            return help;
            case FIREMAN: help.add("To use this kit, use your water bucket to your advantage! You also don't take fire damage, or lightning damage!");
            return help;
            case BURROWER: help.add("To use this kit, create a panic room by right clicking your egg! Use this after you have a pickaxe to mine ores with ease, or during a fight to escape!");
            return help;
            case CULTIVATOR: help.add("To use this kit, plant seeds or saplings to instantly grow them! Use this when making a sky tower to never run out of food or materials!");
            return help;
            case LUMBERJACK: help.add("To use this kit, use your axe to instantly mine whole trees by mining one block! Use this to get rid of players hiding in trees, or to get wood quickly!");
            return help;
            case WEREWOLF: help.add("To use this kit, be sure to hunt at night, when you are most powerful! Gain Speed II and Strength I at night, but be warned that all nearby players will be alerted of your presence!");
            return help;
            case MONSTER: help.add("To use this kit, be sure to hang out with monsters at night! Monsters will no longer target you, but will collectively target whoever you hit in range! Use this to swarm enemy players with armies of mobs!");
            return help;
            case DWARF: help.add("To use this kit, hold shift to charge up your shockwave! Release shift to send all nearby players flying!");
            return help;
            case URGAL: help.add("To use this kit, go all-out on the offensive with your Strength II splash potions!");
            return help;
            case VIPER: help.add("To use this kit, hit other players for a chance to give them poison for a few seconds!");
            return help;
            case ARCHER: help.add("To use this kit, take out big towers with your bow and arrows! You will also receive more arrows every 2 minutes!");
            return help;
            case FORGER: help.add("To use this kit, use your pickaxe to mine any ore and get the mineral without smelting it!");
            return help;
            case GRANDPA: help.add("To use this kit, keep enemies far away from you with your trusty Knockback II stick!");
            return help;
            case VAMPIRE: help.add("To use this kit, kill other players to completely replenish your health! Use this in team battles for efficient and easy healing!");
            return help;
            case ACHILLES: help.add("To use this kit, you will take significantly less damage than usual from other players! However, any wood-based item will act as if it's diamond, so be careful to not fight anyone with wood tools or gear!");
            return help;
            case MADMAN: help.add("To use this kit, stay in other players vicinities to bring them negative effects! However, be aware that all nearby players will be alerted of your presence!");
            return help;
            case TARZAN: help.add("To use this kit, stay on trees to gain Speed and Jump Boost effects! You will also take no fall damage when landing on leaves!");
            return help;
            case HADES: help.add("To use this kit, kill mobs to add them to your personal army! Your mobs will attack nearby players and fight them for you!");
            return help;
            case ACROBAT: help.add("To use this kit, turn all solid blocks into non-solid block temporarily! Use this to make traps and to confuse your enemies! You may also take half of your fall damage by shifting while falling!");
            return help;
            case GAMBLER: help.add("To use this kit, place your stone button and press it to receive a random perk! It may be either good or bad, ranging from effects, items, a new kit, or even a removal of your current kit.");
            return help;
            case EVOKER: help.add("To use this kit, right click your Enchanted Book to spawn evoker fangs that go in a straight line towards your enemies for massive damage!");
            return help;
            case HACKER: help.add("To use this kit, right click your Compass Blockers to disable all nearby enemy compasses! Right click your Kit Disabler to remove kits from nearby players temporarily!");
            return help;
            case IMPOSTER: help.add("To use this kit, right click an animal that you would like to disguise as! You can also right click nearby players to disguise as them. Use this kit to confuse teams and gain the upper hand! Look for vents (Iron Trapdoors) scattered all over the map to instantly teleport out of a bad situation!");
            return help;
            case PEANUT: help.add("To use this kit, use your speed and strength to sneak up behind enemies and finish them off quickly! Take care to not let them turn around, or you will get Slowness 7 in place of your speed and strength!");
            return help;
            case BRIDGER: help.add("To use this kit, throw your Bridge Eggs to instantly build bridges to wherever you threw them. Use this to get down from high places, or to easily reach tower teams!");
            return help;

        }
        return help;
    }

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
            case POSEIDON: desc.add("Water is your natural habitat!");
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
            return desc;
            case PORTAL: desc.add("Place your portals and go between them at will!");
            return desc;
            case VULTURE: desc.add("Place a bet on a player to die, and gain their drops after death! Get a death log to find the coordinates and cause of death!");
            return desc;
            case PLAGUE: desc.add("Spread your disease, if you successfully infect everyone, become Pestilence, Horseman of Death! You must wear your Doctor mask at all times!");
            return desc;
            case IMPOSTER: desc.add("Disguise as animals and catch enemies by surprise! Use vents to travel around quickly!");
            return desc;
            case PEANUT: desc.add("Gain incredible speed at all times, but be sure that no one looks at you!");
            return desc;
            case BRIDGER: desc.add("Use your Bridge Eggs to travel anywhere fast!");
            return desc;
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
