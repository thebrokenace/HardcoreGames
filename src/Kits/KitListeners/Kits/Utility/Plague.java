package Kits.KitListeners.Kits.Utility;

import Kits.KitTools.KitInfo;
import Kits.KitTools.Kits;
import Main.HardcoreGames;
import Util.Game;
import Util.GamePhase;
import Util.SkullCreator;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Plague implements Listener {
    static Game game = Game.getSharedGame();
    static KitInfo kitInfo = KitInfo.getSharedKitInfo();
    static List<UUID> infected = new ArrayList<>();


    @EventHandler
    public void onDamage (EntityDamageByEntityEvent e) {
        if (game.isStarted()) {
            if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
                Player plague = (Player) e.getDamager();
                if (kitInfo.getPlayerKit(plague) == Kits.PLAGUE) {
                    if (holdingInfectItem(plague)) {
                        if (!isInfected((Player) e.getEntity())) {
                            infect((Player) e.getEntity());
                            plague.getInventory().getItemInMainHand().setAmount(plague.getInventory().getItemInMainHand().getAmount()-1);

                        } else {
                            ((Player) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 150, 0));
                            e.setDamage(e.getDamage()*2);
                            plague.getInventory().getItemInMainHand().setAmount(plague.getInventory().getItemInMainHand().getAmount()-1);

                        }
                    }


                }
            }
        }

    }

    @EventHandler
    public void onDamageNormal (EntityDamageByEntityEvent e) {
        if (game.getPhase() == GamePhase.GAMESTARTED) {
            if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
                Player plague = (Player) e.getDamager();
                if (kitInfo.getPlayerKit(plague) != Kits.PLAGUE) {
                    if (isInfected(plague)) {
                        Random random = new Random(System.currentTimeMillis());
                        if (random.nextDouble() <= 0.50) {
                            if (!isInfected((Player) e.getEntity())) {
                                infect((Player) e.getEntity());

                            }
                        }
                    }

                } else {
                    Random random = new Random(System.currentTimeMillis());
                    if (random.nextDouble() <= 0.30) {
                        if (!isInfected((Player) e.getEntity())) {
                            infect((Player) e.getEntity());

                        }
                    }
                }


            }
        }

    }
    @EventHandler
    public void onDamageNormalAttack (EntityDamageByEntityEvent e) {
        if (game.getPhase() == GamePhase.GAMESTARTED) {
            if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
                Player plague = (Player) e.getEntity();
                if (kitInfo.getPlayerKit(plague) != Kits.PLAGUE) {

                    if (isInfected(plague)) {
                        Random random = new Random(System.currentTimeMillis());
                        if (random.nextDouble() <= 0.50) {
                            if (!isInfected((Player) e.getDamager())) {
                                infect((Player) e.getDamager());

                            }
                        }
                    }
                }


            }
        }

    }

    @EventHandler
    public void onInfectHit (EntityDamageByEntityEvent e) {
        if (game.isStarted()) {
            if (e.getDamager() instanceof Arrow) {
                if (e.getDamager().hasMetadata("infectarrow")) {
                    if (e.getEntity() instanceof Player) {
                        infect((Player) e.getEntity());
                        e.setDamage(0);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onShoot (EntityShootBowEvent e) {
        if (game.isStarted()) {
            if (kitInfo.getPlayerKit(e.getEntity()) == Kits.PLAGUE) {
                if ((e.getConsumable() != null) && (e.getConsumable().getItemMeta() != null) && ChatColor.stripColor(e.getConsumable().getItemMeta().getDisplayName()).equals("Infection Arrows")) {
                    e.getProjectile().setMetadata("infectarrow", new FixedMetadataValue(HardcoreGames.getInstance(), true));
                }
            }
        }
    }
    public void spawnInfectParticles (Location loc) {

        loc.getWorld().spawnParticle(Particle.SPELL_MOB, loc, 0, 0, 100, 0, 1);
        loc.getWorld().spawnParticle(Particle.SPELL_MOB, loc.clone().add(0,1,0), 0, 0, 100, 0, 1);
        loc.getWorld().spawnParticle(Particle.SPELL_MOB, loc.clone().add(1,0,0), 0, 0, 100, 0, 1);
        loc.getWorld().spawnParticle(Particle.SPELL_MOB, loc.clone().add(1,1,1), 0, 0, 100, 0, 1);
        loc.getWorld().spawnParticle(Particle.SPELL_MOB, loc.clone().add(0,0,1), 0, 0, 100, 0, 1);
        loc.getWorld().spawnParticle(Particle.SPELL_MOB, loc.clone().add(1,0,1), 0, 0, 100, 0, 1);
        loc.getWorld().spawnParticle(Particle.SPELL_MOB, loc.clone().add(2,0,2), 0, 0, 100, 0, 1);
        loc.getWorld().spawnParticle(Particle.SPELL_MOB, loc.clone().add(2,0,0), 0, 0, 100, 0, 1);
        loc.getWorld().spawnParticle(Particle.SPELL_MOB, loc.clone().add(2,1,1), 0, 0, 100, 0, 1);
        loc.getWorld().spawnParticle(Particle.SPELL_MOB, loc.clone().add(2,2,2), 0, 0, 100, 0, 1);


    }

    public static ItemStack plagueDoctorMask () {
        ItemStack mask = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTg2NGQyNDVjZDQxMDljNGE2MjdiMTkzNGM3N2Q0N2I3NGM1ZWQxODQzY2ViZDU0YWU0NWNhMDFhNmFhNWQwMyJ9fX0=");
        ItemMeta meta = mask.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5&lPlague Doctor Mask"));
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&d&oThis mask protects you from"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&d&othe plague, and a bad fashion sense."));
        meta.setLore(lore);
        mask.setItemMeta(meta);
        return mask;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            if (kitInfo.getPlayerKit(e.getWhoClicked()) == Kits.PLAGUE) {
                if (e.getCurrentItem().equals(plagueDoctorMask())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    public static void plagueCheckForInfection () {
        BukkitTask task = new BukkitRunnable() {
            boolean infectionScene = false;
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (kitInfo.getPlayerKit(p) == Kits.PLAGUE) {
                        p.getEquipment().setHelmet(plagueDoctorMask());


                        if (!infected.contains(p.getUniqueId())) {
                            infected.add(p.getUniqueId());
                        }
                        boolean allInfected = true;
                        for (UUID uuid : game.currentPlayerList()) {
                            if (!infected.contains(uuid)) {
                                allInfected = false;
                            }

                        }

                        boolean atLeastOnePlague = false;
                        for (Player pl : Bukkit.getOnlinePlayers()) {
                            if (pl.getGameMode() != GameMode.SPECTATOR) {
                                if (kitInfo.getPlayerKit(pl) == Kits.PLAGUE) {
                                    atLeastOnePlague = true;
                                    break;
                                }
                            }
                        }

                        if (allInfected && !infectionScene && atLeastOnePlague) {
                            infectionScene = true;
                            Bukkit.broadcastMessage(ChatColor.BLACK + "ALL PLAYERS HAVE BEEN STRICKEN WITH DISEASE! ALL PLAGUES ARE NOW PESTILENCE, THE HORSEMEN OF DEATH!");
                            for (Player pe : Bukkit.getOnlinePlayers()) {
                                if (kitInfo.getPlayerKit(pe) == Kits.PLAGUE) {
                                    pe.getLocation().getWorld().strikeLightningEffect(pe.getLocation());
                                    Horse horse = pe.getLocation().getWorld().spawn(pe.getLocation(), Horse.class);
                                    horse.setColor(Horse.Color.BLACK);
                                    horse.setStyle(Horse.Style.WHITE_DOTS);
                                    horse.getPassengers().add(pe);
                                    horse.setCustomNameVisible(true);
                                    horse.getInventory().setArmor(new ItemStack(Material.DIAMOND_HORSE_ARMOR));
                                    horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                                    horse.addPassenger(pe);
                                    horse.setOwner(pe);
                                    horse.setCustomName(ChatColor.translateAlternateColorCodes('&', "&4&lPESTILENCE"));

                                }
                            }
                        }

                        if (infectionScene) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 35, 1));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 35, 0));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 35, 0));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 35, 0));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 35, 0));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 35, 0));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 35, 4));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 35, 1));


                        }

                    }
                }
            }
        }.runTaskTimer(HardcoreGames.getInstance(), 0L, 20L);
        game.taskID.add(task);
    }



    public boolean holdingInfectItem (Player p) {
        if (isInfectItem(p.getInventory().getItemInMainHand())) {
            return true;
        }
        return false;
    }

    public boolean isInfectItem (ItemStack i) {
        if (i.getItemMeta() != null && i.getType() == Material.GREEN_DYE && ChatColor.stripColor(i.getItemMeta().getDisplayName()).equals("Infect Syringe")) {
            return true;
        }
        return false;
    }

    public boolean isInfected (Player p) {
        if (infected.contains(p.getUniqueId())) {
            return true;
        } else {
            return false;
        }
    }

    public void infect (Player p) {
        infected.add(p.getUniqueId());
        spawnInfectParticles(p.getLocation());
    }
}
