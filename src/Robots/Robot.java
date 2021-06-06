package Robots;

import Kits.KitListeners.Kits.Vanity.Surprise;
import Kits.KitTools.KitInfo;
import Util.Game;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.NavigatorParameters;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Inventory;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import net.citizensnpcs.trait.waypoint.Waypoints;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.mcmonkey.sentinel.SentinelTrait;

import java.util.*;

public class Robot {

    List<NPC> activeRobots = new ArrayList<>();
    //Map<NPC, ArrayList<ItemStack>> robotDrops = new HashMap<>();

    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();

    private Robot() { } // make your constructor private, so the only war
    // to access "application" is through singleton pattern

    private static Robot _robot;

    public static Robot getSharedRobots()
    {
        if (_robot == null)
            _robot = new Robot();
        return _robot;
    }

    public List<NPC> activeRobots() {
        return this.activeRobots;
    }
    public List<SentinelTrait> activeSentinels () {
        List<SentinelTrait> traits = new ArrayList<>();
        for (NPC npc : activeRobots()) {
            traits.add(npc.getOrAddTrait(SentinelTrait.class));
        }
        return traits;
    }

    public void addRobot (NPC npc) {
        this.activeRobots.add(npc);
    }

    public void removeRobot (NPC npc) {
        this.activeRobots.remove(npc);
    }

    public void wander (Location loc) {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, generateName());

        npc.spawn(loc);
        NavigatorParameters parameters = npc.getNavigator().getDefaultParameters();
        parameters.range(200);
        parameters.stuckAction(null);

        npc.getOrAddTrait(Waypoints.class).setWaypointProvider("wander");
            //npc.getOrAddTrait(WanderingNPC_Trait.class);

    }

    public void spawnRobot (Location loc) {

        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, generateName());



        npc.spawn(loc);
        npc.setProtected(false);
        npc.data().set(NPC.TARGETABLE_METADATA, true);

        npc.getOrAddTrait(LookClose.class).lookClose(true);
        npc.getOrAddTrait(LookClose.class).setDisableWhileNavigating(true);
        npc.getOrAddTrait(LookClose.class).setRandomLook(true);
        npc.getOrAddTrait(LookClose.class).setRange(1);
        npc.getOrAddTrait(LookClose.class).setRealisticLooking(true);

        npc.getOrAddTrait(Waypoints.class).setWaypointProvider("wander");

        NavigatorParameters parameters = npc.getNavigator().getDefaultParameters();
        parameters.range(200);
        parameters.straightLineTargetingDistance(3);
        parameters.avoidWater(true);


        //npc.;
        //prevent teleport
        parameters.stuckAction(null);

        Bukkit.broadcastMessage(ChatColor.YELLOW + npc.getName() + " joined the game");

        kitInfo.setPlayerKit(npc.getEntity(), Surprise.getRandomKit());

        //Bukkit.broadcastMessage(kitInfo.getPlayerKit(npc.getEntity()) + "kit");


        npc.getTraitNullable(SkinTrait.class).setSkinName(SkinPicker.getRandomSkin());




        game.addPlayer(npc.getUniqueId());

        //makeNPCBreak(npc, loc);

        addRobot(npc);
      //  resourcePhase(npc);


    }

//    public void setNPCTarget (NPC npc, Entity target) {
//        if (npc.isSpawned()) {
//            npc.getNavigator().cancelNavigation();
//            if (npc.getEntity().getLocation().distance(target.getLocation()) > 50) {
//                //travelToFarTarget(npc, target);
//            } else {
//                npc.getNavigator().setTarget(target, true);
//            }
//        }
//    }

//    public void travelToFarTarget (NPC npc, Entity target) {
//        if (npc.isSpawned()) {
//            npc.getNavigator().getPathStrategy().getPath().
//        }
//    }


//    public void addDrops (NPC npc, ItemStack itemStack) {
//        ArrayList<ItemStack> drops = robotDrops.get(npc);
//        SentinelTrait trait = npc.getOrAddTrait(SentinelTrait.class);
//        drops.add(itemStack);
//        trait.drops = drops;
//    }


//    public void makeNPCBreak (NPC npc, Location location) {
//        BlockBreaker.BlockBreakerConfiguration config = new BlockBreaker.BlockBreakerConfiguration();
//
//            BlockBreaker breaker = npc.getBlockBreaker(location.getBlock(), config);
//
//        if (breaker.shouldExecute()) {
//            TaskRunnable run = new TaskRunnable(breaker);
//            run.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(HardcoreGames.getInstance(), run, 0, 1);
//        }
//
//    }
//    private static class TaskRunnable implements Runnable {
//        private int taskId;
//        private final BlockBreaker breaker;
//
//        public TaskRunnable(BlockBreaker breaker) {
//            this.breaker = breaker;
//        }
//
//        @Override
//        public void run() {
//            if (breaker.run() != BehaviorStatus.RUNNING) {
//                Bukkit.getScheduler().cancelTask(taskId);
//                breaker.reset();
//            }
//        }
//    }
//    public void resourcePhase (NPC npc) {
//        //fake collect resources
//            Block target = null;
//            for (Block b : getNearbyBlocks(npc.getEntity().getLocation(), 5)) {
//                if (b.getType().name().toLowerCase().contains("log") || b.getType().name().toLowerCase().contains("leaves")) {
//                    target = b;
//                    break;
//                }
//            }
//            if (target != null) {
//                makeNPCBreak(npc, target.getLocation());
//            }
//
//
//    }
//    public static List<Block> getNearbyBlocks(Location location, int radius) {
//        List<Block> blocks = new ArrayList<Block>();
//        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
//            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
//                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
//                    blocks.add(location.getWorld().getBlockAt(x, y, z));
//                }
//            }
//        }
//        return blocks;
//    }

    public SentinelTrait assignSentinelStats(SentinelTrait sentinelTrait) {
        sentinelTrait.realistic = false;
        sentinelTrait.enemyDrops = true;
        sentinelTrait.needsAmmo = true;
        sentinelTrait.range = 600;
        sentinelTrait.respawnTime = -1;
        sentinelTrait.speed = 1.15;
        sentinelTrait.chaseRange = 600;
        sentinelTrait.retainTarget = false;
        sentinelTrait.disableTeleporting = true;
        sentinelTrait.allowKnockback = true;
        sentinelTrait.chased = true;
        sentinelTrait.closeChase = true;
       // sentinelTrait.avoidRange = 15;



        List<Integer> randomNumbers = new ArrayList<>();
        for (int i = 0; i < 6; i++)  {
            Random random = new Random();
            randomNumbers.add(random.nextInt(2));
        }
        sentinelTrait.rangedChase = randomNumbers.get(0) == 0;
        sentinelTrait.safeShot = randomNumbers.get(1)  == 0;
        sentinelTrait.fightback = randomNumbers.get(2)  == 0;
        sentinelTrait.autoswitch = false;
        //sentinelTrait.runaway = randomNumbers.get(5) == 0;

        Random random = new Random();

        sentinelTrait.attackRate = random.nextInt(5) + 1;

        return sentinelTrait;
    }

    public List<String> generatedNames () {
        List<String> names = new ArrayList<>();
        for (NPC npc : activeRobots) {
            names.add(npc.getName());
        }
        return names;
    }

    public String generateName () {

        List<String> firstPartName = new ArrayList<>();
        List<String> secondPartName = new ArrayList<>();
        int doubleDigits = 0;
        String completedname = "";
        do {
            firstPartName.add("butter");
            firstPartName.add("string");
            firstPartName.add("water");
            firstPartName.add("socks");
            firstPartName.add("gamer");
            firstPartName.add("sports");
            firstPartName.add("br");
            firstPartName.add("ender");
            firstPartName.add("king");
            firstPartName.add("xx");
            firstPartName.add("nether");
            firstPartName.add("piglin");
            firstPartName.add("Cod");
            firstPartName.add("joe");
            firstPartName.add("dad");
            firstPartName.add("mama");
            firstPartName.add("lil");
            firstPartName.add("dragon");
            firstPartName.add("girl");
            firstPartName.add("boy");
            firstPartName.add("vert");
            firstPartName.add("ava");
            firstPartName.add("potato");
            firstPartName.add("carrot");
            firstPartName.add("red");
            firstPartName.add("yellow");
            firstPartName.add("blue");
            firstPartName.add("orange");
            firstPartName.add("purple");
            firstPartName.add("pink");
            firstPartName.add("nathan");
            firstPartName.add("toby");
            firstPartName.add("sir");
            firstPartName.add("survival");
            firstPartName.add("Sir");
            firstPartName.add("star");
            firstPartName.add("pol");
            firstPartName.add("kek");
            firstPartName.add("chan");
            firstPartName.add("parrot");
            firstPartName.add("car");
            firstPartName.add("bike");
            firstPartName.add("blonde");
            firstPartName.add("brown");
            firstPartName.add("black");
            firstPartName.add("cute");
            firstPartName.add("hair");
            firstPartName.add("hot");
            firstPartName.add("cool");
            firstPartName.add("phone");
            firstPartName.add("anvil");
            firstPartName.add("turtle");
            firstPartName.add("soda");
            firstPartName.add("silver");
            firstPartName.add("joe");
            firstPartName.add("josh");

            secondPartName.add("food");
            secondPartName.add("juice");
            secondPartName.add("king");
            secondPartName.add("gamer");
            secondPartName.add("pine");
            secondPartName.add("fruit");
            secondPartName.add("apple");
            secondPartName.add("xx");
            secondPartName.add("notch");
            secondPartName.add("hero");
            secondPartName.add("se");
            secondPartName.add("popo");
            secondPartName.add("sad3");
            secondPartName.add("lil");
            secondPartName.add("pop");
            secondPartName.add("music");
            secondPartName.add("xan");
            secondPartName.add("uzi");
            secondPartName.add("bands");
            secondPartName.add("happy");
            secondPartName.add("rubber");
            secondPartName.add("number");
            secondPartName.add("witch");
            secondPartName.add("Evoker");
            secondPartName.add("wolf");
            secondPartName.add("g");
            secondPartName.add("burger");
            secondPartName.add("fries");
            secondPartName.add("french");
            secondPartName.add("brazil");
            secondPartName.add("baloon");
            secondPartName.add("monkey");
            secondPartName.add("clam");
            secondPartName.add("bee");
            secondPartName.add("honey");
            secondPartName.add("book");
            secondPartName.add("library");
            secondPartName.add("girl");
            secondPartName.add("senpai");
            secondPartName.add("yandere");
            secondPartName.add("anime");
            secondPartName.add("manga");
            secondPartName.add("japan");
            secondPartName.add("animal");
            secondPartName.add("slayer");
            secondPartName.add("destroyer");
            secondPartName.add("conquer");
            secondPartName.add("polit");
            secondPartName.add("cat");
            secondPartName.add("dog");
            secondPartName.add("kitten");
            secondPartName.add("puppy");
            secondPartName.add("baby");
            secondPartName.add("horse");
            secondPartName.add("donkey");
            secondPartName.add("one");
            secondPartName.add("min");
            secondPartName.add("max");
            secondPartName.add("ror");
            secondPartName.add("rope");
            secondPartName.add("strong");
            secondPartName.add("hacker");
            secondPartName.add("mex");
            secondPartName.add("taco");
            secondPartName.add("burrito");
            secondPartName.add("china");
            secondPartName.add("USA");
            secondPartName.add("american");
            secondPartName.add("jotaro");
            secondPartName.add("jojo");
            secondPartName.add("insta");
            secondPartName.add("snap");
            secondPartName.add("sauce");
            secondPartName.add("game");



            Random random = new Random();
            doubleDigits = random.nextInt(100);

            String first = getRandomElement(firstPartName);
            String second = getRandomElement(secondPartName);
            Random capital = new Random();
            Random capital2 = new Random();
            int capitalYes = capital.nextInt(10);
            int capital2Yes = capital2.nextInt(10);

            if (capitalYes == 0) {
                first = first.substring(0, 1).toUpperCase() + first.substring(1);
            }
            if (capital2Yes == 0) {
                second = second.substring(0, 1).toUpperCase() + second.substring(1);

            }
            Random underscore = new Random();
            if (underscore.nextInt(4) == 0) {
                List<String> order = new ArrayList<>();
                order.add(first);
                order.add(second);
                order.add(String.valueOf(doubleDigits));
                Collections.shuffle(order);
                for (String s : order) {
                    completedname = completedname + "_" + s;
                }
            } else {
                List<String> order = new ArrayList<>();
                order.add(first);
                order.add(second);
                order.add(String.valueOf(doubleDigits));
                Collections.shuffle(order);
                for (String s : order) {
                    completedname = completedname + s;
                }
            }
            Random random1 = new Random();
            if (random1.nextInt(5) == 0) {
                if (completedname.length() > 15) {
                    completedname = completedname.substring(0, 15);

                }
            }

            Random rand = new Random();
            int preselect = rand.nextInt(6);
            if (preselect != 0) {
                completedname = SkinPicker.getRandomSkin();
            }
        } while (generatedNames().contains(completedname) || completedname.length() > 15);
        return completedname;

    }
    public void robotsFight() {
        for (SentinelTrait trait : activeSentinels()) {
            trait.addTarget("NPCS");
            trait.addTarget("PLAYERS");
            trait.addTarget("MONSTERS");

        }


    }

    public void turnNPCsSentinel () {
        for (NPC npc : activeRobots()) {


            SentinelTrait sentinelTrait = npc.getOrAddTrait(SentinelTrait.class);
            sentinelTrait.setHealth(20);

            //make each sentinel random, for attack patterns and realism
            assignSentinelStats(sentinelTrait);
        }
    }


    public void graceRobots () {
        //NPCCheckIfStandstill.startChecking();
        for (NPC npc : activeRobots()) {
            npc.addTrait(RobotTrait.class);
            npc.getOrAddTrait(Waypoints.class).setWaypointProvider("wander");

            ArrayList<ItemStack> robotDefaultDrops = new ArrayList<>();
            robotDefaultDrops.add(new ItemStack(Material.COMPASS));
            robotDefaultDrops.addAll(kitInfo.getKitDrops(kitInfo.getPlayerKit(npc.getEntity())));

            //SentinelTrait sentinelTrait = npc.getOrAddTrait(SentinelTrait.class);

            //robotDrops.put(npc, robotDefaultDrops);
            //sentinelTrait.drops = robotDefaultDrops;
            ItemStack[] converted = new ItemStack[robotDefaultDrops.size()];
            converted = robotDefaultDrops.toArray(converted);
            npc.getOrAddTrait(Inventory.class).setContents(converted);
        }
        turnNPCsSentinel();
    }


//    public void setPose(Pose pose, NPC npc) {
//        switch (pose) {
//            case SNEAKING:
//                npc.getOrAddTrait(SneakingTrait.class).sneak();
//                break;
//            case STANDING:
//                npc.getOrAddTrait(SneakingTrait.class).stand();
//                break;
//        }
//    }

    public void setRobotDrops (NPC npc) {
            ArrayList<ItemStack> itemStacks = new ArrayList<>();
            for (ItemStack i : npc.getOrAddTrait(Inventory.class).getContents()) {
                if (i != null && i.getType() != Material.AIR) {
                    itemStacks.add(i);
                }

            }

            npc.getOrAddTrait(SentinelTrait.class).drops = itemStacks;

    }
    public String getRandomElement(List<String> list)
    {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    public void despawnAllBots () {

        List<NPC> npcs = new ArrayList<>();
       // List<NPCRegistry> registries = new ArrayList<>();

//        Iterator<NPCRegistry> itr = CitizensAPI.getNPCRegistries().iterator();
//
//        while(itr.hasNext()) {
//            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "iterating");
//
//            NPCRegistry npcRegistry = CitizensAPI.getNPCRegistries().iterator().next();
//            registries.add(npcRegistry);
//            //CitizensAPI.getNPCRegistries().iterator().remove();
//
//
//        }

        Iterator<NPC> itr1 = CitizensAPI.getNPCRegistry().sorted().iterator();

        while (itr1.hasNext()) {
            npcs.add(itr1.next());
        }

        for (NPC npc : npcs) {
            removeRobot(npc);
            Bukkit.broadcastMessage(ChatColor.YELLOW + npc.getName() + " has left the game");
            npc.destroy();
        }


//        for (NPC npc : activeRobots()) {
//            //Bukkit.broadcastMessage(ChatColor.YELLOW + npc.getName() + " has left the game");
//            //CitizensAPI.getNPCRegistry().deregister(npc);
//                //npc.destroy();
//
//
//
//        }
    }


    public void clearRobots () {
        activeRobots.clear();
        activeSentinels().clear();

    }



}
