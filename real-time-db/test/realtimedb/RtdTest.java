/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
        // Create all RTD objects. Asign objetcs to deriver RTD.
        cpuWorkLoad = new RtdBaseContinuous<Float>(maxEdgeCpuWorkLoad, new Float(50));
        memWorkLoad = new RtdBaseContinuous<Float>(maxEdgeMemWorkLoad, new Float(100));
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
     * Test of application with RTD data having temporal restrictions.
     */
    @Test
    public void testValidDataRtd() {
        // validate data renge of cpuWorkLoad
        cpuWorkLoad.setMinValid(new Float(30));
        cpuWorkLoad.setMaxValid(new Float(50));
        try {
            cpuWorkLoad.setData(new Float(60));
        } catch (RtdNotValidData ex) {
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
        testByteDiscrete.setMinValid(new Byte((byte) 15));
        try {
            testByteDiscrete.setData(new Byte((byte) 25));
        } catch (RtdNotValidData ex) {
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
        mesurableVal.setMinValid(new Float(0));

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

        // print & verify time limits
        System.out.println("Test now: " + initialTime);
        System.out.println("Initial time seted: " + initialTime);
        System.out.println("Current time seted: " + currentTime);
        System.out.println("----------------------------------------");
        try {
            System.out.println("cpuWorkLoad data: " + cpuWorkLoad.getData());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getLowerLimitInterval());
        System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getUpperLimitInterval());
        assertTrue(initialTime >= cpuWorkLoad.getLowerLimitInterval() && currentTime <= cpuWorkLoad.getUpperLimitInterval());
        System.out.println("----------------------------------------");
        try {
            System.out.println("memWorkLoad data: " + memWorkLoad.getData());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for memWorkLoad: " + memWorkLoad.getLowerLimitInterval());
        System.out.println("Upper limit interval for memWorkLoad: " + memWorkLoad.getUpperLimitInterval());
        assertTrue(initialTime >= memWorkLoad.getLowerLimitInterval() && currentTime <= memWorkLoad.getUpperLimitInterval());
        System.out.println("----------------------------------------");
        try {
            System.out.println("loadTrend data: " + loadTrend.getData());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for loadTrend: " + loadTrend.getLowerLimitInterval());
        System.out.println("Upper limit interval for loadTrend: " + loadTrend.getUpperLimitInterval());
        assertTrue(initialTime >= loadTrend.getLowerLimitInterval() && currentTime <= loadTrend.getUpperLimitInterval());
        System.out.println("----------------------------------------");
        try {
            System.out.println("mesurableVal data: " + mesurableVal.getData());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for mesurableVal: " + mesurableVal.getLowerLimitInterval());
        System.out.println("Upper limit interval for mesurableVal: " + mesurableVal.getUpperLimitInterval());
        assertTrue(initialTime >= mesurableVal.getLowerLimitInterval() && currentTime <= mesurableVal.getUpperLimitInterval());
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
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getLowerLimitInterval());
        System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getUpperLimitInterval());
        assertTrue(initialTime >= cpuWorkLoad.getLowerLimitInterval() && currentTime <= cpuWorkLoad.getUpperLimitInterval());
        System.out.println("----------------------------------------");
        try {
            System.out.println("memWorkLoad data: " + memWorkLoad.getData());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for memWorkLoad: " + memWorkLoad.getLowerLimitInterval());
        System.out.println("Upper limit interval for memWorkLoad: " + memWorkLoad.getUpperLimitInterval());
        assertTrue(initialTime >= memWorkLoad.getLowerLimitInterval() && currentTime <= memWorkLoad.getUpperLimitInterval());
        System.out.println("----------------------------------------");
        try {
            System.out.println("loadTrend data: " + loadTrend.getData());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for loadTrend: " + loadTrend.getLowerLimitInterval());
        System.out.println("Upper limit interval for loadTrend: " + loadTrend.getUpperLimitInterval());
        assertTrue(initialTime >= loadTrend.getLowerLimitInterval() && currentTime <= loadTrend.getUpperLimitInterval());
        System.out.println("----------------------------------------");
        try {
            System.out.println("mesurableVal data: " + mesurableVal.getData());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for mesurableVal: " + mesurableVal.getLowerLimitInterval());
        System.out.println("Upper limit interval for mesurableVal: " + mesurableVal.getUpperLimitInterval());
        assertTrue(initialTime >= mesurableVal.getLowerLimitInterval() && currentTime <= mesurableVal.getUpperLimitInterval());
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
        System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getLowerLimitInterval());
        System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getUpperLimitInterval());
        assertFalse(initialTime >= cpuWorkLoad.getLowerLimitInterval() && currentTime <= cpuWorkLoad.getUpperLimitInterval());
        System.out.println("----------------------------------------");
        try {
            System.out.println("memWorkLoad data: " + memWorkLoad.getData());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for memWorkLoad: " + memWorkLoad.getLowerLimitInterval());
        System.out.println("Upper limit interval for memWorkLoad: " + memWorkLoad.getUpperLimitInterval());
        assertTrue(initialTime >= memWorkLoad.getLowerLimitInterval() && currentTime <= memWorkLoad.getUpperLimitInterval());
        System.out.println("----------------------------------------");
        try {
            System.out.println("loadTrend data: " + loadTrend.getData());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for loadTrend: " + loadTrend.getLowerLimitInterval());
        System.out.println("Upper limit interval for loadTrend: " + loadTrend.getUpperLimitInterval());
        assertTrue(initialTime >= loadTrend.getLowerLimitInterval() && currentTime <= loadTrend.getUpperLimitInterval());
        System.out.println("----------------------------------------");
        try {
            System.out.println("mesurableVal data: " + mesurableVal.getData());
            fail("mesurableVal data has exired");
        } catch (RtdException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("mesurableVal: " + ex.getMessage());
        }
        System.out.println("Lower limit interval for mesurableVal: " + mesurableVal.getLowerLimitInterval());
        System.out.println("Upper limit interval for mesurableVal: " + mesurableVal.getUpperLimitInterval());
        assertFalse(initialTime >= mesurableVal.getLowerLimitInterval() && currentTime <= mesurableVal.getUpperLimitInterval());
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
        System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getLowerLimitInterval());
        System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getUpperLimitInterval());
        assertFalse(initialTime >= cpuWorkLoad.getLowerLimitInterval() && currentTime <= cpuWorkLoad.getUpperLimitInterval());
        System.out.println("----------------------------------------");
        try {
            System.out.println("memWorkLoad data: " + memWorkLoad.getData());
            fail("memWorkLoad data has exired");
        } catch (RtdException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("memWorkLoad: " + ex.getMessage());
        }
        System.out.println("Lower limit interval for memWorkLoad: " + memWorkLoad.getLowerLimitInterval());
        System.out.println("Upper limit interval for memWorkLoad: " + memWorkLoad.getUpperLimitInterval());
        assertFalse(initialTime >= memWorkLoad.getLowerLimitInterval() && currentTime <= memWorkLoad.getUpperLimitInterval());
        System.out.println("----------------------------------------");
        try {
            System.out.println("loadTrend data: " + loadTrend.getData());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for loadTrend: " + loadTrend.getLowerLimitInterval());
        System.out.println("Upper limit interval for loadTrend: " + loadTrend.getUpperLimitInterval());
        assertTrue(initialTime >= loadTrend.getLowerLimitInterval() && currentTime <= loadTrend.getUpperLimitInterval());
        System.out.println("----------------------------------------");
        try {
            System.out.println("mesurableVal data: " + mesurableVal.getData());
            fail("mesurableVal data has exired");
        } catch (RtdException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("mesurableVal: " + ex.getMessage());
        }
        System.out.println("Lower limit interval for mesurableVal: " + mesurableVal.getLowerLimitInterval());
        System.out.println("Upper limit interval for mesurableVal: " + mesurableVal.getUpperLimitInterval());
        assertFalse(initialTime >= mesurableVal.getLowerLimitInterval() && currentTime <= mesurableVal.getUpperLimitInterval());
        System.out.println("----------------------------------------");
        System.out.println("****************************************");
    
    }

}
