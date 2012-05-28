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

    @Override
    public void setValue(T value) throws RtdException {
        this.value = value;
        // side effect: refresh de limit interval with min and max intervals of data set
        refreshLimitInterval();
    }
    
    public void addRtdReadSet(Rtd<T> data) {
        readSet.add(data);
    }

    
    protected void refreshLimitInterval(){
        setVILowerBound(-1);
        setVIUpperBound(-1);
        for (Iterator it=readSet.iterator(); it.hasNext(); ) {
            Rtd<T> element = (Rtd<T>) it.next();
            if (getVILowerBound() == -1 || getVIUpperBound() == -1) {
                setVILowerBound(element.getVILowerBound());
                setVIUpperBound(element.getVIUpperBound());
            } else {
                if (getVILowerBound() < element.getVILowerBound()) {
                    setVILowerBound(element.getVILowerBound());
                }
                // if discrete don't set upper limit
                // TODO revisar con charly el concepto de discreto respecto del upper limit
                if (getVIUpperBound() > element.getVIUpperBound()){
                    setVIUpperBound(element.getVIUpperBound());
                }
            }
        }
    }
}
