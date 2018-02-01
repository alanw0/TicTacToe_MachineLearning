package com.abqappthu.ttt;

public class
Utils {
    public static float mapValueToFloat(Value v) {
        float f = 0f;
        switch(v) {
            case sp: f = 0f; break;
            case O: f = 0.5f; break;
            case X: f = 1f; break;
            default: break;
        }
        return f;
    }
    public static Value mapFloatToValue(float f) {
        if (f < 0.25f) return Value.sp;
        if (f < 0.75f) return Value.O;
        return Value.X;
    }

    public static void setTrainingInputValues(Value XorO, Value[] values, float[] trainingInputs, int idx) {
        for(int i=0; i<values.length; ++i) {
            trainingInputs[idx+i] = mapValueToFloat(values[i]);
        }
    }

    public static void setTrainingOutputValues(int resultPos, int nOutput, float[] trainingResults, int idx) {
        for(int i=0; i<nOutput; ++i) {
            trainingResults[idx+i] = 0f;
        }
        trainingResults[idx+resultPos] = 1f;
    }

    public static void zero(float[] values) {
        for(int i=0; i<values.length; ++i) {
            values[i] = 0f;
        }
    }

    public static int findIndexOfMaxInRange(float[] vals, int start, int num) {
        float maxVal = vals[start];
        int idx = start;
        for(int i=start+1; i<start+num; ++i) {
            if (vals[i] > maxVal) {
                maxVal = vals[i];
                idx = i;
            }
        }
        return idx;
    }

    public static int findIndexOfOpenPositionWithMaxWeightInRange(GameState state, float[] vals,
                                                                  int start, int num)
    {
        float maxVal = Float.NEGATIVE_INFINITY;
        int idxOfMax = -1;
        for(int i=0; i<num; ++i) {
            int idx = i+start;
            if (positionIsOpen(state.values[i]) && vals[idx] > maxVal) {
                maxVal = vals[idx];
                idxOfMax = idx;
            }
        }
        return idxOfMax;
    }

    private static boolean positionIsOpen(Value value) {
        return value == Value.sp;
    }

    public static int findIndexOfMax(float[] vals) {
        return findIndexOfMaxInRange(vals, 0, vals.length);
    }

    public static int mapValueToInt(Value value) {
        if (value == Value.sp) {
            return 0;
        }
        return value==Value.O ? 1 : 2;
    }

    public static int mapGameStateToIndex(GameState gameState) {
        int index = 0;
        int ii = gameState.values.length - 1;
        for(int i=0; i<gameState.values.length; ++i) {
            int thisVal = mapValueToInt(gameState.values[ii]);
            index += thisVal*(int)Math.pow(3, i);
            --ii;
        }
        return index;
    }
}
