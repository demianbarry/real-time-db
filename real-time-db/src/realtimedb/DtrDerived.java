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
        setLowerLimitInterval(-1);
        setUpperLimitInterval(-1);
        for (Iterator it=readSet.iterator(); it.hasNext(); ) {
            Dtr<T> element = (Dtr<T>) it.next();
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
