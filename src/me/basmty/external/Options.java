package me.basmty.external;


import me.basmty.luckyboxes.LuckyBoxes;
import org.bukkit.configuration.file.FileConfiguration;

public class Options {
    private final FileConfiguration config;
    public int setup;
    public boolean small;
    public boolean sound;
    public boolean message;
    public int delay;
    public String texturei;
    public String textureg;
    public String textured;

    public Options(LuckyBoxes luckyBoxes, FileConfiguration configuration) {
        config = configuration;
        if (config.isSet("setup")) {
            if (config.getInt("setup") == 1) {
                setDefaultOptions();
            }
            luckyBoxes.saveConfig();
        } else {
            config.addDefault("setup", 0);
            configuration.options().copyDefaults(true);
            luckyBoxes.saveDefaultConfig();
        }
    }

    public void getOptions() {
        small = config.getBoolean("small");
        sound = config.getBoolean("sound");
        message = config.getBoolean("message");
        delay = config.getInt("respawn-delay");
        texturei = config.getString("iron-texture");
        textureg = config.getString("gold-texture");
        textured = config.getString("diamond-texture");
    }

    void setDefaultOptions() {
        setup = 0;
        small = true;
        sound = true;
        message = true;
        delay = 7;
        texturei =
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzgyYWZhOGYyOTYwOGE5NWE0YmFlNTJmYjJkMTZjMjQzYWUzNTU4YjlmMjMxN2RmMDkwYjQyNjdjYjViNWYzOSJ9fX0=";
        textureg =
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDNkMDRkYmE1MWY4OTI0OTU4MzRmZjcxYTQyOWE4YTkxMDE1YTVhNzg2Yjg1NmZmZTljMDI0Y2RiNTJmYmM4ZiJ9fX0=";
        textured =
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM5Nzg0M2ZkM2E2ODUyOGEyODY3NDE2OWI5YjgzMWMxNjU4ZmIxZDg2YTcyMTgyNmRjZWQ1MDM0MmUzMjVmMiJ9fX0=";
    }

}
