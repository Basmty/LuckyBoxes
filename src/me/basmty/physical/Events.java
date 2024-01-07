package me.basmty.physical;

import me.basmty.luckyboxes.LuckyBoxes;
import me.basmty.luckyboxes.Utils;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static me.basmty.luckyboxes.Utils.Color;
import static me.basmty.luckyboxes.Utils.getNMSversion;

public class Events implements Listener {

    BoxManager manager;
    LuckyBoxes main;
    HashSet<Location> processing;
    HashMap<Player, List<Location>> cooldown;
    HashSet<Player> playerLock;

    public Events(BoxManager manager) {
        this.manager = manager;
        main = LuckyBoxes.getPlugin(LuckyBoxes.class);
        cooldown = new HashMap<>();
        playerLock = new HashSet<>();
        processing = new HashSet<>();
    }

    @EventHandler
    public void invClick(InventoryClickEvent event) {
        if(event.getClickedInventory() !=null) {
            String title = event.getClickedInventory().getTitle();
            if(title.equalsIgnoreCase(Color("&f&lIron Tier Preview")) ||
                    title.equalsIgnoreCase(Color("&6&lGold Tier Preview")) ||
                    title.equalsIgnoreCase(Color("&b&lDiamond Tier Preview"))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) throws NullPointerException {
        if (event.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) {
            event.setCancelled(true);
            Entity e = event.getRightClicked();
            ArmorStand armorStand = (ArmorStand) e;
            Location location = armorStand.getLocation();
            Player player = event.getPlayer();
            String name = armorStand.getCustomName();
            if(name !=null) {
                if(name.equalsIgnoreCase(Color("&f&lIron Tier")) || name.equalsIgnoreCase(Color("&6&lGold Tier"))
                || name.equalsIgnoreCase(Color("&b&lDiamond Tier")) ) {
                    event.setCancelled(true);
                    checkBoxName(armorStand.getHelmet(),armorStand, player);
                }
            } else
            if ((!processing.contains(location))) {
                processing.add(location);
                if (!cooldown.containsKey(player)) {
                    List<Location> locationsList = new ArrayList<>();
                    locationsList.add(location);
                    cooldown.put(player, locationsList);
                    startProcessing(armorStand, player);
                } else {
                    if (cooldown.get(player).isEmpty()) {
                        cooldown.get(player).add(location);
                        startProcessing(armorStand, player);
                    } else {
                        List<Location> locations = cooldown.get(player);
                        if (!locations.contains(location)) {
                            cooldown.get(player).add(location);
                            startProcessing(armorStand, player);
                        } else if (processing.contains(location)) {
                            player.sendMessage(Color("&bSlow down =) &ethis box will refill in &7" + main.options.delay + " &eseconds."));
                        }
                    }
                }
            }
        }
    }

    public ItemStack giveNewHead() {
        double r = Math.abs(Math.random()) * 10;
        int number = (int) r;

        switch (number) {
            case 0:
            case 1:
            case 2:
                return manager.getHead(2);
            case 3:
                return manager.getHead(3);
            default:
                return manager.getHead(1);
        }
    }


    private void checkBoxName(ItemStack helmet, ArmorStand stand, Player player) {
        String displayname = helmet.getItemMeta().getDisplayName();
        switch (displayname) {
            case "iron":
            case "gold":
            case "diamond":
                ItemStack temp = new ItemStack(Material.AIR);
                stand.setHelmet(temp);
                if(main.options.sound) {
                    if (Utils.getNMSversion().equals("1.13+")) {
                        player.playSound(stand.getLocation(), Sound.valueOf("ENTITY_EXPERIENCE_ORB_PICKUP"), 1, 1);
                    } else {
                        player.playSound(stand.getLocation(), Sound.valueOf("ORB_PICKUP"), 1, 1);
                    }
                }
                int i = 1;
                if (displayname.equalsIgnoreCase("gold")) {
                    i = 2;
                }
                if (displayname.equalsIgnoreCase("diamond")) {
                    i = 3;
                }
                getPrize(i, stand.getLocation(), player);
                break;
            case "ironpreview":
            case "goldpreview":
            case "diamondpreview":
                if(main.options.sound) {
                    if (Utils.getNMSversion().equals("1.13+")) {
                        player.playSound(stand.getLocation(), Sound.valueOf("BLOCK_CHEST_OPEN"), 1, 1);
                    } else {
                        player.playSound(stand.getLocation(), Sound.valueOf("CHEST_OPEN"), 1, 1);
                    }
                }
                int j = 1;
                if (displayname.equalsIgnoreCase("goldpreview")) {
                    j = 2;
                }
                if (displayname.equalsIgnoreCase("diamondpreview")) {
                    j = 3;
                }
                main.boxManager.previewManager.openInv(player,j);
                break;
        }
    }

    private void getPrize(int i, Location location, Player player) {
        int size = 0;
        new ItemStack(Material.STONE_SWORD);
        ItemStack stack;
        switch (i) {
            case 2:
                size = main.listGoldRewards.size();
                if (size > 0) {
                    int rand = (int) (Math.random() * size);
                    stack = main.listGoldRewards.get(rand);
                    dropIteminWorld(location, stack);
                    if (main.options.message) {
                        player.sendMessage(Color("&aEnjoy your prize :)"));
                    }
                } else {
                    player.sendMessage(Color("&cPrize tier" + " &6Gold " + "&chas no items. Contact server admin."));
                }
                break;
            case 3:
                size = main.listDiamondRewards.size();
                if (size > 0) {
                    int rand = (int) (Math.random() * size);
                    stack = main.listDiamondRewards.get(rand);
                    dropIteminWorld(location, stack);
                    if (main.options.message) {
                        player.sendMessage(Color("&aEnjoy your prize :)"));
                    }
                } else {
                    player.sendMessage(Color("&cPrize tier" + " &bDiamond " + "&chas no items. Contact server admin."));
                }
                break;
            default:
                size = main.listIronRewards.size();
                if (size > 0) {
                    int rand = (int) (Math.random() * size);
                    stack = main.listIronRewards.get(rand);
                    dropIteminWorld(location, stack);
                    if (main.options.message) {
                        player.sendMessage(Color("&aEnjoy your prize :)"));
                    }
                } else {
                    player.sendMessage(Color("&cPrize tier" + " &fIron " + "&chas no items. Contact server admin."));
                }
                break;
        }
    }

    private void dropIteminWorld(Location loc, ItemStack stack) {
        World world = Bukkit.getWorld(loc.getWorld().getName());
        assert world != null;
        world.dropItem(loc, stack);
    }

    private void startProcessing(ArmorStand stand, Player player) {
        if (!playerLock.contains(player)) {
            playerLock.add(player);
            headClickEventHandler(stand, player);
        } else {
            schedulePlayerUnlock(player);
        }
    }

    public void headClickEventHandler(ArmorStand stand, Player player) throws NullPointerException, IllegalArgumentException {
        Location standLocation = stand.getLocation();
        ItemStack helmet = stand.getHelmet();
        ItemStack nextHead = giveNewHead();
        Material material = helmet.getType();
        int isValidHeadType = 0;
        if (getNMSversion().equals("1.13+")) {
            if (material.equals(Material.valueOf("PLAYER_HEAD"))) {
                isValidHeadType = 1;
            }
        } else {
            if (material.equals(Material.valueOf("SKULL_ITEM")) || material.equals(Material.valueOf("SKULL"))) {
                isValidHeadType = 1;
            }
        }

        if (isValidHeadType == 1) {
            if (!helmet.getType().equals(Material.AIR)) {
                if (processing.contains(stand.getLocation())) {
                    if (!playerLock.contains(player)) {
                        playerLock.add(player);
                        scheduleRespawn(standLocation, stand, nextHead, player);
                        schedulePlayerUnlock(player);
                    } else {
                        if (playerLock.contains(player) && !cooldown.get(player).contains(standLocation)) {
                            playerLock.add(player);
                            cooldown.get(player).add(standLocation);
                            scheduleRespawn(standLocation, stand, nextHead, player);
                            schedulePlayerUnlock(player);
                        } else {
                            playerLock.add(player);
                            scheduleRespawn(standLocation, stand, nextHead, player);
                            schedulePlayerUnlock(player);
                        }
                    }
                    checkBoxName(helmet, stand, player);
                }
            }
        }
    }

    private void schedulePlayerUnlock(Player player) {
        long freezeThreshold = main.options.delay * 2L;
        if (main.options.delay >= 3) {
            freezeThreshold = main.options.delay * 4L;
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
            playerLock.remove(player);
            playerLock.remove(player);
        }, freezeThreshold);
    }

    private void scheduleRespawn(Location standLocation, ArmorStand stand, ItemStack nextHead, Player player) {
        //Dump(Color("&6Debug: &eRespawn scheduled! ") + main.options.delay);

        Runnable runnable = new TimerTask() {
            @Override
            public void run() {
                if (processing.contains(standLocation)) {
                    stand.setHelmet(nextHead);
                    List<Location> locations = cooldown.get(player);
                    locations.remove(standLocation);
                    cooldown.put(player, locations);
                    processing.remove(standLocation);
                }
            }
        };
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main,
                runnable, main.options.delay * 20L);
    }

}
