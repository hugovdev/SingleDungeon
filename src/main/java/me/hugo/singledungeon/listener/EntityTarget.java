package me.hugo.singledungeon.listener;

import me.hugo.singledungeon.SingleDungeon;
import me.hugo.singledungeon.game.Game;
import me.hugo.singledungeon.player.DungeonPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class EntityTarget implements Listener {

    private final SingleDungeon main;

    public EntityTarget(SingleDungeon main) {
        this.main = main;
    }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        Entity monster = event.getEntity();

        if (!monster.hasMetadata("dungeon")) return;

        Game game = main.getMobRegistry().getFromMob(monster);
        DungeonPlayer gameOwner = game.getPlayer();

        if (event.getTarget() == gameOwner.getPlayer()) return;

        event.setCancelled(true);
        event.setTarget(gameOwner.getPlayer());
    }

}
