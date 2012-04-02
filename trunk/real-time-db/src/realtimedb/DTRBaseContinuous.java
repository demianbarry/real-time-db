/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
public class DTRBaseContinuous<T extends Comparable<T>> extends DtrBase<T> {
    private long maximumAge;
    private long period;

    public DTRBaseContinuous(long maximumAge, T maxDataError) {
        this.maximumAge = maximumAge;
        setMaxDataError(maxDataError);
        this.period = maximumAge / 2;
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

    @Override
    public void setData(T data) throws DtrException {
        super.setData(data);
        // side effect: set upper limit of the data interval with the lower interval plus the permited maximun age
        setUpperLimitInterval(getLowerLimitInterval() + maximumAge);
    }
}
