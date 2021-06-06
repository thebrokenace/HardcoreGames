package Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;

public final class WorldUtils {

    private WorldUtils() {
    }

    public static void deleteDirectory(File directory) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + directory.getPath());
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null)
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        if (file.delete()) {
                            Bukkit.getConsoleSender().sendMessage (ChatColor.RED + "Attempting to delete World!");
                        } else {
                            Bukkit.getConsoleSender().sendMessage("FAILED!");
                        }
                        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Deleting World...");
                    }
                }
        }
    }
}