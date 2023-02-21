package Kits.KitListeners.Kits.Vanity;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import Util.GamePhase;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCDamageByEntityEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.Controllable;
import net.citizensnpcs.trait.SkinTrait;
import net.citizensnpcs.util.PlayerAnimation;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Stand implements Listener {
    static Game game = Game.getSharedGame();
    static KitInfo kitInfo = KitInfo.getSharedKitInfo();
    static HashMap<Player, NPC> standMap = new HashMap<>();
    static HashMap<NPC,Entity> attacking = new HashMap<>();


    public static void giveStands () {
        new BukkitRunnable() {
            int time = 0;

            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {

                    if (game.isStarted()) {
                        if (kitInfo.getPlayerKit(p) == Kits.STAND) {
                            if (!standMap.containsKey(p)) {
                                NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, p.getName() + "'s " + "Stand");

                                //npc.removeTrait(SkinTrait.class);
                                //skin(npc, "https://crafatar.com/skins/" + p.getUniqueId());


                                npc.spawn(p.getLocation());
                                npc.getOrAddTrait(SkinTrait.class).setSkinName("StarrPlatnum");
                                //((Player) npc.getEntity()).setInvisible(true);
                                npc.getOrAddTrait(Controllable.class).setEnabled(false);


                                Scoreboard sc = Bukkit.getScoreboardManager().getMainScoreboard();

                                Team sca = sc.getTeam("Stand");
                                if (sc.getTeam("Stand") == null) {
                                    sc.registerNewTeam("Stand");
                                    sca = sc.getTeam("Stand");
                                }
                                if (sca != null) {
                                    for (String s : sca.getEntries()) {
                                        sca.removeEntry(s);
                                    }
                                }


                                npc.setProtected(true);

                                npc.getEntity().setMetadata("standnpc", new FixedMetadataValue(HardcoreGames.getInstance(), true));
                                sca.setCanSeeFriendlyInvisibles(true);

                                sca.addEntry(p.getName());
                                sca.addEntry(npc.getFullName());
                                sca.addEntry(npc.getEntity().getUniqueId().toString());
                                sca.setAllowFriendlyFire(false);

                                sca.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);

                                standMap.put(p, npc);

                                //((Player) npc.getEntity()).setInvisible(true);
                                npc.data().set(NPC.COLLIDABLE_METADATA, true);

                            }
                            NPC npc = standMap.get(p);

                                //p.setCollidable(false);

                                if (p.getEquipment() != null) {

                                    npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HELMET, p.getEquipment().getHelmet());
                                npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.CHESTPLATE, p.getEquipment().getChestplate());
                                npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.LEGGINGS, p.getEquipment().getLeggings());
                                npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.BOOTS, p.getEquipment().getBoots());
                                npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.OFF_HAND, p.getEquipment().getItemInOffHand());
                                npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HAND, p.getEquipment().getItemInMainHand());
                            }

                                if (!attacking.containsKey(npc)) {

                                    Location standby = getBlockBehindPlayer(p).clone().add(-0.5, 1.5, 0);
                                    standby.setDirection(p.getEyeLocation().getDirection().multiply(-1));
                                    standby.setPitch(p.getLocation().getPitch());
                                    standby.setYaw(p.getLocation().getYaw());
                                    if (npc.getEntity().getLocation().distance(p.getLocation()) < 2) {
                                        standby.add(0,1,0);
                                    }
                                    npc.getEntity().teleport(standby);

                                } else {

                                    if (attacking.get(npc).getLocation().distance(p.getLocation()) > 12) {
                                        attacking.remove(npc);
                                    }
                                    if (attacking.get(npc).isDead()) {
                                        attacking.remove(npc);
                                    }
                                    Location behindEntity = getBlockBehindPlayer(attacking.get(npc)).clone().add(-0.5, 1.5, 0);
                                    behindEntity.setDirection(p.getEyeLocation().getDirection().multiply(-1));
                                    behindEntity.setYaw(p.getLocation().getYaw());
                                    behindEntity.setPitch(p.getLocation().getPitch());

                                    if (behindEntity.distance(p.getLocation()) < 1.5) {
                                        behindEntity.add(0,1,0);
                                    }
                                    npc.getEntity().teleport(behindEntity);
                                    npc.faceLocation(attacking.get(npc).getLocation());


                                }



                                if (p.isSneaking()) {
                                    sneak(npc);
                                } else {
                                    unsneak(npc);
                                }

                                if (p.isDead() || !p.isOnline() || p.getGameMode() != GameMode.SURVIVAL || kitInfo.getPlayerKit(p) != Kits.STAND) {
                                    npc.destroy();
                                    cancel();
                                }



                                if (time == 20 * 6) {
                                    //((Player) npc.getEntity()).setInvisible(true);
                                    Scoreboard sc = Bukkit.getScoreboardManager().getMainScoreboard();

                                    Team sca = sc.getTeam("Stand");
                                    if (sc.getTeam("Stand") == null) {
                                        sc.registerNewTeam("Stand");
                                        sca = sc.getTeam("Stand");
                                    }
                                    if (sca != null) {
                                        for (String s : sca.getEntries()) {
                                            sca.removeEntry(s);
                                        }
                                    }


                                    npc.setProtected(true);

                                    npc.getEntity().setMetadata("standnpc", new FixedMetadataValue(HardcoreGames.getInstance(), true));
                                    sca.setCanSeeFriendlyInvisibles(true);

                                    sca.addEntry(p.getName());
                                    sca.addEntry(npc.getFullName());
                                    sca.addEntry(npc.getEntity().getUniqueId().toString());
                                    sca.addEntry(((Player)npc.getEntity()).getUniqueId().toString());
                                    sca.addEntry(((Player)npc.getEntity()).getName());

                                    sca.setAllowFriendlyFire(false);

                                    Bukkit.broadcastMessage(sca.getEntries().toString());
                                    time++;

                                } else {
                                    time++;
                                }


                        }

                    }
                }
            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);

    }

    @EventHandler
    public void damageNPC (NPCDamageByEntityEvent e) {
        if (game.isStarted()) {
            if (e.getNPC().getEntity().hasMetadata("standnpc")) {
                Player owner = getKeyByValue(standMap, e.getNPC());
                if (owner != e.getDamager()) {
                    owner.damage(2);
                }
                e.setCancelled(true);
            }
        }
    }
    public static Player getKeyByValue(Map<Player, NPC> map, NPC value) {
        for (Map.Entry<Player, NPC> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @EventHandler
    public void onConsume (PlayerItemConsumeEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.STAND) {
                if (standMap.get(e.getPlayer()) != null) {
                    NPC npc = standMap.get(e.getPlayer());
                    eatFood(npc);
                }
            }
        }
    }

    @EventHandler
    public void onDamage (EntityDamageByEntityEvent e) {
        if (e.getDamager().hasMetadata("standball")) {
            if (((Player) e.getDamager().getMetadata("standball").get(0).value()).getLocation().distance(e.getEntity().getLocation()) >= 9) {
                e.setCancelled(true);
            }
        }

    }
    @EventHandler
    public void reachStand (PlayerInteractEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.STAND) {
                if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    Entity snowball = e.getPlayer().launchProjectile(Snowball.class);
                    snowball.setVelocity(e.getPlayer().getEyeLocation().getDirection().multiply(20));
                    snowball.setMetadata("standball", new FixedMetadataValue(HardcoreGames.getInstance(), e.getPlayer()));


                    for( Player on : Bukkit.getServer().getOnlinePlayers()) {
                        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(snowball.getEntityId());
                        ((CraftPlayer) on).getHandle().playerConnection.sendPacket(packet);
                    }

//                    new BukkitRunnable() {
//                        int time = 0;
//                        @Override
//                        public void run() {
//                            time++;
//                            if (snowball.getLocation().distance(e.getPlayer().getLocation()) > 20) {
//                                snowball.remove();
//                                cancel();
//                            }
//                            if (time*20 >= 10) {
//                                snowball.remove();
//                                cancel();
//                            }
//                        }
//                    }.runTaskTimer(HardcoreGames.getInstance(), 0L, 1L);
                    //Set<Material> set = new HashSet<>();
                    //Material[] ignoredMaterials = {};

                    //set.addAll(Arrays.asList(ignoredMaterials));
                   // e.getPlayer().getBlo
                }
            }
        }
    }

    @EventHandler
    public void onDamageSnowball (ProjectileHitEvent e) {
        if (game.isStarted()){
            if (e.getHitEntity() != null) {
                if (e.getEntity().getShooter() instanceof Player) {
                    if (kitInfo.getPlayerKit((Player)e.getEntity().getShooter()) == Kits.STAND) {
                        if (e.getEntity() instanceof Snowball) {
                            if (e.getHitEntity() instanceof LivingEntity) {
                                if (e.getHitEntity() instanceof Player && game.getPhase() != GamePhase.GAMESTARTED) {
                                    return;
                                }
                                if (!e.getHitEntity().hasMetadata("standnpc")) {
                                    if (e.getEntity().hasMetadata("standball")) {

                                        if (e.getHitEntity() != e.getEntity().getShooter() && e.getHitEntity() != standMap.get((Player) e.getEntity().getShooter())) {
                                            if (e.getHitEntity().getLocation().distance(((Player) e.getEntity().getMetadata("standball").get(0).value()).getLocation()) <= 9) {

                                                NPC npc  = standMap.get((Player) e.getEntity().getShooter());
                                                if (!attacking.containsKey(npc)) {

                                                    attacking.put(standMap.get((Player) e.getEntity().getShooter()), e.getHitEntity());
                                                    ((LivingEntity) e.getHitEntity()).damage(1, (Player) e.getEntity().getMetadata("standball").get(0).value());
                                                    Random random = new Random();
                                                    if (random.nextDouble() <= 0.10) {
                                                        Random ora = new Random();
                                                        if (ora.nextDouble() <= 0.50) {
                                                        ((Player) e.getEntity().getMetadata("standball").get(0).value()).sendMessage(ChatColor.BLUE + "ゴゴゴゴゴゴゴゴゴゴゴゴ!!");
                                                            ((LivingEntity) e.getHitEntity()).damage(2, (Player) e.getEntity().getMetadata("standball").get(0).value());
                                                            e.getHitEntity().getLocation().getWorld().spawnParticle(org.bukkit.Particle.CRIT, e.getHitEntity().getLocation(), 25);

                                                        } else {

                                                            ((Player) e.getEntity().getMetadata("standball").get(0).value()).sendMessage(ChatColor.BLUE + "ORA ORA");
                                                            ((LivingEntity) e.getHitEntity()).damage(2, (Player) e.getEntity().getMetadata("standball").get(0).value());
                                                            e.getHitEntity().getLocation().getWorld().spawnParticle(org.bukkit.Particle.CRIT, e.getHitEntity().getLocation(), 25);

                                                        }
                                                    }
                                                } else {
                                                    attacking.remove(npc);
                                                    attacking.put(standMap.get((Player) e.getEntity().getShooter()), e.getHitEntity());
                                                    ((LivingEntity) e.getHitEntity()).damage(1, (Player) e.getEntity().getMetadata("standball").get(0).value());
                                                    Random random = new Random();
                                                    if (random.nextDouble() <= 0.10) {
                                                        Random ora = new Random();
                                                        if (ora.nextDouble() <= 0.50) {
                                                            ((Player) e.getEntity().getMetadata("standball").get(0).value()).sendMessage(ChatColor.BLUE + "ゴゴゴゴゴゴゴゴゴゴゴゴ!!");
                                                            ((LivingEntity) e.getHitEntity()).damage(2, (Player) e.getEntity().getMetadata("standball").get(0).value());
                                                            e.getHitEntity().getLocation().getWorld().spawnParticle(org.bukkit.Particle.CRIT, e.getHitEntity().getLocation(), 25);
                                                        } else {

                                                            ((Player) e.getEntity().getMetadata("standball").get(0).value()).sendMessage(ChatColor.BLUE + "ORA ORA");
                                                            ((LivingEntity) e.getHitEntity()).damage(2, (Player) e.getEntity().getMetadata("standball").get(0).value());
                                                            e.getHitEntity().getLocation().getWorld().spawnParticle(org.bukkit.Particle.CRIT, e.getHitEntity().getLocation(), 25);

                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void onInteractPlayer(PlayerInteractEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.STAND) {
                if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    playAnimation(standMap.get(e.getPlayer()));
                }
            }
        }
    }

    private static void sneak(NPC npc) {
        DataWatcher dw = new DataWatcher(null);
        dw.register(new DataWatcherObject<>(6, DataWatcherRegistry.s), EntityPose.CROUCHING);
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(npc.getEntity().getEntityId(), dw, true);
        for (Player p : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }
    private static void unsneak(NPC npc) {
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

    public void eatFood(NPC npc) {

        PlayerAnimation.EAT_FOOD.play((Player) npc.getEntity());
//        PlayerAnimation.START_ELYTRA.play((Player) npc.getEntity());
//
//        PlayerAnimation.EAT_FOOD.play((Player) npc.getEntity());

    }

    public static Location getBlockBehindPlayer(Entity player) {
        Vector inverseDirectionVec = player.getLocation().getDirection().normalize().multiply(-0.75);
        Location standby = player.getLocation().add(inverseDirectionVec);
        standby.setDirection(player.getLocation().getDirection());
        standby.setPitch(player.getLocation().getPitch());
        standby.setYaw(player.getLocation().getYaw());
        return standby;
    }

}
