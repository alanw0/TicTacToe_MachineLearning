package com.abqappthu.ttt;

public class PlayerNN implements Player {
    Value XorO = Value.sp;
    public NeuralNet nnet = null;
    public float[] nnInput = null;
    public float[] nnOutput = null;

    public PlayerNN(Value XorO, NeuralNet nnet) {
        this.XorO = XorO;
        this.nnet = nnet;
        nnInput = new float[nnet.getNinput()];
        nnOutput = new float[nnet.getNoutput()];
    }

    public int getNextPosition(GameState gameState) {
        if (gameState.isGameFull()) return -1;

        Utils.setTrainingInputValues(XorO, gameState.values, nnInput, 0);
        Utils.zero(nnOutput);

        nnet.feedForward(nnInput, nnOutput);
        int pos = Utils.findIndexOfMax(nnOutput);
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
