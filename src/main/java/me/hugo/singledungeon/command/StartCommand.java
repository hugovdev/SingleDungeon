package me.hugo.singledungeon.command;

import me.hugo.singledungeon.SingleDungeon;
import me.hugo.singledungeon.game.Game;
import me.hugo.singledungeon.game.kit.GameKit;
import me.hugo.singledungeon.player.DungeonPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Description;

@Command("start")
@Description("Join the mob fighting dungeon!")
public final class StartCommand {

    private final SingleDungeon main;

    public StartCommand(SingleDungeon main) {
        this.main = main;
    }

    @Default
    public void onExecute(Player player, GameKit gameKit) {
        DungeonPlayer dungeonPlayer = main.getPlayerRegistry().get(player);
        Game currentGame = dungeonPlayer.getCurrentGame();

        if (currentGame != null) {
            player.sendMessage(Component.text("You are already in a dungeon!", NamedTextColor.RED));
            return;
        }

        currentGame = new Game(main, dungeonPlayer);

        main.getGameRegistry().registerGame(new Game(main, dungeonPlayer));
        player.sendMessage(Component.text("Joining dungeon!", NamedTextColor.GREEN));
        currentGame.join(gameKit);
    }

}
