package Robots;

import Main.HardcoreGames;
import Util.Game;
import Util.Sounds;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Inventory;
import net.citizensnpcs.api.util.DataKey;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.waypoint.Waypoints;
import net.minecraft.server.v1_16_R3.PacketPlayOutCollect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class RobotTrait extends Trait {
    public RobotTrait() {
        super("hungerbot");

    }
    HardcoreGames hardcoreGames = HardcoreGames.getInstance();
//make citizen able to pick up items and collect resources
    boolean SomeSetting = false;
    static Game game = Game.getSharedGame();
    static Robot robot = Robot.getSharedRobots();
    @Persist("mysettingname") boolean automaticallyPersistedSetting = false;

    // Here you should load up any values you have previously saved (optional).
    // This does NOT get called when applying the trait for the first time, only loading onto an existing npc at server start.
    // This is called AFTER onAttach so you can load defaults in onAttach and they will be overridden here.
    // This is called BEFORE onSpawn, npc.getEntity() will return null.
    public void load(DataKey key) {
        SomeSetting = key.getBoolean("SomeSetting", false);
    }

    // Save settings for this NPC (optional). These values will be persisted to the Citizens saves file
    public void save(DataKey key) {
        key.setBoolean("SomeSetting",SomeSetting);
    }

    // An example event handler. All traits will be registered automatically as Bukkit Listeners.
    @EventHandler
    public void click(net.citizensnpcs.api.event.NPCRightClickEvent event){
        //Handle a click on a NPC. The event has a getNPC() method.
        //Be sure to check event.getNPC() == this.getNPC() so you only handle clicks on this NPC!

    }


    public List<String> getGoodItems() {
        List<String> gooditems = new ArrayList<>();

        gooditems.add("sword");
        gooditems.add("bow");
        gooditems.add("chestplate");
        gooditems.add("arrow");
        gooditems.add("axe");
        gooditems.add("helmet");
        gooditems.add("leggings");
        gooditems.add("tunic");
        gooditems.add("boots");
        gooditems.add("compass");
        gooditems.add("ingot");
        gooditems.add("diamond");

        return gooditems;
    }


    // Called every tick
    public int cTick = 0;
    public int tickRate = 40;

    public int rTick = 0;
    public int rtickRate = 60;

    public int lTick = 0;
    public int ltickRate = 400;


    @Override
    public void run() {
        if (!npc.isSpawned()) {
            return;
        }
        cTick++;
        if (cTick >= tickRate) {
            cTick = 0;
            runUpdate();
        }

        rTick++;
        if (rTick >= rtickRate) {
            rTick = 0;
            runTarget();
        }

        lTick++;
        if (lTick >= ltickRate) {
            npc.getOrAddTrait(LookClose.class).setRealisticLooking(true);
            npc.getOrAddTrait(LookClose.class).lookClose(true);
            npc.getOrAddTrait(LookClose.class).setRange(5);
            npc.getOrAddTrait(LookClose.class).setRandomLook(false);


            lTick = 0;
            startChecking();
        }
    }

    public void runTarget () {
        // npcPickup(killer);

        if (npc.isSpawned()) {

            equipBestArmor();
            equipBestWeapon();
        }
    }


    public void equipBestWeapon () {
        //equips only the best item it has in inventory
        ItemStack hand = new ItemStack(Material.AIR);
        if (npc.getOrAddTrait(Equipment.class).get(Equipment.EquipmentSlot.HAND) != null) {
            hand = npc.getOrAddTrait(Equipment.class).get(Equipment.EquipmentSlot.HAND).clone();
        }
        ItemStack mostDamage = mostDamageItem(npc.getOrAddTrait(Inventory.class).getContents());


        //right now
        //compass : 1
        //woodensword : 2
        //sets 1 to woodensword
        //doesnt change 2
        if (!hand.equals(mostDamage)) {
            int mostDamageSlot = mostDamageItemSlot(npc.getOrAddTrait(Inventory.class).getContents());
            npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HAND, null);
            //npc.getOrAddTrait(Inventory.class).setItem(1, null);

            ItemStack[] currentInv = npc.getOrAddTrait(Inventory.class).getContents().clone();

            for (int i = 1; i < currentInv.length+1; i++) {
                ItemStack pickup = currentInv[i];
                if (pickup == null) {



                    currentInv[i] = hand;
                    currentInv[mostDamageSlot] = new ItemStack(Material.AIR);
                    npc.getOrAddTrait(Inventory.class).setContents(currentInv);
                    npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HAND, mostDamage);

                    break;
                }
            }

        }

//        if (mostDamageItem(npcInventory(npc)).getType() != npcEquipment(npc).get(Equipment.EquipmentSlot.HAND).getType()) {
//            ItemStack item = mostDamageItem(npcInventory(npc));
//            if (npcEquipment(npc).get(Equipment.EquipmentSlot.HAND) == null) {
//                npcEquipment(npc).set(Equipment.EquipmentSlot.HAND, item);
//            } else {
//                ItemStack stack = npc.getOrAddTrait(Equipment.class).get(Equipment.EquipmentSlot.HAND);
//                ItemStack[] currentInv = npc.getOrAddTrait(Inventory.class).getContents();
//                for (int i = 0; i < currentInv.length; i++) {
//                    ItemStack j = currentInv[i];
//                    if (j == null) {
//                        currentInv[i] = stack;
//                        npcEquipment(npc).set(Equipment.EquipmentSlot.HAND, item);
//                        npc.getOrAddTrait(Inventory.class).setContents(currentInv);
//
//                        break;
//                    }
//                }
//
//            }
//        }


    }
    public int mostDamageItemSlot (ItemStack[] items) {
        ItemStack item = new ItemStack(Material.AIR);
        double attackDamage = 0;
        for (int i = 0; i < items.length; i++) {
            ItemStack current = items[i];
            if (current != null) {
                if (getItemDamageValue(current) > attackDamage) {
                    item = current;
                    attackDamage = getItemDamageValue(current);
                }
            }
        }

        for (int i = 0; i < items.length; i++) {
            ItemStack current = items[i];
            if (current == item) {
                return i;
            }
        }
        return 0;
    }
    public ItemStack mostDamageItem (ItemStack[] items) {
        ItemStack item = new ItemStack(Material.AIR);
        double attackDamage = 0;
        for (ItemStack i : items) {
            if (i != null) {
                if (getItemDamageValue(i) > attackDamage) {
                    item = i;
                    attackDamage = getItemDamageValue(i);
                }
            }
        }
        return item;
    }
    public double getItemDamageValue(ItemStack is) {
        double damageValue = 0;
        if(is != null) {
            if (is.getType() == Material.AIR) {
                damageValue = 0;
            } else if (is.getType() == Material.WOODEN_SWORD) {
                damageValue = 4;
            } else if (is.getType() == Material.STONE_SWORD) {
                damageValue = 5;
            } else if (is.getType() == Material.IRON_SWORD) {
                damageValue = 6;
            } else if (is.getType() == Material.GOLDEN_SWORD) {
                damageValue = 4;
            } else if (is.getType() == Material.DIAMOND_SWORD) {
                damageValue = 7;
            } else if (is.getType() == Material.NETHERITE_SWORD) {
                damageValue = 8;
            } else if (is.getType() == Material.WOODEN_AXE) {
                damageValue = 5;
            } else if (is.getType() == Material.STONE_AXE) {
                damageValue = 6;
            } else if (is.getType() == Material.GOLDEN_AXE) {
                damageValue = 4;
            } else if (is.getType() == Material.IRON_AXE) {
                damageValue = 7;
            } else if (is.getType() == Material.DIAMOND_AXE) {
                damageValue = 8;
            } else if (is.getType() == Material.NETHERITE_AXE) {
                damageValue = 9;
            } else {
                damageValue = 1; // other blocks & items
            }
            if (is.getEnchantments().keySet().size() != 0) {
                damageValue += 1;
            }
            if (is.getType() == Material.BOW) {
                damageValue = 5;
            }
        }


        return Math.round(damageValue);
    }

    public void runUpdate () {
        //runs every tickRate ticks. possibly change this to longer and make it randomly decide what to do next based on
        //game phase, kit, kills, resources, etc
        //check for near item here, add to inventory and delete item
        if (!npc.isSpawned()) {
            return;
        }
        //picks up nearby items
        if (npc.getEntity().getLocation().getWorld() != null)
            for (Entity e : npc.getEntity().getLocation().getWorld().getNearbyEntities(npc.getEntity().getLocation(), 15, 15, 15)) {
                if (e instanceof Item) {
                    Item item = (Item) e;
                    for (String s : getGoodItems()) {
                        if (item.getItemStack().getType().name().toLowerCase().contains(s)) {
                            npc.removeTrait(Waypoints.class);
                            npc.getNavigator().setTarget(item.getLocation());
                            break;
                        }
                    }

                }
            }


        if (npc.getEntity().getLocation().getWorld() != null)
        for (Entity e : npc.getEntity().getLocation().getWorld().getNearbyEntities(npc.getEntity().getLocation(), 2.5, 2.5, 2.5)) {
            if (e instanceof Item) {
                Item item = (Item) e;
                ItemStack[] currentInv = npc.getOrAddTrait(Inventory.class).getContents();
                ItemStack stack = item.getItemStack();
                for (int i = 0; i < currentInv.length; i++) {
                    ItemStack pickup = currentInv[i];
                    if (pickup == null) {
                        currentInv[i] = stack;
                        npc.getOrAddTrait(Inventory.class).setContents(currentInv);
                        npc.getEntity().getLocation().getWorld().playSound(npc.getEntity().getLocation(), Sound.ENTITY_ITEM_PICKUP, 10f, 1);
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            PacketPlayOutCollect packet = new PacketPlayOutCollect(item.getEntityId(), npc.getEntity().getEntityId(), item.getItemStack().getAmount());
                            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
                        }
                        item.remove();
                        break;
                    }
                }

                //Robot.getSharedRobots().setRobotDrops(npc);

            }
        }



        Robot.getSharedRobots().setRobotDrops(npc);

    }
    public void equipBestArmor() {
        for (Equipment.EquipmentSlot eq : Equipment.EquipmentSlot.values()) {
            if (eq != Equipment.EquipmentSlot.HAND && eq != Equipment.EquipmentSlot.OFF_HAND) {
                //check for best armor in this equipmentslot
                ItemStack bestArmor = bestArmor(npcInventory(npc), eq);


                ItemStack[] currentInv = npc.getOrAddTrait(Inventory.class).getContents().clone();
                ItemStack stack = bestArmor.clone();

                ItemStack[] currentArmor = npc.getOrAddTrait(Equipment.class).getEquipment().clone();
                ItemStack bestArmorCurrentlyWorn = bestArmor(currentArmor, eq);



                if (bestArmorCurrentlyWorn.equals(bestArmor)) {

                } else {
                    for (int i = 0; i < currentArmor.length; i++) {
                        if (currentArmor[i] != bestArmor && currentArmor[i] != null) {
                            currentArmor[i] = null;
                            npc.getOrAddTrait(Equipment.class).set(eq, currentArmor[i]);
                        }
                    }

                    for (int i = 0; i < currentInv.length; i++) {
                        if (currentInv[i] == bestArmor) {
                            currentInv[i] = null;
                            npc.getOrAddTrait(Inventory.class).setContents(currentInv);
                            break;
                        }

                    }

                    if (bestArmor.getType() != Material.AIR) {
                        if ((bestArmor.getType().name().toLowerCase().contains("diamond"))) {

                            npcEquipment(npc).set(eq, stack);

                            Sounds.playSound(npc.getEntity().getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND);
                            //npc.getOrAddTrait(Inventory.class).setContents((ItemStack[]) ArrayUtils.removeElement(npc.getOrAddTrait(Inventory.class).getContents(), bestArmor(Arrays.asList(npc.getOrAddTrait(Inventory.class).getContents()), eq)));
                        }
                        if (bestArmor.getType().name().toLowerCase().contains("iron")) {
                            npcEquipment(npc).set(eq, bestArmor);
                            Sounds.playSound(npc.getEntity().getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON);
                            //npc.getOrAddTrait(Inventory.class).setContents((ItemStack[]) ArrayUtils.removeElement(npc.getOrAddTrait(Inventory.class).getContents(), bestArmor(Arrays.asList(npc.getOrAddTrait(Inventory.class).getContents()), eq)));


                        }
                        if (bestArmor.getType().name().toLowerCase().contains("leather")) {
                            npcEquipment(npc).set(eq, bestArmor);
                            Sounds.playSound(npc.getEntity().getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER);

                            //npc.getOrAddTrait(Inventory.class).setContents((ItemStack[]) ArrayUtils.removeElement(npc.getOrAddTrait(Inventory.class).getContents(), bestArmor(Arrays.asList(npc.getOrAddTrait(Inventory.class).getContents()), eq)));
                        }
                        if (bestArmor.getType().name().toLowerCase().contains("turtle")) {
                            npcEquipment(npc).set(eq, bestArmor);
                            Sounds.playSound(npc.getEntity().getLocation(), Sound.ITEM_ARMOR_EQUIP_TURTLE);
                        }
                    }


                }
            }
            }
        }



    public Equipment npcEquipment (NPC npc) {
        return npc.getOrAddTrait(Equipment.class);
    }

    public ItemStack[] npcInventory (NPC npc) {
       return npc.getOrAddTrait(Inventory.class).getContents();
    }
    public int bestArmorSlot (ItemStack[] items, Equipment.EquipmentSlot eq) {

        ItemStack item = null;
        double bestArmor = 0;
        double total = 0;

        int slot = 0;
        if (eq == Equipment.EquipmentSlot.HELMET) {
            for (int j =  0; j < items.length; j++) {
                ItemStack i = items[j];
                if (i != null) {

                    if (getArmorValue(i, eq) > bestArmor) {
                        slot = j;
                        bestArmor = getArmorValue(i, eq);
                        total += getArmorValue(i, eq);
                    }
                }

            }
            if (total == 0) {
                return -1;
            }
            return slot;
        }
        if (eq == Equipment.EquipmentSlot.CHESTPLATE) {
            for (int j =  0; j < items.length; j++) {
                ItemStack i = items[j];
                if (i != null) {

                    if (getArmorValue(i, eq) > bestArmor) {
                        slot = j;
                        bestArmor = getArmorValue(i, eq);
                        total += getArmorValue(i, eq);
                    }
                }

            }
            if (total == 0) {
                return -1;
            }
            return slot;
        }
        if (eq == Equipment.EquipmentSlot.LEGGINGS) {
            for (int j =  0; j < items.length; j++) {
                ItemStack i = items[j];
                if (i != null) {
                    if (getArmorValue(i, eq) > bestArmor) {
                        slot = j;
                        bestArmor = getArmorValue(i, eq);
                        total += getArmorValue(i, eq);
                    }
                }

            }
            if (total == 0) {
                return -1;
            }
            return slot;
        }
        if (eq == Equipment.EquipmentSlot.BOOTS) {
            for (int j =  0; j < items.length; j++) {
                ItemStack i = items[j];
                if (i != null) {
                    if (getArmorValue(i, eq) > bestArmor) {
                        slot = j;
                        bestArmor = getArmorValue(i, eq);
                        total += getArmorValue(i, eq);
                    }
                }

            }
            if (total == 0) {
                return -1;
            }
            return slot;
        }
        return -1;
    }


    public ItemStack bestArmor (ItemStack[] items, Equipment.EquipmentSlot eq) {
        ItemStack item = null;
        double bestArmor = 0;
        double total = 0;
        if (eq == Equipment.EquipmentSlot.HELMET) {
            for (ItemStack i : items) {
                if (i != null) {

                    if (getArmorValue(i, eq) > bestArmor) {
                        item = i;
                        bestArmor = getArmorValue(i, eq);
                        total += getArmorValue(i, eq);
                    }
                }

            }
            if (total == 0) {
                return new ItemStack(Material.AIR);
            }
            return item;
        }
        if (eq == Equipment.EquipmentSlot.CHESTPLATE) {
            for (ItemStack i : items) {
                if (i != null) {

                    if (getArmorValue(i, eq) > bestArmor) {
                        item = i;
                        bestArmor = getArmorValue(i, eq);
                        total += getArmorValue(i, eq);
                    }
                }

            }
            if (total == 0) {
                return new ItemStack(Material.AIR);
            }
            return item;
        }
        if (eq == Equipment.EquipmentSlot.LEGGINGS) {
            for (ItemStack i : items) {
                if (i != null) {
                    if (getArmorValue(i, eq) > bestArmor) {
                        item = i;
                        bestArmor = getArmorValue(i, eq);
                        total += getArmorValue(i, eq);
                    }
                }

            }
            if (total == 0) {
                return new ItemStack(Material.AIR);
            }
            return item;
        }
        if (eq == Equipment.EquipmentSlot.BOOTS) {
            for (ItemStack i : items) {
                if (i != null) {
                    if (getArmorValue(i, eq) > bestArmor) {
                        item = i;
                        bestArmor = getArmorValue(i, eq);
                        total += getArmorValue(i, eq);
                    }
                }

            }
            if (total == 0) {
                return new ItemStack(Material.AIR);
            }
            return item;
        }
        return new ItemStack(Material.AIR);
    }
    public double getArmorValue(ItemStack is, Equipment.EquipmentSlot eq) {
        if (is != null) {

            double damageValue = 0;
            if (eq == Equipment.EquipmentSlot.HELMET) {
                if (is.getType() == Material.AIR) {
                    damageValue = 0;
                } else if (is.getType() == Material.LEATHER_HELMET) {
                    damageValue = 1;
                } else if (is.getType() == Material.IRON_HELMET) {
                    damageValue = 3;
                } else if (is.getType() == Material.DIAMOND_HELMET) {
                    damageValue = 4;
                } else if (is.getType() == Material.TURTLE_HELMET) {
                    damageValue = 2;
                } else {
                    damageValue = 0; // other blocks & items
                }

            }
            if (eq == Equipment.EquipmentSlot.CHESTPLATE) {
                if (is.getType() == Material.AIR) {
                    damageValue = 0;
                } else if (is.getType() == Material.LEATHER_CHESTPLATE) {
                    damageValue = 1;
                } else if (is.getType() == Material.IRON_CHESTPLATE) {
                    damageValue = 3;
                } else if (is.getType() == Material.DIAMOND_CHESTPLATE) {
                    damageValue = 4;
                } else {
                    damageValue = 0; // other blocks & items
                }


            }
            if (eq == Equipment.EquipmentSlot.LEGGINGS) {
                if (is.getType() == Material.AIR) {
                    damageValue = 0;
                } else if (is.getType() == Material.LEATHER_LEGGINGS) {
                    damageValue = 1;
                } else if (is.getType() == Material.IRON_LEGGINGS) {
                    damageValue = 3;
                } else if (is.getType() == Material.DIAMOND_LEGGINGS) {
                    damageValue = 4;
                } else {
                    damageValue = 0; // other blocks & items
                }


            }
            if (eq == Equipment.EquipmentSlot.BOOTS) {
                if (is.getType() == Material.AIR) {
                    damageValue = 0;
                } else if (is.getType() == Material.LEATHER_BOOTS) {
                    damageValue = 1;
                } else if (is.getType() == Material.IRON_BOOTS) {
                    damageValue = 3;
                } else if (is.getType() == Material.DIAMOND_BOOTS) {
                    damageValue = 4;
                } else {
                    damageValue = 0; // other blocks & items
                }


            }
            return Math.round(damageValue);

        }
        return 0;


    }


    //Run code when your trait is attached to a NPC.
    //This is called BEFORE onSpawn, so npc.getEntity() will return null
    //This would be a good place to load configurable defaults for new NPCs.
    @Override
    public void onAttach() {
        HardcoreGames.getInstance().getServer().getLogger().info(npc.getName() + "has been assigned MyTrait!");
    }

    // Run code when the NPC is despawned. This is called before the entity actually despawns so npc.getEntity() is still valid.
    @Override
    public void onDespawn() {
    }

    //Run code when the NPC is spawned. Note that npc.getEntity() will be null until this method is called.
    //This is called AFTER onAttach and AFTER Load when the server is started.
    @Override
    public void onSpawn() {

    }

    //run code when the NPC is removed. Use this to tear down any repeating tasks.
    @Override
    public void onRemove() {
    }


    public static HashSet<Material> bad_blocks = new HashSet<>();
    static HashMap<NPC, Location> npcLocationHashMap = new HashMap<>();

    static {
        bad_blocks.add(Material.LAVA);
        bad_blocks.add(Material.FIRE);
        bad_blocks.add(Material.CACTUS);
        bad_blocks.add(Material.WATER);
        bad_blocks.add(Material.MAGMA_BLOCK);
    }


    public static void startChecking () {
            for (NPC npc : robot.activeRobots()) {
                if (npc.isSpawned()) {
                    Location npcLocation = npc.getEntity().getLocation();
                    if (!game.getWorld().getWorldBorder().isInside(npcLocation)) {

                        npc.getNavigator().cancelNavigation();
                        npc.getNavigator().setTarget(game.getWorld().getSpawnLocation());
                    }

                    if (npcLocationHashMap.containsKey(npc)) {

                        if (npcLocationHashMap.get(npc).distance(npcLocation) < 1) {
                            //npc has not moved for 5 seconds

                            //force npc to move to random  nearby location
                            //remove targets temporarily

                            //npc.getOrAddTrait(SentinelTrait.class).targetingHelper.currentTargets.clear();
                            //npc.getOrAddTrait(SentinelTrait.class).targetingHelper.updateTargets();
                            //npc.getNavigator().getPathStrategy().stop();
                            //npc.getNavigator().cancelNavigation();
                            ///npc.getNavigator().setTarget(findSafeLocation(npc.getEntity()));

                            //npc.getNavigator().get
                            npc.getOrAddTrait(Waypoints.class).setWaypointProvider("wander");


                        } else {
                            npc.removeTrait(Waypoints.class);
                        }
                        npcLocationHashMap.put(npc, npcLocation);

                    } else {
                        npcLocationHashMap.put(npc, npcLocation);
                    }
                }
            }


    }





}
