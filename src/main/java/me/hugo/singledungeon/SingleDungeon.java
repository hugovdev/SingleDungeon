package me.hugo.singledungeon;

import me.hugo.singledungeon.command.LeaveCommand;
import me.hugo.singledungeon.command.SingleDungeonCommand;
import me.hugo.singledungeon.command.StartCommand;
import me.hugo.singledungeon.game.Game;
import me.hugo.singledungeon.game.GameRegistry;
import me.hugo.singledungeon.game.mob.DungeonMobRegistry;
import me.hugo.singledungeon.listener.CancelledEvents;
import me.hugo.singledungeon.listener.EntityDeath;
import me.hugo.singledungeon.listener.PlayerJoinLeave;
import me.hugo.singledungeon.player.DungeonPlayerRegistry;
import me.hugo.singledungeon.schedule.GameRunTask;
import me.hugo.singledungeon.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class SingleDungeon extends JavaPlugin {

    private final DungeonPlayerRegistry playerRegistry = new DungeonPlayerRegistry();
    private final DungeonMobRegistry mobRegistry = new DungeonMobRegistry();
    private final GameRegistry gameRegistry = new GameRegistry();

    private BukkitCommandHandler commandHandler;

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

        mobSpawnLocations = getConfig().getStringList("game-data.mob-spawn-locations").stream().map(LocationUtil::getLocationByString).collect(Collectors.toList());

        if (mobSpawnLocations.size() == 0) {
            logger.warning("Dungeon mob spawn locations are not set up or empty!");
            mobSpawnLocations = new ArrayList<>();
        }

        logger.info("Registering listeners...");
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerJoinLeave(this), this);
        pluginManager.registerEvents(new CancelledEvents(this), this);
        pluginManager.registerEvents(new EntityDeath(this), this);

        logger.info("Creating commands...");
        commandHandler = BukkitCommandHandler.create(this);

        commandHandler.register(new SingleDungeonCommand(this));
        commandHandler.register(new StartCommand(this));
        commandHandler.register(new LeaveCommand(this));

        commandHandler.registerBrigadier();

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

    public List<Location> getMobSpawnLocations() {
        return mobSpawnLocations;
    }
}
