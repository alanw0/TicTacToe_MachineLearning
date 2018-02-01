#include "NNetwork.hpp"
#include <algorithm>
#include <random>

namespace NN {

Network::Network(unsigned nInput, unsigned nHidden, unsigned nOutput)
 : b(2), w(2), tmpHidden1(nHidden), tmpHidden2(nHidden), tmpOutput1(nOutput),
   randNumGenerator(nullptr), numLayers(3), activations(numLayers), zs(),
   tmp(nOutput), tmp2(nOutput), sp(), delta(), 
   nabla_b(), nabla_w(), delta_nabla_b(), delta_nabla_w()
{
  b[0].assign(nHidden, 0.0);
  b[1].assign(nOutput, 0.0);

  w[0].initialize(nHidden, nInput);
  w[1].initialize(nOutput, nHidden);

  float mean = 0.0, stddev = 1.0;
  randNumGenerator = new utils::RandomGaussian(mean, stddev);

  LinAlg::initialize_vecs(b, randNumGenerator);
  LinAlg::initialize_mats(w, randNumGenerator);

  activations[0].assign(w[0].ncols, 0.0);
  activations[1].assign(w[0].nrows, 0.0);
  activations[2].assign(w[1].nrows, 0.0);

  LinAlg::duplicate_vecs_sizes(b, zs);
  sp.assign(zs[0].size(), 0.0);
  delta.assign(zs[0].size(), 0.0);

  LinAlg::duplicate_vecs_sizes(b, nabla_b);
  LinAlg::duplicate_mats_sizes(w, nabla_w);
  LinAlg::duplicate_vecs_sizes(b, delta_nabla_b);
  LinAlg::duplicate_mats_sizes(w, delta_nabla_w);
}

Network::~Network()
{
  delete randNumGenerator;
}

void Network::update_mini_batch(const std::vector<LinAlg::VVec>& inputs,
                                const std::vector<LinAlg::VVec>& results,
				float eta)
{
    for(LinAlg::Vec& v : nabla_b) {
        LinAlg::fill_vec(v, 0.0);
    }
    for(LinAlg::Mat& m : nabla_w) {
        LinAlg::fill_mat(m, 0.0);
    }

    for(size_t i=0; i<inputs.size(); ++i) {
        back_propagation(inputs[i], results[i],
                         delta_nabla_b, delta_nabla_w);
        for(size_t j=0; j<nabla_b.size(); ++j) {
            LinAlg::vec_plus_vec(delta_nabla_b[j], nabla_b[j]);
        }
        for(size_t j=0; j<nabla_w.size(); ++j) {
            LinAlg::mat_plus_mat(delta_nabla_w[j], nabla_w[j]);
        }
    }

    float scaleFactor = -eta/inputs.size();
    for(size_t i=0; i<w.size(); ++i) {
        LinAlg::scaled_mat_plus_mat(scaleFactor, nabla_w[i], w[i]);
    }
    for(size_t i=0; i<b.size(); ++i) {
        LinAlg::scaled_vec_plus_vec(scaleFactor, nabla_b[i], b[i]);
    }
}

void Network::SGD(DigitData& trainingData, int epochs, int miniBatchSize,
           float eta)
{
  std::vector<LinAlg::VVec> pixelVecs(miniBatchSize);
  std::vector<LinAlg::VVec> resultVecs(miniBatchSize);
  std::vector<int> idx(trainingData.numDigits());
  std::random_device rd;
  std::mt19937 g(rd());

  for(int epoch=0; epoch<epochs; ++epoch) {
    for(size_t i=0; i<idx.size(); ++i) {idx[i] = i;}
      std::shuffle(idx.begin(), idx.end(), g);

    int counter = 0;
    int numBatches = trainingData.numDigits()/miniBatchSize;
    for(int i=0; i<numBatches; ++i) {
      for(int j=0; j<miniBatchSize; ++j) {
        pixelVecs[j] = trainingData.digitPixelVec(idx[counter]);
        resultVecs[j] = trainingData.resultVec(idx[counter]);
        ++counter;
      }
      update_mini_batch(pixelVecs, resultVecs, eta);
    }
  }
}

}

