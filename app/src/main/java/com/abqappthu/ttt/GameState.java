package com.abqappthu.ttt;

public class GameState {
    public Value[] values = new Value[9];

    public GameState() {
        for(int i=0; i<values.length; ++i) {
            values[i] = Value.sp;
        }
    }

    public GameState(Value v1, Value v2, Value v3,
                     Value v4, Value v5, Value v6,
                     Value v7, Value v8, Value v9)
    {
        set(v1, v2, v3, v4, v5, v6, v7, v8, v9);
    }

    public void set(Value v1, Value v2, Value v3,
                    Value v4, Value v5, Value v6,
                    Value v7, Value v8, Value v9)
    {
        values[0] = v1; values[1] = v2; values[2] = v3;
        values[3] = v4; values[4] = v5; values[5] = v6;
        values[6] = v7; values[7] = v8; values[8] = v9;
    }

    public static void copy(GameState src, GameState dest) {
        for(int i=0; i<src.values.length; ++i) {
            dest.values[i] = src.values[i];
        }
    }

    public void clear() {
        for(int i=0; i<values.length; ++i) {
            values[i] = Value.sp;
        }
    }

    public boolean setValue(Value v, int pos) {
        if (pos >= 0 && pos < 9) {
            values[pos] = v;
            return true;
        }
        return false;
    }

    public boolean allMatch(Value v1, Value v2, Value v3) {
        return v1 == v2 && v1 == v3 && v1 != Value.sp;
    }

    public boolean isThereAWinner() {
        return  allMatch(values[0], values[1], values[2]) ||
                allMatch(values[3], values[4], values[5]) ||
                allMatch(values[6], values[7], values[8]) ||
                allMatch(values[0], values[3], values[6]) ||
                allMatch(values[1], values[4], values[7]) ||
                allMatch(values[2], values[5], values[8]) ||
                allMatch(values[0], values[4], values[8]) ||
                allMatch(values[6], values[4], values[2]);
    }

    public Value whoWon() {
        if (allMatch(values[0], values[1], values[2])) return values[0];
        if (allMatch(values[3], values[4], values[5])) return values[3];
        if (allMatch(values[6], values[7], values[8])) return values[6];
        if (allMatch(values[0], values[3], values[6])) return values[0];
        if (allMatch(values[1], values[4], values[7])) return values[1];
        if (allMatch(values[2], values[5], values[8])) return values[2];
        if (allMatch(values[0], values[4], values[8])) return values[0];
        if (allMatch(values[6], values[4], values[2])) return values[6];
        return Value.sp;
    }

    public boolean isGameFull() {
        for(Value val : values) {
            if (val == Value.sp) {
                return false;
            }
        }
        return true;
    }

    public boolean isGameOver() {
        return isThereAWinner() || isGameFull();
    }

    public boolean thisMoveWinsGame(Value XorO, int newPos) {
        setValue(XorO, newPos);
        boolean thisMoveWins = isThereAWinner();
        setValue(Value.sp, newPos);
        return thisMoveWins;
    }

    public boolean thisMoveIsBlock(Value XorO, int newPos) {
        boolean isBlock = false;
        Value otherPlayer = XorO==Value.O ? Value.X : Value.O;
        setValue(otherPlayer, newPos);
        if (isThereAWinner()) {
            isBlock = true;
        }
        setValue(Value.sp, newPos);
        return isBlock;
    }

    public boolean thisMoveMissesWin(Value XorO, int newPos) {
        boolean missedWin = false;
        for(int i=0; i<values.length; ++i) {
            if (i != newPos && values[i] == Value.sp) {
                setValue(XorO, i);
                missedWin = isThereAWinner();
                setValue(Value.sp, i);
                if (missedWin) {
                    break;
                }
            }
        }
        return missedWin;
    }

    public boolean thisMoveMissesBlock(Value XorO, int newPos) {
        Value otherPlayer = XorO==Value.O ? Value.X : Value.O;
        boolean missedBlock = false;
        for(int i=0; i<values.length; ++i) {
            if (i != newPos && values[i] == Value.sp) {
                setValue(otherPlayer, i);
                missedBlock = isThereAWinner();
                setValue(Value.sp, i);
                if (missedBlock) {
                    break;
                }
            }
        }
        return missedBlock;
    }
}
