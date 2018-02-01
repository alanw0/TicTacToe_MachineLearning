package com.abqappthu.ttt;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class NeuralNetTrainerTests {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.abqappthu.ttt", appContext.getPackageName());
    }
    @Test
    public void constructionTest() throws Exception {
        NeuralNetTrainer nnTrainer = new NeuralNetTrainer(Value.O);
        assertEquals(0, nnTrainer.trainingCounter);
        assertEquals(nnTrainer.nInput, nnTrainer.nnet.getNinput());
    }
    @Test
    public void addTrainingInputTest() throws Exception {
        NeuralNetTrainer nnTrainer = new NeuralNetTrainer(Value.O);
        int n = nnTrainer.nInput;
        Value[] values = new Value[n];
        for(int i=0; i<n; ++i) {
            values[i] = Value.O;
        }
        int resultPos = 4;
        nnTrainer.addTrainingInput(Value.O, values, resultPos);
        assertEquals(1, nnTrainer.trainingCounter);
        for(int i=0; i<n; ++i) {
            assertEquals(Utils.mapValueToFloat(Value.O), nnTrainer.trainingInputs[i]);
        }
        assertEquals(1f, nnTrainer.trainingResults[resultPos], 0.00001);
    }
}
