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

    @Override
    public void setData(T data) throws RtdException {
        // Check acceptable values
        // min and max defined!
        if (minValid != null && maxValid != null) {
            // data berween min and max values only if defined
            if (!(minValid.compareTo(data) <= 0 && data.compareTo(maxValid) <= 0)) {
                throw new RtdNotValidData("Data is not valid in range.");
            }
        }
        // data change?
        // TODO queda pendiente resolver la comparación del abs en el genérico
        // Una solución es preguntar si es clase Number y castear
        if (true) {
            this.data = data;
            // side effect: set the lower limit of data interval with now
            setLowerLimitInterval(System.currentTimeMillis());
        } else {
            throw new RtdDataPrecisionError("Data hava a precisionError.");
        }
    }
}
