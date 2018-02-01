#ifndef _LinAlg_hpp_
#define _LinAlg_hpp_

#include "Rand.hpp"

#include <vector>
#include <cmath>

namespace LinAlg {

typedef std::vector<float> Vec;

struct VVec
{
  VVec() : m_ptr(nullptr), m_offset(0), m_len(0)
  {}

  VVec(float* ptr, size_t offset, size_t len)
  : m_ptr(ptr), m_offset(offset), m_len(len)
  {}

  ~VVec(){}

  VVec& operator=(VVec& src)
  {
    m_ptr = src.m_ptr;
    m_offset = src.m_offset;
    m_len = src.m_len;
    return *this;
  }

  void initialize(float* ptr, size_t offset, size_t len)
  {
    m_ptr = ptr;
    m_offset = offset;
    m_len = len;
  }

  size_t size() const { return m_len; }

  float& operator[](size_t idx) { return m_ptr[m_offset+idx]; }
  const float& operator[](size_t idx) const { return m_ptr[m_offset+idx]; }

  float* begin() { return m_ptr+m_offset; }
  const float* begin() const { return m_ptr+m_offset; }

  float* end() { return m_ptr+m_offset+m_len; }
  const float* end() const { return m_ptr+m_offset+m_len; }

private:
  float* m_ptr;
  size_t m_offset;
  size_t m_len;
};

struct Mat
{
  Mat()
  : vals(), nrows(0), ncols(0)
  {}

  Mat(unsigned nrows_, unsigned ncols_)
  : vals(nrows_*ncols_, 0.0), nrows(nrows_), ncols(ncols_)
  {}

  void initialize(unsigned nrows_, unsigned ncols_)
  {
    vals.assign(nrows_*ncols_, 0.0);
    nrows = nrows_;
    ncols = ncols_;
  }

  float& operator()(unsigned r, unsigned c) { return vals[idx(r,c)]; }
  const float& operator()(unsigned r, unsigned c) const { return vals[idx(r,c)]; }

  unsigned idx(unsigned r, unsigned c) const { return r*ncols + c; }

  std::vector<float> vals;
  unsigned nrows;
  unsigned ncols;
};

template<typename VecType>
inline void fill_vec(VecType& v, float val)
{
  std::fill(v.begin(), v.end(), val);
}

inline void fill_mat(Mat& M, float val)
{
  std::fill(M.vals.begin(), M.vals.end(), val);
}

template<typename VecType1, typename VecType2>
inline void copy_vec(const VecType1& x, VecType2& result)
{
  for(size_t i=0; i<x.size(); ++i) {
    result[i] = x[i];
  }
}

template<typename VecType1, typename VecType2>
inline void vec_plus_vec(const VecType1& x, VecType2& result)
{
  for(size_t i=0; i<x.size(); ++i) {
    result[i] += x[i];
  }
}

template<typename VecType1, typename VecType2>
inline void scaled_vec_plus_vec(float scaleFactor, const VecType1& x, VecType2& result)
{
  for(size_t i=0; i<x.size(); ++i) {
    result[i] += scaleFactor*x[i];
  }
}

template<typename VecType1, typename VecType2>
inline void hadamard_product(const VecType1& v1, const VecType1& v2, VecType2& result)
{
    for(unsigned i=0; i<v1.size(); ++i) {
        result[i] = v1[i]*v2[i];
    }
}

template<typename VecType1, typename VecType2>
inline void sigmoid(const VecType1& input, VecType2& output)
{
    for(unsigned i=0; i<input.size(); ++i) {
        output[i] = 1.0/(1.0 + std::exp(-input[i]));
    }
}

template<typename VecType1, typename VecType2>
inline void sigmoid_prime(const VecType1& input, VecType2& output)
{
    sigmoid(input, output);
    for(unsigned i=0; i<input.size(); ++i) {
        output[i] = output[i]*(1.0-output[i]);
    }
}

template<typename VecType1, typename VecType2, typename VecType3>
inline void vec_difference(const VecType1& v1, const VecType2& v2, VecType3& result)
{
    for(unsigned i=0; i<v1.size(); ++i) {
        result[i] = v1[i] - v2[i];
    }
}

template<typename VecType1, typename VecType2>
inline void vec_vec_trans(const VecType1& v1, const VecType2& v2, Mat& result)
{
    int idx = 0;
    for(unsigned r=0; r<v1.size(); ++r) {
        for(unsigned c=0; c<v2.size(); ++c) {
            result.vals[idx++] = v1[r]*v2[c];
        }
    }
}

inline void mat_plus_mat(const Mat& M, Mat& result)
{
  vec_plus_vec(M.vals, result.vals);
}

inline void scaled_mat_plus_mat(float scaleFactor, const Mat& M, Mat& result)
{
  scaled_vec_plus_vec(scaleFactor, M.vals, result.vals);
}

template<typename VecType>
inline float mat_row_dot(const Mat& M, int& idx, const VecType& x)
{
    float tmp = 0.0;
    for(int c=0; c<M.ncols; ++c) {
        tmp += M.vals[idx++] * x[c];
    }
    return tmp;
}

template<typename VecType1, typename VecType2, typename VecType3>
inline void mat_vec_plus_vec(const Mat& M, const VecType1& x, const VecType2& b, VecType3& result)
{
    int idx = 0;
    for(int r=0; r<M.nrows; ++r) {
        result[r] = mat_row_dot(M, idx, x) + b[r];
    }
}

template<typename VecType>
inline float mat_col_dot(const Mat& M, int col, const VecType& x)
{
    float tmp = 0.0;
    for(unsigned r=0; r<M.nrows; ++r) {
        tmp += M(r,col)*x[r];
    }
    return tmp;
}

template<typename VecType1, typename VecType2>
inline void mat_trans_vec(const Mat& M, const VecType1& x, VecType2& result)
{
    for(unsigned c=0; c<M.ncols; ++c) {
        result[c] = mat_col_dot(M, c, x);
    }
}

template<typename VecType1, typename VecType2>
inline void mat_trans_vec_plus_vec(const Mat& M, const VecType1& x, const VecType1& b, VecType2& result)
{
    for(unsigned c=0; c<M.ncols; ++c) {
        result[c] = mat_col_dot(M, c, x) + b[c];
    }
}

inline void duplicate_vecs_sizes(const std::vector<Vec>& vecs, std::vector<Vec>& output)
{
  output.resize(vecs.size());
  for(unsigned i=0; i<vecs.size(); ++i) {
    output[i].assign(vecs[i].size(), 0.0);
  }
}

inline void duplicate_mats_sizes(const std::vector<Mat>& mats, std::vector<Mat>& output)
{
  output.resize(mats.size());
  for(unsigned i=0; i<mats.size(); ++i) {
    output[i].initialize(mats[i].nrows, mats[i].ncols);
  }
}

inline void initialize_vecs(std::vector<Vec>& vecs, utils::Rand* random)
{
  for(unsigned i=0; i<vecs.size(); ++i) {
    for(unsigned j=0; j<vecs[i].size(); ++j) {
      vecs[i][j] = random->nextRandomValue();
    }
  }
}

inline void initialize_mats(std::vector<Mat>& mats, utils::Rand* random)
{
  for(unsigned i=0; i<mats.size(); ++i) {
    for(unsigned j=0; j<mats[i].vals.size(); ++j) {
      mats[i].vals[j] = random->nextRandomValue();
    }
  }
}

} // namespace LinAlg

#endif

