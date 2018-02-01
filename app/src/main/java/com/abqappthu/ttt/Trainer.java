package com.abqappthu.ttt;

public interface Trainer {
    public void addGame(GameStatesWithMoves fullGame);
    public void commitTrainingData();
    public boolean doneTraining();
}
