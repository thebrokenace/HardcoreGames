package Main;

import Kits.KitTools.Kits;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStats {

    Map<UUID, Integer> totalWins = new HashMap<>();
    Map<UUID, Integer> totalKills = new HashMap<>();
    Map<UUID, Integer> gamesPlayed = new HashMap<>();
    Map<UUID, Map<Kits, Integer>> mostFrequentKit = new HashMap<>();

    int currentGame = 0;

    private FileConfiguration customConfig;
    private File configFile;

    private PlayerStats() { } // make your constructor private, so the only war
    // to access "application" is through singleton pattern

    private static PlayerStats stats;


    public static PlayerStats getStats()
    {
        if (stats == null)
            stats = new PlayerStats();
        return stats;
    }


    public void saveStats () {
        //save stats from map to YAML
        if (this.customConfig == null || this.configFile == null) {
            return;
        }

        try {
            this.getCustomConfig().save(this.configFile);
        } catch (IOException e) {

        }



    }

    public void saveDefaultConfig () {
        if (this.configFile == null) {
            this.configFile = new File(HardcoreGames.getInstance().getDataFolder(), "stats.yml");
        }
        if (!this.configFile.exists()) {
            HardcoreGames.getInstance().saveResource("stats.yml", false);
        }

     }
    public void reloadStats () {
        if (this.configFile == null) {
            this.configFile = new File(HardcoreGames.getInstance().getDataFolder(), "stats.yml");
        }
        this.customConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = HardcoreGames.getInstance().getResource("stats.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.customConfig.setDefaults(defaultConfig);

        }
    }

    public FileConfiguration getCustomConfig() {
        if (this.customConfig == null ) {
            reloadStats();
        }
        return this.customConfig;
    }

    public void loadStats () {
        //load stats here into maps

        assignMapValues("Stats.Kills", totalKills);


       assignMapValues("Stats.GamesPlayed", gamesPlayed);

       assignMapValues("Stats.Wins", totalWins);


       assignMapValuesFreq("Stats.FrequentlyUsed");


        assignCurrentGame("Stats.CurrentGame");

        saveDefaultConfig();

    }
    public void assignCurrentGame (String path) {
        int current = 0;
        if (getCustomConfig().contains(path)) {
            current = getCustomConfig().getInt(path);
        } else {
            getCustomConfig().createSection(path);

        }
        currentGame = current;

    }

    public void assignMapValues (String path, Map<UUID,Integer> map) {
        Map<String, Object> converted = new HashMap<>();
        if (getCustomConfig().contains(path)) {
            converted = getCustomConfig().getConfigurationSection(path).getValues(false);
           // Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Found map");
        } else {
            getCustomConfig().createSection(path, converted);

        }

        for (String s : converted.keySet()) {
            UUID uuid = UUID.fromString(s);
            Integer num = (Integer) converted.get(s);
            map.put(uuid, num);
        }
    }
    public void assignMapValuesFreq (String path) {
        Map<String, Object> converted = new HashMap<>();
        if (getCustomConfig().contains(path)) {

            //Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + converted.toString());
            //Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "^^ this is converted map");

            ConfigurationSection conf = getCustomConfig().getConfigurationSection(path);

            mostFrequentKit = getexpandedMapIntoConfig(conf); //value of main map, contains our kits and integer
            //Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + mostFrequentKit.toString());


//            for (String s : (getCustomConfig().getConfigurationSection(path).getKeys(false))) {
//              Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + s);
//               Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "path is " + path);
//
//                //map objecthere
//
//
////                if (test != null) {
////                    Bukkit.getConsoleSender().sendMessage(test.toString());
////                } else {
////                    Bukkit.getConsoleSender().sendMessage("test is null");
////
////                }
//                //test.put(Kits.valueOf())
//              //Bukkit.getConsoleSender().sendMessage(path + s);
//              //Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "" + getCustomConfig().getConfigurationSection(path).get(s));
//            }
//            //converted = (Map<String, Object>) getCustomConfig().getConfigurationSection(path).getValues(false);
//
//
//            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Found map");
        } else {
            getCustomConfig().createSection(path, converted);

        }

    }

    @SuppressWarnings("unchecked")
    public static Map<UUID, Map<Kits, Integer>> getexpandedMapIntoConfig(ConfigurationSection conf) {
        Map<UUID, Map<Kits, Integer>> temp = new HashMap<>();
            for (Map.Entry<String, Object> e : conf.getValues(false).entrySet()) {
                    ConfigurationSection keys = conf.getConfigurationSection(e.getKey());
                   // Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "conf is null");
                    //Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + keys.getValues(false).toString());

                    //Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + conf.get(e.getKey()).toString() + "this");
                        //Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "instance of map");
                        Map<String, Object> unFormattedMap = keys.getValues(false);
                        Map<Kits, Integer> kitsIntegerMap = new HashMap<>();
                        //kit int map
                        for (String s : unFormattedMap.keySet()) {
                            kitsIntegerMap.put(Kits.valueOf(s), (int) unFormattedMap.get(s));
                        }

                        temp.put(UUID.fromString(e.getKey()), kitsIntegerMap);
                           // Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + temp.toString() + " is temp");




            }
        return temp;
    }

    @SuppressWarnings("unchecked")
    public static void expandMapIntoConfig(ConfigurationSection conf, Map<String, Object> map) {
        for (Map.Entry<String, Object> e : map.entrySet()) {
            if (e.getValue() instanceof Map<?,?>) {
                ConfigurationSection section = conf.createSection(e.getKey());


                Map<Kits,Integer> unFormattedMap = (Map<Kits, Integer>) e.getValue();
                //convert this into a nice string - object
                Map<String, Object> subMap = new HashMap<>();

                for (Kits kits : unFormattedMap.keySet()) {
                    subMap.put(kits.toString(), unFormattedMap.getOrDefault(kits, 0));
                }

                expandMapIntoConfig(section, subMap);
            } else {
                conf.set(e.getKey(), e.getValue());
            }
        }
    }

    public void saveCurrentGame (String path, int i) {
        if (getCustomConfig().contains(path)) {

            getCustomConfig().set(path, currentGame);

        } else {

            getCustomConfig().createSection(path);

        }
    }

    public void saveFrequentMapToYAML (String path, Map<UUID, Map<Kits, Integer>> map) {
        Map<String, Object> converted = new HashMap<>();
        Map<Kits,Integer> defaultMap = new HashMap<>();
        for (UUID uuid : map.keySet()) {
            converted.put(uuid.toString(), map.getOrDefault(uuid, defaultMap));
        }

        if (getCustomConfig().contains(path)) {
            expandMapIntoConfig(getCustomConfig().getConfigurationSection(path), converted);
            //getCustomConfig().set(path, converted);
        } else {
            getCustomConfig().createSection(path, converted);
        }
    }
    public void saveMapToYAML (String path, Map<UUID,Integer> map) {
        Map<String, Object> converted = new HashMap<>();
        for (UUID uuid : map.keySet()) {
            converted.put(uuid.toString(), map.getOrDefault(uuid, 0));
        }
        if (getCustomConfig().contains(path)) {
            getCustomConfig().set(path, converted);
        } else {
            getCustomConfig().createSection(path, converted);
        }
    }

    public void addKitToFrequent (Player p, Kits kit) {
        Map<Kits, Integer> map = new HashMap<Kits, Integer>();

        map = mostFrequentKit.getOrDefault(p.getUniqueId(), map);

        map.put(kit, map.getOrDefault(kit, 0) + 1);

        mostFrequentKit.put(p.getUniqueId(), map);
        saveFrequentMapToYAML("Stats.FrequentlyUsed", mostFrequentKit);
        saveStats();
    }

    public Kits getMostFrequentlyUsedKit (OfflinePlayer p) {
        Map<Kits, Integer> map = new HashMap<Kits, Integer>();

        map = mostFrequentKit.getOrDefault(p.getUniqueId(), map);

        Map.Entry<Kits, Integer> maxEntry = null;

        for (Map.Entry<Kits, Integer> entry : map.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) >= 0)
            {
                maxEntry = entry;
            }
        }
        //maximum kit
        if (maxEntry != null) {
            return maxEntry.getKey();
        } else {
            return Kits.NONE;
        }

    }
    public Kits getMostFrequentlyUsedKit (Player p) {
        Map<Kits, Integer> map = new HashMap<Kits, Integer>();

        map = mostFrequentKit.getOrDefault(p.getUniqueId(), map);

        Map.Entry<Kits, Integer> maxEntry = null;

        for (Map.Entry<Kits, Integer> entry : map.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) >= 0)
            {
                maxEntry = entry;
            }
        }
        //maximum kit
        if (maxEntry != null) {
            return maxEntry.getKey();
        } else {
            return Kits.NONE;
        }

    }







    public int getTotalGamesPlayed (Player p) {
        return gamesPlayed.getOrDefault(p.getUniqueId(),0);
    }
    public int getTotalGamesPlayed (OfflinePlayer p) {
        return gamesPlayed.getOrDefault(p.getUniqueId(),0);
    }
    public void addGame (Player p) {
        gamesPlayed.put(p.getUniqueId(), gamesPlayed.getOrDefault(p.getUniqueId(), 0) + 1);


        saveMapToYAML("Stats.GamesPlayed", gamesPlayed);


        saveStats();
    }

    public int getTotalKills (Player p) {
        return totalKills.getOrDefault(p.getUniqueId(),0);
    }
    public int getTotalKills (OfflinePlayer p) {
        return totalKills.getOrDefault(p.getUniqueId(),0);
    }
    public void addKill (Player p) {
        totalKills.put(p.getUniqueId(), totalKills.getOrDefault(p.getUniqueId(), 0) + 1);


//        getCustomConfig().set("Kills", totalKills.getOrDefault(p.getUniqueId(), 0));
//        getCustomConfig().set("Kills", totalKills);

        saveMapToYAML("Stats.Kills", totalKills);



        saveStats();
    }

    public int getTotalWins (Player p) {
        return totalWins.getOrDefault(p.getUniqueId(), 0);
    }
    public int getTotalWins (OfflinePlayer p) {
        return totalWins.getOrDefault(p.getUniqueId(), 0);
    }
    public void addWinForPlayer (Player p) {
        totalWins.put(p.getUniqueId(), totalWins.getOrDefault(p.getUniqueId(), 0) + 1);

        saveMapToYAML("Stats.Wins", totalWins);


        saveStats();
    }


    public int getCurrentGame () {
        return currentGame;
    }

    public void addCurrentGame () {
        currentGame++;

        saveCurrentGame("Stats.CurrentGame", currentGame);

        saveStats();
    }








}
