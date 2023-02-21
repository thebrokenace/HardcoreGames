package Kits.KitListeners.Kits.Defense;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Util.Game;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;

public class Scout implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    public static final ArrayList<UUID> jumpers = new ArrayList<>();

    @EventHandler
    public void onFallDamage (EntityDamageEvent e) {
        if (game.isStarted()) {
            if (e.getEntity() instanceof Player) {
                Player stomper = (Player)e.getEntity();
                if (kitInfo.getPlayerKit(stomper) == Kits.SCOUT) {

                    if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                        for (PotionEffect pot : stomper.getActivePotionEffects()) {
                            if (pot.getType().equals(PotionEffectType.SPEED)) {
                                if (e.getDamage() > 1) {
                                    e.setDamage(1);
                                }
                            }
                        }



                    }

                }

            }

        }

    }








    @EventHandler
    public void attemptDoubleJump(PlayerToggleFlightEvent event) {
        if (kitInfo.getPlayerKit(event.getPlayer()) == Kits.SCOUT) {
            Player player = event.getPlayer();

            //Don't double jump in these cases
            if (jumpers.contains(player.getUniqueId()) ||
                    !event.isFlying() ||
                    player.getGameMode() == GameMode.CREATIVE ||
                    player.getGameMode() == GameMode.SPECTATOR)
                return;

            event.setCancelled(true);
            player.setAllowFlight(false);
            player.setFlying(false);//Disable to prevent wobbling

            Vector direction = player.getEyeLocation().getDirection();
            if (direction.getY() <= 0)
                direction.setY(0.01);

            player.setVelocity(direction);
            jumpers.add(player.getUniqueId());
            //player.getLocation().getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_LARGE,0, 20);
            //TODO Rework effect and add sound
        }
    }






    @EventHandler
    public void refresh(PlayerMoveEvent event) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(event.getPlayer()) == Kits.SCOUT) {


        Player player = event.getPlayer();

        if(!jumpers.contains(player.getUniqueId()))
            return;

        Location belowPlayer = player.getLocation().subtract(0,0.1,0);
        Block block = belowPlayer.getBlock();

        // Player definitely not grounded so no refresh in sight
        if(block.isEmpty() || block.isLiquid())
            return;

        // No normal block below
        if(isNonGroundMaterial(block.getType()))
            return;
        player.setAllowFlight(true);
        jumpers.remove(player.getUniqueId());
            }
        }
    }

    private boolean isNonGroundMaterial(Material type) {
        return type == Material.LADDER ||
                type == Material.VINE ||
                type == Material.TALL_GRASS ||
                type == Material.LEGACY_DOUBLE_PLANT ||
                type == Material.TORCH ||
                type.toString().contains("FENCE") || // Filters out all fences and gates
                type.toString().contains("DOOR") || // Filters out doors and trapdoors
                type.toString().contains("FLOWER") ||
                type.toString().contains("WALL");
    }
}
