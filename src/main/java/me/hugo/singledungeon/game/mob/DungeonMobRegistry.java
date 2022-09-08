package me.hugo.singledungeon.game.mob;

import me.hugo.singledungeon.game.Game;
import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public final class DungeonMobRegistry {

    private final Map<Entity, Game> mobOrigin = new HashMap<>();

    public void registerMob(Entity entity, Game game) {
        mobOrigin.put(entity, game);
    }

    public Game getFromMob(Entity entity) {
        return mobOrigin.get(entity);
    }

    public void removeMob(Entity entity) {
        mobOrigin.remove(entity);
    }

}
