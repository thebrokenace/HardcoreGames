package Kits.KitListeners.Kits.Vanity;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import com.mojang.authlib.GameProfile;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.astar.pathfinder.Path;
import net.citizensnpcs.api.event.NPCDamageByEntityEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import net.citizensnpcs.trait.waypoint.Waypoints;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.UUID;

public class Wisp implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void onRightClickInteract (PlayerInteractEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.WISP) {
                Block b = e.getClickedBlock();
                if (b != null) {
                    Location spawnlocation = b.getLocation().clone().add(0,1,0);
                    if (e.getItem() != null && e.getItem().getItemMeta() != null && ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Wisp Decoy")) {
                        spawnWisp(spawnlocation, e.getPlayer());
                        e.getItem().setAmount(e.getItem().getAmount()-1);
                    }


                }


            }
        }
    }

    public void spawnWisp (Location loc, Player p) {

        spawnFakePlayer(p, loc, p.getName());

    }


    public void spawnFakePlayer(Player player, Location location, String displayName) {
//        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
//        WorldServer world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
//        EntityPlayer npc = new EntityPlayer(server,
//                world,
//                new GameProfile(player.getUniqueId(),
//                        displayName),
//                new PlayerInteractManager(world));
//
//        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, displayName);
            npc.setName(displayName);
            npc.getOrAddTrait(SkinTrait.class).setSkinName(displayName);
            npc.getOrAddTrait(Waypoints.class).setWaypointProvider("wander");
            npc.getNavigator().getDefaultParameters().stuckAction(null);
            npc.spawn(location);
            npc.setProtected(true);
            npc.getEntity().setMetadata("wisp", new FixedMetadataValue(HardcoreGames.getInstance(), true));
            Bukkit.getServer().getScheduler().runTaskLater(HardcoreGames.getInstance(), (Runnable) npc::destroy, 1200);



//        for (Player p : Bukkit.getOnlinePlayers()) {
//            PlayerConnection connection = ((CraftPlayer)p).getHandle().playerConnection;
//            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
//            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
//
//            Bukkit.getServer().getScheduler().runTaskLater(HardcoreGames.getInstance(), () ->  connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc)), 200);
//            Bukkit.getServer().getScheduler().runTaskLater(HardcoreGames.getInstance(), () ->  connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc)), 200);
//
//        }

    }

    @EventHandler
    public void entityDamage (NPCDamageByEntityEvent e) {
        if (e.getNPC().getEntity().hasMetadata("wisp")) {
            e.getNPC().getEntity().getLocation().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, e.getNPC().getEntity().getLocation(), 10);
            e.getNPC().destroy();
        }
    }


    @EventHandler
    public void craftDiamond (PrepareItemCraftEvent e) {
        if (game.isStarted()) {
            for (ItemStack i : e.getInventory().getMatrix()) {
                if (i != null && i.getItemMeta() != null && ChatColor.stripColor(i.getItemMeta().getDisplayName()).equalsIgnoreCase("Wisp Decoy")) {
                    e.getInventory().setResult(new ItemStack(Material.AIR));
                }
            }
        }
    }
}
