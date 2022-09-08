package me.hugo.singledungeon.player;

import me.hugo.singledungeon.game.Game;
import me.hugo.singledungeon.player.state.PlayerState;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class DungeonPlayer {

    private Player player;
    private final UUID playerUuid;
    private PlayerState lastState;

    private Game currentGame;

    public DungeonPlayer(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }

    public void createState() {
        this.lastState = new PlayerState(player);
    }

    public PlayerState getLastState() {
        return lastState;
    }

    public Player getPlayer() {
        return player;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public DungeonPlayer setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
        return this;
    }

    public DungeonPlayer setPlayer(Player player) {
        this.player = player;
        return this;
    }
}
