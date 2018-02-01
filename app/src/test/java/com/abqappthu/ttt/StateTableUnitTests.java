package com.abqappthu.ttt;

import org.junit.Test;

import static com.abqappthu.ttt.Value.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class StateTableUnitTests {
    @Test
    public void constructStateTable() throws Exception {
        StateTable stateTable = new StateTable();
        int expectedNumStates = (int) Math.pow(3, 9);
        int actualNumStates = stateTable.statePositionWeights.length / stateTable.sizePerState;
        assertEquals(expectedNumStates, actualNumStates);
    }
    @Test
    public void bestOpenPosition() throws Exception {
        StateTable stateTable = new StateTable();
        GameState gameState = new GameState(sp, sp, sp, sp, O, sp, sp, sp, sp);
        int position = 4;
        float weight = 0.9f;
        stateTable.addWeight(gameState, position, weight);
        position = 7;
        weight = 0.4f;
        stateTable.addWeight(gameState, position, weight);
        assertEquals(position, stateTable.getBestOpenPosition(gameState));
    }
    @Test
    public void getBestOpenPositionWeight() throws Exception {
        StateTable stateTable = new StateTable();
        GameState gameState = new GameState(sp, sp, sp, sp, O, sp, sp, sp, sp);
        int position = 4;
        float weight = 0.9f;
        stateTable.addWeight(gameState, position, weight);
        position = 7;
        weight = 0.4f;
        stateTable.addWeight(gameState, position, weight);
        float maxWeight = stateTable.getWeightOfBestOpenPosition(gameState);
        assertEquals(weight, maxWeight, 1.e-8);
    }
}
