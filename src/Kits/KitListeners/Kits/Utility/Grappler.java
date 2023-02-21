package Kits.KitListeners.Kits.Utility;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Kits.KitTools.PlayerGrappleEvent;
import Main.HardcoreGames;
import Util.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Grappler implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();

    public HashMap<Integer, Integer> noFallEntities = new HashMap<Integer, Integer>(); //entity id, delayed task id
    public HashMap<String, Integer> noGrapplePlayers = new HashMap<String, Integer>(); //name, delayed task id
    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event){

        if(event.getDamager() instanceof FishHook){
            FishHook hook = (FishHook)event.getDamager();
            if( ! (hook.getShooter() instanceof Player))
                return;
            Player player = (Player)hook.getShooter();

            //check for if have grap hook
            boolean hasHook = player.getInventory().getItemInMainHand().getItemMeta() != null && ChatColor.stripColor(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Grappling Hook");
            if(hasHook){

                if(event.getEntity() instanceof Player){
                    Player hooked = (Player)event.getEntity();

                        hooked.sendMessage(ChatColor.YELLOW+"You have been hooked by "+ ChatColor.RESET+player.getName()+ChatColor.YELLOW+"!");
                        player.sendMessage(ChatColor.GOLD+"You have hooked "+ChatColor.RESET+hooked.getName()+ChatColor.YELLOW+"!");

                }
                else{
                    String entityName = event.getEntityType().toString().replace("_", " ").toLowerCase();
                    player.sendMessage(ChatColor.GOLD+"You have hooked a "+entityName+"!");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {

            if(noFallEntities.containsKey(event.getEntity().getEntityId()))
                event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onGrapple(PlayerGrappleEvent event){
        if(event.isCancelled())
            return;
        final Player player = event.getPlayer();

        //event.getHookItem().setDurability((short)-10);

        if(noGrapplePlayers.containsKey(player.getName())){
                player.sendMessage(ChatColor.GRAY+"You cannot do that yet.");
                return;

        }

        Entity e = event.getPulledEntity();
        Location loc = event.getPullLocation();

        if(player.equals(e)){ //the player is pulling themself to a location
                if(player.getLocation().distance(loc) < 6) //hook is too close to player
                    pullPlayerSlightly(player, loc);
                else
                    pullEntityToLocation(player, loc);

        }
        else{ //the player is pulling an entity to them

                pullEntityToLocation(e, loc);
                if(e instanceof Item){
                    ItemStack is = ((Item)e).getItemStack();
                    String itemName = is.getType().toString().replace("_", " ").toLowerCase();
                    player.sendMessage(ChatColor.GOLD+"You have hooked a stack of "+is.getAmount()+" "+itemName+"!");
                }

        }

//        if(HookAPI.addUse(player, event.getHookItem()))
//            HookAPI.playGrappleSound(player.getLocation());

//        if(GrapplingHook.timeBetweenUses != 0)
//            HookAPI.addPlayerCooldown(player, GrapplingHook.timeBetweenUses);
    }

    @EventHandler()
    public void onRightClick(PlayerFishEvent e) {
        Player p = e.getPlayer();

        if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.GRAPPLER) {
            boolean hasHook = p.getInventory().getItemInMainHand().getItemMeta() != null && ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).equalsIgnoreCase("Grappling Hook");
            if (e.getState() == PlayerFishEvent.State.REEL_IN || e.getState() == PlayerFishEvent.State.IN_GROUND) {
                if (hasHook) {

                    Location loc1 = p.getLocation();
                    Location loc2 = e.getHook().getLocation();

                    if (e.getHook().getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid()) {

                        Vector v = new Vector(loc2.getX() - loc1.getX(), 1, loc2.getZ() - loc1.getZ());
                        p.setVelocity(v.multiply(new Vector(0.3, 1, 0.3)));
                    }
                }

            } else if (e.getState() == PlayerFishEvent.State.CAUGHT_ENTITY) {
                if (e.getCaught() != null) {
                    Location loc1 = e.getCaught().getLocation();
                    Location loc2 = e.getHook().getLocation();
                    Vector v = new Vector(loc2.getX() - loc1.getX(), 1, loc2.getZ() - loc1.getZ());
                    e.getCaught().setVelocity(v);

                }
            }
        }


        }


    private void pullPlayerSlightly(Player p, Location loc){


        if(loc.getY() > p.getLocation().getY()){
            p.setVelocity(new Vector(0,2,0));
            return;
        }

        Location playerLoc = p.getLocation();

        Vector vector = loc.toVector().subtract(playerLoc.toVector());
        p.setVelocity(vector);
    }

    private void pullEntityToLocation(final Entity e, Location loc){


        Location entityLoc = e.getLocation();

        entityLoc.setY(entityLoc.getY()+0.5);
        e.teleport(entityLoc);

        double g = -0.08;
        double d = loc.distance(entityLoc);
        double t = d;
        double v_x = (1.0+0.07*t) * (loc.getX()-entityLoc.getX())/t;
        double v_y = (1.0+0.03*t) * (loc.getY()-entityLoc.getY())/t -0.5*g*t;
        double v_z = (1.0+0.07*t) * (loc.getZ()-entityLoc.getZ())/t;

        Vector v = e.getVelocity();
        v.setX(v_x);
        v.setY(v_y);
        v.setZ(v_z);
        e.setVelocity(v);

        addNoFall(e, 100);
    }

    public void addNoFall(final Entity e, int ticks) {
        if(noFallEntities.containsKey(e.getEntityId()))
            Bukkit.getServer().getScheduler().cancelTask(noFallEntities.get(e.getEntityId()));

        int taskId = HardcoreGames.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(HardcoreGames.getInstance(),new Runnable() {
            @Override
            public void run(){
                if(noFallEntities.containsKey(e.getEntityId()))
                    noFallEntities.remove(e.getEntityId());
            }
        }, ticks);

        noFallEntities.put(e.getEntityId(), taskId);
    }
}
