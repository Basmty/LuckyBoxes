package me.basmty.luckyboxes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class Utils {

    public static String Color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void Dump(String s) {
        Bukkit.getServer().getConsoleSender().sendMessage(Color(s));
    }

    public ItemStack getHeadFrom64(String value) {
        ItemStack skull;
        skull = SkullMaker.makeSkull(value);
        return skull;
    }

    public static String getNMSversion() {
        String v = Bukkit.getBukkitVersion();
        int i = v.indexOf('-');
        v = v.substring(0, i);
        if (v.contains("1.8") || v.contains("1.9") || v.contains("1.10")
                || v.contains("1.11") || v.contains("1.12")) {
            return "legacy";
        } else {
            return "1.13+";
        }
    }
}
