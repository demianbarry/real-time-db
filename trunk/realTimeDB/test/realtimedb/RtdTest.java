/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import realtimedbutils.ValidatorRtdByte;
import realtimedbutils.ValidatorRtdFloat;

/**
 *
 * @author demian
 */
public class RtdTest {
    
    static protected RtdBaseContinuous<Float> cpuWorkLoad;
    static protected long maxAgeCpuWorkLoad = 4000; // 4 secs of maximun adge
    
    static protected RtdBaseContinuous<Float> memWorkLoad;
    static protected long maxAgeMemWorkLoad = 6000; // 6 secs of maximun adge
    
    static protected RtdBaseDiscrete<Float> loadTrend;
    
    static protected RtdDerived<Float> measureVal;
    
    static protected RtdBaseDiscrete<Byte> testByteDiscrete;
    
    // class to implement derivation calculus of 
    private static class MeasureValCalc extends AbstractCalcRtdDerivation {

        public MeasureValCalc(long worstCaseExecutionTime) {
            setWorstCaseExecutionTime(worstCaseExecutionTime);
        }

        @Override
        public void calc(RtdDerived d) {
            try {
                // retrieve each value stored in RtdDerived readSet
                Float cpuWorkLoadToDerive = (Float) ((Rtd)(d.getReadSet().get("cpuWorkLoad"))).getData();
                Float memWorkLoadToDerive = (Float) ((Rtd)(d.getReadSet().get("memWorkLoad"))).getData();
                Float loadTrendToDerive = (Float) ((Rtd)(d.getReadSet().get("loadTrend"))).getData();
                try {
                    // calc derived value and store it
                    d.setData(new Float(cpuWorkLoadToDerive*0.5 + memWorkLoadToDerive*0.3 + loadTrendToDerive*0.2));
                } catch (RtdException ex) {
                    Logger.getLogger(RtdTest.class.getName()).log(Level.SEVERE, null, ex);
                }
               
            } catch (RtdException ex) {
                Logger.getLogger(RtdTest.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        }

    }

    public RtdTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Create all Rtd objects. Asign objetcs to deriver Rtd.
        cpuWorkLoad = new RtdBaseContinuous<Float>(maxAgeCpuWorkLoad);
        cpuWorkLoad.setValidatorRtd((ValidatorRtdInterface) new ValidatorRtdFloat());
        
        memWorkLoad = new RtdBaseContinuous<Float>(maxAgeMemWorkLoad);
        memWorkLoad.setValidatorRtd((ValidatorRtdInterface) new ValidatorRtdFloat());

        loadTrend = new RtdBaseDiscrete<Float>();
        loadTrend.setValidatorRtd((ValidatorRtdInterface) new ValidatorRtdFloat());
        
        measureVal = new RtdDerived<Float>((AbstractCalcRtdDerivation) new MeasureValCalc(Long.valueOf(5)));
        measureVal.addRtdReadSet("cpuWorkLoad",cpuWorkLoad);
        measureVal.addRtdReadSet("memWorkLoad",memWorkLoad);
        measureVal.addRtdReadSet("loadTrend",loadTrend);
        
        testByteDiscrete = new RtdBaseDiscrete<Byte>();
        testByteDiscrete.setValidatorRtd((ValidatorRtdInterface) new ValidatorRtdByte());
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of application with Rtd data having temporal restrictions.
     */
    @Test
    public void testValidDataRtd() throws InterruptedException {
        // validate data renge of cpuWorkLoad
        cpuWorkLoad.setMinValid(new Float(30));
        cpuWorkLoad.setMaxValid(new Float(50));
        try {
            cpuWorkLoad.setData(new Float(60));
            fail("Data not in valid range");
        } catch (NotValidRtdData ex) {
            assertNotNull("Exception exit for not valid data for float type", ex);
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        try {
            cpuWorkLoad.setData(new Float(40));
            assertNotNull("Pass set valid data range for float", cpuWorkLoad);
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        
        // validate data range for byte test type 
        testByteDiscrete.setMinValid(new Byte((byte) 5));
        testByteDiscrete.setMaxValid(new Byte((byte) 15));
        try {
            testByteDiscrete.setData(new Byte((byte) 25));
            fail("Data not in valid range");
        } catch (NotValidRtdData ex) {
            assertNotNull("Exception exit for not valid data for byte type", ex);
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        try {
            testByteDiscrete.setData(new Byte((byte) 5));
            assertNotNull("Pass set valid data range for byte", testByteDiscrete);
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        try {
            testByteDiscrete.setData(new Byte((byte) 15));
            assertNotNull("Pass set valid data range for byte", testByteDiscrete);
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        
        // validate maxDataError nor change but valid time
        System.out.println("*******************************");
        System.out.println("***   Test maxDataError     ***");
        System.out.println("*** set rate of change to 5 ***");
        System.out.println("*******************************");

        try {
            cpuWorkLoad.setData(new Float(30));
            assertTrue(new Float(30).equals(cpuWorkLoad.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        

        cpuWorkLoad.setMaxDataError(new Float(5.0));
        try {
            // set current time
            cpuWorkLoad.setData(new Float(36));
            System.out.println("set cpuWorkLoad data to 36");
            System.out.println("cpuWorkLoad data: " + cpuWorkLoad.getData());
            assertTrue(new Float(36).equals(cpuWorkLoad.getData()));
            System.out.println("Current time: " + System.currentTimeMillis());
            System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalLowerBound());
            System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalUpperBound());
            assertTrue(System.currentTimeMillis() <= cpuWorkLoad.getValidityIntervalUpperBound());
            System.out.println("----------------------------------------");
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }

        // Sleep 1 sec
        Thread.sleep(1000);
        
        try {
            // set current time
            cpuWorkLoad.setData(new Float(35.66));
            System.out.println("set cpuWorkLoad data to 36.66");
            System.out.println("cpuWorkLoad data: " + cpuWorkLoad.getData());
            assertTrue(new Float(36).equals(cpuWorkLoad.getData()));
            System.out.println("Current time: " + System.currentTimeMillis());
            System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalLowerBound());
            System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalUpperBound());
            assertTrue(System.currentTimeMillis() <= cpuWorkLoad.getValidityIntervalUpperBound());
            System.out.println("----------------------------------------");
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        
        // Sleep 1 sec
        Thread.sleep(1000);
        
        try {
            // set current time
            cpuWorkLoad.setData(new Float(39.99999));
            System.out.println("set cpuWorkLoad data to 39.99999");
            System.out.println("cpuWorkLoad data: " + cpuWorkLoad.getData());
            assertTrue(new Float(36).equals(cpuWorkLoad.getData()));
            System.out.println("Current time: " + System.currentTimeMillis());
            System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalLowerBound());
            System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalUpperBound());
            assertTrue(System.currentTimeMillis() <= cpuWorkLoad.getValidityIntervalUpperBound());
            System.out.println("----------------------------------------");
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }

        // Sleep 1 sec
        Thread.sleep(1000);
        
        try {
            // set current time
            cpuWorkLoad.setData(new Float(45));
            System.out.println("set cpuWorkLoad data to 45");
            System.out.println("cpuWorkLoad data: " + cpuWorkLoad.getData());
            assertTrue(new Float(45).equals(cpuWorkLoad.getData()));
            System.out.println("Current time: " + System.currentTimeMillis());
            System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalLowerBound());
            System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalUpperBound());
            assertTrue(System.currentTimeMillis() <= cpuWorkLoad.getValidityIntervalUpperBound());
            System.out.println("----------------------------------------");
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        


    }

    @Test
    public void testPrototypeRtd() throws InterruptedException {
        // initialice valida data 
        cpuWorkLoad.setMinValid(new Float(0));
        cpuWorkLoad.setMaxValid(new Float(100));
        cpuWorkLoad.setMaxDataError(new Float(5));
        memWorkLoad.setMinValid(new Float(0));
        memWorkLoad.setMaxValid(new Float(100));
        memWorkLoad.setMaxDataError(new Float(5));
        loadTrend.setMinValid(new Float(0));
        loadTrend.setMaxValid(new Float(100));
        loadTrend.setMaxDataError(new Float(5));

        // set initial time
        long initialTime = System.currentTimeMillis();
        
        // set input data
        try {
            cpuWorkLoad.setData(new Float(60));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        try {
            memWorkLoad.setData(new Float(35));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        try {
            loadTrend.setData(new Float(50));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        
        // set derived data
        try {
            measureVal.calcDerivation();
        } catch (RtdDataHasExpired ex) {
            assertNotNull(ex);
            System.out.println(ex.getMessage()); 
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        
        System.out.println("************************************************************");
        System.out.println("*** Test Real Time data behaviour - Validity time bounds ***");
        System.out.println("************************************************************");

        // print & verify time limits
        System.out.println("Test now: " + initialTime);
        System.out.println("Initial time seted: " + initialTime);
        System.out.println("Current time seted: " + System.currentTimeMillis());
        System.out.println("----------------------------------------");
        try {
            System.out.println("cpuWorkLoad data: " + cpuWorkLoad.getData());
            assertTrue(new Float(60).equals(cpuWorkLoad.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalUpperBound());
        assertTrue(initialTime >= cpuWorkLoad.getValidityIntervalLowerBound() && System.currentTimeMillis() <= cpuWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("memWorkLoad data: " + memWorkLoad.getData());
            assertTrue(new Float(35).equals(memWorkLoad.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalUpperBound());
        assertTrue(initialTime >= memWorkLoad.getValidityIntervalLowerBound() && System.currentTimeMillis() <= memWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("loadTrend data: " + loadTrend.getData());
            assertTrue(new Float(50).equals(loadTrend.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for loadTrend: " + loadTrend.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for loadTrend: " + loadTrend.getValidityIntervalUpperBound());
        assertTrue(initialTime >= loadTrend.getValidityIntervalLowerBound() && System.currentTimeMillis() <= loadTrend.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            measureVal.calcDerivation();
            System.out.println("mesurableVal data: " + measureVal.getData());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for mesurableVal: " + measureVal.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for mesurableVal: " + measureVal.getValidityIntervalUpperBound());
        assertTrue(initialTime >= measureVal.getValidityIntervalLowerBound() && System.currentTimeMillis() <= measureVal.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        System.out.println("****************************************");
        System.out.println();
        
        // Sleep 3 sec, still valid
        Thread.sleep(3000);
        System.out.println("Test 3 secs after: " + initialTime);
        System.out.println("Initial time seted: " + initialTime);
        System.out.println("Current time seted: " + System.currentTimeMillis());
        System.out.println("----------------------------------------");
        try {
            System.out.println("cpuWorkLoad data: " + cpuWorkLoad.getData());
            assertTrue(new Float(60).equals(cpuWorkLoad.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalUpperBound());
        assertTrue(initialTime >= cpuWorkLoad.getValidityIntervalLowerBound() && System.currentTimeMillis() <= cpuWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("memWorkLoad data: " + memWorkLoad.getData());
            assertTrue(new Float(35).equals(memWorkLoad.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalUpperBound());
        assertTrue(initialTime >= memWorkLoad.getValidityIntervalLowerBound() && System.currentTimeMillis() <= memWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("loadTrend data: " + loadTrend.getData());
            assertTrue(new Float(50).equals(loadTrend.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for loadTrend: " + loadTrend.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for loadTrend: " + loadTrend.getValidityIntervalUpperBound());
        assertTrue(initialTime >= loadTrend.getValidityIntervalLowerBound() && System.currentTimeMillis() <= loadTrend.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            measureVal.calcDerivation();
            System.out.println("mesurableVal data: " + measureVal.getData());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for mesurableVal: " + measureVal.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for mesurableVal: " + measureVal.getValidityIntervalUpperBound());
        assertTrue(initialTime >= measureVal.getValidityIntervalLowerBound() && System.currentTimeMillis() <= measureVal.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        System.out.println("****************************************");
        System.out.println();
        
        // Sleep 1 sec more, not valid for cpuWorkLoad and measureVal
        Thread.sleep(1000);
        System.out.println("Test 1 secs after: " + initialTime);
        System.out.println("Initial time seted: " + initialTime);
        System.out.println("Current time seted: " + System.currentTimeMillis());
        System.out.println("----------------------------------------");
        try {
            System.out.println("cpuWorkLoad data: " + cpuWorkLoad.getData());
            fail("cpuWorkLoad data has exired");
        } catch (RtdException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("cpuWorkLoad: " + ex.getMessage());
        }
        System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalUpperBound());
        assertFalse(initialTime >= cpuWorkLoad.getValidityIntervalLowerBound() && System.currentTimeMillis() <= cpuWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("memWorkLoad data: " + memWorkLoad.getData());
            assertTrue(new Float(35).equals(memWorkLoad.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalUpperBound());
        assertTrue(initialTime >= memWorkLoad.getValidityIntervalLowerBound() && System.currentTimeMillis() <= memWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("loadTrend data: " + loadTrend.getData());
            assertTrue(new Float(50).equals(loadTrend.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for loadTrend: " + loadTrend.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for loadTrend: " + loadTrend.getValidityIntervalUpperBound());
        assertTrue(initialTime >= loadTrend.getValidityIntervalLowerBound() && System.currentTimeMillis() <= loadTrend.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            measureVal.calcDerivation();
            System.out.println("mesurableVal data: " + measureVal.getData());
            fail("mesurableVal data has exired");
        } catch (RtdException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("mesurableVal: " + ex.getMessage());
        }
        System.out.println("Lower limit interval for mesurableVal: " + measureVal.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for mesurableVal: " + measureVal.getValidityIntervalUpperBound());
        assertFalse(initialTime >= measureVal.getValidityIntervalLowerBound() && System.currentTimeMillis() <= measureVal.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        System.out.println("****************************************");

        // Sleep 2 sec more, invalid data for all except loadTrend
        Thread.sleep(2000);
        System.out.println("Test 2 secs after: " + initialTime);
        System.out.println("Initial time seted: " + initialTime);
        System.out.println("Current time seted: " + System.currentTimeMillis());
        System.out.println("----------------------------------------");
        try {
            System.out.println("cpuWorkLoad data: " + cpuWorkLoad.getData());
            fail("cpuWorkLoad data has exired");
        } catch (RtdException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("cpuWorkLoad: " + ex.getMessage());
        }
        System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalUpperBound());
        assertFalse(initialTime >= cpuWorkLoad.getValidityIntervalLowerBound() && System.currentTimeMillis() <= cpuWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("memWorkLoad data: " + memWorkLoad.getData());
            fail("memWorkLoad data has exired");
        } catch (RtdException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("memWorkLoad: " + ex.getMessage());
        }
        System.out.println("Lower limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalUpperBound());
        assertFalse(initialTime >= memWorkLoad.getValidityIntervalLowerBound() && System.currentTimeMillis() <= memWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("loadTrend data: " + loadTrend.getData());
            assertTrue(new Float(50).equals(loadTrend.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for loadTrend: " + loadTrend.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for loadTrend: " + loadTrend.getValidityIntervalUpperBound());
        assertTrue(initialTime >= loadTrend.getValidityIntervalLowerBound() && System.currentTimeMillis() <= loadTrend.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            measureVal.calcDerivation();
            System.out.println("mesurableVal data: " + measureVal.getData());
            fail("mesurableVal data has exired");
        } catch (RtdException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("mesurableVal: " + ex.getMessage());
        }
        System.out.println("Lower limit interval for mesurableVal: " + measureVal.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for mesurableVal: " + measureVal.getValidityIntervalUpperBound());
        assertFalse(initialTime >= measureVal.getValidityIntervalLowerBound() && System.currentTimeMillis() <= measureVal.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        System.out.println("****************************************");
    
    }

}
