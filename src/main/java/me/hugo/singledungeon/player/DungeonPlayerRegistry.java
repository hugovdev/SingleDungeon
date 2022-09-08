package me.hugo.singledungeon.player;

import me.hugo.singledungeon.SingleDungeon;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class DungeonPlayerRegistry {

    private final SingleDungeon main;

    public DungeonPlayerRegistry(SingleDungeon main) {
        this.main = main;
    }

    private final Map<UUID, DungeonPlayer> playerStorage = new HashMap<>();

    public DungeonPlayer create(UUID playerUuid) {
        DungeonPlayer dungeonPlayer = new DungeonPlayer(main, playerUuid);

        playerStorage.put(playerUuid, dungeonPlayer);

        return dungeonPlayer;
    }

    public DungeonPlayer get(Player player) {
        return playerStorage.get(player.getUniqueId());
    }

    public void remove(Player player) {
        playerStorage.remove(player.getUniqueId());
    }

}
