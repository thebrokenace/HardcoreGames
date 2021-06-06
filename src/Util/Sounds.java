package Util;

import Main.HardcoreGames;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Sounds {
    public static void playNinjaWoosh (Player location) {
        if (location.getWorld() != null)
        location.playSound(location.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 10, 1);
        location.playSound(location.getLocation(), Sound.ENTITY_IRON_GOLEM_ATTACK, 10, 1);


    }

    public static void playMenuClick (Player location) {
        if (location.getWorld() != null)
            location.playSound(location.getLocation(), Sound.UI_BUTTON_CLICK, 10, 1);

    }

//    public static void playMenuClick (Location location) {
//        if (location.getWorld() != null)
//            location.playSound(location.getLocation(), Sound.UI_BUTTON_CLICK, 10, 1);
//
//    }

    public static void testSounds (Player location, Sound sound) {
        location.playSound(location.getLocation(), sound, 10, 1);

    }
    public static void testSounds (Player location, Sound sound, int i, int j) {
        location.playSound(location.getLocation(), sound, i, j);

    }

    public static void nextPage (Player location) {
        playSound(location, Sound.ENTITY_IRON_GOLEM_ATTACK);
    }

    public static void achillesHitBad (Location loc) {
        playSound(loc, Sound.ENTITY_IRON_GOLEM_DAMAGE);
    }

    public static void achillesHitGood (Location loc) {
        playSound(loc, Sound.ENTITY_PLAYER_ATTACK_CRIT);
    }

    public static void turtleHitShift (Location loc) {
        playSound(loc, Sound.ENTITY_TURTLE_HURT);
    }

    public static void gameStartedSound () {
        for (Player p : Bukkit.getOnlinePlayers()) {
            playSound(p, Sound.ENTITY_ENDER_DRAGON_GROWL);

        }
    }

    public static void countDownEnoughPlayers () {
        for (Player p : Bukkit.getOnlinePlayers()) {
            playSound(p, Sound.BLOCK_NOTE_BLOCK_PLING);
        }
    }
    public static void lastCountDown () {
        for (Player p : Bukkit.getOnlinePlayers()) {
            playSound(p, Sound.ENTITY_GENERIC_EXPLODE);
            Bukkit.getServer().getScheduler().runTaskLater(HardcoreGames.getInstance(), () -> playSound(p, Sound.ENTITY_ENDERMAN_TELEPORT), 2);

        }
    }
    public static void countDownFailed () {
        for (Player p : Bukkit.getOnlinePlayers()) {
            playSound(p, Sound.BLOCK_DISPENSER_FAIL);
        }
    }

    public static void obfuscation (Player loc) {
        playSound(loc, Sound.ENTITY_BEE_LOOP_AGGRESSIVE);
    }

    public static void acrobatChangeBlock (Player loc) {
        playSound(loc, Sound.BLOCK_DISPENSER_FAIL);
    }

    public static void beserkerKill (Player loc) {
        playSound(loc, Sound.ENTITY_ENDER_DRAGON_GROWL);
    }

    public static void blazeStart (Location loc) {
        playSound(loc, Sound.ENTITY_GENERIC_EXTINGUISH_FIRE);
    }

    public static void boxerFist (Location loc) {
        playSound(loc, Sound.ENTITY_PLAYER_ATTACK_CRIT);
    }
    public static void burrowRoom (Location loc) {
        playSound(loc, Sound.ENTITY_MINECART_INSIDE);
    }
    public static void cannibalPunch (Location loc) {
        playSound(loc, Sound.ENTITY_GENERIC_EAT);
    }
    public static void wormEat (Player loc) {
        playSound(loc, Sound.ENTITY_GENERIC_EAT);
    }
    public static void checkPointTeleport (Location loc) {
        playSound(loc, Sound.ENTITY_SHULKER_TELEPORT);
    }
    public static void checkPointPlace (Player loc) {
        playSound(loc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
    }
    public static void copycatKitStole (Location loc) {
        playSound(loc, Sound.ENTITY_ITEM_PICKUP);
    }
    public static void cultivatorGrowTree (Location loc) {
        playSound(loc, Sound.BLOCK_CORAL_BLOCK_BREAK);
    }
    public static void demomanRig (Player loc) {
        playSound(loc, Sound.BLOCK_LEVER_CLICK);
    }
    public static void chargeDwarf (Location loc) {
        playSound(loc, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE);
    }
    public static void chargeReadyDwarf (Location loc) {playSound(loc, Sound.ENTITY_PLAYER_LEVELUP);}
    public static void shockwaveDwarf (Location loc) {
        playSound(loc, Sound.ENTITY_ENDER_DRAGON_SHOOT);
    }
    public static void mineVein (Player loc) {
        playSound(loc, Sound.ENTITY_GENERIC_EXTINGUISH_FIRE);
    }
    public static void summonHades (Location loc) {
        playSound(loc, Sound.BLOCK_BEACON_ACTIVATE);
    }
    public static void kayaTrap (Player loc) {
        playSound(loc, Sound.BLOCK_LEVER_CLICK);
    }
    public static void lumberBreakTree (Location loc) {
        playSound(loc, Sound.BLOCK_WOOD_BREAK);
    }
    public static void neoDeflect (Location loc) {
        playSound(loc, Sound.ITEM_SHIELD_BLOCK);
    }
    public static void phantomFly (Location loc) {
        playSound(loc, Sound.ITEM_ELYTRA_FLYING);
    }
    public static void pickpocket (Player loc) {
        playSound(loc, Sound.BLOCK_ANVIL_LAND);
    }
    public static void switcherSwitch (Player loc) {
        playSound(loc, Sound.ENTITY_GENERIC_SWIM);
    }
    public static void vampireHeal (Player loc) {
        playSound(loc, Sound.BLOCK_BEACON_POWER_SELECT);
    }
    public static void wolfNight (Player loc) {
        playSound(loc, Sound.ENTITY_WOLF_HOWL);
    }

    public static void launcherLauncher (Player loc) { playSound(loc, Sound.ENTITY_IRON_GOLEM_ATTACK);}

    public static void countDownPVP () {
        for (Player p : Bukkit.getOnlinePlayers()) {
            playSound(p, Sound.BLOCK_NOTE_BLOCK_PLING);
        }
    }

    public static void playSound (Player location, Sound sound) {
            location.playSound(location.getLocation(), sound, 10, 1);

    }
    public static void playSound (Location location, Sound sound) {
        if (location.getWorld() != null)
        location.getWorld().playSound(location, sound, 10, 1);

    }

}
