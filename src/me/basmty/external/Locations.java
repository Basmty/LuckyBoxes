package me.basmty.external;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Locations {
    private static File file;

    private static YamlConfiguration customfile;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("LuckyBoxes").getDataFolder(), "locations.yml");
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException iOException) {
            }
        customfile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return customfile;
    }

    public static void save() {
        try {
            customfile.save(file);
        } catch (IOException e) {
            System.out.println("Couldn't save file");
        }
    }

    public static void reload() {
        customfile = YamlConfiguration.loadConfiguration(file);
    }
}