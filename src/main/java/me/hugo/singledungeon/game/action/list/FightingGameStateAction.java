package me.hugo.singledungeon.game.action.list;

import me.hugo.singledungeon.SingleDungeon;
import me.hugo.singledungeon.game.Game;
import me.hugo.singledungeon.game.action.GameStateAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.concurrent.ThreadLocalRandom;

public final class FightingGameStateAction implements GameStateAction {

    private final int mobCap = 10;

    @Override
    public void gameTick(SingleDungeon main, Game game) {
        // Mob Cap
        if (game.getCurrentMobs().size() >= mobCap) return;

        for (Location mobSpawn : main.getMobSpawnLocations()) {
            // If after spawning a mob (or not) the cap is met, we return.
            if (game.getCurrentMobs().size() >= mobCap) return;

            // Some randomness per spawnpoint for a mob to spawn there or not
            if (ThreadLocalRandom.current().nextInt(0, 12) != 0) continue;

            spawnMob(main, game, mobSpawn);
        }
    }

    public void spawnMob(SingleDungeon main, Game game, Location location) {
        Player bukkitPlayer = game.getPlayer().getPlayer();

        Creature entity = (Creature) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        entity.setSilent(true);
        entity.setCustomNameVisible(true);
        entity.setCollidable(false);
        entity.customName(Component.text("Zombie", NamedTextColor.AQUA));

        entity.setTarget(bukkitPlayer);

        entity.setMetadata("dungeon", new FixedMetadataValue(main, "dungeon"));
        main.getMobRegistry().registerMob(entity, game);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player == bukkitPlayer) continue;

            player.hideEntity(main, entity);
        }

        game.getCurrentMobs().add(entity);
    }

}
