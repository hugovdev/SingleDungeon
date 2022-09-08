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

@Command("leave")
@Description("Leave the mob fighting dungeon!")
public final class LeaveCommand {

    private final SingleDungeon main;

    public LeaveCommand(SingleDungeon main) {
        this.main = main;
    }

    @Default
    public void onExecute(Player player) {
        DungeonPlayer dungeonPlayer = main.getPlayerRegistry().get(player);
        Game currentGame = dungeonPlayer.getCurrentGame();

        if (currentGame == null) {
            player.sendMessage(Component.text("You are not in a dungeon!", NamedTextColor.RED));
            return;
        }

        currentGame.end();
        player.sendMessage(Component.text("You left the dungeon!", NamedTextColor.GREEN));
    }

}
