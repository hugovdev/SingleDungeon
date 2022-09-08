package me.hugo.singledungeon.game;

import me.hugo.singledungeon.game.action.GameStateAction;
import me.hugo.singledungeon.game.action.list.FightingGameStateAction;
import me.hugo.singledungeon.game.action.list.StartingGameStateAction;

public enum GameState {

    STARTING(new StartingGameStateAction()), FIGHTING(new FightingGameStateAction());

    private final GameStateAction action;

    GameState(GameStateAction action) {
        this.action = action;
    }

    public GameStateAction getAction() {
        return action;
    }
}
