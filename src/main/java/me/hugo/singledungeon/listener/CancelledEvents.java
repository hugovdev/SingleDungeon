package me.hugo.singledungeon.listener;

import me.hugo.singledungeon.SingleDungeon;
import me.hugo.singledungeon.player.DungeonPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

}
