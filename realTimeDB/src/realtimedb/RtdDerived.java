/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author demian
 */
public class RtdDerived<T> extends Rtd<T> {
    private Collection<Rtd<T>> readSet;

    public RtdDerived() {
        this.readSet = new ArrayList<Rtd<T>>();
    }
    
    public void addRtdReadSet(Rtd<T> data) {
        readSet.add(data);
    }

    @Override
    public void setData(T data) throws RtdException {
        this.data = data;
        // side effect: refresh de limit interval with min and max intervals of data set
        refreshLimitInterval();
    }
    
    protected void refreshLimitInterval(){
        setValidityIntervalLowerBound(-1);
        setValidityIntervalUpperBound(-1);
        for (Iterator it=readSet.iterator(); it.hasNext(); ) {
            Rtd<T> element = (Rtd<T>) it.next();
            if (getValidityIntervalLowerBound() == -1 || getValidityIntervalUpperBound() == -1) {
                setValidityIntervalLowerBound(element.getValidityIntervalLowerBound());
                setValidityIntervalUpperBound(element.getValidityIntervalUpperBound());
            } else {
                if (getValidityIntervalLowerBound() < element.getValidityIntervalLowerBound()) {
                    setValidityIntervalLowerBound(element.getValidityIntervalLowerBound());
                }
                // if discrete don't set upper limit
                // TODO revisar con charly el concepto de discreto respecto del upper limit
                if (getValidityIntervalUpperBound() > element.getValidityIntervalUpperBound()){
                    setValidityIntervalUpperBound(element.getValidityIntervalUpperBound());
                }
            }
        }
    }
}
