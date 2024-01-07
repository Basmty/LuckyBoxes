package me.basmty.physical;

import me.basmty.luckyboxes.LuckyBoxes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static me.basmty.luckyboxes.Utils.Color;

public class BoxManager {
    LuckyBoxes main;
    ItemStack headi;
    ItemStack headii;
    ItemStack headiii;
    List<ArmorStand> stands;
    List<ArmorStand> previews;
    List<World> worlds;
    MotionScheduler motionScheduler;

    public PreviewMenus previewManager;

    public BoxManager(LuckyBoxes luckyBoxes) {
        main = luckyBoxes;
        stands = new ArrayList<>();
        worlds = Bukkit.getWorlds();
        previews = new ArrayList<>();
        previewManager = new PreviewMenus(main);
        loadHeads();
        Bukkit.getServer().getPluginManager().registerEvents(new Events(this), main);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, () -> {
            clearOldStands();
            createStands();
            loadPreviews();
        }, 40L);
    }

    public ItemStack getHead(int i) {
        ItemStack head;
        switch (i) {
            case 2:
                head = main.utils.getHeadFrom64(main.options.textureg);
                auditSkullName(head, "gold");
                break;
            case 3:
                head = main.utils.getHeadFrom64(main.options.textured);
                auditSkullName(head, "diamond");
                break;
            case 4:
                head = main.utils.getHeadFrom64(main.options.texturei);
                auditSkullName(head, "ironpreview");
                break;
            case 5:
                head = main.utils.getHeadFrom64(main.options.textureg);
                auditSkullName(head, "goldpreview");
                break;
            case 6:
                head = main.utils.getHeadFrom64(main.options.textured);
                auditSkullName(head, "diamondpreview");
                break;
            default:
                head = main.utils.getHeadFrom64(main.options.texturei);
                auditSkullName(head, "iron");
                break;
        }
        return head;
    }

    private void startMotion() {
        motionScheduler = new MotionScheduler(main, stands);
    }

    public void loadPreviews() {
        int s = main.listpreviews.size();
        if(s >= 1) {
            switch (s) {
                case 1:
                    createPreview(1, main.listpreviews.get(0));
                    break;
                case 2:
                    createPreview(1, main.listpreviews.get(0));
                    createPreview(2, main.listpreviews.get(1));
                    break;
                case 3:
                    createPreview(1, main.listpreviews.get(0));
                    createPreview(2, main.listpreviews.get(1));
                    createPreview(3, main.listpreviews.get(2));
                break;
            }
        }
    }

    public void createPreview(int i, Location location) {
        ItemStack stack;
        ArmorStand s = box(location);
        switch (i) {
            case 2:
                stack = getHead(5);
                s.setCustomName(Color("&6&lGold Tier"));
                break;
            case 3:
                stack = getHead(6);
                s.setCustomName(Color("&b&lDiamond Tier"));
                break;
            default:
                stack = getHead(4);
                s.setCustomName(Color("&f&lIron Tier"));
                break;
        }
        stack.setAmount(1);
        s.setHelmet(stack);

        s.setCustomNameVisible(true);
        previews.add(s);
        motionScheduler.motion(s);
    }

    private ItemStack auditSkullName(ItemStack item, String name) {
        ItemStack stack = item;
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public void loadHeads() {
        if (main != null) {
            headi = main.utils.getHeadFrom64(main.options.texturei);
            auditSkullName(headi, "iron");
            headii = main.utils.getHeadFrom64(main.options.textureg);
            auditSkullName(headii, "gold");
            headiii = main.utils.getHeadFrom64(main.options.textured);
            auditSkullName(headiii, "diamond");
        }
    }

    public void clearOldStands() {
        List<Entity> filter = new ArrayList<>();
        if (!main.listlocations.isEmpty() || !main.listpreviews.isEmpty()) {
            for (World world : worlds) {
                List<Entity> entities = world.getEntities();
                for (Entity entity : entities) {
                    if (entity.getType().equals(EntityType.ARMOR_STAND)) {
                        filter.add(entity);
                    }
                }
            }
        }
        if (!filter.isEmpty()) {
            for (Entity e : filter) {
                ArmorStand s = (ArmorStand) e;
                s.setHelmet(new ItemStack(Material.AIR));
                e.remove();
                e.getWorld().getEntities().remove(e);
            }
        }
    }

    public void createStands() {
        if (!main.getListlocations().isEmpty()) {
            for (Location loc : main.getListlocations()) {
                if (loc.getWorld() != null) {
                    ArmorStand stand = box(loc);
                    stands.add(stand);
                } else {
                    Bukkit.getServer().getConsoleSender().sendMessage(Color("&cERROR: &eInvalid Location detected!"));
                }
            }
            main.DumpToConsole("&6&lLuckyBoxes &f: &a&2Boxes Initiated!");
            startMotion();
        }
    }

    private ArmorStand box(Location location) {
        World w = location.getWorld();
        ArmorStand stand = (ArmorStand) w.spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setHelmet(headi);
        if(main.options.small) {
            stand.setSmall(true);
        }
        return stand;
    }
}
