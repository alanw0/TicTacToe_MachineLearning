package com.abqappthu.ttt;

public class PlayerNextSlot implements Player {
    public Value XorO;

    public int getNextPosition(GameState gameState) {
        for(int i=0; i<gameState.values.length; ++i) {
            if (gameState.values[i] == Value.sp) {
                return i;
            }
        }
        return -1;
    }

    public void setXorO(Value XorO) {
        this.XorO = XorO;
    }

    public Value getXorO() {
        return this.XorO;
    }
}
