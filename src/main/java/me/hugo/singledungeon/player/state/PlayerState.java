package me.hugo.singledungeon.player.state;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.Map;

public final class PlayerState {

    private final Location location;

    private final double maxHealth;
    private final double health;
    private final int foodLevel;

    private final GameMode gameMode;

    private final int expLevel;
    private final float expProgress;

    private final Map<Integer, ItemStack> inventoryMap = new HashMap<>();

    public PlayerState(Player player) {
        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        this.maxHealth = maxHealth == null ? 20.0 : maxHealth.getBaseValue();
        this.health = player.getHealth();
        this.foodLevel = player.getFoodLevel();
        this.location = player.getLocation();
        this.gameMode = player.getGameMode();

        this.expLevel = player.getLevel();
        this.expProgress = player.getExp();

        PlayerInventory inventory = player.getInventory();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack currentItem = inventory.getItem(i);

            if (currentItem != null && currentItem.getType() != Material.AIR) {
                this.inventoryMap.put(i, currentItem);
            }
        }
    }

    public void restore(Player player) {
        player.teleport(this.location);
        player.setGameMode(this.gameMode);

        for (PotionEffect potionType : player.getActivePotionEffects()) {
            player.removePotionEffect(potionType.getType());
        }

        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth != null) maxHealth.setBaseValue(this.maxHealth);

        player.setHealth(this.health);
        player.setFoodLevel(this.foodLevel);

        player.setLevel(this.expLevel);
        player.setExp(this.expProgress);

        player.setFireTicks(0);

        PlayerInventory inventory = player.getInventory();

        inventory.setArmorContents(null);
        inventory.clear();

        for (Integer slot : this.inventoryMap.keySet()) {
            inventory.setItem(slot, this.inventoryMap.get(slot));
        }
    }

}
