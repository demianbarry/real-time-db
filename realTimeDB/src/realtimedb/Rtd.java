/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
 abstract public class Rtd<T> {
     // TODO evaluar la exclusión mutua en el set y get de data;
     protected  T data;
     private long validityIntervalLowerBound = -1;
     private long validityIntervalUpperBound = -1;

    public T getData() throws RtdException{
        // valid data? check temporal restriction
        if (validityIntervalLowerBound <= System.currentTimeMillis() && System.currentTimeMillis() <= validityIntervalUpperBound) {
            return data;
        } else {
            throw new RtdDataHasExpired("Rtd Data has expired within lower and upper bound limits.");
        }
    }
    
    protected  long getValidityIntervalLowerBound(){
        return validityIntervalLowerBound;
    }
    protected  long getValidityIntervalUpperBound(){
        return validityIntervalUpperBound;
    }

    protected  void setValidityIntervalLowerBound(long validityIntervalLowerBound) {
        this.validityIntervalLowerBound = validityIntervalLowerBound;
    }
    
    protected  void setValidityIntervalUpperBound(long validityIntervalUpperBound) {
        this.validityIntervalUpperBound = validityIntervalUpperBound;
    }

    abstract public void setData(T data) throws RtdException ;
}
