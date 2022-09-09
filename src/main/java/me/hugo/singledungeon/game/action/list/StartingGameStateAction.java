package me.hugo.singledungeon.game.action.list;

import me.hugo.singledungeon.SingleDungeon;
import me.hugo.singledungeon.game.Game;
import me.hugo.singledungeon.game.GameState;
import me.hugo.singledungeon.game.action.GameStateAction;
import me.hugo.singledungeon.player.DungeonPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public final class StartingGameStateAction implements GameStateAction {

    @Override
    public void gameTick(SingleDungeon main, Game game) {
        DungeonPlayer player = game.getPlayer();
        Player bukkitPlayer = player.getPlayer();

        int currentCountdown = game.getCurrentCountdown();

        if (currentCountdown > 0) {
            bukkitPlayer.sendMessage(Component.text("Game will start in ", NamedTextColor.YELLOW)
                    .append(Component.text(currentCountdown, NamedTextColor.RED))
                    .append(Component.text(" " + (currentCountdown == 1 ? "second" : "seconds") + "!")));

            bukkitPlayer.showTitle(Title.title(Component.text(currentCountdown, NamedTextColor.GREEN, TextDecoration.BOLD),
                    Component.text(""),
                    Title.Times.times(Ticks.duration(5), Ticks.duration(5), Ticks.duration(5))));

            bukkitPlayer.playSound(bukkitPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
            game.setCurrentCountdown(currentCountdown - 1);

            return;
        }

        bukkitPlayer.playSound(bukkitPlayer.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
        bukkitPlayer.showTitle(Title.title(Component.text("GO!", NamedTextColor.GREEN, TextDecoration.BOLD),
                Component.text("Good luck!"),
                Title.Times.times(Ticks.duration(10), Ticks.duration(20), Ticks.duration(10))));
        bukkitPlayer.sendMessage(Component.text("Game has started!", NamedTextColor.GREEN));
        game.setState(GameState.FIGHTING);
    }

}
