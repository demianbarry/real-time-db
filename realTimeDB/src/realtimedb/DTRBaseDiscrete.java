/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
public class DTRBaseDiscrete<T extends Comparable<T>> extends DtrBase<T> {

    public DTRBaseDiscrete() {
        
    }

    @Override
    public void setData(T data) throws DtrException {
        super.setData(data);
        setValidityIntervalUpperBound(Long.MAX_VALUE);
    }
}
