/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
public class RtdBaseContinuous<T extends Comparable<T>> extends RtdBase<T> {
    private long maximumAge;
    private long period;

    public RtdBaseContinuous(long maximumAge) {
        this.maximumAge = maximumAge;
        setMaxDataError(null);
        this.period = maximumAge / 2;
    }
    
    public RtdBaseContinuous(long maximumAge, T maxDataError) {
        this.maximumAge = maximumAge;
        setMaxDataError(maxDataError);
        this.period = maximumAge / 2;
    }
    
    public RtdBaseContinuous(long maximumAge, T maxDataError, T minValid, T maxValid) {
        this.maximumAge = maximumAge;
        setMaxDataError(maxDataError);
        this.period = maximumAge / 2;
        setMinValid(minValid);
        setMaxValid(maxValid);
    }
    

    @Override
    public void setValue(T value) throws RtdException {
        super.setValue(value);
        // side effect: set upper limit of the data interval with the lower interval plus the permited maximun age
        setVIUpperBound(getVILowerBound() + maximumAge);
    }

    protected  long getDuration() {
        return maximumAge;
    }

    protected  void setDuration(long duration) {
        this.maximumAge = duration;
    }

    public  long getPeriod() {
        return period;
    }

    protected  void setPeriod(long period) {
        this.period = period;
    }
}
