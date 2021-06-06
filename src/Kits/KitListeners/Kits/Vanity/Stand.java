package Kits.KitListeners.Kits.Vanity;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
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
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
                                NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, p.getName() + "'s Stand");

                                //npc.removeTrait(SkinTrait.class);
                                //skin(npc, "https://crafatar.com/skins/" + p.getUniqueId());


                                npc.spawn(p.getLocation());
                                //((Player) npc.getEntity()).setInvisible(true);


                                Scoreboard sc = Bukkit.getScoreboardManager().getMainScoreboard();

                                Team sca = sc.getTeam(p.getName() + "Stand");
                                if (sc.getTeam(p.getName() + "Stand") == null) {
                                    sc.registerNewTeam(p.getName() + "Stand");
                                    sca = sc.getTeam(p.getName() + "Stand");
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

                                    Location standby = p.getLocation().clone();
                                    standby.setDirection(p.getLocation().getDirection());
                                    standby.setPitch(p.getLocation().getPitch());
                                    standby.setYaw(p.getLocation().getYaw());

                                    npc.getEntity().teleport(getBlockBehindPlayer(p).clone().add(-0.5, 1, 0));

                                } else {

                                    if (attacking.get(npc).getLocation().distance(p.getLocation()) > 12) {
                                        attacking.remove(npc);
                                    }
                                    if (attacking.get(npc).isDead()) {
                                        attacking.remove(npc);
                                    }
                                    Location behindEntity = getBlockBehindPlayer(attacking.get(npc)).clone().add(-0.5, 1, 0);
                                    behindEntity.setDirection(p.getEyeLocation().getDirection());
                                    behindEntity.setYaw(p.getLocation().getYaw());
                                    behindEntity.setPitch(p.getLocation().getPitch());
                                    npc.getEntity().teleport(behindEntity);
                                    npc.faceLocation(attacking.get(npc).getLocation());


                                }

                                if (p.isInWater()) {
                                    if (!p.isInsideVehicle()) {
                                        npc.getOrAddTrait(Controllable.class).setEnabled(true);
                                        npc.getOrAddTrait(Controllable.class).mount(p);
                                        npc.getNavigator().getDefaultParameters().baseSpeed(5L);
                                    }
                                } else {
                                    npc.getOrAddTrait(Controllable.class).setEnabled(true);

//                                    npc.removeTrait(Controllable.class);
//                                    npc.getNavigator().getDefaultParameters().baseSpeed(1L);



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
                                    ((Player) npc.getEntity()).setInvisible(true);
                                    Scoreboard sc = Bukkit.getScoreboardManager().getMainScoreboard();

                                    Team sca = sc.getTeam(p.getName() + "Stand");
                                    if (sc.getTeam(p.getName() + "Stand") == null) {
                                        sc.registerNewTeam(p.getName() + "Stand");
                                        sca = sc.getTeam(p.getName() + "Stand");
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
                Bukkit.broadcastMessage("hit stand");
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
                                                        } else {

                                                            ((Player) e.getEntity().getMetadata("standball").get(0).value()).sendMessage(ChatColor.BLUE + "ORA ORA");

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
                                                        } else {

                                                            ((Player) e.getEntity().getMetadata("standball").get(0).value()).sendMessage(ChatColor.BLUE + "ORA ORA");

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
    public static BufferedImage invertImage(BufferedImage inputFile) {


        for (int x = 0; x < inputFile.getWidth(); x++) {
            for (int y = 0; y < inputFile.getHeight(); y++) {
                int rgba = inputFile.getRGB(x, y);
                Color col = new Color(rgba, true);
                col = new Color(255 - col.getRed(),
                        255 - col.getGreen(),
                        255 - col.getBlue());
                inputFile.setRGB(x, y, col.getRGB());
            }
        }

        return inputFile;
    }
    public static void skin(final NPC npc, String URL)  {
        String skinName = npc.getName();
        final SkinTrait trait = npc.getOrAddTrait(SkinTrait.class);

            final String url = URL;
            Bukkit.getScheduler().runTaskAsynchronously(CitizensAPI.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    DataOutputStream out = null;
                    BufferedReader reader = null;
                    try {
                        URL target = new URL("https://api.mineskin.org/generate/url");
                        HttpURLConnection con = (HttpURLConnection) target.openConnection();
                        con.setRequestMethod("POST");
                        con.setDoOutput(true);
                        con.setConnectTimeout(1000);
                        con.setReadTimeout(30000);
                        out = new DataOutputStream(con.getOutputStream());
                        out.writeBytes("url=" + URLEncoder.encode(url, "UTF-8"));
                        out.close();

                        reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        JSONObject output = (JSONObject) new JSONParser().parse(reader);
                        JSONObject data = (JSONObject) output.get("data");
                        String uuid = (String) data.get("uuid");
                        JSONObject texture = (JSONObject) data.get("texture");
                        String textureEncoded = (String) texture.get("value");
                        String signature = (String) texture.get("signature");
                        con.disconnect();

                        trait.setSkinPersistent(uuid, signature, textureEncoded);


                    } catch (Throwable t) {

                    } finally {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException e) {
                            }
                        }
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                }

            });

        trait.setSkinName(skinName, true);


    }
}
