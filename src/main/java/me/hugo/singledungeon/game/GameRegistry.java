package me.hugo.singledungeon.game;

import java.util.ArrayList;
import java.util.List;

public final class GameRegistry {

    private final List<Game> runningGames = new ArrayList<>();

    /*
    Returns a copy to avoid Concurrent exception(s).
     */
    public List<Game> getRunningGames() {
        return new ArrayList<>(runningGames);
    }

    public void registerGame(Game game) {
        game.getPlayer().setCurrentGame(game);
        this.runningGames.add(game);
    }

    public void unregisterGame(Game game) {
        game.getPlayer().setCurrentGame(null);
        this.runningGames.remove(game);
    }

}
