package me.basmty.physical;

import me.basmty.luckyboxes.LuckyBoxes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.List;
import java.util.Timer;

public class MotionScheduler {
    LuckyBoxes main;
    List<ArmorStand> stands;
    Timer daemon;

    public MotionScheduler(LuckyBoxes plugin, List<ArmorStand> stands) {
        this.stands = stands;
        this.main = plugin;
        daemon = new Timer();
        startMotion();
    }

    private void startMotion() {
        for (ArmorStand stand : stands) {
            Location loc = stand.getLocation();
            if (loc != null) {
                motion(stand);
            }
        }
    }

    public void motion(ArmorStand stand) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
            Location o = stand.getLocation();
            o.setYaw(o.getYaw() + 60);
            stand.teleport(o);
        }, 10L, 10L);
    }
}
