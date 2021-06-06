package Main;

import Feast.MainFeast;
import KitGUI.InventoryListener;
import Kits.KitListeners.Kits.Attack.*;
import Kits.KitListeners.Kits.Defense.*;
import Kits.KitListeners.Kits.Utility.*;
import Kits.KitListeners.Kits.Vanity.*;
import Listeners.*;
import Messages.DeathMessageHandler;
import Messages.Scoreboard;
import Robots.NPCKilledHandler;
import Robots.RobotTrait;
import Util.Game;
import com.shanebeestudios.vf.api.FurnaceManager;
import com.shanebeestudios.vf.api.RecipeManager;
import com.shanebeestudios.vf.api.TileManager;
import com.shanebeestudios.vf.api.VirtualFurnaceAPI;
import com.shanebeestudios.vf.api.recipe.Fuel;
import com.shanebeestudios.vf.api.recipe.FurnaceRecipe;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class HardcoreGames extends JavaPlugin {
    private static HardcoreGames instance;

    private static LuckPerms luckPermsInstance;

    Recipes recipes = new Recipes();
    Game game = Game.getSharedGame();
    Config config = new Config();
    PlayerStats stats = PlayerStats.getStats();
    private VirtualFurnaceAPI virtualFurnaceAPI;
    private FurnaceManager furnaceManager;
    private RecipeManager recipeManager;
    private TileManager tileManager;
    @Override
    public void onEnable() {
        instance = this;
        this.virtualFurnaceAPI = new VirtualFurnaceAPI(this);
        this.recipeManager = virtualFurnaceAPI.getRecipeManager();
        this.furnaceManager = virtualFurnaceAPI.getFurnaceManager();
        this.tileManager = virtualFurnaceAPI.getTileManager();
        registerRecipes();
        registerFuels();

        if(getServer().getPluginManager().getPlugin("Citizens") == null || getServer().getPluginManager().getPlugin("Citizens").isEnabled() == false) {
            getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        //Register your trait with Citizens.
        net.citizensnpcs.api.CitizensAPI.getTraitFactory().registerTrait(net.citizensnpcs.api.trait.TraitInfo.create(RobotTrait.class).withName("hungerbot"));


        boolean lp = Bukkit.getServer().getPluginManager().isPluginEnabled("LuckPerms");
        if (lp) {
            RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            if (provider != null) {
                luckPermsInstance = provider.getProvider();

            }
        }


        this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(), this);
        this.getServer().getPluginManager().registerEvents(new DeathListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDamageEvent(), this);
        this.getServer().getPluginManager().registerEvents(new PortalListener(), this);
        this.getServer().getPluginManager().registerEvents(new ItemPickupListener(), this);
        this.getServer().getPluginManager().registerEvents(new MobListener(), this);
        this.getServer().getPluginManager().registerEvents(new AnvilListener(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryClickEvent(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        this.getServer().getPluginManager().registerEvents(new BreedListener(), this);
        this.getServer().getPluginManager().registerEvents(new MainFeast(), this);



        this.getServer().getPluginManager().registerEvents(new DeathMessageHandler(), this);
        //this.getServer().getPluginManager().registerEvents(new HDBHandler(), this);


        this.getCommand("hardcoregames").setExecutor(new Commands());

        this.getCommand("hardcoregames").setTabCompleter(new CommandsTab());

        this.getCommand("kit").setExecutor(new KitCommand());

        this.getCommand("kit").setTabCompleter(new KitTabComplete());

        this.getCommand("help").setExecutor(new HelpCommand());

        this.getCommand("help").setTabCompleter(new HelpTabComplete());

        this.getCommand("stats").setExecutor(new StatsCommand());

        this.getCommand("stats").setTabCompleter(new StatsTabComplete());

        this.getServer().getPluginManager().registerEvents(new Scoreboard(), this);

        //Kit Listeners
        this.getServer().getPluginManager().registerEvents(new Miner(), this);
        this.getServer().getPluginManager().registerEvents(new Demoman(),this);
        this.getServer().getPluginManager().registerEvents(new Spy(), this);
        this.getServer().getPluginManager().registerEvents(new Vampire(), this);
        this.getServer().getPluginManager().registerEvents(new Beserker(), this);
        this.getServer().getPluginManager().registerEvents(new Achilles(), this);
        this.getServer().getPluginManager().registerEvents(new Worm(), this);
        this.getServer().getPluginManager().registerEvents(new Beastmaster(), this);
        this.getServer().getPluginManager().registerEvents(new Jumper(), this);
        this.getServer().getPluginManager().registerEvents(new Neo(), this);
        this.getServer().getPluginManager().registerEvents(new Hacker(), this);
        this.getServer().getPluginManager().registerEvents(new Turtle(), this);
        this.getServer().getPluginManager().registerEvents(new Poseidon(), this);
        this.getServer().getPluginManager().registerEvents(new Monster(), this);
        this.getServer().getPluginManager().registerEvents(new Pyro(), this);
        this.getServer().getPluginManager().registerEvents(new Switcher(), this);
        this.getServer().getPluginManager().registerEvents(new Lumberjack(), this);
        this.getServer().getPluginManager().registerEvents(new Ninja(), this);
        this.getServer().getPluginManager().registerEvents(new Tank(), this);
        this.getServer().getPluginManager().registerEvents(new Viper(), this);
        this.getServer().getPluginManager().registerEvents(new Magma(), this);
        this.getServer().getPluginManager().registerEvents(new Kaya(), this);
        this.getServer().getPluginManager().registerEvents(new Pickpocket(), this);
        this.getServer().getPluginManager().registerEvents(new Boxer(), this);
        this.getServer().getPluginManager().registerEvents(new Cultivator(), this);
        this.getServer().getPluginManager().registerEvents(new Forger(), this);
        this.getServer().getPluginManager().registerEvents(new Burrower(), this);
        this.getServer().getPluginManager().registerEvents(new Cannibal(), this);
        this.getServer().getPluginManager().registerEvents(new Copycat(), this);
        this.getServer().getPluginManager().registerEvents(new Fireman(), this);
        this.getServer().getPluginManager().registerEvents(new Thor(), this);
        this.getServer().getPluginManager().registerEvents(new Stomper(), this);
        this.getServer().getPluginManager().registerEvents(new Dwarf(), this);
        this.getServer().getPluginManager().registerEvents(new Anchor(), this);
        this.getServer().getPluginManager().registerEvents(new Blink(), this);
        this.getServer().getPluginManager().registerEvents(new Phantom(), this);
        this.getServer().getPluginManager().registerEvents(new Scout(), this);
        this.getServer().getPluginManager().registerEvents(new Chameleon(), this);
        //this.getServer().getPluginManager().registerEvents(new Skywalker(), this);
        this.getServer().getPluginManager().registerEvents(new Checkpoint(), this);
        this.getServer().getPluginManager().registerEvents(new Tarzan(), this);
        this.getServer().getPluginManager().registerEvents(new Grappler(), this);
        this.getServer().getPluginManager().registerEvents(new Blaze(), this);
        this.getServer().getPluginManager().registerEvents(new Gambler(), this);
        this.getServer().getPluginManager().registerEvents(new Madman(), this);
        this.getServer().getPluginManager().registerEvents(new Hades(), this);
        this.getServer().getPluginManager().registerEvents(new Acrobat(), this);
        this.getServer().getPluginManager().registerEvents(new Evoker(), this);
        this.getServer().getPluginManager().registerEvents(new Prophet(), this);
        this.getServer().getPluginManager().registerEvents(new Crafter(), this);
        this.getServer().getPluginManager().registerEvents(new Radar(), this);
        this.getServer().getPluginManager().registerEvents(new Xray(), this);
        this.getServer().getPluginManager().registerEvents(new Gladiator(), this);
        this.getServer().getPluginManager().registerEvents(new Wisp(), this);
        //this.getServer().getPluginManager().registerEvents(new Phaser(), this);
        //deprecated
        this.getServer().getPluginManager().registerEvents(new Engineer(), this);
        this.getServer().getPluginManager().registerEvents(new Stand(), this);
        this.getServer().getPluginManager().registerEvents(new Launcher(), this);





        //robots
        this.getServer().getPluginManager().registerEvents(new NPCKilledHandler(), this);


        for (ShapelessRecipe recipe : recipes.allSoups()) {
            this.getServer().addRecipe(recipe);
        }


        game.startGame(game);

        HardcoreGames.getInstance().saveDefaultConfig();

        config.setConfig();

        setUpDataFolder();

        stats.loadStats();


        super.onEnable();
    }
    public VirtualFurnaceAPI getVirtualFurnaceAPI() {
        return virtualFurnaceAPI;
    }
    public FurnaceManager getFurnaceManager () {
        return furnaceManager;
    }
    private void registerRecipes() {
        for (FurnaceRecipe recipe : FurnaceRecipe.getVanillaFurnaceRecipes()) {
            this.recipeManager.registerFurnaceRecipe(recipe);
        }
    }

    private void registerFuels() {
        for (Fuel fuel : Fuel.getVanillaFuels()) {
            this.recipeManager.registerFuel(fuel);
        }
    }

    public void setUpDataFolder () {
        if(!this.getDataFolder().exists()) {
            try {
                this.getDataFolder().mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static HardcoreGames getInstance() {
        return instance;
    }

    public static LuckPerms getLuckPermsInstance() {return luckPermsInstance;}

//    @Override
//    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
//        return (ChunkGenerator) WorldGeneratorApi // (1)
//                .getInstance(this, 0, 5) // (2)
//                .createCustomGenerator(WorldRef.ofName(worldName), generator -> { // (3)
//
//                    generator.setBaseTerrainGenerator(new TerrainGenerator()); // (5)
//                    this.getLogger().info("Enabled the Terrain generator for world \"" + worldName + "\""); // (6)
//
//                });
//    }
//    // End of addition to class
//    static void replaceBiome(String toRemove, Biomes toPlace) throws Exception {
//        Field field = Biomes.class.getDeclaredField(toRemove);
//        field.setAccessible(true);
//
//        Field modifiersField = Field.class.getDeclaredField("modifiers");
//        modifiersField.setAccessible(true);
//        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
//        field.set(null, toPlace);
//    }
//    public static void replaceBiomes(String[] toRemove, Biomes toPlace) throws Exception {
//        for(String s : toRemove) {
//            replaceBiome(s, toPlace);
//        }
//    }
//    public static void setupBiomes() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
//
//    }
}
