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
public class DtrDerived<T extends Comparable<T>> extends DtrBase<T> {
    private Collection<Dtr<T>> readSet;

    public DtrDerived() {
        this.readSet = new ArrayList<Dtr<T>>();
    }
    
    public void addDTRReadSet(Dtr<T> data) {
        readSet.add(data);
    }

    @Override
    public void setData(T data) throws DtrException {
        super.setData(data);
        // side effect: refresh de limit interval with min and max intervals of data set
        refreshLimitInterval();
    }
    
    protected void refreshLimitInterval(){
        setValidityIntervalLowerBound(-1);
        setValidityIntervalUpperBound(-1);
        for (Iterator it=readSet.iterator(); it.hasNext(); ) {
            Dtr<T> element = (Dtr<T>) it.next();
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
