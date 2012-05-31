/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
 abstract public class Dtr<T> {
     // TODO evaluar la exclusión mutua en el set y get de data;
     protected  T data;
     private long validityIntervalLowerBound = -1;
     private long validityIntervalUpperBound = -1;

    public T getData() throws DtrException{
        // valid data? check temporal restriction
        long now = System.currentTimeMillis();
        if (validityIntervalLowerBound <= now && now <= validityIntervalUpperBound) {
            return data;
        } else {
            throw new DtrDataHasExpired("Dtr Data has expired within lower and upper bound limits.");
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

    abstract public void setData(T data) throws DtrException ;
}
