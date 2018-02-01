package com.abqappthu.ttt;

import android.widget.TextView;

import java.util.Random;

public class PlayerTable implements Player {
    Random random = new Random();
    private Value XorO = Value.sp;
    private StateTable stateTable = null;
    private TextView diagView = null;
    private int trainingThreshold = 500;

    public PlayerTable(Value XorO, StateTable stateTable) {
        this.XorO = XorO;
        this.stateTable = stateTable;
    }

    public void setDiagView(TextView view) { diagView = view; }
    public void setTrainingThreshold(int threshold) { trainingThreshold = threshold; }
    public int getNextPosition(GameState gameState) {
        if (gameState.isGameOver()) return -1;
        if (stateTable.count[Utils.mapGameStateToIndex(gameState)] > trainingThreshold) {
            if (diagView != null) {
                stateTable.showOpenPositionWeights(gameState, diagView);
            }
            return stateTable.getBestOpenPosition(gameState);
        }
        return getNextRandomPosition(gameState);
    }

    public int getNextRandomPosition(GameState gameState) {
        if (gameState.isGameOver()) return -1;
        int pos = random.nextInt(9);
        while(gameState.values[pos] != Value.sp) {
            pos += 1;
            if (pos > 8) pos = 0;
        }
        return pos;
    }

    public void setXorO(Value XorO) {
        this.XorO = XorO;
    }
    public Value getXorO() { return XorO; }
}
