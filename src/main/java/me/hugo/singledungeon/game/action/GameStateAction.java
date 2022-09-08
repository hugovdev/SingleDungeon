package me.hugo.singledungeon.game.action;

import me.hugo.singledungeon.SingleDungeon;
import me.hugo.singledungeon.game.Game;

public interface GameStateAction {

    void gameTick(SingleDungeon main, Game game);

}
