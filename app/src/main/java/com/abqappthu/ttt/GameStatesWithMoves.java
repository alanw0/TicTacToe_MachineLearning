package com.abqappthu.ttt;

public class GameStatesWithMoves {
    public GameState[] states = new GameState[9];
    public int[] moves = new int[9];
    public int numStates = 0;
    public Value player = Value.sp;

    public GameStatesWithMoves(Value player) {

        for(int i=0; i<9; ++i) {
            states[i] = new GameState();
        }
        this.player = player;
    }
    public void setWhichPlayer(Value XorO) { player = XorO; }

    public Value whichPlayer() { return player; }

    public void addStateWithMove(GameState state, int move) {
        if (numStates < 9) {
            for(int i=0; i<state.values.length; ++i) {
                states[numStates].values[i] = state.values[i];
            }
            moves[numStates] = move;
            ++numStates;
        }
    }

    public void clear() {
        numStates = 0;
    }
}
