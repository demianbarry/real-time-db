/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
 abstract public class Rtd<T extends Comparable<T>> {
     // TODO evaluar la exclusión mutua en el set y get de data;
     protected  T data;
     private long lowerLimitInterval = -1;
     private long upperLimitInterval = -1;

    public T getData() throws RtdException{
        // valid data? check temporal restriction
        long now = System.currentTimeMillis();
        if (lowerLimitInterval <= now && now <= upperLimitInterval) {
            return data;
        } else {
            throw new RtdOutOfTemporalBounds("Rtd is Out of Temporal Bound limits");
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

    abstract public void setData(T data) throws RtdException ;
}
