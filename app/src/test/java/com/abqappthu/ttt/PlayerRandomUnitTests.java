package com.abqappthu.ttt;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class PlayerRandomUnitTests {
    @Test
    public void getNextPositionTest1() throws Exception {
        GameState gameState = new GameState();
        PlayerRandom player1 = new PlayerRandom();
        player1.setXorO(Value.O);
        PlayerRandom player2 = new PlayerRandom();
        player2.setXorO(Value.X);

        int counter = 0;
        while(!gameState.isGameOver()) {
            if (counter%2 == 0) {
                assertTrue(gameState.setValue(player1.XorO, player1.getNextPosition(gameState)));
            }
            else {
                assertTrue(gameState.setValue(player2.XorO, player2.getNextPosition(gameState)));
            }
            ++counter;
        }
        assertTrue(gameState.isGameOver());
    }
}
