package com.abqappthu.ttt;
/*
Generated JNI header using this command from the jni directory in the terminal window under View->Tool Windows menu item:
javah -classpath ..\..\..\build\intermediates\classes\debug com.abqappthu.ttt.NeuralNet
Note: make sure java build is up-to-date so that .class includes NeuralNet methods.
 */
public class NeuralNet {
    static
    {
        java.lang.System.loadLibrary("nnet");
    }

    private int nnHandle = -1;

    public NeuralNet(int ninput, int nhidden, int noutput) {
        nnHandle = jniCreateNeuralNet(ninput, nhidden, noutput);
    }
    public native int jniCreateNeuralNet(int ninput, int nhidden, int noutput);

    public void destroyNeuralNet() {
        jniDestroyNeuralNet(nnHandle);
        nnHandle = -1;
    }
    public native void jniDestroyNeuralNet(int handle);

    public void setTestWeights() {
        jniSetTestWeights(nnHandle);
    }
    public native void jniSetTestWeights(int handle);

    public void setTestBiases() {
        jniSetTestBiases(nnHandle);
    }
    public native void jniSetTestBiases(int handle);

    public void feedForward(float[] input, float[] output) {
        jniFeedForward(nnHandle, input, output);
    }
    public native void jniFeedForward(int handle, float input[], float output[]);

    public void SGD(float inputData[], float resultData[], int numDataPoints,
                    int epochs, int miniBatchSize, float eta) {
        jniSGD(nnHandle, inputData, resultData, numDataPoints, epochs, miniBatchSize, eta);
    }
    public native void jniSGD(int handle, float inputData[], float resultData[], int numDataPoints,
                           int epochs, int miniBatchSize, float eta);

    public float getWeight(int i, int r, int c) { return jniGetWeight(nnHandle, i, r, c); }
    public native float jniGetWeight(int handle, int i, int r, int c);

    public float getBias(int i, int j) { return jniGetBias(nnHandle, i, j); }
    public native float jniGetBias(int handle, int i, int j);

    public int getNinput() { return jniGetNinput(nnHandle); }
    public native int jniGetNinput(int handle);

    public int getNhidden() { return jniGetNhidden(nnHandle); }
    public native int jniGetNhidden(int handle);

    public int getNoutput() { return jniGetNoutput(nnHandle); }
    public native int jniGetNoutput(int handle);
}
