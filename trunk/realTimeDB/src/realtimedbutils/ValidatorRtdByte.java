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
// Class to implement valid Byte
public class ValidatorRtdByte implements ValidatorRtdInterface<Byte> {

    public ValidatorRtdByte() {
    }

    @Override
    public boolean validRtdDataValidRage(Byte minValid, Byte maxValid, Byte dataToCompare) {
        if (dataToCompare.byteValue() < minValid.byteValue() || dataToCompare.byteValue() > maxValid.byteValue()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean validRtdMaxDataError(Byte compareFrom, Byte compareTo, Byte deltaToCompare) {
        if (!(Math.abs(compareFrom.byteValue() - compareTo.byteValue()) > deltaToCompare.byteValue())) {
            return false;
        }
        return true;
    }
}
