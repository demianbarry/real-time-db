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
public class ValidatorRtdInteger implements ValidatorRtdInterface<Integer> {

    public ValidatorRtdInteger() {
    }

    @Override
    public boolean validRtdDataValidRage(Integer minValid, Integer maxValid, Integer dataToCompare) {
        if (dataToCompare.intValue() < minValid.intValue() || dataToCompare.intValue() > maxValid.intValue()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean validRtdMaxDataError(Integer compareFrom, Integer compareTo, Integer deltaToCompare) {
        if (!(Math.abs(compareFrom.intValue() - compareTo.intValue()) > deltaToCompare.intValue())) {
            return false;
        }
        return true;
    }
}
