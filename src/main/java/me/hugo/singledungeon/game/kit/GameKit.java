package me.hugo.singledungeon.game.kit;

import me.hugo.singledungeon.player.DungeonPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.List;

public enum GameKit {

    EASY("Easy Kit",
            new ItemStack(Material.DIAMOND_HELMET),
            new ItemStack(Material.IRON_CHESTPLATE),
            new ItemStack(Material.DIAMOND_LEGGINGS),
            new ItemStack(Material.IRON_BOOTS),
            Arrays.asList(
                    new GameKitItem(0, new ItemStack(Material.IRON_SWORD)),
                    new GameKitItem(1, new ItemStack(Material.GOLDEN_APPLE, 3)))),

    MEDIUM("Medium Kit",
            new ItemStack(Material.IRON_HELMET),
            new ItemStack(Material.IRON_CHESTPLATE),
            new ItemStack(Material.IRON_LEGGINGS),
            new ItemStack(Material.IRON_BOOTS),
            Arrays.asList(
                    new GameKitItem(0, new ItemStack(Material.STONE_SWORD)),
                    new GameKitItem(1, new ItemStack(Material.GOLDEN_APPLE, 2)))),

    DIFFICULT("Difficult Kit",
            new ItemStack(Material.LEATHER_HELMET),
            new ItemStack(Material.CHAINMAIL_CHESTPLATE),
            new ItemStack(Material.LEATHER_LEGGINGS),
            new ItemStack(Material.LEATHER_BOOTS),
            Arrays.asList(
                    new GameKitItem(0, new ItemStack(Material.STONE_SWORD)),
                    new GameKitItem(1, new ItemStack(Material.GOLDEN_APPLE, 1)))),
    ;

    private final String kitName;
    private final ItemStack helmet;
    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;

    private final List<GameKitItem> inventoryItems;

    GameKit(String kitName, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, List<GameKitItem> inventoryItems) {
        this.kitName = kitName;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;

        this.inventoryItems = inventoryItems;
    }

    public String getKitName() {
        return kitName;
    }

    public void give(DungeonPlayer player) {
        Player bukkitPlayer = player.getPlayer();

        PlayerInventory inventory = bukkitPlayer.getInventory();

        inventory.setHelmet(this.helmet);
        inventory.setChestplate(this.chestplate);
        inventory.setLeggings(this.leggings);
        inventory.setBoots(this.boots);

        for (GameKitItem kitItem : this.inventoryItems) {
            inventory.setItem(kitItem.getSlot(), kitItem.getItem());
        }
    }
}
