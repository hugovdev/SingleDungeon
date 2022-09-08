package me.hugo.singledungeon.game;

import me.hugo.singledungeon.SingleDungeon;
import me.hugo.singledungeon.game.kit.GameKit;
import me.hugo.singledungeon.player.DungeonPlayer;
import me.hugo.singledungeon.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class Game {

    private int currentCountdown = 5;

    private final SingleDungeon main;
    private final DungeonPlayer player;
    private final List<Entity> currentMobs = new ArrayList<>();
    private GameState state = GameState.STARTING;

    public Game(SingleDungeon main, DungeonPlayer player) {
        this.main = main;
        this.player = player;
    }

    public void join(GameKit gameKit) {
        this.player.createState();
        PlayerUtil.cleanPlayer(this.player, GameMode.ADVENTURE);

        gameKit.give(this.player);

        Player bukkitPlayer = this.player.getPlayer();
        bukkitPlayer.teleportAsync(main.getDungeonPlayerLocation());

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.canSee(bukkitPlayer) && onlinePlayer != bukkitPlayer)
                onlinePlayer.hidePlayer(main, bukkitPlayer);
        }
    }

    public void end() {
        for (Entity entity : currentMobs) entity.remove();

        Player bukkitPlayer = player.getPlayer();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.canSee(bukkitPlayer) && onlinePlayer != bukkitPlayer)
                onlinePlayer.showPlayer(main, bukkitPlayer);
        }

        player.getLastState().restore(bukkitPlayer);
        main.getGameRegistry().unregisterGame(this);
    }

    public DungeonPlayer getPlayer() {
        return player;
    }

    public int getCurrentCountdown() {
        return currentCountdown;
    }

    public Game setCurrentCountdown(int currentCountdown) {
        this.currentCountdown = currentCountdown;
        return this;
    }

    public GameState getState() {
        return state;
    }

    public Game setState(GameState state) {
        this.state = state;
        return this;
    }

    public List<Entity> getCurrentMobs() {
        return currentMobs;
    }
}
