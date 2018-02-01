package com.abqappthu.ttt;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static com.abqappthu.ttt.Value.*;

public class PlayerTableUnitTests {
    @Test
    public void getNextPositionTest1() throws Exception {
        StateTable stateTableO = new StateTable();
        GameState gameState = new GameState();
        PlayerTable player1 = new PlayerTable(stateTableO);
        player1.setXorO(Value.O);
        StateTable stateTableX = new StateTable();
        PlayerTable player2 = new PlayerTable(stateTableX);
        player2.setXorO(Value.X);

        int counter = 0;
        while (!gameState.isGameOver()) {
            if (counter % 2 == 0) {
                assertTrue(gameState.setValue(player1.getXorO(), player1.getNextPosition(gameState)));
            } else {
                assertTrue(gameState.setValue(player2.getXorO(), player2.getNextPosition(gameState)));
            }
            ++counter;
        }
        assertTrue(gameState.isGameOver());
    }
}
