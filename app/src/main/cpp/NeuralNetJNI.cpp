#include "LinAlg.hpp"
#include "NNetwork.hpp"

#include "com_abqappthu_ttt_NeuralNet.h"

namespace {

void set_test_weights(NN::Network& nnet)
{
    for(LinAlg::Mat& weightsMatrix : nnet.w) {
        for(unsigned r=0; r<weightsMatrix.nrows; ++r) {
            for(unsigned c=0; c<weightsMatrix.ncols; ++c) {
                weightsMatrix(r, c) = c==1 ? 0.5 : 1.0;
            }
        }
    }
}

void set_test_biases(NN::Network& nnet)
{
    for(LinAlg::Vec& biasesVec : nnet.b) {
        LinAlg::fill_vec(biasesVec, 1.0);
    }
}

}

static std::vector<NN::Network*> nnets;

JNIEXPORT jint JNICALL Java_com_abqappthu_ttt_NeuralNet_jniCreateNeuralNet
(JNIEnv *jenv, jobject jobj, jint ninput, jint nhidden, jint noutput)
{
    int returnValue = nnets.size();
    nnets.push_back(new NN::Network(ninput, nhidden, noutput));
    return returnValue;
}

JNIEXPORT void JNICALL Java_com_abqappthu_ttt_NeuralNet_jniDestroyNeuralNet
(JNIEnv *, jobject, jint nnIndex)
{
    delete nnets[nnIndex];
    nnets[nnIndex] = nullptr;
}

JNIEXPORT void JNICALL Java_com_abqappthu_ttt_NeuralNet_jniSetTestWeights
(JNIEnv *, jobject, jint nnIndex)
{
  if (nnets[nnIndex] != nullptr) {
    set_test_weights(*nnets[nnIndex]);
  }
}

JNIEXPORT void JNICALL Java_com_abqappthu_ttt_NeuralNet_jniSetTestBiases
(JNIEnv *, jobject, jint nnIndex)
{
  if (nnets[nnIndex] != nullptr) {
    set_test_biases(*nnets[nnIndex]);
  }
}

JNIEXPORT void JNICALL Java_com_abqappthu_ttt_NeuralNet_jniFeedForward
(JNIEnv *jenv, jobject, jint nnIndex, jfloatArray jinput, jfloatArray joutput)
{
  jfloat* cinput = (*jenv).GetFloatArrayElements(jinput, nullptr);
  jfloat* coutput = (*jenv).GetFloatArrayElements(joutput, nullptr);

  unsigned nInput = nnets[nnIndex]->getNinput();
  unsigned nOutput = nnets[nnIndex]->getNoutput();

  LinAlg::VVec vin(cinput, 0, nInput);
  LinAlg::VVec vout(coutput, 0, nOutput);
  nnets[nnIndex]->feed_forward(vin, vout);

  (*jenv).ReleaseFloatArrayElements(jinput, cinput, JNI_ABORT);
  (*jenv).ReleaseFloatArrayElements(joutput, coutput, 0);
}

JNIEXPORT void JNICALL Java_com_abqappthu_ttt_NeuralNet_jniSGD
(JNIEnv *jenv, jobject jobj, jint nnIndex, jfloatArray jpixelData, jfloatArray jresultData,
        jint numDigits, jint epochs, jint miniBatchSize, jfloat eta)
{
  jfloat* cpixelData = (*jenv).GetFloatArrayElements(jpixelData, nullptr);
  jfloat* cresultData = (*jenv).GetFloatArrayElements(jresultData, nullptr);

  static std::vector<int> digits;
  digits.resize(numDigits);

  unsigned numPixelsPerDigit = nnets[nnIndex]->getNinput();
  unsigned resultVecLenPerDigit = nnets[nnIndex]->getNoutput();

  NN::DigitData digitData(numDigits, numPixelsPerDigit, resultVecLenPerDigit,
                   cpixelData, digits.data(), cresultData);

  nnets[nnIndex]->SGD(digitData, epochs, miniBatchSize, eta);

  (*jenv).ReleaseFloatArrayElements(jpixelData, cpixelData, JNI_ABORT);
  (*jenv).ReleaseFloatArrayElements(jresultData, cresultData, JNI_ABORT);
}

JNIEXPORT jfloat JNICALL Java_com_abqappthu_ttt_NeuralNet_jniGetWeight
        (JNIEnv *, jobject, jint nnIndex, jint wIndex, jint row, jint col)
{
    return nnets[nnIndex]->w[wIndex](row,col);
}

JNIEXPORT jfloat JNICALL Java_com_abqappthu_ttt_NeuralNet_jniGetBias
        (JNIEnv *, jobject, jint nnIndex, jint bIndex, jint index)
{
    return nnets[nnIndex]->b[bIndex][index];
}

JNIEXPORT jint JNICALL Java_com_abqappthu_ttt_NeuralNet_jniGetNinput
        (JNIEnv *, jobject, jint nnIndex)
{
    return nnets[nnIndex]->getNinput();
}

JNIEXPORT jint JNICALL Java_com_abqappthu_ttt_NeuralNet_jniGetNhidden
        (JNIEnv *, jobject, jint nnIndex)
{
    return nnets[nnIndex]->getNhidden();
}

JNIEXPORT jint JNICALL Java_com_abqappthu_ttt_NeuralNet_jniGetNoutput
        (JNIEnv *, jobject, jint nnIndex)
{
    return nnets[nnIndex]->getNoutput();
}