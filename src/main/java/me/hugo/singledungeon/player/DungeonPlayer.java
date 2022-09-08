package me.hugo.singledungeon.player;

import me.hugo.singledungeon.SingleDungeon;
import me.hugo.singledungeon.game.Game;
import me.hugo.singledungeon.player.state.PlayerState;
import me.hugo.singledungeon.stats.PlayerStat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public final class DungeonPlayer {

    private Player player;
    private final UUID playerUuid;
    private PlayerState lastState;

    private int currentMobKills = 0;
    private Map<PlayerStat, Integer> statsData = new HashMap<>();

    private Game currentGame;

    public DungeonPlayer(SingleDungeon main, UUID playerUuid) {
        this.playerUuid = playerUuid;

        String playerId = playerUuid.toString();

        /*
        Load or create data from the database here
         */
        try (Connection connection = main.getConnectionHandler().getConnection();
             PreparedStatement dataSearch = connection.prepareStatement("SELECT * FROM player_stats WHERE uuid=?")) {

            dataSearch.setString(1, playerId);
            ResultSet playerData = dataSearch.executeQuery();

            if (playerData.next()) {
                for (PlayerStat stats : PlayerStat.values()) {
                    statsData.put(stats, playerData.getInt(stats.getDatabaseName()));
                }
            } else {
                PreparedStatement insert = connection.prepareStatement("INSERT INTO player_stats(uuid) VALUES(?)");
                insert.setString(1, playerId);
                insert.execute();

                insert.close();

                for (PlayerStat stats : PlayerStat.values()) {
                    statsData.put(stats, 0);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToStat(PlayerStat playerStat) {
        addToStat(playerStat, 1);
    }

    public void addToStat(PlayerStat playerStat, int amount) {
        statsData.put(playerStat, statsData.getOrDefault(playerStat, 0) + amount);
    }

    public void addToAverageStat(PlayerStat playerStat, int amount) {
        if (statsData.getOrDefault(playerStat, 0) == 0) {
            statsData.put(playerStat, amount);
            return;
        }

        statsData.put(playerStat, (statsData.get(playerStat) + amount) / 2);
    }

    public void saveData(SingleDungeon main) {
        Bukkit.getScheduler().runTaskAsynchronously(main, task -> {
            try (Connection connection = main.getConnectionHandler().getConnection();
                 PreparedStatement updateStatement = connection.prepareStatement("UPDATE player_stats SET " +
                         statsData.keySet().stream().map(PlayerStat::getDatabaseName).collect(Collectors.joining(" = ?, ")) + " = ? WHERE uuid = ?")) {

                int index = 1;

                for (PlayerStat stat : statsData.keySet()) {
                    updateStatement.setInt(index, statsData.getOrDefault(stat, 0));
                    index++;
                }

                updateStatement.setString(index, playerUuid.toString());
                updateStatement.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public int getCurrentMobKills() {
        return currentMobKills;
    }

    public void resetMobKills() {
        this.currentMobKills = 0;
    }

    public void addMobKill() {
        this.currentMobKills++;
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
