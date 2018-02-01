package com.abqappthu.ttt;

import org.junit.Test;

import static com.abqappthu.ttt.Value.*;
import static junit.framework.Assert.assertEquals;

public class GameStatesWithMovesUnitTests {
    @Test
    public void addStateAndMove_thenClear() throws Exception {
        GameStatesWithMoves statesAndMoves = new GameStatesWithMoves(O);
        assertEquals(O, statesAndMoves.whichPlayer());
        statesAndMoves.addStateWithMove(new GameState(sp,sp,sp,
                                                      sp,sp,sp,
                                                      sp,sp,sp), 4);
        assertEquals(4, statesAndMoves.moves[0]);
        assertEquals(1, statesAndMoves.numStates);
        GameState gs = new GameState(sp,sp,sp,
                                     sp,O,sp,
                                     X,sp,sp);
        int move = 8;
        statesAndMoves.addStateWithMove(gs, move);
        assertEquals(2, statesAndMoves.numStates);
        assertEquals(move, statesAndMoves.moves[1]);
        int idx = Utils.mapGameStateToIndex(gs);
        assertEquals(idx, Utils.mapGameStateToIndex(statesAndMoves.states[1]));

        statesAndMoves.clear();
        assertEquals(0, statesAndMoves.numStates);
    }
}
