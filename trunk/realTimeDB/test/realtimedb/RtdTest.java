/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author demian
 */
public class RtdTest {
    
    static protected RtdBaseContinuous<Float> cpuWorkLoad;
    static protected long maxEdgeCpuWorkLoad = 4000; // 4 secs of maximun adge
    
    static protected RtdBaseContinuous<Float> memWorkLoad;
    static protected long maxEdgeMemWorkLoad = 6000; // 6 secs of maximun adge
    
    static protected RtdBaseDiscrete<Float> loadTrend;
    
    static protected RtdDerived<Float> mesurableVal;
    
    static protected RtdBaseDiscrete<Byte> testByteDiscrete;
    
    public RtdTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Create all Rtd objects. Asign objetcs to deriver Rtd.
        cpuWorkLoad = new RtdBaseContinuous<Float>(maxEdgeCpuWorkLoad);
        memWorkLoad = new RtdBaseContinuous<Float>(maxEdgeMemWorkLoad);
        loadTrend = new RtdBaseDiscrete<Float>();
        
        mesurableVal = new RtdDerived<Float>();
        mesurableVal.addRtdReadSet(cpuWorkLoad);
        mesurableVal.addRtdReadSet(memWorkLoad);
        mesurableVal.addRtdReadSet(loadTrend);
        
        testByteDiscrete = new RtdBaseDiscrete<Byte>();
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
            long currentTime = System.currentTimeMillis();
            System.out.println("Current time: " + currentTime);
            System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalLowerBound());
            System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalUpperBound());
            assertTrue(currentTime <= cpuWorkLoad.getValidityIntervalUpperBound());
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
            long currentTime = System.currentTimeMillis();
            System.out.println("Current time: " + currentTime);
            System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalLowerBound());
            System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalUpperBound());
            assertTrue(currentTime <= cpuWorkLoad.getValidityIntervalUpperBound());
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
            long currentTime = System.currentTimeMillis();
            System.out.println("Current time: " + currentTime);
            System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalLowerBound());
            System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalUpperBound());
            assertTrue(currentTime <= cpuWorkLoad.getValidityIntervalUpperBound());
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
            long currentTime = System.currentTimeMillis();
            System.out.println("Current time: " + currentTime);
            System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalLowerBound());
            System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalUpperBound());
            assertTrue(currentTime <= cpuWorkLoad.getValidityIntervalUpperBound());
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
        memWorkLoad.setMinValid(new Float(0));
        memWorkLoad.setMaxValid(new Float(100));
        loadTrend.setMinValid(new Float(0));
        loadTrend.setMaxValid(new Float(100));

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
            mesurableVal.setData(new Float(cpuWorkLoad.getData()*0.5 + memWorkLoad.getData()*0.3 + loadTrend.getData()*0.2));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        
        // set current time
        long currentTime = System.currentTimeMillis();
        
        System.out.println("************************************************************");
        System.out.println("*** Test Real Time data behaviour - Validity time bounds ***");
        System.out.println("************************************************************");

        // print & verify time limits
        System.out.println("Test now: " + initialTime);
        System.out.println("Initial time seted: " + initialTime);
        System.out.println("Current time seted: " + currentTime);
        System.out.println("----------------------------------------");
        try {
            System.out.println("cpuWorkLoad data: " + cpuWorkLoad.getData());
            assertTrue(new Float(60).equals(cpuWorkLoad.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalUpperBound());
        assertTrue(initialTime >= cpuWorkLoad.getValidityIntervalLowerBound() && currentTime <= cpuWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("memWorkLoad data: " + memWorkLoad.getData());
            assertTrue(new Float(35).equals(memWorkLoad.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalUpperBound());
        assertTrue(initialTime >= memWorkLoad.getValidityIntervalLowerBound() && currentTime <= memWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("loadTrend data: " + loadTrend.getData());
            assertTrue(new Float(50).equals(loadTrend.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for loadTrend: " + loadTrend.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for loadTrend: " + loadTrend.getValidityIntervalUpperBound());
        assertTrue(initialTime >= loadTrend.getValidityIntervalLowerBound() && currentTime <= loadTrend.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("mesurableVal data: " + mesurableVal.getData());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for mesurableVal: " + mesurableVal.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for mesurableVal: " + mesurableVal.getValidityIntervalUpperBound());
        assertTrue(initialTime >= mesurableVal.getValidityIntervalLowerBound() && currentTime <= mesurableVal.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        System.out.println("****************************************");
        System.out.println();
        
        // Sleep 3 sec, still valid
        Thread.sleep(3000);
        // set current time
        currentTime = System.currentTimeMillis();
        System.out.println("Test 3 secs after: " + initialTime);
        System.out.println("Initial time seted: " + initialTime);
        System.out.println("Current time seted: " + currentTime);
        System.out.println("----------------------------------------");
        try {
            System.out.println("cpuWorkLoad data: " + cpuWorkLoad.getData());
            assertTrue(new Float(60).equals(cpuWorkLoad.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalUpperBound());
        assertTrue(initialTime >= cpuWorkLoad.getValidityIntervalLowerBound() && currentTime <= cpuWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("memWorkLoad data: " + memWorkLoad.getData());
            assertTrue(new Float(35).equals(memWorkLoad.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalUpperBound());
        assertTrue(initialTime >= memWorkLoad.getValidityIntervalLowerBound() && currentTime <= memWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("loadTrend data: " + loadTrend.getData());
            assertTrue(new Float(50).equals(loadTrend.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for loadTrend: " + loadTrend.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for loadTrend: " + loadTrend.getValidityIntervalUpperBound());
        assertTrue(initialTime >= loadTrend.getValidityIntervalLowerBound() && currentTime <= loadTrend.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("mesurableVal data: " + mesurableVal.getData());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for mesurableVal: " + mesurableVal.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for mesurableVal: " + mesurableVal.getValidityIntervalUpperBound());
        assertTrue(initialTime >= mesurableVal.getValidityIntervalLowerBound() && currentTime <= mesurableVal.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        System.out.println("****************************************");
        System.out.println();
        
        // Sleep 1 sec more, not valid for cpuWorkLoad and mesurableVal
        Thread.sleep(1000);
        // set current time
        currentTime = System.currentTimeMillis();
        System.out.println("Test 1 secs after: " + initialTime);
        System.out.println("Initial time seted: " + initialTime);
        System.out.println("Current time seted: " + currentTime);
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
        assertFalse(initialTime >= cpuWorkLoad.getValidityIntervalLowerBound() && currentTime <= cpuWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("memWorkLoad data: " + memWorkLoad.getData());
            assertTrue(new Float(35).equals(memWorkLoad.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalUpperBound());
        assertTrue(initialTime >= memWorkLoad.getValidityIntervalLowerBound() && currentTime <= memWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("loadTrend data: " + loadTrend.getData());
            assertTrue(new Float(50).equals(loadTrend.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for loadTrend: " + loadTrend.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for loadTrend: " + loadTrend.getValidityIntervalUpperBound());
        assertTrue(initialTime >= loadTrend.getValidityIntervalLowerBound() && currentTime <= loadTrend.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("mesurableVal data: " + mesurableVal.getData());
            fail("mesurableVal data has exired");
        } catch (RtdException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("mesurableVal: " + ex.getMessage());
        }
        System.out.println("Lower limit interval for mesurableVal: " + mesurableVal.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for mesurableVal: " + mesurableVal.getValidityIntervalUpperBound());
        assertFalse(initialTime >= mesurableVal.getValidityIntervalLowerBound() && currentTime <= mesurableVal.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        System.out.println("****************************************");

        // Sleep 2 sec more, invalid data for all except loadTrend
        Thread.sleep(2000);
        // set current time
        currentTime = System.currentTimeMillis();
        System.out.println("Test 2 secs after: " + initialTime);
        System.out.println("Initial time seted: " + initialTime);
        System.out.println("Current time seted: " + currentTime);
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
        assertFalse(initialTime >= cpuWorkLoad.getValidityIntervalLowerBound() && currentTime <= cpuWorkLoad.getValidityIntervalUpperBound());
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
        assertFalse(initialTime >= memWorkLoad.getValidityIntervalLowerBound() && currentTime <= memWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("loadTrend data: " + loadTrend.getData());
            assertTrue(new Float(50).equals(loadTrend.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for loadTrend: " + loadTrend.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for loadTrend: " + loadTrend.getValidityIntervalUpperBound());
        assertTrue(initialTime >= loadTrend.getValidityIntervalLowerBound() && currentTime <= loadTrend.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("mesurableVal data: " + mesurableVal.getData());
            fail("mesurableVal data has exired");
        } catch (RtdException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("mesurableVal: " + ex.getMessage());
        }
        System.out.println("Lower limit interval for mesurableVal: " + mesurableVal.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for mesurableVal: " + mesurableVal.getValidityIntervalUpperBound());
        assertFalse(initialTime >= mesurableVal.getValidityIntervalLowerBound() && currentTime <= mesurableVal.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        System.out.println("****************************************");
    
    }

}
