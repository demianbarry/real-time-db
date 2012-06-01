/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author demian
 */
abstract public class RtdBase<T> extends Rtd<T>  {
    private T maxDataError = null;
    private T minValid = null;
    private T maxValid = null;

    protected  T getMaxDataError() {
        return maxDataError;
    }

    protected  void setMaxDataError(T maxDataError) {
        this.maxDataError = maxDataError;
    }

    protected  T getMaxValid() {
        return maxValid;
    }

    protected  void setMaxValid(T maxValid) {
        this.maxValid = maxValid;
    }

    protected  T getMinValid() {
        return minValid;
    }

    protected  void setMinValid(T minValid) {
        this.minValid = minValid;
    }

    @Override
    public void setData(T data) throws RtdException {
        // Check acceptable values
        // min and max defined! Only if T is Comparable
        if (isComparable(data)) {
            if (minValid != null && maxValid != null) {
                // Asign to Comparable type
                Comparable minValidComparable = (Comparable) minValid;
                Comparable maxValidComparable = (Comparable) maxValid;
                Comparable dataComparable = (Comparable) data;
                // data berween min and max values only if defined
                if (!(minValidComparable.compareTo(dataComparable) <= 0 && dataComparable.compareTo(maxValidComparable) <= 0)) {
                    throw new NotValidRtdData("Data is not valid in range.");
                }
            }
        }
        // data change?
        // Only chage if diference value means change. Only if data is Number
        this.data = newDataIfGTMaxDataError(data);
        setValidityIntervalLowerBound(System.currentTimeMillis());
    }

    private boolean isComparable(T data) {
        Type[] intfs = data.getClass().getGenericInterfaces();
        if (intfs.length != 0) {
            for (Type intf : intfs) {
                if (intf.getClass().getName().equalsIgnoreCase("comparable")) {
                    return true;
                }
            }
        }
        return false;
    }

    private T newDataIfGTMaxDataError(T data) {
      
        // if data is subclass of Number and maxDataError is defined and is not first time
        if (data.getClass().getGenericSuperclass() == Number.class && maxDataError != null && this.data != null) {
            if (data instanceof Integer) {
                if (Math.abs(((Integer) data).intValue() - ((Integer) this.data).intValue()) <= ((Integer) maxDataError).intValue()) {
                    // discard if the variation between the data registered value and the new value is not sufficiently significant.
                    return this.data;
                }
            } else if (data instanceof BigDecimal) {
                if (Math.abs(((BigDecimal) data).subtract((BigDecimal) this.data).doubleValue()) <= ((BigDecimal) maxDataError).doubleValue()) {
                    // discard if the variation between the data registered value and the new value is not sufficiently significant.
                    return this.data;
                }
            } else if (data instanceof BigInteger) {
                if (Math.abs(((BigInteger) data).subtract((BigInteger) this.data).longValue()) <= ((BigInteger) maxDataError).longValue()) {
                    // discard if the variation between the data registered value and the new value is not sufficiently significant.
                    return this.data;
                }
            } else if (data instanceof Double) {
                if (Math.abs(((Double) data).doubleValue() - ((Double) this.data).doubleValue()) <= ((Double) maxDataError).doubleValue()) {
                    // discard if the variation between the data registered value and the new value is not sufficiently significant.
                    return this.data;
                }
            } else if (data instanceof Float) {
                if (Math.abs(((Float) data).floatValue() - ((Float) this.data).floatValue()) <= ((Float) maxDataError).floatValue()) {
                    // discard if the variation between the data registered value and the new value is not sufficiently significant.
                    return this.data;
                }
            } else if (data instanceof Byte) {
                if (Math.abs(((Byte) data).byteValue() - ((Byte) this.data).byteValue()) <= ((Byte) maxDataError).byteValue()) {
                    // discard if the variation between the data registered value and the new value is not sufficiently significant.
                    return this.data;
                }
            } else if (data instanceof Long) {
                if (Math.abs(((Long) data).longValue() - ((Long) this.data).longValue()) <= ((Long) maxDataError).longValue()) {
                    // discard if the variation between the data registered value and the new value is not sufficiently significant.
                    return this.data;
                }
            } else if (data instanceof Short) {
                if (Math.abs(((Short) data).shortValue() - ((Short) this.data).shortValue()) <= ((Short) maxDataError).shortValue()) {
                    // discard if the variation between the data registered value and the new value is not sufficiently significant.
                    return this.data;
                }
            } else if (data instanceof AtomicInteger) {
                if (Math.abs(((AtomicInteger) data).intValue() - ((AtomicInteger) this.data).intValue()) <= ((AtomicInteger) maxDataError).intValue()) {
                    // discard if the variation between the data registered value and the new value is not sufficiently significant.
                    return this.data;
                }
            } else if (data instanceof AtomicLong) {
                if (Math.abs(((AtomicLong) data).longValue() - ((AtomicLong) this.data).longValue()) <= ((AtomicLong) maxDataError).longValue()) {
                    // discard if the variation between the data registered value and the new value is not sufficiently significant.
                    return this.data;
                }
            }
        }
        return data;
    }
}
