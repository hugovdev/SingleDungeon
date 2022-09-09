package me.hugo.singledungeon.menu;

import me.hugo.singledungeon.player.DungeonPlayer;
import me.hugo.singledungeon.stats.PlayerStat;
import me.hugo.singledungeon.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StatsMenu implements InventoryHolder {

    private final Inventory inventory;

    private final ItemStack closeMenu = new ItemBuilder(Material.BARRIER).setName(Component.text("Close", NamedTextColor.RED)).toItemStack();

    public StatsMenu(DungeonPlayer player) {
        this.inventory = Bukkit.createInventory(this, 9 * 4, Component.text("Player Stats"));

        this.inventory.setItem(13, new ItemBuilder(Material.PAPER)
                .setName(Component.text("Your Dungeon Stats", NamedTextColor.GREEN))
                .setLore(
                        Component.text("Mob Kills: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text(player.getStatValue(PlayerStat.MOB_KILLS), NamedTextColor.WHITE)),
                        Component.text("Average Kills: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text(player.getStatValue(PlayerStat.AVERAGE_KILLS), NamedTextColor.WHITE)),
                        Component.text("Sessions: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).append(Component.text(player.getStatValue(PlayerStat.SESSIONS), NamedTextColor.WHITE)))
                .toItemStack());

        this.inventory.setItem(31, closeMenu);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public ItemStack getCloseMenuItem() {
        return closeMenu;
    }
}
