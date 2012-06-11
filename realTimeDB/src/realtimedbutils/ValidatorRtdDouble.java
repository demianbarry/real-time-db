/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedbutils;

import realtimedb.ValidatorRtdInterface;

/**
 *
 * @author demian
 */
public class ValidatorRtdDouble  implements ValidatorRtdInterface<Double> {

    public ValidatorRtdDouble() {
    }

    @Override
    public boolean validRtdDataValidRage(Double minValid, Double maxValid, Double dataToCompare) {
        if (dataToCompare.doubleValue() < minValid.doubleValue() || dataToCompare.doubleValue() > maxValid.doubleValue()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean validRtdMaxDataError(Double compareFrom, Double compareTo, Double deltaToCompare) {
        if (!(Math.abs(compareFrom.doubleValue() - compareTo.doubleValue()) > deltaToCompare.doubleValue())) {
            return false;
        }
        return true;
    }
}
