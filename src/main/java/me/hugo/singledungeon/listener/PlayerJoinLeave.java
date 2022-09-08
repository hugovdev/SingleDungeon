package me.hugo.singledungeon.listener;

import me.hugo.singledungeon.SingleDungeon;
import me.hugo.singledungeon.game.Game;
import me.hugo.singledungeon.player.DungeonPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public final class PlayerJoinLeave implements Listener {

    private final SingleDungeon main;

    public PlayerJoinLeave(SingleDungeon main) {
        this.main = main;
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID playerUuid = event.getPlayerProfile().getId();

        if (playerUuid == null) {
            event.kickMessage(Component.text("There was a problem loading your profile!", NamedTextColor.RED));
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);

            return;
        }

        // Create the player profile and load all their data from the database!
        main.getPlayerRegistry().create(playerUuid);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        main.getPlayerRegistry().get(player).setPlayer(player);

        for (Game runningGame : main.getGameRegistry().getRunningGames()) {
            for (Entity dungeonMob : runningGame.getCurrentMobs()) player.hideEntity(main, dungeonMob);
            player.hidePlayer(main, runningGame.getPlayer().getPlayer());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        DungeonPlayer dungeonPlayer = main.getPlayerRegistry().get(player);

        if (dungeonPlayer == null) return;
        Game currentGame = dungeonPlayer.getCurrentGame();

        if (currentGame == null) return;
        currentGame.end();

        // Save player data
    }

}
