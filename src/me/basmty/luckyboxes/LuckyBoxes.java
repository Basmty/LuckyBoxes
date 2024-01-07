package me.basmty.luckyboxes;

import me.basmty.commands.Boxes;
import me.basmty.external.Locations;
import me.basmty.external.Options;
import me.basmty.external.Rewards;
import me.basmty.physical.BoxManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.basmty.luckyboxes.Utils.Color;

public final class LuckyBoxes extends JavaPlugin {

    // Classes
    public Options options;
    public BoxManager boxManager;

    // Lists
    public List<Location> listlocations = new ArrayList<>();
    public List<Location> listpreviews = new ArrayList<>();

    public List<ItemStack> listIronRewards = new ArrayList<>();
    public List<ItemStack> listGoldRewards = new ArrayList<>();
    public List<ItemStack> listDiamondRewards = new ArrayList<>();

    public Utils utils = new Utils();

    public List<Location> getListlocations() {
        return listlocations;
    }

    public List<ItemStack> getListrewards(int i) {
        return switch (i) {
            case 2 -> listGoldRewards;
            case 3 -> listDiamondRewards;
            default -> listIronRewards;
        };
    }

    public void loadLocations() {
        ConfigurationSection section = Locations.get().getConfigurationSection("locations");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                Location l = (Location) section.get(key);
                listlocations.add(l);
            }
            if(!listlocations.isEmpty()) {
                DumpToConsole("&6&lLuckyBoxes &f: &aLocations Loaded!");
            } else {
                DumpToConsole("&6&lLuckyBoxes &f: &cNo locations found!");
            }
        } else {
            Locations.get().createSection("locations");
            Locations.save();
        }

        ConfigurationSection section1 = Locations.get().getConfigurationSection("previews");
        if (section1 != null) {
            for (String key : section1.getKeys(false)) {
                Location l = (Location) section1.get(key);
                listpreviews.add(l);
            }
            if(!listpreviews.isEmpty()) {
                DumpToConsole("&6&lLuckyBoxes &f: &bPreviews Loaded!");
            }
        } else {
            Locations.get().createSection("previews");
            Locations.save();
        }
    }

    public void loadRewards() {
        ConfigurationSection section = Rewards.get().getConfigurationSection("rewards");
        if (section != null) {
            if (section.getKeys(false).isEmpty()) {
                Objects.requireNonNull(Rewards.get().getConfigurationSection("rewards")).createSection("iron");
                Objects.requireNonNull(Rewards.get().getConfigurationSection("rewards")).createSection("gold");
                Objects.requireNonNull(Rewards.get().getConfigurationSection("rewards")).createSection("diamond");
                Rewards.save();
            }
            for (String key : section.getKeys(false)) {
                String rewardTier = key;
                if (rewardTier.contains("iron") || rewardTier.contains("gold") || rewardTier.contains("diamond")) {
                    ConfigurationSection subsection = Rewards.get().getConfigurationSection("rewards." + rewardTier);
                    for (String key1 : subsection.getKeys(false)) {
                        ItemStack r = (ItemStack) subsection.get(key1);
                        if (rewardTier.contains("iron")) {
                            listIronRewards.add(r);
                        }
                        if (rewardTier.contains("gold")) {
                            listGoldRewards.add(r);
                        }
                        if (rewardTier.contains("diamond")) {
                            listDiamondRewards.add(r);
                        }
                    }
                } else {
                    if (!rewardTier.contains("iron")) {
                        Objects.requireNonNull(Rewards.get().getConfigurationSection("rewards")).createSection("iron");
                    } else if (!rewardTier.contains("gold")) {
                        Objects.requireNonNull(Rewards.get().getConfigurationSection("rewards")).createSection("gold");
                    } else if (!rewardTier.contains("diamond")) {
                        Objects.requireNonNull(Rewards.get().getConfigurationSection("rewards")).createSection("diamond");
                    }
                    Rewards.save();
                }
            }
            DumpToConsole("&6&lLuckyBoxes &f: &aRewards Loaded!");
        } else {
            Rewards.get().createSection("rewards");
            Rewards.save();

            section = Rewards.get().getConfigurationSection("rewards");
            if (section != null) {
                Objects.requireNonNull(Rewards.get().getConfigurationSection("rewards")).createSection("iron");
                Objects.requireNonNull(Rewards.get().getConfigurationSection("rewards")).createSection("gold");
                Objects.requireNonNull(Rewards.get().getConfigurationSection("rewards")).createSection("diamond");
                Rewards.save();
            }

        }
    }

    public void loadFiles() {
        Locations.setup();
        Locations.get().options().copyDefaults();
        Locations.save();
        loadLocations();

        Rewards.setup();
        Rewards.get().options().copyDefaults();
        Rewards.save();
        loadRewards();
    }

    public void loadCommands() {
        Boxes boxes = new Boxes("boxes");
        getCommand("boxes").setExecutor(boxes);
    }

    public void DumpToConsole(String s) {
        Bukkit.getServer().getConsoleSender().sendMessage(Color(s));
    }

    public void loadBoxManager() {
        boxManager = new BoxManager(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        DumpToConsole("&6&lLuckyBoxes &f: &eStarting..");
        options = null;
        options = new Options(this, getConfig());
        loadFiles();
        options.getOptions();
        loadCommands();
        loadBoxManager();
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            DumpToConsole("&6&lLuckyBoxes &f: &aEnabled!");
        }, 60L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DumpToConsole("&6&lLuckyBoxes &f: &cDisabled.");
    }

}
