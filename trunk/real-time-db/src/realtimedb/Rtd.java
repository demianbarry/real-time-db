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
     // TODO evaluar la exclusión mutua en el set y get de value;
     protected  T value;
     private long VILowerBound = -1;
     private long VIUpperBound = -1;

    public T getValue() throws RtdException{
        // valid value? check temporal restriction
        long now = System.currentTimeMillis();
        if (VILowerBound <= now && now <= VIUpperBound) {
            return value;
        } else {
            throw new RtdOutOfTemporalBounds("Rtd is Out of Temporal Bound limits");
        }
    }

    abstract public void setValue(T value) throws RtdException ;
    
    protected  long getVILowerBound(){
        return VILowerBound;
    }
    protected  long getVIUpperBound(){
        return VIUpperBound;
    }

    protected  void setVILowerBound(long VILowerBound) {
        this.VILowerBound = VILowerBound;
    }
    
    protected  void setVIUpperBound(long VIUpperBound) {
        this.VIUpperBound = VIUpperBound;
    }

}
