package com.abqappthu.ttt;

import android.widget.TextView;

public class StateTable {
    public float[] statePositionWeights = null;
    public int[] count = null;
    public int sizePerState = 9;
    public int numStatesSet = 0;
    public int minCount = 9999999;
    public int maxCount = 0;

    public StateTable() {
        int numStates = (int)Math.pow(3, 9);
        statePositionWeights = new float[numStates*sizePerState];
        count = new int[numStates];
    }

    public void addWeight(GameState state, int position, float weight) {
        int idx = Utils.mapGameStateToIndex(state);
        statePositionWeights[idx*sizePerState + position] += weight;
        count[idx] += 1;
    }

    public int getBestOpenPosition(GameState state) {
        int stateIdx = Utils.mapGameStateToIndex(state)*sizePerState;
        return Utils.findIndexOfOpenPositionWithMaxWeightInRange(state, statePositionWeights,
                                                    stateIdx, sizePerState) - stateIdx;
    }

    public float getWeightOfBestOpenPosition(GameState state) {
        int stateIdx = Utils.mapGameStateToIndex(state)*sizePerState;
        int pos = Utils.findIndexOfOpenPositionWithMaxWeightInRange(state, statePositionWeights,
                                                    stateIdx, sizePerState);
        return statePositionWeights[pos];
    }

    public void showOpenPositionWeights(GameState state, final TextView view) {
        if (view != null) {
            int stateIdx = Utils.mapGameStateToIndex(state);
            int stateWeightsIdx = stateIdx*sizePerState;
            String str = "";
            if (numStatesSet > 0) {
                str = Integer.toString(numStatesSet)+"/"+Integer.toString(count.length)+" {"+Integer.toString(count[stateIdx])+"} ";
            }
            for(int i=0; i<state.values.length; ++i) {
                if (state.values[i] == Value.sp) {
                    str += Integer.toString(i) + ":" +
                            String.format(java.util.Locale.US, "%.1f",
                                    statePositionWeights[stateWeightsIdx + i]) + " ";
                }
            }
            final String fstr = str;
            view.post(new Runnable() {
                @Override
                public void run() {
                    view.setText(fstr);
                }
            });
        }
    }
}
