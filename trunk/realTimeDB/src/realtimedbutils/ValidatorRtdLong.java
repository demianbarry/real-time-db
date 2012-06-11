package realtimedbutils;

import realtimedb.ValidatorRtdInterface;

/**
 *
 * @author demian
 */
public class ValidatorRtdLong  implements ValidatorRtdInterface<Long> {

    public ValidatorRtdLong() {
    }

    @Override
    public boolean validRtdDataValidRage(Long minValid, Long maxValid, Long dataToCompare) {
        if (dataToCompare.longValue() < minValid.longValue() || dataToCompare.longValue() > maxValid.longValue()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean validRtdMaxDataError(Long compareFrom, Long compareTo, Long deltaToCompare) {
        if (!(Math.abs(compareFrom.longValue() - compareTo.longValue()) > deltaToCompare.longValue())) {
            return false;
        }
        return true;
    }
}
