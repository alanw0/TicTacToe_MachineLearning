package com.abqappthu.ttt;

import org.junit.Test;

import static com.abqappthu.ttt.Value.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TableTrainerUnitTests {
    @Test
    public void trainOneGame() throws Exception {
        StateTable stateTable = new StateTable();
        TableTrainer tableTrainer = new TableTrainer(X, stateTable);
        GameStatesWithMoves fullGame = new GameStatesWithMoves(X);
        fullGame.addStateWithMove(new GameState(sp,sp,sp,
                                                sp,O,sp,
                                                sp,sp,sp), 0);
        fullGame.addStateWithMove(new GameState(X,sp,O,
                                                sp,O,sp,
                                                sp,sp,sp), 6);
        fullGame.addStateWithMove(new GameState(X,sp,O,
                                                O,O,sp,
                                                X,sp,sp), 5);
        fullGame.addStateWithMove(new GameState(X,O,O,
                                                O, O,X,
                                                X,sp,sp), 7);
        fullGame.addStateWithMove(new GameState(X,O,O,
                                                O,O,X,
                                                X,X,O), -1);
        assertEquals(5, fullGame.numStates);
        assertTrue(fullGame.states[4].isGameOver());
        assertTrue(fullGame.states[4].isGameFull());
        assertFalse(fullGame.states[4].isThereAWinner());
        tableTrainer.addGame(fullGame);
        for(int i=0; i<fullGame.numStates-1; ++i) {
            assertEquals(fullGame.moves[i], stateTable.getBestOpenPosition(fullGame.states[i]));
        }
    }
}
