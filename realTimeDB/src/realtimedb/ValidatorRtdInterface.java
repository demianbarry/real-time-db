/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
interface ValidatorRtdInterface<T> {
    
    public boolean validRtdMaxDataError(T compareFrom, T compareTo); // return true if realy data change
    public boolean validRtdDataValidRage(T minValid, T maxValid); // return true if data is valid
    
}
