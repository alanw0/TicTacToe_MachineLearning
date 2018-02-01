#ifndef _DigitData_hpp_
#define _DigitData_hpp_

#include "LinAlg.hpp"

namespace NN {

struct DigitData {
  DigitData(unsigned NumDigits, unsigned numPixelsPerDigit,
            unsigned resultVecLenPerDigit,
            float* pixelData, int* digits, float* resultVecData)
   : m_pixelData(pixelData), m_digits(digits), m_resultVecData(resultVecData),
     m_digitPixelVecs(NumDigits), m_resultVecs(NumDigits)
  {
     for(unsigned i=0; i<NumDigits; ++i) {
       m_digitPixelVecs[i].initialize(m_pixelData, i*numPixelsPerDigit, numPixelsPerDigit);
       m_resultVecs[i].initialize(m_resultVecData, i*resultVecLenPerDigit, resultVecLenPerDigit);
     }
  }

  ~DigitData(){}

  unsigned numDigits() const { return m_digitPixelVecs.size(); }
  int digit(unsigned i) const { return m_digits[i]; }
  LinAlg::VVec& digitPixelVec(unsigned i) { return m_digitPixelVecs[i]; }
  LinAlg::VVec& resultVec(unsigned i) { return m_resultVecs[i]; }

  const std::vector<LinAlg::VVec>& digitPixelVecs() const
  { return m_digitPixelVecs; }
  const std::vector<LinAlg::VVec>& resultVecs() const
  { return m_resultVecs; }

private:
  float* m_pixelData;
  int* m_digits;
  float* m_resultVecData;
  std::vector<LinAlg::VVec> m_digitPixelVecs;
  std::vector<LinAlg::VVec> m_resultVecs;
};

} // namespace NN

#endif

