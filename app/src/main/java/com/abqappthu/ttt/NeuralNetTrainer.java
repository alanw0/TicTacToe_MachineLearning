package com.abqappthu.ttt;

public class NeuralNetTrainer implements Trainer {
    public int nInput = 9;
    public int nHidden = 9;
    public int nOutput = 9;
    public NeuralNet nnet = null;
    public int numTrainingInputs = 40000;
    public float[] trainingInputs = null;
    public float[] trainingResults = null;
    public int trainingCounter = 0;
    public Value playerToTrain = Value.sp;

    public int epochs = 5;
    public int miniBatchSize = 10;
    public float eta = 2.0f;

    public NeuralNetTrainer(Value playerToTrain) {
        nnet = new NeuralNet(nInput, nHidden, nOutput);
        trainingInputs = new float[nInput*numTrainingInputs];
        trainingResults = new float[nOutput*numTrainingInputs];
        trainingCounter = 0;
        this.playerToTrain = playerToTrain;
    }

    public void addTrainingInput(Value XorO, Value[] values, int resultPos) {
        if (XorO != playerToTrain) {
            return;
        }
        if (trainingCounter == numTrainingInputs) {
            return;
        }

        Utils.setTrainingInputValues(XorO, values, trainingInputs, trainingCounter*nInput);
        Utils.setTrainingOutputValues(resultPos, nOutput, trainingResults, trainingCounter*nOutput);

        ++trainingCounter;
    }

    public void addGame(GameStatesWithMoves fullGame) {
        if (trainingCounter == numTrainingInputs) {
            return;
        }
        if (fullGame.whichPlayer() == playerToTrain) {
            GameState endOfGame = fullGame.states[fullGame.numStates-1];
            if (endOfGame.isThereAWinner() && endOfGame.whoWon()==playerToTrain) {
                for(int i=0; i<fullGame.numStates-1; ++i) {
                    addTrainingInput(playerToTrain, fullGame.states[i].values, fullGame.moves[i]);
                }
            }
        }
    }

    public void commitTrainingData() {
        nnet.SGD(trainingInputs, trainingResults, numTrainingInputs,
                epochs, miniBatchSize, eta);
        trainingCounter = 0;
    }

    public boolean doneTraining() {
        return trainingCounter == numTrainingInputs;
    }
}
