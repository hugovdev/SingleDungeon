package me.hugo.singledungeon.listener;

import me.hugo.singledungeon.menu.StatsMenu;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClick implements Listener {

    @EventHandler
    public void onStatsMenuClick(InventoryClickEvent event) {
        HumanEntity player = event.getWhoClicked();

        if (player.getOpenInventory().getTopInventory().getHolder() instanceof StatsMenu menu) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null) return;
            if (event.getCurrentItem().isSimilar(menu.getCloseMenuItem())) {
                player.closeInventory();
                ((Player) player).playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
            }
        }
    }

}
