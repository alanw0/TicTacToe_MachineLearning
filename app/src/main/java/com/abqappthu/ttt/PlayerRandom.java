package com.abqappthu.ttt;

import java.util.Random;

public class PlayerRandom implements Player {
    Random random = new Random();
    Value XorO = Value.sp;

    public PlayerRandom(Value XorO) {
        this.XorO = XorO;
    }
    public int getNextPosition(GameState gameState) {
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
