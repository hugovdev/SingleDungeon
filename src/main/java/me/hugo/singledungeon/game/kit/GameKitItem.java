package me.hugo.singledungeon.game.kit;

import org.bukkit.inventory.ItemStack;

public final class GameKitItem {

    private final int slot;
    private final ItemStack item;

    public GameKitItem(int slot, ItemStack item) {
        this.slot = slot;
        this.item = item;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }
}
