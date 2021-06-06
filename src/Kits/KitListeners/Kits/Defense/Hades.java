package Kits.KitListeners.Kits.Defense;

import Kits.KitListeners.KitUtils.PathfinderGoalFollowPlayer;
import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import Util.Sounds;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Hades implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    HashMap<UUID, Long> cooldownMap = new HashMap<UUID, Long>();
    HashMap<UUID, List<LivingEntity>> hadesArmy = new HashMap<>();
//    @EventHandler
//    public void onRightClick (PlayerInteractEvent e) {
//        if (game.getPhase() == GamePhase.GAMESTARTED) {
//            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.HADES) {
//                if (e.getAction() == Action.RIGHT_CLICK_AIR) {
//                    if (e.getItem() != null && e.getItem().getItemMeta() != null && e.getItem().getItemMeta().getDisplayName().equals("Hades Summoner")) {
//                        Player p = e.getPlayer();
//                        if (cooldownMap.containsKey(p.getUniqueId())) {
//                            if (System.currentTimeMillis() - cooldownMap.get(p.getUniqueId()) >= 60000) {
//                                if (summon(e.getPlayer())) {
//                                    cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
//                                }
//
//
//                            } else {
//                                p.sendMessage(ChatColor.RED + "You must wait " + (60 - Math.round((System.currentTimeMillis() - ((cooldownMap.get(p.getUniqueId()))))/1000f)) + " seconds to use this again!");
//                            }
//                        } else {
//                            if (summon(e.getPlayer())) {
//                                cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
//                            }
//
//                        }
//                    }
//                }
//            }
//        }
//    }

    @EventHandler
    public void onZombieKilled (EntityDeathEvent e) {
        if (game.isStarted()) {
            if (e.getEntity() instanceof Zombie) {
                if (e.getEntity().getKiller() != null) {
                    Player hades = e.getEntity().getKiller();
                    if (kitInfo.getPlayerKit(hades) == Kits.HADES) {
                        if (!((Zombie) e.getEntity()).hasMetadata("noburn")) {


                            summon(hades, e.getEntity().getLocation());
                        }

                    }
                }
            }
            if (e.getEntity() instanceof Skeleton) {
                if (e.getEntity().getKiller() != null) {
                    Player hades = e.getEntity().getKiller();
                    if (kitInfo.getPlayerKit(hades) == Kits.HADES) {
                        if (!((Skeleton) e.getEntity()).hasMetadata("noburn")) {


                            summonskeleton(hades, e.getEntity().getLocation());
                        }

                    }
                }
            }
        }
    }

    @EventHandler
    public void onZombieBurn (EntityDamageEvent e) {
        if (e.getEntity() instanceof  Zombie) {
            if ((e.getEntity()).hasMetadata("noburn")) {
                if (e.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || e.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
                    e.getEntity().setFireTicks(0);
                    e.setCancelled(true);
                }
            }
        }
    }
    public void summonskeleton (Player hades, Location l) {
        Sounds.summonHades(hades.getLocation());

        if (l.getWorld() != null)
            l.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, l, 20);
        Skeleton zombie = l.getWorld().spawn(l, Skeleton.class);
        zombie.setHealth(3);
        //zombie.setTarget(target);
        if (zombie.getEquipment() != null)
            zombie.getEquipment().setItem(EquipmentSlot.HEAD, playerHead(hades));
            //zombie.getEquipment().setItem(EquipmentSlot.HAND, new ItemStack(Material.AIR));
        zombie.setMetadata("noburn", new FixedMetadataValue(HardcoreGames.getInstance(), hades));
        zombie.setCustomName(ChatColor.DARK_PURPLE + "HADES SKELETON");
        zombie.setCustomNameVisible(true);
        zombie.setGlowing(true);
        makePet((LivingEntity) zombie, hades);
        List<LivingEntity> entities = hadesArmy.getOrDefault(hades.getUniqueId(),new ArrayList<>());
        entities.add((LivingEntity) zombie);
        hadesArmy.put(hades.getUniqueId(), entities);
        for (Entity e : l.getWorld().getNearbyEntities(l, 10, 10, 10)) {
            if (e instanceof LivingEntity) {
                zombie.setTarget((LivingEntity) e);

                break;
            }
        }


    }
    public void summon (Player hades, Location l) {
        Sounds.summonHades(hades.getLocation());

                            if (l.getWorld() != null)
                                l.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, l, 20);
                            Zombie zombie = l.getWorld().spawn(l, Zombie.class);
                            zombie.setAdult();
                            zombie.setHealth(3);
                            //zombie.setTarget(target);
                            if (zombie.getEquipment() != null)
                            zombie.getEquipment().setItem(EquipmentSlot.HEAD, playerHead(hades));
                            zombie.setMetadata("noburn", new FixedMetadataValue(HardcoreGames.getInstance(), hades));
                            zombie.setCustomName(ChatColor.DARK_PURPLE + "HADES ZOMBIES");
                            zombie.setCustomNameVisible(true);
                            zombie.setGlowing(true);
                            makePet((LivingEntity) zombie, hades);
                            List<LivingEntity> entities = hadesArmy.getOrDefault(hades.getUniqueId(),new ArrayList<>());
                            entities.add((LivingEntity) zombie);
                            hadesArmy.put(hades.getUniqueId(), entities);
                            for (Entity e : l.getWorld().getNearbyEntities(l, 10, 10, 10)) {
                                if (e instanceof LivingEntity) {
                                    zombie.setTarget((LivingEntity) e);

                                    break;
                                }
                            }


    }
    public static void makePet(LivingEntity entity, Player owner) {
        EntityLiving entityLiving = ((CraftLivingEntity)entity).getHandle();
        if (entityLiving instanceof EntityInsentient) {
            EntityPlayer entityPlayer = ((CraftPlayer)owner).getHandle();
            EntityInsentient entityInsentient = (EntityInsentient)entityLiving;
            EntityCreature entityCreature = (EntityCreature) entityLiving;
            entityInsentient.goalSelector = new PathfinderGoalSelector(entityInsentient.world.getMethodProfilerSupplier());
            entityInsentient.targetSelector = new PathfinderGoalSelector(entityInsentient.world.getMethodProfilerSupplier());
            entityInsentient.goalSelector.a(0, new PathfinderGoalFloat(entityInsentient));
            entityInsentient.goalSelector.a(1, new PathfinderGoalFollowPlayer(entityInsentient, entityPlayer, 1.5D, 10.0F));
            entityInsentient.goalSelector.a(8, new PathfinderGoalLookAtPlayer(entityInsentient, EntityPlayer.class, 8.0F));
            entityInsentient.getAttributeMap().b().add(new AttributeModifiable(GenericAttributes.ATTACK_DAMAGE, (a) -> {a.setValue(1.0);}));
            entityInsentient.getAttributeMap().b().add(new AttributeModifiable(GenericAttributes.FOLLOW_RANGE, (a) -> {a.setValue(1.0);}));
            // Adds attack goal to pig
            entityCreature.goalSelector.a(0, new PathfinderGoalMeleeAttack(entityCreature, 1.0D, false));
            entityInsentient.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(entityInsentient, EntityPlayer.class, true));
            entityCreature.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(entityCreature, 1.0));

            entityCreature.goalSelector.a(7, new PathfinderGoalRandomStrollLand(entityCreature, 1.0));


        } else {
            throw new IllegalArgumentException(entityLiving.getClass().getSimpleName() + " is not an instance of an EntityInsentient.");
        }
    }

    public ItemStack playerHead (Player hades) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null)
        meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);

        item.setItemMeta(meta);
        SkullMeta skullmeta = (SkullMeta)item.getItemMeta();
        skullmeta.setOwningPlayer(Bukkit.getOfflinePlayer(hades.getUniqueId()));
        item.setItemMeta(skullmeta);
        return item;
    }


    @EventHandler
    public void onTarget (EntityTargetEvent e) {
        if (e.getEntity().hasMetadata("noburn")) {
            Player p = (Player) (e.getEntity()).getMetadata("noburn").get(0).value();
            if (e.getTarget() == (p)) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onEntityDamage (EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Zombie) {
            if (e.getEntity() instanceof Player) {
                if (((Zombie) e.getDamager() ).hasMetadata("noburn")) {
                    Player p = (Player) ((Zombie) e.getDamager()).getMetadata("noburn").get(0).value();
                    //Bukkit.broadcastMessage(e.getEntity().getName() + "is damaged by " + p.getName() + " zombie");
                    if (p != (Player) e.getEntity()) {
                        ((Player) e.getEntity()).damage(e.getDamage(), p);
                        e.setCancelled(true);
                    } else {
                        e.setCancelled(true);
                    }
                }
            }
        }
        if (e.getDamager() instanceof Skeleton) {
            if (e.getEntity() instanceof Player) {
                if (((Skeleton) e.getDamager() ).hasMetadata("noburn")) {
                    Player p = (Player) ((Skeleton) e.getDamager()).getMetadata("noburn").get(0).value();
                    //Bukkit.broadcastMessage(e.getEntity().getName() + "is damaged by " + p.getName() + " zombie");
                    if (p != (Player) e.getEntity()) {
                        ((Player) e.getEntity()).damage(e.getDamage(), p);
                        e.setCancelled(true);
                    } else {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

//    @EventHandler
//    public void makeTarget (EntityDamageByEntityEvent e) {
//        if (e.getDamager() instanceof Player) {
//            if (e.getEntity() instanceof Player) {
//                if (kitInfo.getPlayerKit((Player) e.getDamager()) == Kits.HADES) {
//                    if (hadesArmy.containsKey(e.getDamager().getUniqueId())) {
//                        List<LivingEntity> army = hadesArmy.get(e.getDamager().getUniqueId());
//                        Player owner = (Player) e.getDamager();
//                        for (LivingEntity ent : army) {
//
//                            EntityLiving entityLiving = ((CraftLivingEntity)ent).getHandle();
//                            if (entityLiving instanceof EntityInsentient) {
//                                EntityPlayer entityPlayer = ((CraftPlayer) owner).getHandle();
//                                EntityPlayer target  = ((CraftPlayer) (Player) e.getEntity()).getHandle();
//
//                                EntityInsentient entityInsentient = (EntityInsentient) entityLiving;
//                                EntityCreature entityCreature = (EntityCreature) entityLiving;
//                                entityCreature.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(entityCreature, EntityCreeper.class, true));
//
//                                entityCreature.goalSelector.a(0, new PathfinderGoalMeleeAttack(entityCreature, 1.0D, false));
//
//
//                                entityInsentient.goalSelector.a(1, new PathfinderGoalFollowPlayer(entityInsentient, target, 1.5D, 1.0F));
//
//
//
//
//                            }
//                        }
//
//                    }
//                }
//            }
//        }
//    }
}
