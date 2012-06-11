/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
public interface ValidatorRtdInterface<T> {
    
    public boolean validRtdMaxDataError(T compareFrom, T compareTo, T deltaToCompare); // return true if realy data change
    public boolean validRtdDataValidRage(T minValid, T maxValid, T dataToCompare); // return true if data is valid
    
}
