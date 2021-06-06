package Robots;

import Main.HardcoreGames;
import Util.Game;
import com.google.common.collect.Maps;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.event.NPCDamageByEntityEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Spawned;
import net.citizensnpcs.trait.waypoint.Waypoints;
import net.citizensnpcs.util.PlayerAnimation;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.util.Vector;
import org.mcmonkey.sentinel.SentinelTrait;
import org.mcmonkey.sentinel.events.SentinelAttackEvent;
import org.mcmonkey.sentinel.events.SentinelNoMoreTargetsEvent;

import java.util.*;

import static java.lang.Double.MAX_VALUE;

public class NPCKilledHandler implements Listener {
    Robot robot = Robot.getSharedRobots();
    Map<UUID, Integer> timesStuck = new HashMap<>();
    Map<UUID, Long> jumpCooldown = new HashMap<>();


    private EntityPlayer lastUpdatedPlayer;
    private Entity tracker;

    private final Map<String, NPCRegistry> storedRegistries = Maps.newHashMap();


    @EventHandler
    public void sentinelKill(PlayerDeathEvent e) throws net.citizensnpcs.api.command.exception.CommandException {
            if (e.getEntity().hasMetadata("NPC")) {
                NPC npc = CitizensAPI.getNPCRegistry().getNPC(e.getEntity());




//                for (NPC npcs : robot.activeRobots()) {
//                    if (npc.equals(npcs)) {
//                        Bukkit.broadcastMessage(ChatColor.YELLOW + npc.getName() + " left the game");
//                        npc.destroy();
//                        robot.removeRobot(npcs);
//                        //should be deregistered, doesn't get rid of our tablist
//
//                    }
//
//                }
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

//                while(CitizensAPI.getNPCRegistries().iterator().hasNext()) {
//                    NPCRegistry npcRegistry = CitizensAPI.getNPCRegistries().iterator().next();
//                    while(npcRegistry.sorted().iterator().hasNext()) {
//                        //Bukkit.broadcastMessage("iterating npc..");
//                        NPC npccheck = npcRegistry.sorted().iterator().next();
//                        Bukkit.broadcastMessage(npccheck.getName() + "checking");
//
//                        if (npc == npccheck) {
//
//                        npcs.add(npc);
//
//                        }
//                        //npcRegistry.sorted().iterator().remove();
//
//
//                    }
//                   // CitizensAPI.getNPCRegistries().iterator().remove();
//                }



                for (NPC npc1 : npcs) {
                    if (npc.equals(npc1)) {
                        npc.getOrAddTrait(Spawned.class).setSpawned(false);
                        npc.despawn(DespawnReason.REMOVAL);

                        CitizensAPI.getNPCRegistry().deregister(npc1);
                        robot.removeRobot(npc1);
                        //Bukkit.broadcastMessage(robot.activeRobots().size() + "active robots amount");
                        Bukkit.broadcastMessage(ChatColor.YELLOW + npc1.getName() + " has left the game");


                        npc1.destroy();
                    }
                }


            }


    }



    @EventHandler
    public void onBadAttack(SentinelAttackEvent e) {
        if (e.getNPC().getOrAddTrait(Equipment.class).get(Equipment.EquipmentSlot.HAND) != null && e.getNPC().getOrAddTrait(Equipment.class).get(Equipment.EquipmentSlot.HAND).getType().equals(Material.SPLASH_POTION)) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void npcKilledByNPC(PlayerDeathEvent e) {
        if (e.getEntity().hasMetadata("NPC") && e.getEntity().getKiller() != null && e.getEntity().getKiller().hasMetadata("NPC")) {
            NPC npc = CitizensAPI.getNPCRegistry().getNPC(e.getEntity());


        }
    }
//    @EventHandler
//    public void npcKilledPlayer(PlayerDeathEvent e) {
//        if (e.getEntity().getKiller() != null && e.getEntity().getKiller().hasMetadata("NPC") && !e.getEntity().hasMetadata("NPC")) {
//            NPC killer = CitizensAPI.getNPCRegistry().getNPC(e.getEntity());
//            Player p = e.getEntity();
//           // npcPickup(killer);
//            if (nearestPlayer(killer.getEntity()) != null) {
//                //Bukkit.broadcastMessage(nearestPlayer(killer.getEntity()).toString() + " new ateget");
//
//                killer.getOrAddTrait(SentinelTrait.class).targetingHelper.addTarget(nearestPlayer(killer.getEntity()).getUniqueId());
//
//                killer.getOrAddTrait(SentinelTrait.class).targetingHelper.updateTargets();
//            }
//
//
//        }
//    }
//    public void npcPickup (NPC npc) {
//    if (npc.isSpawned())
//    if (npc.getEntity().getLocation().getWorld() != null)
//            for (
//    Entity e : npc.getEntity().getLocation().getWorld().getNearbyEntities(npc.getEntity().getLocation(), 5, 5, 5)) {
//        if (e instanceof Item) {
//            Item item = (Item) e;
//            ItemStack[] currentInv = npc.getOrAddTrait(Inventory.class).getContents();
//            ItemStack stack = item.getItemStack();
//            npc.getOrAddTrait(Inventory.class).setContents(add_element(currentInv.length, currentInv, stack));
//            Robot.getSharedRobots().addDrops(npc, stack);
//
//            npc.getEntity().getLocation().getWorld().playSound(npc.getEntity().getLocation(), Sound.ENTITY_ITEM_PICKUP, 10f, 1);
//            for (Player p : Bukkit.getOnlinePlayers()) {
//                PacketPlayOutCollect packet = new PacketPlayOutCollect(item.getEntityId(), npc.getEntity().getEntityId(), item.getItemStack().getAmount());
//                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
//            }
//            item.remove();
//        }
//    }
//}
//
//    public static ItemStack[] add_element(int n, ItemStack[] myarray, ItemStack ele)
//    {
//        int i;
//
//        ItemStack[] newArray = new ItemStack[n + 1];
//        //copy original array into new array
//        for (i = 0; i < n; i++)
//            newArray[i] = myarray[i];
//
//        //add element to the new array
//        newArray[n] = ele;
//
//        return newArray;
//    }

//    @EventHandler
//    public void whenStuck (NavigationStuckEvent e) {
//        e.getNPC().getOrAddTrait(Waypoints.class).setWaypointProvider("wander");
//    }
    @EventHandler
    public void onSentinelTarget (SentinelNoMoreTargetsEvent e) {
        e.getNPC().getOrAddTrait(Waypoints.class).setWaypointProvider("wander");
//        Bukkit.broadcastMessage(e.getNPC().getName() + " no longer has a targetr");
//        Bukkit.broadcastMessage(nearestPlayer(e.getNPC().getEntity()).toString());

        if (nearestPlayer(e.getNPC().getEntity()) != null) {
           // Bukkit.broadcastMessage(nearestPlayer(e.getNPC().getEntity()).toString() + " new ateget");

            e.getNPC().getOrAddTrait(SentinelTrait.class).targetingHelper.addTarget(nearestPlayer(e.getNPC().getEntity()).getUniqueId());

           // e.getNPC().getOrAddTrait(SentinelTrait.class).targetingHelper.updateTargets();
        }
    }

    @EventHandler
    public void onAttack (SentinelAttackEvent e) {
        if (e.getTarget().hasMetadata("NPC")) {
            //is targeting npc
            NPC npc = CitizensAPI.getNPCRegistry().getNPC(e.getTarget());
            if (npc.isSpawned()) {
                npc.removeTrait(Waypoints.class);

                boolean runStatusAttacker = false;
                boolean runStatusVictim = false;
                runStatusVictim = npc.getOrAddTrait(SentinelTrait.class).runaway;
                runStatusAttacker = e.getNPC().getOrAddTrait(SentinelTrait.class).runaway;
                if (runStatusAttacker && runStatusVictim) {
                    e.getNPC().getOrAddTrait(SentinelTrait.class).runaway = false;
                }
            }

        }
    }

    @EventHandler
    public void onHitNPC (NPCDamageByEntityEvent e) {
        if (robot.activeRobots.contains(e.getNPC())) {
            if (Game.getSharedGame().isStarted()) {


                NPC npc = e.getNPC();
                npc.removeTrait(Waypoints.class);

                //Bukkit.broadcastMessage("npc damagwed");
                if (((LivingEntity) npc.getEntity()).getHealth() < 5) {
                    // Bukkit.broadcastMessage("npc health too low");

                    //npc.getOrAddTrait(SentinelTrait.class).targetingHelper.addAvoid(e.getDamager().getUniqueId());
                    if (e.getDamager() instanceof LivingEntity) {
                        npc.getOrAddTrait(SentinelTrait.class).targetingHelper.removeTarget(e.getDamager().getUniqueId());

                        npc.getOrAddTrait(SentinelTrait.class).targetingHelper.updateTargets();
                        if (((LivingEntity) e.getDamager()).getHealth() > 10) {
                            npc.getOrAddTrait(SentinelTrait.class).targetingHelper.addAvoid(e.getDamager().getUniqueId());
                            npc.getOrAddTrait(SentinelTrait.class).targetingHelper.updateAvoids();
                            npc.getOrAddTrait(SentinelTrait.class).runaway = true;
                        }
                    }

                    //npc.getOrAddTrait(SentinelTrait.class).targetingHelper.updateAvoids();

                } else {
                    //npc.getOrAddTrait(SentinelTrait.class).targetingHelper.addAvoid(e.getDamager().getUniqueId());
                    npc.getOrAddTrait(SentinelTrait.class).runaway = false;

                    npc.getOrAddTrait(SentinelTrait.class).targetingHelper.addTarget(e.getDamager().getUniqueId());
                    npc.getOrAddTrait(SentinelTrait.class).targetingHelper.updateTargets();
                    npc.getOrAddTrait(SentinelTrait.class).targetingHelper.currentAvoids.clear();
                    npc.getOrAddTrait(SentinelTrait.class).targetingHelper.updateAvoids();

                }
            } else {
                NPC npc = e.getNPC();
                npc.removeTrait(Waypoints.class);
                Bukkit.getServer().getScheduler().runTaskLater(HardcoreGames.getInstance(), () -> npc.getOrAddTrait(Waypoints.class).setWaypointProvider("wander"), 20 * 10);


                playAnimation(npc);
                if (e.getDamager() instanceof Player) {
                    Random random = new Random();
                    int jump = random.nextInt(2);
                    if (jump == 0) {
                        if (jumpCooldown.containsKey(npc.getUniqueId())) {
                            if (System.currentTimeMillis() - jumpCooldown.get(npc.getUniqueId()) > 5000) {
                                jumpCooldown.put(npc.getUniqueId(), System.currentTimeMillis());
                                jump(npc);
                            }

                        } else {
                            jumpCooldown.put(npc.getUniqueId(), System.currentTimeMillis());
                            jump(npc);

                        }
                    }
                    if (((Player) e.getDamager()).isSneaking()) {
                        sneak(npc);
                        Bukkit.getServer().getScheduler().runTaskLater(HardcoreGames.getInstance(), () -> unsneak(npc), 4);


                    }
                }


            }
        }
    }
    public void jump(NPC npc) {
        npc.getEntity().setVelocity(npc.getEntity().getVelocity().add(new Vector(0.0f, 0.42f, 0.0f)));
    }
    private void sneak(NPC npc) {
        DataWatcher dw = new DataWatcher(null);
        dw.register(new DataWatcherObject<>(6, DataWatcherRegistry.s), EntityPose.CROUCHING);
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(npc.getEntity().getEntityId(), dw, true);
        for (Player p : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }
    private void unsneak(NPC npc) {
        DataWatcher dw = new DataWatcher(null);
        dw.register(new DataWatcherObject<>(6, DataWatcherRegistry.s), EntityPose.STANDING);
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(npc.getEntity().getEntityId(), dw, true);
        for (Player p : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }
    public void playAnimation(NPC npc) {

        PlayerAnimation.ARM_SWING.play((Player) npc.getEntity());
//        PlayerAnimation.START_ELYTRA.play((Player) npc.getEntity());
//
//        PlayerAnimation.EAT_FOOD.play((Player) npc.getEntity());

    }
    public Entity nearestPlayer (Entity p) {
        if (p.getLocation().getWorld() == null ) { return null; }
        List<Entity> candidates = new ArrayList<>();
        for (Entity e : p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 100, 100, 100)) {

            if (e instanceof Player) {
                if ((Player) e != p) {
                        if (((Player) e).getGameMode().equals(GameMode.SURVIVAL)) {

                            candidates.add((Player) e);

                    }
                }
            }

        }
        double tempDistance = MAX_VALUE;
        Entity closest = null;
        for (Entity near : candidates) {
            double distance = near.getLocation().distance(p.getLocation());
            if (distance < tempDistance) {
                tempDistance = distance;
                closest = near;

            }


        }
        return closest;

    }

//    @EventHandler
//    public void onStuck  (NavigationStuckEvent e) {
////        Bukkit.broadcastMessage(ChatColor.RED + e.getNPC().getName() + " is stuck");
////
////
////       // timesStuck.put(e.getNPC().getUniqueId(), timesStuck.getOrDefault(e.getNPC().getUniqueId(), 0) + 1);
////         //if (timesStuck.getOrDefault(e.getNPC().getUniqueId(), 0) >= 10) {
////        if (e.getNPC().getEntity() instanceof Player) {
////            if (e.getNPC().hasTrait(SentinelTrait.class)) {
////                if (e.getNPC().getEntity().getLocation().getBlock().isLiquid()) {
////                    if (e.getNPC().getEntity().getVehicle() == null) {
////                        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.BOAT, "boat");
////                        Bukkit.broadcastMessage(ChatColor.BLACK + "bo0at");
////                        npc.spawn(e.getNPC().getEntity().getLocation());
////                        npc.getEntity().addPassenger(e.getNPC().getEntity());
////                        npc.getEntity().setGravity(true);
////                        npc.getNavigator().setTarget(Bukkit.getPlayer("Clew").getLocation());
////                    }
////                }
////            }
//        }
//             timesStuck.put(e.getNPC().getUniqueId(), 0);
//             if (!nearestPlayer(e.getNPC().getEntity()).isEmpty()) {
//                 Bukkit.broadcastMessage(nearestPlayer(e.getNPC().getEntity()).toString() + " new ateget");
//
//                 e.getNPC().getOrAddTrait(SentinelTrait.class).targetingHelper.addTarget(nearestPlayer(e.getNPC().getEntity()).getUniqueId());
//
//                 e.getNPC().getOrAddTrait(SentinelTrait.class).targetingHelper.updateTargets();
//             } else {
//                 Bukkit.broadcastMessage("no targets inbound. switch to random loc range phase");
//             }
         //}
    //}
}



