/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
 abstract public class Dtr<T extends Comparable<T>> {
     // TODO evaluar la exclusión mutua en el set y get de data;
     protected  T data;
     private long lowerLimitInterval = -1;
     private long upperLimitInterval = -1;

    public T getData() throws DtrException{
        // valid data? check temporal restriction
        long now = System.currentTimeMillis();
        if (lowerLimitInterval <= now && now <= upperLimitInterval) {
            return data;
        } else {
            throw new DtrDataHasExpired("Dtr Data has expired within lower and upper bound limits.");
        }
    }
    
    protected  long getLowerLimitInterval(){
        return lowerLimitInterval;
    }
    protected  long getUpperLimitInterval(){
        return upperLimitInterval;
    }

    protected  void setLowerLimitInterval(long lowerLimitInterval) {
        this.lowerLimitInterval = lowerLimitInterval;
    }
    
    protected  void setUpperLimitInterval(long upperLimitInterval) {
        this.upperLimitInterval = upperLimitInterval;
    }

    abstract public void setData(T data) throws DtrException ;
}
