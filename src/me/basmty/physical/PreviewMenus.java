package me.basmty.physical;

import me.basmty.luckyboxes.LuckyBoxes;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static me.basmty.luckyboxes.Utils.Color;

public class PreviewMenus {
    LuckyBoxes main;
    public PreviewMenus(LuckyBoxes main) {
        this.main=main;
        updatePreviews(0);
    }
    Inventory ironinv;
    Inventory goldinv;
    Inventory diamondinv;

    int irons=0;
    int golds=0;
    int diamonds=0;

    public void openInv(Player player, int i) {
        switch (i) {
            case 2:
                if(main.listGoldRewards.isEmpty()) {
                    player.sendMessage(Color("&cPrize tier" + " &6Gold " + "&chas no items. Contact server admin."));
                } else {
                    player.openInventory(goldinv);
                }
                break;
            case 3:
                if(main.listDiamondRewards.isEmpty()) {
                    player.sendMessage(Color("&cPrize tier" + " &bDiamond " + "&chas no items. Contact server admin."));
                } else {
                    player.openInventory(diamondinv);
                }
                break;
            default:
                if(main.listIronRewards.isEmpty()) {
                    player.sendMessage(Color("&cPrize tier" + " &fIron " + "&chas no items. Contact server admin."));
                } else {
                    player.openInventory(ironinv);
                }
                break;
        }
    }

    public void updatePreviews(int i) {
        String name;
        switch (i) {
            case 1:
                name = Color("&f&lIron Tier Preview");
                ironinv = roundup(1, ironinv, irons, name, main.listIronRewards);
                break;
            case 2:
                name = Color("&6&lGold Tier Preview");
                goldinv = roundup(2, goldinv, golds, name, main.listGoldRewards);
                break;
            case 3:
                name = Color("&b&lDiamond Tier Preview");
                diamondinv = roundup(3, diamondinv, diamonds, name, main.listDiamondRewards);
                break;
            case 0:
                name = Color("&f&lIron Tier Preview");
                ironinv = roundup(1, ironinv, irons, name, main.listIronRewards);
                name = Color("&6&lGold Tier Preview");
                goldinv = roundup(2, goldinv, golds, name, main.listGoldRewards);
                name = Color("&b&lDiamond Tier Preview");
                diamondinv = roundup(3, diamondinv, diamonds, name, main.listDiamondRewards);
                break;
        }
    }
    private Inventory roundup(int i,Inventory inv, int counter, String title, List<ItemStack> list) {
        int size;
        if (list.size() >= 9) {
            size = list.size() / 9;
        } else {
            size = 1;
            if (list.isEmpty()) {
                size = 0;
            }
        }

        if (counter == 0) {
            inv = Bukkit.createInventory(null, size * 9, title);
            for (int j = 0; j < list.size(); j++) {
                inv.setItem(j, list.get(j));
            }
            counter = list.size() - 1;
        } else {
            if (counter <= inv.getSize() && counter < list.size()) {
                for (int j = counter + 1; j < list.size(); j++) {
                    inv.setItem(j, list.get(j));
                }
            } else {
                if (counter > inv.getSize()) {
                    inv = Bukkit.createInventory(null, size * 9, title);
                    for (int j = 0; j < list.size(); j++) {
                        inv.setItem(j, list.get(j));
                        counter = list.size();
                    }
                }
            }
            counter = list.size();
        }

        switch (i){
            case 1 -> irons = counter;
            case 2 -> golds = counter;
            case 3 -> diamonds = counter;
        }

        return inv;
    }

}
