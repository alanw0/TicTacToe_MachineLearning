#ifndef _Rand_hpp_
#define _Rand_hpp_

#include <random>
#include <cmath>

namespace utils {

class Rand {
public:
  virtual ~Rand(){}

  virtual float nextRandomValue() = 0;
};

class RandomGaussian : public Rand {
public:
  RandomGaussian(float mean, float stddev)
  : gen(1911), normal_dist(mean, stddev)
  {}
  virtual ~RandomGaussian() {}
  
  virtual float nextRandomValue() override { return normal_dist(gen); }

private:
  std::mt19937 gen;
  std::normal_distribution<float> normal_dist;
};

class OnesForTesting : public Rand {
public:
  OnesForTesting() {}
  virtual ~OnesForTesting(){}

  virtual float nextRandomValue() override { return 1.0; }
};

}//namespace utils

#endif

