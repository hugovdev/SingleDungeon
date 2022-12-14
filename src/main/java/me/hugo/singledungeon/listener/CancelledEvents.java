package me.hugo.singledungeon.listener;

import me.hugo.singledungeon.SingleDungeon;
import me.hugo.singledungeon.menu.StatsMenu;
import me.hugo.singledungeon.player.DungeonPlayer;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class CancelledEvents implements Listener {

    private final SingleDungeon main;

    public CancelledEvents(SingleDungeon main) {
        this.main = main;
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        DungeonPlayer dungeonPlayer = main.getPlayerRegistry().get(event.getPlayer());

        if (dungeonPlayer.getCurrentGame() != null) event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        DungeonPlayer dungeonPlayer = main.getPlayerRegistry().get(event.getPlayer());

        if (dungeonPlayer.getCurrentGame() != null) event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        DungeonPlayer dungeonPlayer = main.getPlayerRegistry().get(event.getPlayer());

        if (dungeonPlayer.getCurrentGame() != null) event.setCancelled(true);
    }

}
