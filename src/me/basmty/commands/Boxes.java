package me.basmty.commands;

import me.basmty.external.Locations;
import me.basmty.external.Rewards;
import me.basmty.luckyboxes.LuckyBoxes;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import static me.basmty.luckyboxes.Utils.Color;
import static me.basmty.luckyboxes.Utils.getNMSversion;

public class Boxes extends Command implements CommandExecutor {

    LuckyBoxes main = JavaPlugin.getPlugin(LuckyBoxes.class);

    public Boxes(String name) {
        super(name);
    }

    @Override
    @SuppressWarnings("deprecated")
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            System.out.println("This can be only executed by a player");
        } else {
            if (args.length == 0) {

                player.sendMessage(Color("&eNo arguments provided!"));
            } else {
                switch (args.length) {
                    case 1:
                        if (args[0].equalsIgnoreCase("add-loc")) {

                            Location loc = player.getLocation();
                            loc.setX(loc.getBlockX() + 0.5D);
                            loc.setZ(loc.getBlockZ() + 0.5D);
                            int size = main.getListlocations().size() + 1;
                            Locations.get().set("locations." + size, loc);
                            Locations.save();
                            main.listlocations.add(loc);
                            player.sendMessage(Color("Location added"));
                        }
                        break;
                    case 2:
                        if (args[0].equalsIgnoreCase("add-reward")) {
                            if (args[1].equalsIgnoreCase("iron") || args[1].equalsIgnoreCase("gold") ||
                                    args[1].equalsIgnoreCase("diamond")) {
                                ItemStack item;
                                if (getNMSversion().equalsIgnoreCase("1.13+")) {
                                    int i = player.getInventory().getHeldItemSlot();
                                    assert player.getInventory().getItem(i) != null;
                                    item = player.getInventory().getItem(i);
                                } else {
                                    assert player.getItemInHand() != null;
                                    item = player.getItemInHand();
                                }
                                if (!item.equals(Material.AIR)) {
                                    ConfigurationSection section = Locations.get().getConfigurationSection("rewards." + args[1]);
                                    assert section != null;
                                    int size = 0;
                                    if (args[1].contains("iron")) {
                                        size = main.getListrewards(1).size() + 1;
                                        main.getListrewards(1).add(item);
                                        main.boxManager.previewManager.updatePreviews(1);
                                    }
                                    if (args[1].contains("gold")) {
                                        size = main.getListrewards(2).size() + 1;
                                        main.getListrewards(2).add(item);
                                        main.boxManager.previewManager.updatePreviews(2);
                                    }
                                    if (args[1].contains("diamond")) {
                                        size = main.getListrewards(3).size() + 1;
                                        main.getListrewards(3).add(item);
                                        main.boxManager.previewManager.updatePreviews(3);
                                    }
                                    Rewards.get().set("rewards." + args[1] + "." + size, item);
                                    Rewards.save();
                                    player.sendMessage(Color("Item added"));
                                }
                            } else {
                                player.sendMessage(Color("&eInvalid box tier."));
                            }

                        }
                        if (args[0].equalsIgnoreCase("add-preview")) {
                            String arg1 = args[1].toLowerCase();
                            if (arg1.equals("iron") || arg1.equals("gold") || arg1.equals("diamond")) {
                                
                                Location loc = player.getLocation();
                                loc.setX(loc.getBlockX() + 0.5D);
                                loc.setZ(loc.getBlockZ() + 0.5D);

                                if (arg1.equalsIgnoreCase("iron")) {
                                    Locations.get().set("previews." + 1, loc);
                                    main.boxManager.createPreview(1, loc);
                                }
                                if (arg1.equalsIgnoreCase("gold")) {
                                    Locations.get().set("previews." + 2, loc);
                                    main.boxManager.createPreview(2, loc);
                                }
                                if (arg1.equalsIgnoreCase("diamond")) {
                                    Locations.get().set("previews." + 3, loc);
                                    main.boxManager.createPreview(3, loc);
                                }
                                Locations.save();
                                main.listpreviews.add(loc);
                                player.sendMessage(Color("&aPreview created!"));
                            } else {
                                player.sendMessage(Color("&eInvalid box tier."));
                            }

                        }
                        break;
                    default:
                        player.sendMessage(Color("&6LuckyBoxes &bv2.0"));
                        break;
                }
            }
        }
        return false;
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        return false;
    }
}