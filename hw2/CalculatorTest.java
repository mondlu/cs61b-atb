import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CalculatorTest {
    /* Do not change this to be private. For silly testing reasons it is public. */
    public Calculator tester;

    /**
     * setUp() performs setup work for your Calculator.  In short, we 
     * initialize the appropriate Calculator for you to work with.
     * By default, we have set up the Staff Calculator for you to test your 
     * tests.  To use your unit tests for your own implementation, comment 
     * out the StaffCalculator() line and uncomment the Calculator() line.
     **/
    @Before
    public void setUp() {
        //tester = new StaffCalculator(); // Comment me out to test your Calculator
        tester = new Calculator();   // Un-comment me to test your Calculator
    }

    @Test
	public void testPositiveNegatives() {
		int x1 = 10;
		int y1 = -20;
		assertEquals(-10, tester.add(x1, y1));
	}
	@Test
	public void testPositivePositive() {
		int x2 = 1;
		int y2 = 11;
		assertEquals(12, tester.add(x2, y2));
	}
	@Test
	public void testNegativePositive() {
		int x3 = -500;
		int y3 = 500;
		assertEquals(0, tester.add(x3, y3));
	}
	@Test
	public void testNegativeNegative() {
		int x4 = -25;
		int y4 = -35;
		assertEquals(-60, tester.add(x4 ,y4));
	}
	@Test
	public void testMultiplicaiton1() {
		int x5 = 100;
		int y5 = 14;
		assertEquals (1400, tester.multiply(x5, y5));
	}
	@Test
	public void testMultiplication2(){
		int x6 = 0;
		int y6 = -15;
		assertEquals(0, tester.multiply(x6,y6));
	}
	@Test
	public void testMultiplication3(){
		int x7 = -3;
		int y7 = 17;
		assertEquals(-51, tester.multiply(x7, y7));
	}
	@Test
	public void testMultiplication4() {
		int x8 = -5;
		int y8 = -4;
		assertEquals(20, tester.multiply(x8, y8));		
	}
	@Test
	public void testMultiplicaton5(){
		int x9 = 190;
		int y9 = -5;
		assertEquals(-950, tester.multiply(x9, y9));
	}
    /* Run the unit tests in this file. */
    public static void main(String... args) {
        jh61b.junit.textui.runClasses(CalculatorTest.class);
    }       
}