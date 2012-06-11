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
// Class to implement valid Float
public class ValidatorRtdFloat implements ValidatorRtdInterface<Float> {

    public ValidatorRtdFloat() {
    }

    @Override
    public boolean validRtdDataValidRage(Float minValid, Float maxValid, Float dataToCompare) {
        if (dataToCompare.floatValue() < minValid.floatValue() || dataToCompare.floatValue() > maxValid.floatValue()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean validRtdMaxDataError(Float compareFrom, Float compareTo, Float deltaToCompare) {
        if (!(Math.abs(compareFrom.floatValue() - compareTo.floatValue()) > deltaToCompare.floatValue())) {
            return false;
        }
        return true;
    }
}
