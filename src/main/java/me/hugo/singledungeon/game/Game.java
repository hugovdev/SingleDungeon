package me.hugo.singledungeon.game;

import me.hugo.singledungeon.SingleDungeon;
import me.hugo.singledungeon.game.kit.GameKit;
import me.hugo.singledungeon.player.DungeonPlayer;
import me.hugo.singledungeon.stats.PlayerStat;
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

        player.addToStat(PlayerStat.SESSIONS);
    }

    public void end() {
        for (Entity entity : currentMobs) {
            main.getMobRegistry().removeMob(entity);
            entity.remove();
        }

        Player bukkitPlayer = player.getPlayer();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.canSee(bukkitPlayer) && onlinePlayer != bukkitPlayer)
                onlinePlayer.showPlayer(main, bukkitPlayer);
        }

        int mobKills = player.getCurrentMobKills();

        player.addToAverageStat(PlayerStat.AVERAGE_KILLS, mobKills);
        player.addToStat(PlayerStat.MOB_KILLS, mobKills);
        player.resetTemporaryStats();
        player.getPlayer().closeInventory();

        player.getLastState().restore(bukkitPlayer);
        main.getGameRegistry().unregisterGame(this);
    }

    public DungeonPlayer getPlayer() {
        return player;
    }

    public int getCurrentCountdown() {
        return currentCountdown;
    }

    public void setCurrentCountdown(int currentCountdown) {
        this.currentCountdown = currentCountdown;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public List<Entity> getCurrentMobs() {
        return currentMobs;
    }
}
