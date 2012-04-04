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
public class RtdDerived<T extends Comparable<T>> extends Rtd<T> {
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
        setLowerLimitInterval(-1);
        setUpperLimitInterval(-1);
        for (Iterator it=readSet.iterator(); it.hasNext(); ) {
            Rtd<T> element = (Rtd<T>) it.next();
            if (getLowerLimitInterval() == -1 || getUpperLimitInterval() == -1) {
                setLowerLimitInterval(element.getLowerLimitInterval());
                setUpperLimitInterval(element.getUpperLimitInterval());
            } else {
                if (getLowerLimitInterval() < element.getLowerLimitInterval()) {
                    setLowerLimitInterval(element.getLowerLimitInterval());
                }
                // if discrete don't set upper limit
                // TODO revisar con charly el concepto de discreto respecto del upper limit
                if (getUpperLimitInterval() > element.getUpperLimitInterval()){
                    setUpperLimitInterval(element.getUpperLimitInterval());
                }
            }
        }
    }
}
