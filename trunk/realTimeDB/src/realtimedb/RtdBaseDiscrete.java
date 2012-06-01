/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
public class RtdBaseDiscrete<T> extends RtdBase<T> {

    public RtdBaseDiscrete() {
        
    }

    @Override
    public void setData(T data) throws RtdException {
        super.setData(data);
        setValidityIntervalUpperBound(Long.MAX_VALUE);
    }
}
