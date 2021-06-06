package Kits.KitListeners.Kits.Defense;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import Util.GamePhase;
import Util.SkullCreator;
import com.google.common.base.Strings;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Random;

public class Engineer implements Listener {

    //eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDZkYjEzN2EzNTY3OWJlYWY3OTAwNzBkMGM5Yzk2YzkwNjc2MjYwZWJjMDBkZDJjNzAwNTYyYTA5OWRiMDdjMCJ9fX0=
    //sentry head ^
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void onPlaceSentry (BlockPlaceEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.ENGINEER) {
                if (e.getItemInHand().getItemMeta() != null) {
                    if (ChatColor.stripColor(e.getItemInHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Sentry Gun")) {
                        //spawnSentryGun(e.getPlayer(), e.getBlock().getLocation().clone().add(0.5, 0, 0.5));
                        buildSentryGun(e.getPlayer(), e.getBlock().getLocation().clone().add(0.5, 0, 0.5));
                        e.getItemInHand().setAmount(e.getItemInHand().getAmount()-1);
                        e.getBlockPlaced().setType(Material.AIR);
                    }
                }
            }
        }
    }

    @EventHandler
    public void rightClickEntity(PlayerArmorStandManipulateEvent e) {
        //Bukkit.broadcastMessage("clicked on ent");

        if (game.isStarted()) {
                if (e.getRightClicked().hasMetadata("sentrygun")) {
                   // Bukkit.broadcastMessage("clicked on sentry");
                   // Bukkit.broadcastMessage(((Player) e.getRightClicked().getMetadata("sentrygun").get(0).value()).getDisplayName());

                    if ((Player) e.getRightClicked().getMetadata("sentrygun").get(0).value() == e.getPlayer()) {

                        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou picked up your Sentry Gun."));
                    } else {
                       // Bukkit.broadcastMessage("bad sentry clcikc");

                        e.setCancelled(true);

                    }
                }

        }
    }

    HashMap<ArmorStand, Boolean> sentryDownMap = new HashMap<>();
    HashMap<ArmorStand, Long> lastDestroyed = new HashMap<>();

    public void buildSentryGun (Player p, Location loc) {
        ArmorStand stand = (ArmorStand) loc.getWorld().spawnEntity(loc.subtract(0,1,0), EntityType.ARMOR_STAND);
        stand.setInvulnerable(true);
        //stand.setMetadata("sentrygun", new FixedMetadataValue(HardcoreGames.getInstance(), p));
        stand.setInvisible(true);
        stand.setGravity(false);
        stand.setSmall(false);
        stand.getEquipment().setHelmet(sentryItemStack());
        stand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
        stand.setHeadPose(stand.getHeadPose());

        float orientation = p.getLocation().getYaw();
        new BukkitRunnable() {
            int time = 0;
            int seconds = 0;
            @Override
            public void run() {
                if (stand.getEquipment().getHelmet().getType() == Material.AIR) {
                    stand.remove();
                    cancel();
                }
                if (p.isDead() || !p.isOnline()) {
                    stand.getLocation().getWorld().dropItemNaturally(stand.getLocation(), sentryItemStack());
                    stand.remove();
                    cancel();
                }
                if (!stand.isValid()) {
                    stand.remove();
                    cancel();
                }


                time++;
                stand.setCustomNameVisible(true);

                stand.setCustomName("§8[§r" + getProgressBar(time, 201, 40, '|', ChatColor.GREEN, ChatColor.GRAY) + "§8]");

                if (time % 20 == 0) {
                    //second passed
                    if (seconds == 0) {
                        Location loc = stand.getLocation();
                        stand.getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 10);
                        stand.getLocation().getWorld().spawnParticle(Particle.FLAME, loc, 10);
                        stand.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 10);
                        stand.getLocation().getWorld().playSound(loc, Sound.BLOCK_ANVIL_USE, 10f, 1f);
                    }
                    if (seconds == 1) {
                        Location loc = stand.getLocation();
                        stand.getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 10);
                        stand.getLocation().getWorld().spawnParticle(Particle.FLAME, loc, 10);
                        stand.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 10);
                        stand.getLocation().getWorld().playSound(loc, Sound.BLOCK_ANVIL_USE, 10f, 1f);
                    }

                    if (seconds == 1) {
                        Location loc = stand.getLocation();
                        stand.getLocation().getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 5);
                        stand.getLocation().getWorld().spawnParticle(Particle.FLAME, loc, 10);
                        stand.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 10);
                        stand.getLocation().getWorld().playSound(loc, Sound.BLOCK_ANVIL_USE, 10f, 2f);

                    }

                    if (seconds == 2) {
                        Location loc = stand.getLocation();
                        stand.getLocation().getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 10);
                        stand.getLocation().getWorld().spawnParticle(Particle.FLAME, loc, 10);
                        stand.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 10);
                        stand.getLocation().getWorld().playSound(loc, Sound.BLOCK_ANVIL_USE, 10f, 3f);

                    }

                    if (seconds > 3 && seconds != 10) {
                        Location loc = stand.getLocation();
                        stand.getLocation().getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, loc, 25);
                        stand.getLocation().getWorld().spawnParticle(Particle.FLAME, loc, 10);
                        stand.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 15);
                        stand.getLocation().getWorld().playSound(loc, Sound.BLOCK_ANVIL_USE, 10f, seconds);

                    }

                    seconds++;





                }

                if (seconds > 10) {
                    stand.getLocation().getWorld().playSound(stand.getLocation(), Sound.BLOCK_ANVIL_USE, 10f, 1f);
                    stand.removeEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
                    p.sendMessage(ChatColor.GREEN + "Sentry Gun was built!");
                    stand.setCustomName(null);
                    stand.setCustomNameVisible(false);
                    spawnSentryGun(p, loc, stand, orientation);
                    cancel();
                }
            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);

        double pitch = 90;
        pitch = Math.toRadians(pitch);
        stand.setHeadPose(new EulerAngle(pitch, 0, 0));



    }
    public String getProgressBar(int current, int max, int totalBars, char symbol, ChatColor completedColor,
                                 ChatColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        if (totalBars - progressBars < 0) {
            return "";
        }

        return Strings.repeat("" + completedColor + symbol, progressBars)
                + Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }



    public void spawnSentryGun (Player p, Location loc, ArmorStand stand, float orientation) {
        //if (loc.getWorld() != null)
//        Giant giant = (Giant) loc.getWorld().spawnEntity(loc.subtract(0, 1, 0), EntityType.GIANT);
//        giant.setAI(false);
//        giant.setInvisible(true);
//        giant.setGravity(false);
//        giant.getEquipment().setHelmet(sentryItemStack());
//        giant.getEquipment().setItem(EquipmentSlot.HAND, sentryItemStack());

        //ArmorStand stand = (ArmorStand) loc.getWorld().spawnEntity(loc.subtract(0,1,0), EntityType.ARMOR_STAND);
        stand.setInvulnerable(false);
        stand.setMetadata("sentrygun", new FixedMetadataValue(HardcoreGames.getInstance(), p));
        stand.setInvisible(true);
        stand.setGravity(false);
        stand.setSmall(false);

        stand.setHeadPose(new EulerAngle(0, 0, 0));


        //start with level 1, needs 10 ingots for level 2
        sentryLevelMap.put(stand, 1);
        sentryIngotsLeftForUpgrade.put(stand, 10);
        //forcedTarget.put(stand, null);


        //stand.getEquipment().setHelmet(sentryItemStack());
        update(stand, loc, p, orientation);
        //p.playSound(p.getLocation(), Sound);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (stand.getEquipment().getHelmet().getType() == Material.AIR) {
                    stand.remove();
                    cancel();
                }
                if (p.isDead() || !p.isOnline()) {
                    stand.getLocation().getWorld().dropItemNaturally(stand.getLocation(), sentryItemStack());
                    stand.remove();
                    cancel();
                }
                if (!stand.isValid()) {
                    stand.remove();
                    cancel();
                }
            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);
    }

    //HashMap<ArmorStand, Entity> forcedTarget = new HashMap<>();
//    @EventHandler
//    public void onHit (EntityDamageByEntityEvent e) {
//        if (game.isStarted()) {
//            if (e.getDamager() instanceof Player) {
//                if (e.getEntity() instanceof LivingEntity && !(e.getEntity().hasMetadata("sentrygun"))) {
//                    Player p = (Player) e.getDamager();
//                    if (kitInfo.getPlayerKit(p) == Kits.ENGINEER){
//                        if (((Player)e.getDamager()).getInventory().getItemInMainHand().getItemMeta() != null && ChatColor.stripColor(((Player)e.getDamager()).getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Wrench")) {
//                            ArmorStand stand = null;
//                            for (ArmorStand stands : forcedTarget.keySet()) {
//                                if (stands.getMetadata("sentrygun").get(0).value() == p) {
//                                    stand = stands;
//                                    break;
//                                }
//                            }
//                            if (stand == null) {
//                                return;
//                            } else {
//                                if (forcedTarget.get(stand) == null) {
//                                    forcedTarget.put(stand, e.getEntity());
//                                    ArmorStand finalStand = stand;
//                                    Bukkit.getServer().getScheduler().runTaskLater(HardcoreGames.getInstance(), () -> forcedTarget.replace(finalStand, null), 200);
//                                }
//
//                            }
//
//
//
//                        }
//
//                        }
//                }
//            }
//        }
//    }


    public void update(ArmorStand armorStand, Location loc, Player creator, float orientation) {
        Location location = armorStand.getLocation();
        final boolean[] floatLoop = {false};
        Location sameLocation = loc.clone();
        final Entity[] target = {null};
        long timeSinceLastTarget = System.currentTimeMillis();
        //final int[] timesMoved = {0};


        final int[] ticks = {0};
        HashMap<Entity, Boolean> soundMap = new HashMap<>();
        float finalOrientation = orientation;
        new BukkitRunnable() {
            @Override
            public void run() {
                ticks[0]++;

                int sentryLevel = sentryLevelMap.get(armorStand);

                int range = 10;
                if (sentryLevel == 2) {
                    range = 17;
                }
                if (sentryLevel == 3) {
                    range = 22;
                }
                if (!armorStand.hasMetadata("sentrygun")){
                    return;
                }
                if (!armorStand.isValid()) {
                    cancel();
                }

                if (sentryDownMap.containsKey(armorStand)) {
                    armorStand.setHeadPose(new EulerAngle(90, 0 , 0));
                    armorStand.setCustomName(ChatColor.RED + "Sentry Down!");
                    armorStand.setCustomNameVisible(true);
                    return;
                } else {
                    if (sentryLevel == 1) {
                        armorStand.setCustomName(null);
                        armorStand.setCustomNameVisible(false);
                    }
                    if (sentryLevel == 2) {
                        armorStand.setCustomName(ChatColor.AQUA + "Level 2 Sentry Gun");
                        armorStand.setCustomNameVisible(true);

                    }
                    if (sentryLevel == 3) {
                        armorStand.setCustomName(ChatColor.AQUA + "Level 3 Sentry Gun");

                        armorStand.setCustomNameVisible(true);

                    }
                }


                for (Entity e : armorStand.getLocation().getWorld().getNearbyEntities(armorStand.getLocation(), range, range, range)) {
                    if (e != armorStand) {

                        if (e instanceof Monster || e instanceof Player) {
                            if (!(e instanceof ArmorStand)) {
                                if (!((LivingEntity) e).isInvisible()) {
                                    if (e != creator) {
                                        if (armorStand.hasLineOfSight(e)) {
                                            if (!entityBehindPlayer(e, armorStand)) {
                                            if (e instanceof Player) {
                                                if (((Player) e).isSneaking()) {
                                                    Random random = new Random();
                                                    double chanceSneak = random.nextDouble();
                                                    if (chanceSneak <= 0.04) {
                                                        target[0] = e;
                                                    }

                                                } else {
                                                    target[0] = e;

                                                }
                                            } else {
                                                target[0] = e;

                                            }
                                            //Bukkit.getServer().getScheduler().runTaskLater(HardcoreGames.getInstance(), () -> target[0] = null, 100);

                                            break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


                if (target[0] == null) {

//                    if (forcedTarget.get(armorStand) != null) {
//                        if (armorStand.hasLineOfSight(forcedTarget.get(armorStand))) {
//                            if (forcedTarget.get(armorStand).getLocation().distance(armorStand.getLocation()) <= (range)) {
//
//                                target[0] = forcedTarget.get(armorStand);
//                            }
//                        }
//                    }
                    if (!floatLoop[0]) {
                        location.setYaw((location.getYaw() + 7.5F));


                        armorStand.teleport(location);
                        //timesMoved[0]++;
                        if (location.getYaw() > finalOrientation + 75F) {
                            //timesMoved[0] = 0;
                            floatLoop[0] = true;
                            //Bukkit.broadcastMessage("250 moved switching");
                            armorStand.getLocation().getWorld().playSound(armorStand.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 10f, 3f);

                        }
                    } else {
                        location.setYaw((location.getYaw() - 7.5F));
                        armorStand.teleport(location);
                        //timesMoved[0]--;
                        if (location.getYaw() < finalOrientation - 75F) {
                            //timesMoved[0] = 0;
                            floatLoop[0] = false;
                            //Bukkit.broadcastMessage("250 moved switching other");
                            armorStand.getLocation().getWorld().playSound(armorStand.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 10f, 3f);

                        }

                    }
                } else {

                    if (armorStand.hasLineOfSight(target[0])) {
                        if (!soundMap.containsKey(target[0])) {
                            new BukkitRunnable() {
                                int time = 0;
                                @Override
                                public void run() {
                                    if (time == 0) {
                                        armorStand.getLocation().getWorld().playSound(armorStand.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 10f, 1f);

                                    }
                                    if (time == 1) {
                                        armorStand.getLocation().getWorld().playSound(armorStand.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 10f, 2f);
                                    }
                                    if (time == 2) {
                                        armorStand.getLocation().getWorld().playSound(armorStand.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 10f, 3f);
                                    }
                                    if (time == 3) {
                                        armorStand.getLocation().getWorld().playSound(armorStand.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 10f, 4f);
                                    }
                                    if (time == 4) {
                                        armorStand.getLocation().getWorld().playSound(armorStand.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 10f, 5f);
                                    }
                                    if (time == 5) {
                                        armorStand.getLocation().getWorld().playSound(armorStand.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 10f, 6f);
                                    }
                                    time++;
                                    if (time > 5) {
                                        cancel();
                                    }
                                }
                            }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);

                            soundMap.put(target[0], true);
                            //Bukkit.getServer().getScheduler().runTaskLater(HardcoreGames.getInstance(), () -> soundMap.remove(target[0]), 200);
                        }


                        if (target[0].getLocation().distance(armorStand.getLocation()) <= (range + 1)) {


                            ArmorStand stand = armorStand;
                            Vector diff = ((LivingEntity) target[0]).getEyeLocation().toVector().subtract(stand.getEyeLocation().toVector());
                            diff.normalize(); //set to a distance of 1
                            Location fixed = stand.getLocation();
                            fixed.setDirection(diff); //set the direction to the difference of locations between the two players
                            double pitch = fixed.getPitch();
                            pitch = Math.toRadians(pitch);



                            stand.setHeadPose(new EulerAngle(pitch, 0, 0));




                            if (target[0].getLocation().distance(armorStand.getLocation()) <= range) {
                                if (game.getPhase() == GamePhase.GAMESTARTED) {
                                    int fireRate = 8;
                                    if (sentryLevel == 2) {
                                        fireRate = 6;
                                    }
                                    if (sentryLevel == 3) {
                                        fireRate = 4;
                                    }
                                    if (ticks[0] % fireRate == 0) {
                                        if (sentryLevel == 1) {
                                            shootParticle(armorStand, Particle.ASH, 1.50);
                                            shootParticle(armorStand, Particle.FLAME, 2.5);
                                        }
                                        if (sentryLevel == 2) {

                                            shootParticle(armorStand, Particle.ASH, 1.50);
                                            shootParticle(armorStand, Particle.FLAME, 2.5);
                                            shootParticle(armorStand, Particle.FLAME, 2.5);

                                        }

                                        if (sentryLevel == 3) {
                                            shootParticle(armorStand, Particle.WHITE_ASH,1.50);
                                            shootParticle(armorStand, Particle.SOUL_FIRE_FLAME, 2.5);
                                        }

                                        if (sentryLevel == 1) {

                                            armorStand.getLocation().getWorld().playSound(armorStand.getLocation(), Sound.ENTITY_SHULKER_SHOOT, 5f, 8f);
                                        }
                                        if (sentryLevel == 2) {

                                            armorStand.getLocation().getWorld().playSound(armorStand.getLocation(), Sound.ENTITY_SHULKER_SHOOT, 7f, 4f);
                                        }

                                        if (sentryLevel == 3) {

                                            armorStand.getLocation().getWorld().playSound(armorStand.getLocation(), Sound.ENTITY_SHULKER_SHOOT, 10f, 2f);
                                        }
                                        Random random = new Random(System.currentTimeMillis());
                                        //check for successful hit

                                        double magnitude = Math.pow(target[0].getVelocity().getX(), 2) + Math.pow(target[0].getVelocity().getZ(), 2);
                                        if (target[0].getLocation().distance(armorStand.getLocation()) > 5) {
                                            if (random.nextDouble() < 0.95) {
                                                //Bukkit.broadcastMessage(magnitude + " speed");
                                                if (sentryLevel == 3) {
                                                    Random fireball = new Random(System.currentTimeMillis());
                                                    if (fireball.nextDouble() <= 0.20) {
                                                        armorStand.launchProjectile(Fireball.class);
                                                    }
                                                }
                                                if (magnitude < 5) {

                                                    ((LivingEntity) target[0]).damage(1.25*sentryLevel, creator);
                                                }
                                            }
                                        } else {
                                            if (random.nextDouble() < 0.95) {
                                                if (sentryLevel == 3) {
                                                    Random fireball = new Random(System.currentTimeMillis());
                                                    if (fireball.nextDouble() <= 0.20) {
                                                        armorStand.launchProjectile(Fireball.class);
                                                    }
                                                }
                                                if (magnitude < 5) {
                                                    ((LivingEntity) target[0]).damage(1.75*sentryLevel, creator);

                                                }

                                            }

                                        }

                                        //fix bullet particles
                                        //sentry will have 10 sec build time with animation of sort (sentry head will be down)
                                        //can be hit after completion to disable for 3 seconds

                                        //knockback
                                        Vector vec = armorStand.getLocation().getDirection();
                                        vec.subtract(new Vector(0, -1,0));

                                        //armorStand.setGravity(false);
                                        armorStand.setVelocity(vec);
                                        Vector clone = armorStand.getLocation().getDirection();
                                        clone.add(new Vector(0, 1,0));

                                        Bukkit.getServer().getScheduler().runTaskLater(HardcoreGames.getInstance(), () -> armorStand.setVelocity(clone), 40);



                                        target[0].getLocation().clone().add(0,1,0).getWorld().spawnParticle(Particle.EXPLOSION_NORMAL,target[0].getLocation(), 4);

                                        if (((LivingEntity) target[0]).getHealth() <= 0) {
                                            armorStand.getLocation().getWorld().playSound(armorStand.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10f, 10f);
                                            //fix target pitch + particle effects
                                            if (target[0] instanceof Player) {
                                                creator.sendMessage(ChatColor.GREEN + "Your Sentry Gun just killed " + ((Player) target[0]).getName() + "!");
                                            }
                                        }
                                    }

                                }
                            }
//                        Vector targetDirection = target[0].getLocation().getDirection(); // Direction target is facing
//
//                        Vector newFacingTargetDirection = (targetDirection.multiply(-1)); // opposite direction
//
//                        Location location = armorStand.getLocation();
//
//                        location.setDirection(newFacingTargetDirection);  //set direction to opposite direction
//                        location.setPitch(0); //if they are on flat ground set this to 0 so they are looking straight ahead (optional)

                            stand.teleport(fixed); //teleported facing opposite direction of target!
                        } else {
                            soundMap.remove(target[0]);

                            target[0] = null;
                        }
                    } else {
                        soundMap.remove(target[0]);

                        target[0] = null;

                    }
                }
            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);

    }

    HashMap<ArmorStand, Integer> sentryLevelMap = new HashMap<>();
    HashMap<ArmorStand, Integer> sentryIngotsLeftForUpgrade = new HashMap<>();


    @EventHandler
    public void onSentryDamage (EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof ArmorStand) {
            if (e.getEntity().hasMetadata("sentrygun")) {
                if (e.getDamager() instanceof Player) {
                    if (((Player)e.getDamager()) == e.getEntity().getMetadata("sentrygun").get(0).value()) {
                        if (((Player)e.getDamager()).getInventory().getItemInMainHand().getItemMeta() != null && ChatColor.stripColor(((Player)e.getDamager()).getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Wrench")) {
                            ArmorStand stand = (ArmorStand) e.getEntity();

                            if (sentryLevelMap.get(stand) < 3) {
                                if (hasIron((Player) e.getDamager())) {
                                    //upgrade sentry
                                    if (sentryIngotsLeftForUpgrade.get(stand) > 0) {
                                        sentryIngotsLeftForUpgrade.put(stand, sentryIngotsLeftForUpgrade.get(stand) - 1);

                                        ((Player) e.getDamager()).sendMessage(ChatColor.GREEN + "You used one Iron Ingot to upgrade your Sentry Gun!");
                                        ((Player) e.getDamager()).sendMessage(ChatColor.GREEN + "You need " + sentryIngotsLeftForUpgrade.get(stand) + " more for Level " + (sentryLevelMap.get(stand) + 1) + " Sentry!");

                                        ((Player) e.getDamager()).playSound(e.getDamager().getLocation(), Sound.ENTITY_IRON_GOLEM_REPAIR, 10f, 1f);
                                    }
                                    if (sentryIngotsLeftForUpgrade.get(stand) <= 0) {
                                        //upgrade here
                                        sentryLevelMap.put(stand, sentryLevelMap.get(stand) + 1);
                                        int upgradetime = 30;
                                        if (sentryLevelMap.get(stand) >= 3) {
                                            upgradetime = 60;
                                        }
                                        int finalUpgradetime = upgradetime;
                                        new BukkitRunnable() {
                                            int time = 0;
                                            int seconds = 0;

                                            @Override
                                            public void run() {
                                                time++;
                                                stand.setCustomNameVisible(true);
                                                stand.setInvulnerable(true);
                                                stand.setHeadPose(new EulerAngle(90,0,0));
                                                stand.removeMetadata("sentrygun", HardcoreGames.getInstance());
                                                if (sentryLevelMap.get(stand) == 2) {
                                                    stand.setCustomName("§8[§r" + getProgressBar(time, (finalUpgradetime *20)+1, 40, '|', ChatColor.YELLOW, ChatColor.GRAY) + "§8]");
                                                }
                                                if (sentryLevelMap.get(stand) == 3) {
                                                    stand.setCustomName("§8[§r" + getProgressBar(time, (finalUpgradetime *20)+1, 40, '|', ChatColor.AQUA, ChatColor.GRAY) + "§8]");
                                                }
                                                if (time % 20 == 0) {
                                                    //second passed
                                                    if (seconds == 0) {
                                                        Location loc = stand.getLocation();
                                                        stand.getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 10);
                                                        stand.getLocation().getWorld().spawnParticle(Particle.FLAME, loc, 10);
                                                        stand.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 10);
                                                        stand.getLocation().getWorld().playSound(loc, Sound.BLOCK_ANVIL_USE, 10f, 1f);
                                                    }
                                                    if (seconds == 1) {
                                                        Location loc = stand.getLocation();
                                                        stand.getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 10);
                                                        stand.getLocation().getWorld().spawnParticle(Particle.FLAME, loc, 10);
                                                        stand.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 10);
                                                        stand.getLocation().getWorld().playSound(loc, Sound.BLOCK_ANVIL_USE, 10f, 1f);
                                                    }

                                                    if (seconds == 1) {
                                                        Location loc = stand.getLocation();
                                                        stand.getLocation().getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 5);
                                                        stand.getLocation().getWorld().spawnParticle(Particle.FLAME, loc, 10);
                                                        stand.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 10);
                                                        stand.getLocation().getWorld().playSound(loc, Sound.BLOCK_ANVIL_USE, 10f, 2f);

                                                    }

                                                    if (seconds == 2) {
                                                        Location loc = stand.getLocation();
                                                        stand.getLocation().getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 10);
                                                        stand.getLocation().getWorld().spawnParticle(Particle.FLAME, loc, 10);
                                                        stand.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 10);
                                                        stand.getLocation().getWorld().playSound(loc, Sound.BLOCK_ANVIL_USE, 10f, 3f);

                                                    }

                                                    if (seconds == 3) {
                                                        Location loc = stand.getLocation();
                                                        stand.getLocation().getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, loc, 25);
                                                        stand.getLocation().getWorld().spawnParticle(Particle.FLAME, loc, 10);
                                                        stand.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 15);
                                                        stand.getLocation().getWorld().playSound(loc, Sound.BLOCK_ANVIL_USE, 10f, seconds);

                                                    }
                                                    if (seconds > 3 && seconds % 2 == 0) {
                                                        Location loc = stand.getLocation();
                                                        stand.getLocation().getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, loc, 25);
                                                        stand.getLocation().getWorld().spawnParticle(Particle.FLAME, loc, 10);
                                                        stand.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 15);
                                                        stand.getLocation().getWorld().playSound(loc, Sound.BLOCK_ANVIL_USE, 10f, seconds);

                                                    } else  {
                                                        Location loc = stand.getLocation();
                                                        stand.getLocation().getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 10);
                                                        stand.getLocation().getWorld().spawnParticle(Particle.FLAME, loc, 10);
                                                        stand.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 10);
                                                        stand.getLocation().getWorld().playSound(loc, Sound.BLOCK_ANVIL_USE, 10f, 3f);

                                                    }

                                                    seconds++;





                                                }

                                                if (seconds > finalUpgradetime) {
                                                    stand.getLocation().getWorld().playSound(stand.getLocation(), Sound.BLOCK_ANVIL_USE, 10f, 1f);
                                                    stand.removeEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
                                                    ((Player) e.getDamager()).playSound(e.getDamager().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10f, 1f);
                                                    ((Player) e.getDamager()).sendMessage(ChatColor.GREEN + "Sentry Gun was upgraded!");
                                                    ((Player) e.getDamager()).sendMessage(ChatColor.AQUA + "Sentry Gun is now Level " + sentryLevelMap.get(stand) + "!");
                                                    stand.setInvulnerable(false);
                                                    stand.setHeadPose(new EulerAngle(0,0,0));

                                                    if (sentryLevelMap.get(stand) == 2) {
                                                        sentryIngotsLeftForUpgrade.put(stand, 32);
                                                    }
                                                    stand.setMetadata("sentrygun", new FixedMetadataValue(HardcoreGames.getInstance(), ((Player) e.getDamager())));

                                                    cancel();
                                                }
                                            }
                                        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);


                                    }


                                } else {
                                    //do nothing
                                    ((Player) e.getDamager()).sendMessage(ChatColor.RED + "You don't have any Iron Ingots!");
                                    ((Player) e.getDamager()).playSound(e.getDamager().getLocation(), Sound.BLOCK_ANVIL_LAND, 10f, 1f);


                                }
                            } else {
                                ((Player) e.getDamager()).sendMessage(ChatColor.RED + "Your Sentry Gun is at its maximum level!");

                            }
                        }

                        e.setCancelled(true);
                        return;
                    }
                }
                if (!lastDestroyed.containsKey((ArmorStand) e.getEntity())) {
                    if (!sentryDownMap.containsKey((ArmorStand) e.getEntity())) {
                        lastDestroyed.put((ArmorStand) e.getEntity(), System.currentTimeMillis());

                        sentryDownMap.put((ArmorStand) e.getEntity(), true);
                        e.getEntity().getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, e.getEntity().getLocation(), 30);
                        e.getEntity().getLocation().getWorld().spawnParticle(Particle.FLAME, e.getEntity().getLocation(), 30);
                        e.getEntity().getLocation().getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, e.getEntity().getLocation(), 10);

                        e.getEntity().getLocation().getWorld().playSound(e.getEntity().getLocation(), Sound.ITEM_SHIELD_BREAK, 10f, 3f);


                        Random random = new Random(System.currentTimeMillis());
                        int time = ((random.nextInt(8) + 4));
                        Bukkit.getServer().getScheduler().runTaskLater(HardcoreGames.getInstance(), () -> sentryDownMap.remove((ArmorStand) e.getEntity()), 20 * time);
                        Bukkit.getServer().getScheduler().runTaskLater(HardcoreGames.getInstance(), () -> e.getEntity().getLocation().getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10f, 3f), 20 * time);
                        Bukkit.getServer().getScheduler().runTaskLater(HardcoreGames.getInstance(), () -> ((ArmorStand) e.getEntity() ).setHeadPose(new EulerAngle(0, 0, 0)), 20 * time);

                    }
                } else if (System.currentTimeMillis() - lastDestroyed.get((ArmorStand) e.getEntity())  >= 20000) {
                    lastDestroyed.put((ArmorStand) e.getEntity(), System.currentTimeMillis());

                    sentryDownMap.put((ArmorStand) e.getEntity(), true);
                    e.getEntity().getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, e.getEntity().getLocation(), 30);
                    e.getEntity().getLocation().getWorld().spawnParticle(Particle.FLAME, e.getEntity().getLocation(), 30);
                    e.getEntity().getLocation().getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, e.getEntity().getLocation(), 10);

                    e.getEntity().getLocation().getWorld().playSound(e.getEntity().getLocation(), Sound.ITEM_SHIELD_BREAK, 10f, 3f);


                    Random random = new Random(System.currentTimeMillis());
                    Bukkit.getServer().getScheduler().runTaskLater(HardcoreGames.getInstance(), () -> sentryDownMap.remove((ArmorStand) e.getEntity()), 20 * (random.nextInt(4) + 2));
                    Bukkit.getServer().getScheduler().runTaskLater(HardcoreGames.getInstance(), () -> e.getEntity().getLocation().getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10f, 3f), 20 * ((random.nextInt(4) + 2)));

                }

                e.setCancelled(true);
            }
        }

    }

    public boolean hasIron (Player p) {
        //check if iron was consumed successfully
        if (kitInfo.getPlayerKit(p) == Kits.ENGINEER) {
            for (ItemStack i : p.getInventory().getContents()) {
                if (i != null) {
                    if (i.getType() == Material.IRON_INGOT) {
                        i.setAmount(i.getAmount() - 1);
                        return true;
                    }
                }
            }
        } else {
            return false;
        }
        return false;
    }

    private long durationMillis = 5000;

//    public void fireInTheHole(ArmorStand player, Entity target){
//        new BukkitRunnable() {
//            long startMilli = System.currentTimeMillis();
//
//            public void run() {
//                long difference = ((startMilli + durationMillis) - System.currentTimeMillis());
//
//                //5seconds is up
//                if (player.getLocation().distance(target.getLocation()) > 10) {
//                    cancel();
//                }
//                if (difference < 0) {
//                    cancel();
//                }
//            }
//        }.runTaskTimer(HardcoreGames.getInstance(), 0, 10L);
//    }

    public void shootParticle(ArmorStand player, Particle particle, double velocity) {
        Location location = player.getEyeLocation();
        Vector direction = location.getDirection();
        player.getWorld().spawnParticle(particle, location.getX(), location.getY(), location.getZ(), 0, (float) direction.getX(), (float) direction.getY(), (float) direction.getZ(),velocity);
    }
//    @EventHandler
//    public void onMoveEng (PlayerMoveEvent e) {
//        for (Entity ent : e.getPlayer().getLocation().getWorld().getNearbyEntities(e.getPlayer().getLocation(), 10, 10, 10)) {
//            if (ent != e.getPlayer()) {
//                if (entityBehindPlayer(ent, e.getPlayer())) {
//                    Bukkit.broadcastMessage(ent.getType().name() + " behind player");
//                } else {
//                    Bukkit.broadcastMessage(ent.getType().name() + " not behind player");
//                }
//            }
//        }
//    }

    public ItemStack sentryItemStack () {
        Random random = new Random();
        ItemStack skull = null;
        if (random.nextInt(2) == 0) {
            skull = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDZkYjEzN2EzNTY3OWJlYWY3OTAwNzBkMGM5Yzk2YzkwNjc2MjYwZWJjMDBkZDJjNzAwNTYyYTA5OWRiMDdjMCJ9fX0=");
        } else {
            skull = applySkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmU3ZmVmMjUwZTZiM2ViNjM2MGM5NzdlN2M1YzQ4ZjljZGNhNTI5MGJjN2JkY2M5MGQ2MDc5MzNiN2QzYjBkNyJ9fX0==");

        }
        if (skull.getItemMeta() != null) {
            ItemMeta meta = skull.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&1&lSentry Gun"));
            skull.setItemMeta(meta);
        }
        return skull;
    }

    private static boolean entityBehindPlayer(Entity entity, Entity player) {
        Double yaw = 2*Math.PI-Math.PI*player.getLocation().getYaw()/180;
        Vector v = entity.getLocation().toVector().subtract(player.getLocation().toVector());
        Vector r = new Vector(Math.sin(yaw),0, Math.cos(yaw));
        float theta = r.angle(v);
        if (Math.PI/2<theta && theta<3*Math.PI/2) {
            return true;
        }
        return false;
    }
    public static ItemStack applySkullTexture(String base) {

        return SkullCreator.itemFromBase64(base);
    }

}
