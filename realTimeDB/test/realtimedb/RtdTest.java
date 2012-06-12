/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

import java.awt.Point;
import java.math.BigDecimal;
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
    
    // abtract data type for type test
    private static class EmployeeentityBeanClassTest {

        private int id;
        private String name;
        private long salary;

        public EmployeeentityBeanClassTest(){
        }

        public EmployeeentityBeanClassTest(int id) {
            this.id = id;
        }
        
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public long getSalary() {
            return salary;
        }
        public void setSalary(long salary) {
            this.salary = salary;
        }
    }
    
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
     * Test Rtd with multiple data types
     */
    @Test
    public void testDiferentDataTypes() throws InterruptedException, RtdException {
        RtdBaseContinuous<BigDecimal> bigDecimalRtd = new RtdBaseContinuous<BigDecimal>();
        bigDecimalRtd.setDuration(500);
        bigDecimalRtd.setData(new BigDecimal(10));
        
        RtdBaseDiscrete<Point> pointRtd = new RtdBaseDiscrete<Point>();
        pointRtd.setData(new Point(5, 6));
        
        RtdBaseDiscrete<EmployeeentityBeanClassTest> employeeRtd = new RtdBaseDiscrete<EmployeeentityBeanClassTest>();
        EmployeeentityBeanClassTest emp = new EmployeeentityBeanClassTest();
        emp.setId(1);
        emp.setName("Max");
        emp.setSalary(1000);
        employeeRtd.setData(emp);
        
        assertTrue("Incorrect BigDecimal", bigDecimalRtd.getData().floatValue()==BigDecimal.valueOf(10).floatValue());
        assertTrue("Incorrect Point", pointRtd.getData().getX() == 5 && pointRtd.getData().getY() == 6);
        assertTrue("Incorrect Employee ", employeeRtd.getData().getId() == 1 
                && employeeRtd.getData().getName().equals("Max")
                && employeeRtd.getData().getSalary() == 1000);
        
    }
    
    
    /**
     * Test of application with Rtd data having temporal restrictions.
     */
    @Test
    public void testValidDataRtd() throws InterruptedException {
        // validate data renge of cpuWorkLoad
        System.out.println("**********************************");
        System.out.println("validate data renge of cpuWorkLoad");
        System.out.println("**********************************");
        cpuWorkLoad.setMinValid(new Float(0));
        cpuWorkLoad.setMaxValid(new Float(100));
        try {
            System.out.print("cpuWorkLoad=100.0001;");
            cpuWorkLoad.setData(new Float(100.0001));
            fail("Data not in valid range");
        } catch (NotValidRtdData ex) {
            System.out.println("Not valid data for cpuWorkLoad<Float> type");
            assertNotNull("Exception exit for not valid data for cpuWorkLoad<Float> type", ex);
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        try {
            cpuWorkLoad.setData(new Float(70));
            System.out.print("cpuWorkLoad="+cpuWorkLoad.getData()+";");
            assertNotNull("Pass set valid data range for float", cpuWorkLoad);
            System.out.println("Valid data for cpuWorkLoad<Float> type");
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        
        // validate maxDataError nor change but valid time
        System.out.println("*******************************");
        System.out.println("***   Test maxDataError     ***");
        System.out.println("*** set minimum change to 5 ***");
        System.out.println("*******************************");

//        try {
//            cpuWorkLoad.setData(new Float(30));
//            assertTrue(new Float(30).equals(cpuWorkLoad.getData()));
//        } catch (RtdException ex) {
//            fail(ex.getMessage());
//        }
        

        cpuWorkLoad.setMaxDataError(new Float(5.0));
        try {
            // set current time
            cpuWorkLoad.setData(new Float(35));
            System.out.print("set=35;");
            System.out.print("cpuWorkLoad=" + cpuWorkLoad.getData()+";");
            assertTrue(new Float(35).equals(cpuWorkLoad.getData()));
            System.out.print("now=" + System.currentTimeMillis()+";");
            System.out.print("VILB=" + cpuWorkLoad.getValidityIntervalLowerBound()+";");
            System.out.println("VIUB=" + cpuWorkLoad.getValidityIntervalUpperBound());
            assertTrue(System.currentTimeMillis() <= cpuWorkLoad.getValidityIntervalUpperBound());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }

        try {
            // set current time
            cpuWorkLoad.setData(new Float(39.99999));
            System.out.print("set=39.99999;");
            System.out.print("cpuWorkLoad=" + cpuWorkLoad.getData()+";");
            assertTrue(new Float(35).equals(cpuWorkLoad.getData()));
            System.out.print("now=" + System.currentTimeMillis()+";");
            System.out.print("VILB=" + cpuWorkLoad.getValidityIntervalLowerBound()+";");
            System.out.println("VIUB=" + cpuWorkLoad.getValidityIntervalUpperBound());
            assertTrue(System.currentTimeMillis() <= cpuWorkLoad.getValidityIntervalUpperBound());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }

       try {
            // set current time
            cpuWorkLoad.setData(new Float(45));
            System.out.print("set=45;");
            System.out.print("cpuWorkLoad=" + cpuWorkLoad.getData()+";");
            assertTrue(new Float(45).equals(cpuWorkLoad.getData()));
            System.out.print("now=" + System.currentTimeMillis()+";");
            System.out.print("VILB=" + cpuWorkLoad.getValidityIntervalLowerBound()+";");
            System.out.println("VIUB=" + cpuWorkLoad.getValidityIntervalUpperBound());
            assertTrue(System.currentTimeMillis() <= cpuWorkLoad.getValidityIntervalUpperBound());
            System.out.println("----------------------------------------");
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("----------------------------------------");
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
        System.out.print("now");
        System.out.print(";");
        System.out.print("Initial=" + initialTime);
        System.out.print(";");
        System.out.println("Current=" + System.currentTimeMillis());
        try {
            System.out.print("cpuWorkLoad=" + cpuWorkLoad.getData());
            System.out.print(";");
            assertTrue(new Float(60).equals(cpuWorkLoad.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.print("VILB=" + cpuWorkLoad.getValidityIntervalLowerBound());
        System.out.print(";");
        System.out.println("VIUB=" + cpuWorkLoad.getValidityIntervalUpperBound());
        assertTrue(initialTime >= cpuWorkLoad.getValidityIntervalLowerBound() && System.currentTimeMillis() <= cpuWorkLoad.getValidityIntervalUpperBound());
        try {
            System.out.print("memWorkLoad=" + memWorkLoad.getData());
            System.out.print(";");
            assertTrue(new Float(35).equals(memWorkLoad.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.print("VILB=" + memWorkLoad.getValidityIntervalLowerBound());
        System.out.print(";");
        System.out.println("VIUB=" + memWorkLoad.getValidityIntervalUpperBound());
        assertTrue(initialTime >= memWorkLoad.getValidityIntervalLowerBound() && System.currentTimeMillis() <= memWorkLoad.getValidityIntervalUpperBound());
        try {
            System.out.print("loadTrend=" + loadTrend.getData());
            System.out.print(";");
            assertTrue(new Float(50).equals(loadTrend.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.print("VILB=" + loadTrend.getValidityIntervalLowerBound());
        System.out.print(";");
        System.out.println("VIUB=" + loadTrend.getValidityIntervalUpperBound());
        assertTrue(initialTime >= loadTrend.getValidityIntervalLowerBound() && System.currentTimeMillis() <= loadTrend.getValidityIntervalUpperBound());
        try {
            measureVal.calcDerivation();
            System.out.print("mesurableVal=" + measureVal.getData());
            System.out.print(";");
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.print("VILB=" + measureVal.getValidityIntervalLowerBound());
        System.out.print(";");
        System.out.println("VIUB=" + measureVal.getValidityIntervalUpperBound());
        assertTrue(initialTime >= measureVal.getValidityIntervalLowerBound() && System.currentTimeMillis() <= measureVal.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        
        // Sleep 3 sec, still valid
        Thread.sleep(3000);
        System.out.print("3 secs after");
        System.out.print(";");
        System.out.print("Initial=" + initialTime);
        System.out.print(";");
        System.out.println("Current=" + System.currentTimeMillis());
        try {
            System.out.print("cpuWorkLoad=" + cpuWorkLoad.getData());
            System.out.print(";");
            assertTrue(new Float(60).equals(cpuWorkLoad.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.print("VILB=" + cpuWorkLoad.getValidityIntervalLowerBound());
        System.out.print(";");
        System.out.println("VIUB=" + cpuWorkLoad.getValidityIntervalUpperBound());
        assertTrue(initialTime >= cpuWorkLoad.getValidityIntervalLowerBound() && System.currentTimeMillis() <= cpuWorkLoad.getValidityIntervalUpperBound());
        try {
            System.out.print("memWorkLoad=" + memWorkLoad.getData());
            System.out.print(";");
            assertTrue(new Float(35).equals(memWorkLoad.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.print("VILB=" + memWorkLoad.getValidityIntervalLowerBound());
        System.out.print(";");
        System.out.println("VIUB=" + memWorkLoad.getValidityIntervalUpperBound());
        assertTrue(initialTime >= memWorkLoad.getValidityIntervalLowerBound() && System.currentTimeMillis() <= memWorkLoad.getValidityIntervalUpperBound());
        try {
            System.out.print("loadTrend=" + loadTrend.getData());
            System.out.print(";");
            assertTrue(new Float(50).equals(loadTrend.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.print("VILB=" + loadTrend.getValidityIntervalLowerBound());
        System.out.print(";");
        System.out.println("VIUB=" + loadTrend.getValidityIntervalUpperBound());
        assertTrue(initialTime >= loadTrend.getValidityIntervalLowerBound() && System.currentTimeMillis() <= loadTrend.getValidityIntervalUpperBound());
        try {
            measureVal.calcDerivation();
            System.out.print("mesurableVal=" + measureVal.getData());
            System.out.print(";");
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.print("VILB=" + measureVal.getValidityIntervalLowerBound());
        System.out.print(";");
        System.out.println("VIUB=" + measureVal.getValidityIntervalUpperBound());
        assertTrue(initialTime >= measureVal.getValidityIntervalLowerBound() && System.currentTimeMillis() <= measureVal.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        
        // Sleep 1 sec more, not valid for cpuWorkLoad and measureVal
        Thread.sleep(1000);
        System.out.print("1 sec after");
        System.out.print(";");
        System.out.print("Initial=" + initialTime);
        System.out.print(";");
        System.out.println("Current=" + System.currentTimeMillis());
        try {
            System.out.print("cpuWorkLoad=" + cpuWorkLoad.getData());
            System.out.print(";");
            fail("cpuWorkLoad data has exired");
        } catch (RtdException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("cpuWorkLoad: " + ex.getMessage());
        }
        System.out.print("VILB=" + cpuWorkLoad.getValidityIntervalLowerBound());
        System.out.print(";");
        System.out.println("VIUB=" + cpuWorkLoad.getValidityIntervalUpperBound());
        assertFalse(initialTime >= cpuWorkLoad.getValidityIntervalLowerBound() && System.currentTimeMillis() <= cpuWorkLoad.getValidityIntervalUpperBound());
        try {
            System.out.print("memWorkLoad=" + memWorkLoad.getData());
            System.out.print(";");
            assertTrue(new Float(35).equals(memWorkLoad.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.print("VILB=" + memWorkLoad.getValidityIntervalLowerBound());
        System.out.print(";");
        System.out.println("VIUB=" + memWorkLoad.getValidityIntervalUpperBound());
        assertTrue(initialTime >= memWorkLoad.getValidityIntervalLowerBound() && System.currentTimeMillis() <= memWorkLoad.getValidityIntervalUpperBound());
        try {
            System.out.print("loadTrend=" + loadTrend.getData());
            System.out.print(";");
            assertTrue(new Float(50).equals(loadTrend.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.print("VILB=" + loadTrend.getValidityIntervalLowerBound());
        System.out.print(";");
        System.out.println("VIUB=" + loadTrend.getValidityIntervalUpperBound());
        assertTrue(initialTime >= loadTrend.getValidityIntervalLowerBound() && System.currentTimeMillis() <= loadTrend.getValidityIntervalUpperBound());
        try {
            measureVal.calcDerivation();
            System.out.print("mesurableVal=" + measureVal.getData());
            System.out.print(";");
            fail("mesurableVal data has exired");
        } catch (RtdException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("mesurableVal: " + ex.getMessage());
        }
        System.out.print("VILB=" + measureVal.getValidityIntervalLowerBound());
        System.out.print(";");
        System.out.println("VIUB=" + measureVal.getValidityIntervalUpperBound());
        assertFalse(initialTime >= measureVal.getValidityIntervalLowerBound() && System.currentTimeMillis() <= measureVal.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");

        // Sleep 2 sec more, invalid data for all except loadTrend
        Thread.sleep(2000);
        System.out.print("2 secs after");
        System.out.print(";");
        System.out.print("Initial=" + initialTime);
        System.out.print(";");
        System.out.println("Current=" + System.currentTimeMillis());
        try {
            System.out.print("cpuWorkLoad=" + cpuWorkLoad.getData());
            System.out.print(";");
            fail("cpuWorkLoad data has exired");
        } catch (RtdException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("cpuWorkLoad: " + ex.getMessage());
        }
        System.out.print("VILB=" + cpuWorkLoad.getValidityIntervalLowerBound());
        System.out.print(";");
        System.out.println("VIUB=" + cpuWorkLoad.getValidityIntervalUpperBound());
        assertFalse(initialTime >= cpuWorkLoad.getValidityIntervalLowerBound() && System.currentTimeMillis() <= cpuWorkLoad.getValidityIntervalUpperBound());
        try {
            System.out.print("memWorkLoad=" + memWorkLoad.getData());
            System.out.print(";");
            fail("memWorkLoad data has exired");
        } catch (RtdException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("memWorkLoad: " + ex.getMessage());
        }
        System.out.print("VILB=" + memWorkLoad.getValidityIntervalLowerBound());
        System.out.print(";");
        System.out.println("VIUB=" + memWorkLoad.getValidityIntervalUpperBound());
        assertFalse(initialTime >= memWorkLoad.getValidityIntervalLowerBound() && System.currentTimeMillis() <= memWorkLoad.getValidityIntervalUpperBound());
        try {
            System.out.print("loadTrend=" + loadTrend.getData());
            System.out.print(";");
            assertTrue(new Float(50).equals(loadTrend.getData()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.print("VILB=" + loadTrend.getValidityIntervalLowerBound());
        System.out.print(";");
        System.out.println("VIUB=" + loadTrend.getValidityIntervalUpperBound());
        assertTrue(initialTime >= loadTrend.getValidityIntervalLowerBound() && System.currentTimeMillis() <= loadTrend.getValidityIntervalUpperBound());
        try {
            measureVal.calcDerivation();
            System.out.print("mesurableVal=" + measureVal.getData());
            System.out.print(";");
            fail("mesurableVal data has exired");
        } catch (RtdException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("mesurableVal: " + ex.getMessage());
        }
        System.out.print("VILB=" + measureVal.getValidityIntervalLowerBound());
        System.out.print(";");
        System.out.println("VIUB=" + measureVal.getValidityIntervalUpperBound());
        assertFalse(initialTime >= measureVal.getValidityIntervalLowerBound() && System.currentTimeMillis() <= measureVal.getValidityIntervalUpperBound());
        System.out.println("****************************************");
    
    }

}
