package Kits.KitListeners.Kits.Attack;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import Util.GamePhase;
import Util.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Evoker implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    HashMap<UUID, Long> cooldownMap = new HashMap<UUID, Long>();

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (game.getPhase() == GamePhase.GAMESTARTED) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.EVOKER) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (e.getItem() != null && e.getItem().getItemMeta() != null && ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equals("Evoker Fangs")) {
                        Player p = e.getPlayer();
                        if (cooldownMap.containsKey(p.getUniqueId())) {
                            if (System.currentTimeMillis() - cooldownMap.get(p.getUniqueId()) >= 60000) {
                                if (summon(e.getPlayer())) {
                                    cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                                }


                            } else {
                                p.sendMessage(ChatColor.RED + "You must wait " + (60 - Math.round((System.currentTimeMillis() - ((cooldownMap.get(p.getUniqueId())))) / 1000f)) + " seconds to use this again!");
                            }
                        } else {
                            if (summon(e.getPlayer())) {
                                cooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                            }

                        }
                    }
                }
            }
        }
    }


    public boolean summon(Player hades) {
        Sounds.summonHades(hades.getLocation());
        fangs(hades.getLocation(), hades);
        return true;
    }
    public void fangs (Location l, Player p) {
        Vector forward = p.getEyeLocation().getDirection().setY(0).normalize();
        Location start = p.getLocation().clone().add(forward.clone().multiply(1));
        int count = (int) p.getLocation().distance(l) + 30;
        for (int i = 0; i < count; i++) {
            final int index = i;
            Bukkit.getScheduler().scheduleSyncDelayedTask(HardcoreGames.getInstance(), () -> {
                Location loc = start.clone().add(forward.clone().multiply(index));
                start.getWorld().spawnEntity(loc, EntityType.EVOKER_FANGS);
            }, i * 3 + 3);
        }


//        l.getWorld().spawn(l, EvokerFangs.class);
//        //fangs1.setMetadata("nodamage", new FixedMetadataValue(HardcoreGames.getInstance(), true));
//        for (Location loc : getCircle(l, 2, 25)) {
//            Entity e = loc.getWorld().spawnEntity(loc, EntityType.EVOKER_FANGS);
//            EvokerFangs fangs = ((EvokerFangs) e);
//            fangs.setOwner(p);
//            //fangs.setMetadata("nodamage", new FixedMetadataValue(HardcoreGames.getInstance(), true));
//        }
//        for (Location loc : getCircle(l, 3, 25)) {
//            Entity e = loc.getWorld().spawnEntity(loc, EntityType.EVOKER_FANGS);
//            EvokerFangs fangs = ((EvokerFangs) e);
//            fangs.setOwner(p);
//            //fangs.setMetadata("nodamage", new FixedMetadataValue(HardcoreGames.getInstance(), true));
//        }
//        for (Location loc : getCircle(l, 4, 25)) {
//            Entity e = loc.getWorld().spawnEntity(loc, EntityType.EVOKER_FANGS);
//            EvokerFangs fangs = ((EvokerFangs) e);
//            fangs.setOwner(p);
//            //fangs.setMetadata("nodamage", new FixedMetadataValue(HardcoreGames.getInstance(), true));
//        }
    }

    public ArrayList<Location> getCircle(Location center, double radius, int amount)
    {
        World world = center.getWorld();
        double increment = (2 * Math.PI) / amount;
        ArrayList<Location> locations = new ArrayList<Location>();
        for(int i = 0;i < amount; i++)
        {
            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, center.getY(), z));
        }
        return locations;
    }

//    @EventHandler
//    public void onDamage (EntityDamageByEntityEvent e) {
//        if (e.getDamager() instanceof EvokerFangs) {
//            if (e.getEntity() instanceof Player) {
//                if (kitInfo.getPlayerKit(e.getEntity()) == Kits.EVOKER) {
//                    e.setCancelled(true);
//                }
//            }
//        }
//    }
}