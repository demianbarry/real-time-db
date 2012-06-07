/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author demian
 */
public class RtdDerived<T> extends Rtd<T> {
    private HashMap<String,Rtd<T>> readSet;
    private CalcRtdDerivationInterface derivation = null;

    public RtdDerived() {
        this.readSet = new HashMap<String, Rtd<T>>();
    }

    public RtdDerived(CalcRtdDerivationInterface derivation) {
        this.derivation = derivation;
        this.readSet = new HashMap<String, Rtd<T>>();
    }
    
    public void addRtdReadSet(String nameKey, Rtd<T> data) {
        readSet.put(nameKey,data);
    }

    @Override
    public void setData(T data) throws RtdException {
        this.data = data;
    }

    public HashMap<String, Rtd<T>> getReadSet() {
        return readSet;
    }

    @Override
    public T getData() throws RtdException {
        refreshLimitInterval();
        return super.getData();
    }
    
    public void calcDerivation() throws RtdException {
        //Method for calculation of derivation
        long now = System.currentTimeMillis();

        //Validate Temporal Consistency prior to calc
        //In this case, substract WorstCaseExecutionTime of calc method from DataDeadline
        refreshLimitInterval();

        // calculate derivation. Only if has derivation with calcRtdDerivationInterface defined
        if (derivation != null) {
            if (this.getValidityIntervalUpperBound() - this.derivation.getWorstCaseExecutionTime() <= now) {
                throw new RtdDataHasExpired("Rtd Derivation it's not possible prior to DataDeadline.");
            }

            // If OK then calc 
            this.derivation.calc(this);
        }
    }

    
    protected void refreshLimitInterval(){
        setValidityIntervalLowerBound(-1);
        setValidityIntervalUpperBound(-1);
        for (Iterator it=readSet.values().iterator(); it.hasNext(); ) {
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
