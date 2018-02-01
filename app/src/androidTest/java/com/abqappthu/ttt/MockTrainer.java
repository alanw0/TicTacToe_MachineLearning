package com.abqappthu.ttt;

public class MockTrainer implements Trainer {
    public Value XorO = Value.sp;
    public int numGamesAdded = 0;
    public int numPlayerMoves = 0;
    public int numTrainingGamesNeeded = 1;
    public int[] stateCounts = new int[(int)Math.pow(3,9)];
    public boolean allInputsValid = true;

    public MockTrainer(Value XorO) {
        this.XorO = XorO;
    }

    public boolean allInputsValid() { return allInputsValid; }

    public void addGame(GameStatesWithMoves fullGame) {
        for(int i=0; i<fullGame.numStates; ++i) {
            int idx = Utils.mapGameStateToIndex(fullGame.states[i]);
            ++stateCounts[idx];
            if (fullGame.moves[i] != -1) {
                ++numPlayerMoves;
            }
            if (i == fullGame.numStates-1) {
                if (!fullGame.states[i].isGameOver()) {
                    allInputsValid = false;
                }
                if (fullGame.moves[i] != -1) {
                    allInputsValid = false;
                }
            }
        }
        ++numGamesAdded;
    }

    public void commitTrainingData() {
        numGamesAdded = 0;
        numPlayerMoves = 0;
        for(int i=0; i<stateCounts.length; ++i) {
            stateCounts[i] = 0;
        }
    }

    public boolean doneTraining() {
        return numTrainingGamesNeeded == numGamesAdded;
    }

}
