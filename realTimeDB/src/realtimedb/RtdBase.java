/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
abstract public class RtdBase<T> extends Rtd<T>  {
    private T maxDataError = null;
    private T minValid = null;
    private T maxValid = null;
    private ValidatorRtdInterface validatorRtd = null;

    protected  T getMaxDataError() {
        return maxDataError;
    }

    protected  void setMaxDataError(T maxDataError) {
        this.maxDataError = maxDataError;
    }

    protected  T getMaxValid() {
        return maxValid;
    }

    protected  void setMaxValid(T maxValid) {
        this.maxValid = maxValid;
    }

    protected  T getMinValid() {
        return minValid;
    }

    protected  void setMinValid(T minValid) {
        this.minValid = minValid;
    }

    public void setValidatorRtd(ValidatorRtdInterface validatorRtd) {
        this.validatorRtd = validatorRtd;
    }    

    @Override
    public void setData(T data) throws RtdException {
        // Check acceptable values
        // min and max and data rate change! Only if has validatorRtdDataInterface defined
        if (validatorRtd != null) {
            // data berween min and max values only if defined
            if (minValid != null && maxValid != null) {
                if (!validatorRtd.validRtdDataValidRage(minValid, maxValid, data)) {
                    throw new NotValidRtdData("Data is not valid in range.");
                }
            }
            
            // data change?
            // Only chage if diference value means change and not first set.
            if (maxDataError != null && this.data != null) {
                if (validatorRtd.validRtdMaxDataError(this.data, data, maxDataError)) {
                    this.data = data;
                }
            } else {
                // maxDataError not set: set vale anyway
                this.data = data;
            }
            
            // set new valid range time
             setValidityIntervalLowerBound(System.currentTimeMillis());
        } else {
            // validator not set: set value anyway
            this.data = data;
        }
        
    }

}
