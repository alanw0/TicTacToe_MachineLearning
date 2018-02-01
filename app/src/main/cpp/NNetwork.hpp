#ifndef _NNetwork_hpp_
#define _NNetwork_hpp_

#include "LinAlg.hpp"
#include "Rand.hpp"
#include "DigitData.hpp"

#include <vector>

namespace NN {

struct Network {
  Network(unsigned nInput, unsigned nHidden, unsigned nOutput);
  ~Network();

  template<typename VecType>
  void feed_forward(const VecType& input, VecType& output)
  {
      LinAlg::mat_vec_plus_vec(w[0], input, b[0], tmpHidden1);
      LinAlg::sigmoid(tmpHidden1, tmpHidden2);
      LinAlg::mat_vec_plus_vec(w[1], tmpHidden2, b[1], tmpOutput1);
      LinAlg::sigmoid(tmpOutput1, output);
  }

  template<typename VecType>
  void back_propagation(const VecType& x, const VecType& y,
                        std::vector<LinAlg::Vec>& nablb,
			std::vector<LinAlg::Mat>& nablw)
  {
      LinAlg::copy_vec(x, activations[0]);
      for(int i=0; i<(numLayers-1); ++i) {
          LinAlg::mat_vec_plus_vec(w[i], activations[i], b[i], zs[i]);
          LinAlg::sigmoid(zs[i], activations[i + 1]);
      }
      LinAlg::vec_difference(activations[numLayers-1], y, tmp);
      LinAlg::sigmoid_prime(zs[1], tmp2);
      LinAlg::hadamard_product(tmp, tmp2, nablb[1]);
      LinAlg::vec_vec_trans(nablb[1], activations[numLayers-2], nablw[1]);

      LinAlg::sigmoid_prime(zs[0], sp);
      LinAlg::mat_trans_vec(w[1], nablb[1], delta);
      LinAlg::hadamard_product(delta, sp, nablb[0]);
      LinAlg::vec_vec_trans(nablb[0], activations[0], nablw[0]);
  }

  void update_mini_batch(const std::vector<LinAlg::VVec>& inputs,
                         const std::vector<LinAlg::VVec>& results,
                         float eta);

  void SGD(DigitData& trainingData, int epochs, int miniBatchSize,
           float eta);

  unsigned getNinput() const { return w[0].ncols; }
  unsigned getNhidden() const { return w[0].nrows; }
  unsigned getNoutput() const { return w[1].nrows; }

  std::vector<LinAlg::Vec> b;
  std::vector<LinAlg::Mat> w;

private:
  LinAlg::Vec tmpHidden1;
  LinAlg::Vec tmpHidden2;
  LinAlg::Vec tmpOutput1;
  utils::Rand* randNumGenerator;
  int numLayers;
  std::vector<LinAlg::Vec> activations;
  std::vector<LinAlg::Vec> zs;
  LinAlg::Vec tmp;
  LinAlg::Vec tmp2;
  LinAlg::Vec sp;
  LinAlg::Vec delta;
  std::vector<LinAlg::Vec> nabla_b;
  std::vector<LinAlg::Mat> nabla_w;
  std::vector<LinAlg::Vec> delta_nabla_b;
  std::vector<LinAlg::Mat> delta_nabla_w;
};

} // namespace NN

#endif

