package me.hugo.singledungeon.schedule;

import me.hugo.singledungeon.SingleDungeon;
import me.hugo.singledungeon.game.Game;
import org.bukkit.scheduler.BukkitRunnable;

public final class GameRunTask extends BukkitRunnable {

    private final SingleDungeon main;

    public GameRunTask(SingleDungeon main) {
        this.main = main;
    }

    @Override
    public void run() {
        for (Game game : main.getGameRegistry().getRunningGames()) {
            game.getState().getAction().gameTick(main, game);
        }
    }

}
