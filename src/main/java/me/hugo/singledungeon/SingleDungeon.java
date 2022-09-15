package me.hugo.singledungeon;

import me.hugo.singledungeon.command.LeaveCommand;
import me.hugo.singledungeon.command.SingleDungeonCommand;
import me.hugo.singledungeon.command.StartCommand;
import me.hugo.singledungeon.command.StatsCommand;
import me.hugo.singledungeon.database.DatabaseConnectionHandler;
import me.hugo.singledungeon.game.Game;
import me.hugo.singledungeon.game.GameRegistry;
import me.hugo.singledungeon.game.mob.DungeonMobRegistry;
import me.hugo.singledungeon.listener.*;
import me.hugo.singledungeon.player.DungeonPlayerRegistry;
import me.hugo.singledungeon.schedule.GameRunTask;
import me.hugo.singledungeon.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class SingleDungeon extends JavaPlugin {

    private final DungeonPlayerRegistry playerRegistry = new DungeonPlayerRegistry(this);
    private final DungeonMobRegistry mobRegistry = new DungeonMobRegistry();
    private final GameRegistry gameRegistry = new GameRegistry();

    private BukkitCommandHandler commandHandler;

    private DatabaseConnectionHandler connectionHandler;

    private Location dungeonPlayerLocation;
    private List<Location> mobSpawnLocations;

    @Override
    public void onEnable() {
        Logger logger = getLogger();
        logger.info("Starting SingleDungeon...");

        logger.info("Reading config...");
        saveDefaultConfig();

        String serializedPlayerLocation = getConfig().getString("game-data.player-spawn-location");

        if (serializedPlayerLocation != null) {
            dungeonPlayerLocation = LocationUtil.getLocationByString(serializedPlayerLocation);
        } else {
            logger.warning("Dungeon player location is not set up");
        }

        mobSpawnLocations = getConfig().getStringList("game-data.mob-spawn-locations")
                .stream().map(LocationUtil::getLocationByString).collect(Collectors.toList());

        if (mobSpawnLocations.size() == 0) {
            logger.warning("Dungeon mob spawn locations are not set up or empty!");
            mobSpawnLocations = new ArrayList<>();
        }

        logger.info("Registering listeners...");
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerJoinLeave(this), this);
        pluginManager.registerEvents(new CancelledEvents(this), this);
        pluginManager.registerEvents(new EntityDamageDeath(this), this);
        pluginManager.registerEvents(new EntityTarget(this), this);
        pluginManager.registerEvents(new InventoryClick(), this);

        logger.info("Creating commands...");
        commandHandler = BukkitCommandHandler.create(this);

        commandHandler.register(new SingleDungeonCommand(this));
        commandHandler.register(new StartCommand(this));
        commandHandler.register(new LeaveCommand(this));
        commandHandler.register(new StatsCommand(this));

        commandHandler.registerBrigadier();

        logger.info("Starting up MySQL Connection Handler...");
        connectionHandler = new DatabaseConnectionHandler(this);

        try (Connection connection = connectionHandler.getConnection()) {
            Statement tableCreation = connection.createStatement();

            /*
            Create query from PlayerStat enum.
             */
            tableCreation.executeUpdate("CREATE TABLE IF NOT EXISTS `player_stats` (`uuid` VARCHAR(36) PRIMARY KEY, " +
                    "`mob_kills` INT NOT NULL DEFAULT '0', " +
                    "`sessions` INT NOT NULL DEFAULT '0', " +
                    "`average_kills` INT NOT NULL DEFAULT '0', " +
                    "`deaths` INT NOT NULL DEFAULT '0')");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        logger.info("Starting game running tasks...");
        new GameRunTask(this).runTaskTimer(this, 0L, 20L);

        logger.info("Plugin started up correctly!");
    }

    @Override
    public void onDisable() {
        commandHandler.unregisterAllCommands();

        for (Game game : gameRegistry.getRunningGames()) {
            game.end();
        }

        connectionHandler.getDataSource().close();
    }

    public DungeonPlayerRegistry getPlayerRegistry() {
        return playerRegistry;
    }

    public DungeonMobRegistry getMobRegistry() {
        return mobRegistry;
    }

    public GameRegistry getGameRegistry() {
        return gameRegistry;
    }

    public Location getDungeonPlayerLocation() {
        return dungeonPlayerLocation;
    }

    public DatabaseConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    public List<Location> getMobSpawnLocations() {
        return mobSpawnLocations;
    }
}
