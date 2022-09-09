package me.hugo.singledungeon.command;

import me.hugo.singledungeon.SingleDungeon;
import me.hugo.singledungeon.game.Game;
import me.hugo.singledungeon.menu.StatsMenu;
import me.hugo.singledungeon.player.DungeonPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Description;

@Command("stats")
@Description("Open a menu to see your stats in dungeons!")
public final class StatsCommand {

    private final SingleDungeon main;

    public StatsCommand(SingleDungeon main) {
        this.main = main;
    }

    @Default
    public void onExecute(Player player) {
        DungeonPlayer dungeonPlayer = main.getPlayerRegistry().get(player);
        Game currentGame = dungeonPlayer.getCurrentGame();

        if (currentGame != null) {
            player.sendMessage(Component.text("You can't open this menu inside a dungeon!", NamedTextColor.RED));
            return;
        }

        player.openInventory(new StatsMenu(dungeonPlayer).getInventory());
    }

}

