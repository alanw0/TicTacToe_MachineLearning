package com.abqappthu.ttt;

public class TableTrainer implements Trainer {
    public Value whichPlayer = Value.sp;
    public StateTable stateTable = null;
    public int numWeightsSet = 0;
    public int numWeightsThreshold = 2000000;

    public TableTrainer(Value playerToTrain, StateTable stateTable) {
        this.whichPlayer = playerToTrain;
        this.stateTable = stateTable;
    }

    public void addGame(GameStatesWithMoves fullGame) {
        if (fullGame.whichPlayer() == whichPlayer) {
            GameState endOfGame = fullGame.states[fullGame.numStates-1];
            if (endOfGame.isGameOver()) {
                float weight = getTrainingWeight(endOfGame);
                for(int i=0; i<fullGame.numStates-1; ++i) {
                    float wt = i==fullGame.numStates-2 ? 5*weight : weight;
                    stateTable.addWeight(fullGame.states[i], fullGame.moves[i], wt);
                    ++numWeightsSet;
                }
            }
        }
    }

    public void commitTrainingData() {
        numWeightsSet = 0;
        stateTable.numStatesSet = 0;
        for(int i=0; i<stateTable.count.length; ++i) {
            if (stateTable.count[i] > 0) {
                stateTable.numStatesSet += 1;
                if (stateTable.count[i] < stateTable.minCount) {
                    stateTable.minCount = stateTable.count[i];
                }
                if (stateTable.count[i] > stateTable.maxCount) {
                    stateTable.maxCount = stateTable.count[i];
                }
            }
        }
    }

    public void setNumWeightsThreshold(int threshold) {numWeightsThreshold = threshold; }
    public boolean doneTraining() {
        return numWeightsSet > numWeightsThreshold;
    }

    private float getTrainingWeight(GameState endOfGame) {
        if (!endOfGame.isThereAWinner()) {
            return 0.1f;
        }
        return endOfGame.whoWon()==whichPlayer ? 0.3f : -0.3f;
    }
}
