/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
abstract public class RtdBase<T extends Comparable<T>> extends Rtd<T>  {
    private T maxDataError;
    private T minValid = null;
    private T maxValid = null;

    @Override
    public void setValue(T value) throws RtdException {
        // Check acceptable values
        // min and max defined!
        if (minValid != null && maxValid != null) {
            // value berween min and max values only if defined
            if (!(minValid.compareTo(value) <= 0 && value.compareTo(maxValid) <= 0)) {
                throw new RtdNotValidData("Value is not valid in range.");
            }
        }
        // value change?
        // TODO queda pendiente resolver la comparación del abs en el genérico
        // Una solución es preguntar si es clase Number y castear
        if (true) {
            this.value = value;
            // side effect: set the lower limit of data interval with now
            setVILowerBound(System.currentTimeMillis());
        } else {
            throw new RtdDataPrecisionError("Value hava a precisionError.");
        }
    }
    
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

   
}
