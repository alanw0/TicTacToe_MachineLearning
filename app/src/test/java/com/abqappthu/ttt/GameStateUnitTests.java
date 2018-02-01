package com.abqappthu.ttt;

import org.junit.Test;

import static org.junit.Assert.*;
import static com.abqappthu.ttt.Value.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GameStateUnitTests {
    @Test
    public void setValue_all_Os() throws Exception {
        GameState gameState = new GameState(O,O,O,O,O,O,O,O,O);
        assertTrue(gameState.isGameFull());
        assertTrue(gameState.isThereAWinner());
        assertTrue(gameState.isGameOver());
    }
    @Test
    public void setValue_alternating() throws Exception {
        GameState gameState = new GameState(O,X,O,
                                            X,O,X,
                                            O,X,O);
        assertTrue(gameState.isGameFull());
        assertTrue(gameState.isThereAWinner());
        assertTrue(gameState.isGameOver());
        assertFalse(gameState.allMatch(gameState.values[0], gameState.values[1], gameState.values[2]));
        assertTrue(gameState.allMatch(gameState.values[0], gameState.values[4], gameState.values[8]));
        assertFalse(gameState.allMatch(gameState.values[0], gameState.values[3], gameState.values[6]));
        assertTrue(gameState.allMatch(gameState.values[2], gameState.values[4], gameState.values[6]));
    }

    @Test
    public void gameFullNoWinnerGameOver() throws Exception {
        GameState gameState = new GameState(O,X,O,
                                            X,X,O,
                                            X,O,X);
        assertTrue(gameState.isGameFull());
        assertFalse(gameState.isThereAWinner());
        assertTrue(gameState.isGameOver());
    }

    @Test
    public void gameNotFullNoWinnerGameNotOver() throws Exception {
        GameState gameState = new GameState(O,X,O,
                                            X,sp,X,
                                            sp,O,sp);
        assertFalse(gameState.isGameFull());
        assertFalse(gameState.isThereAWinner());
        assertFalse(gameState.isGameOver());
    }

    @Test
    public void thisMoveIsBlock() throws Exception {
        GameState gameState = new GameState(O,X,O,
                                            X,O,sp,
                                            sp,sp,sp);
        assertTrue(gameState.thisMoveIsBlock(Value.X, 8));
        assertFalse(gameState.thisMoveIsBlock(Value.X, 5));
    }

    @Test
    public void mapToIndex() throws Exception {
        GameState gameState = new GameState();
        gameState.set(sp, sp, sp, sp, sp, sp, sp, sp, sp);
        assertEquals(0, Utils.mapGameStateToIndex(gameState));
        gameState.set(sp, sp, sp, sp, sp, sp, sp, sp,O);
        assertEquals(1, Utils.mapGameStateToIndex(gameState));
        gameState.set(sp, sp, sp, sp, sp, sp, sp, sp,X);
        assertEquals(2, Utils.mapGameStateToIndex(gameState));
        gameState.set(sp, sp, sp, sp, sp, sp, sp,O, sp);
        assertEquals(3, Utils.mapGameStateToIndex(gameState));
        gameState.set(sp, sp, sp, sp, sp, sp, sp,O,O);
        assertEquals(4, Utils.mapGameStateToIndex(gameState));
        gameState.set(sp, sp, sp, sp, sp, sp, sp,O,X);
        assertEquals(5, Utils.mapGameStateToIndex(gameState));
        gameState.set(sp, sp, sp, sp, sp, sp, sp,X, sp);
        assertEquals(6, Utils.mapGameStateToIndex(gameState));

        gameState.set(X, sp, sp, sp, sp, sp, sp, sp, sp);
        assertEquals(2*(int)Math.pow(3, 8), Utils.mapGameStateToIndex(gameState));

        gameState.set(X,X,X,X,X,X,X,X,X);
        assertEquals((int)Math.pow(3, 9)-1, Utils.mapGameStateToIndex(gameState));
    }
}