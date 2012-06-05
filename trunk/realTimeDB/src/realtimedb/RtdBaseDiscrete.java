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

    public RtdBaseDiscrete(T maxDataError) {
        setMaxDataError(maxDataError);
    }
    
    public RtdBaseDiscrete(T maxDataError, T minValid, T maxValid) {
        setMaxDataError(maxDataError);
        setMinValid(minValid);
        setMaxValid(maxValid);
    }
    
    

    @Override
    public void setData(T data) throws RtdException {
        super.setData(data);
        setValidityIntervalUpperBound(Long.MAX_VALUE);
    }
}
