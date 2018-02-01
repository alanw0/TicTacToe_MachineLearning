package com.abqappthu.ttt;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class GameControllerTests {
    @Test
    public void constructionTest() throws Exception {
        GameController gameController = new GameController();
        assertEquals(null, gameController.trainer_O);
        assertEquals(null, gameController.gameDisplayer);
    }

    @Test
    public void playGameWithTrainingTest() throws Exception {
        GameController gameController = new GameController();

        MockTrainer trainer_O = new MockTrainer(Value.O);
        MockTrainer trainer_X = new MockTrainer(Value.X);
        gameController.setTrainers(trainer_O, trainer_X);
        assertTrue(gameController.isTraining);

        Player playerO = new PlayerNextSlot();
        playerO.setXorO(Value.O);
        Player playerX = new PlayerNextSlot();
        playerX.setXorO(Value.X);
        gameController.setPlayers(playerO, playerX);

        assertEquals(0, gameController.fullGameStates[0].numStates);
        assertEquals(0, gameController.fullGameStates[1].numStates);

        gameController.playGame(-1);

        assertEquals(1, gameController.oWins);
        assertEquals(0, gameController.xWins);
        int numMovesByWinningPlayer = 4;
        assertEquals(numMovesByWinningPlayer, trainer_O.numPlayerMoves);
        int numMovesByLosingPlayer = 3;
        assertEquals(numMovesByLosingPlayer, trainer_X.numPlayerMoves);

        int idx = Utils.mapGameStateToIndex(gameController.gameState);
        assertEquals(1, trainer_O.stateCounts[idx]);
        assertEquals(1, trainer_X.stateCounts[idx]);
        assertTrue(trainer_O.allInputsValid());
        assertTrue(trainer_X.allInputsValid());
    }
}
