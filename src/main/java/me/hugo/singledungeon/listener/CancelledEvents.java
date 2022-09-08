package me.hugo.singledungeon.listener;

import me.hugo.singledungeon.SingleDungeon;
import me.hugo.singledungeon.game.Game;
import me.hugo.singledungeon.player.DungeonPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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
