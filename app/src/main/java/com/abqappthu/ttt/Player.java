package com.abqappthu.ttt;

public interface Player {
    public abstract int getNextPosition(GameState gameState);
    public abstract void setXorO(Value XorO);
    public abstract Value getXorO();
}
