package Kits.KitListeners.Kits.Utility;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import Util.Sounds;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Acrobat implements Listener {
    Game game = Game.getSharedGame();
    KitInfo kitInfo = KitInfo.getSharedKitInfo();
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getPlayer()) == Kits.ACROBAT) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (e.getItem() != null && e.getItem().getItemMeta() != null && ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()).equals("Acrobat Tool")) {
                        if (e.getClickedBlock() != null) {
                            Block block = e.getClickedBlock();
                            if (block.getType() != Material.AIR && block.getType() != Material.BEDROCK) {
                                convertBlock(block, e.getPlayer());
                                e.getItem().setAmount(e.getItem().getAmount()-1);
                                Sounds.acrobatChangeBlock(e.getPlayer());
                            }

                        }

                    }

                }
            }
        }
    }

    @EventHandler
    public void onFall (EntityDamageEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getEntity()) == Kits.ACROBAT) {
                if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                    if (((Player) e.getEntity()).isSneaking()) {
                        e.setDamage(e.getDamage()/2);
                        e.getEntity().sendMessage(ChatColor.GREEN + "Did a roll!");
                    }
                }
            }
        }
    }
    public void convertBlock(Block b, Player p) {
        Material mat = b.getType();
        if (b.getLocation().getWorld() != null) {

            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 2);

            FallingBlock fallingBlock = b.getLocation().getWorld().spawnFallingBlock(b.getLocation().clone().add(0.5, 0, 0.5), b.getBlockData());
            fallingBlock.setInvulnerable(true);
            fallingBlock.setGravity(false);
            fallingBlock.setDropItem(false);

            b.setType(Material.AIR);

            new BukkitRunnable() {
                int time = 0;

                @Override
                public void run() {
                    p.spawnParticle(Particle.REDSTONE,fallingBlock.getLocation().clone().add(0,1,0), 1, dustOptions);
                    if (time > 30) {
                        fallingBlock.remove();
                        b.setType(mat);
                        cancel();
                    }
                    time++;
                }
            }.runTaskTimer(HardcoreGames.getInstance(), 0L, 20L);

        }
    }
}
