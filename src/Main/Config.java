package Main;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    public void setConfig () {
        FileConfiguration config = HardcoreGames.getInstance().getConfig();
        config.addDefault("Minimum Players", 2);
        config.addDefault("Maximum Players", 8);
        config.addDefault("Pre-Game Timer", 20);
        config.addDefault("Grace Period Timer", 10);
        config.addDefault("Bot Count", 20);
        config.addDefault("Max Bot Interval", 200);
        config.addDefault("Min Bot Interval", 40);
        config.addDefault("Free kits", false);
        config.addDefault("Soup Enabled", true);
        config.addDefault("Bots Enabled", true);
        config.addDefault("Bots Count As Players", false);
        config.addDefault("Minimum Time Before Border Close", 4);
        config.addDefault("Maximum Time Before Border Close", 8);
        config.options().copyDefaults(true);
        HardcoreGames.getInstance().saveConfig();
    }

    public int getMinBorderTime () {
        FileConfiguration config = HardcoreGames.getInstance().getConfig();
        return config.getInt("Minimum Time Before Border Close");
    }

    public int getMaxBorderTime () {
        FileConfiguration config = HardcoreGames.getInstance().getConfig();
        return config.getInt("Maximum Time Before Border Close");
    }
    public void setBotsCountAsPlayers (Boolean bool) {
        FileConfiguration config = HardcoreGames.getInstance().getConfig();
        config.set("Bots Count As Players", bool);
        HardcoreGames.getInstance().saveConfig();
        HardcoreGames.getInstance().reloadConfig();
    }

    public boolean getBotsCountAsPlayers () {
        FileConfiguration config = HardcoreGames.getInstance().getConfig();
        return config.getBoolean("Bots Count As Players");
    }

    public void setBotsStatus (Boolean bool) {
        FileConfiguration config = HardcoreGames.getInstance().getConfig();
        config.set("Bots Enabled", bool);
        HardcoreGames.getInstance().saveConfig();
        HardcoreGames.getInstance().reloadConfig();
    }

    public boolean getBotsEnabled () {
        FileConfiguration config = HardcoreGames.getInstance().getConfig();
        return config.getBoolean("Bots Enabled");
    }

    public void setSoupStatus (Boolean bool) {
        FileConfiguration config = HardcoreGames.getInstance().getConfig();
        config.set("Soup Enabled", bool);
        HardcoreGames.getInstance().saveConfig();
        HardcoreGames.getInstance().reloadConfig();
    }
    public boolean getSoupStats () {
        FileConfiguration config = HardcoreGames.getInstance().getConfig();
        return config.getBoolean("Soup Enabled");
    }

    public void setFreeKits (Boolean bool) {
        FileConfiguration config = HardcoreGames.getInstance().getConfig();
        config.set("Free kits", bool);
        HardcoreGames.getInstance().saveConfig();
        HardcoreGames.getInstance().reloadConfig();
    }
    public boolean freeKits () {
        FileConfiguration config = HardcoreGames.getInstance().getConfig();
        return config.getBoolean("Free kits");
    }

    public int minIntervalBot () {
        FileConfiguration config = HardcoreGames.getInstance().getConfig();
        return config.getInt("Min Bot Interval");
    }
    public int maxIntervalBot () {
        FileConfiguration config = HardcoreGames.getInstance().getConfig();
        return config.getInt("Max Bot Interval");
    }

    public int botCount () {
        FileConfiguration config = HardcoreGames.getInstance().getConfig();
        return config.getInt("Bot Count");
    }

    public int minPlayers () {
        FileConfiguration config = HardcoreGames.getInstance().getConfig();
        return config.getInt("Minimum Players");
    }

    public int maxPlayers () {
        FileConfiguration config = HardcoreGames.getInstance().getConfig();
        return config.getInt("Maximum Players");
    }

    public int preGameTimer () {
        FileConfiguration config = HardcoreGames.getInstance().getConfig();
        return config.getInt("Pre-Game Timer");
    }

    public int graceTimer () {
        FileConfiguration config = HardcoreGames.getInstance().getConfig();
        return config.getInt("Grace Period Timer");
    }

    public void reload() {

        HardcoreGames.getInstance().reloadConfig();


    }


}
