/*
 * JUnit tests for the Triangle class
 */
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author melaniecebula
 */
public class TriangleTest {
  /**  We've already created a testScalene method.  Please fill in testEquilateral, and additionally
   *   create tests for Isosceles, Negative Sides, and Invalid sides
   **/

    @Test
    public void testScalene() {
        Triangle t = new Triangle(30, 40, 50);
        String result = t.triangleType();
        assertEquals("Scalene", result);
    }

    @Test
    public void testEquilateral() {
<<<<<<< HEAD
      Triangle t = new Triangle (40, 40, 40);
	  String result = t.triangleType();
	  assertEquals ("Equilateral", result);
    }
	
	@Test
	public void testIsosceles() {
		Triangle t = new Triangle (4, 10, 10);
		String result = t.triangleType();
		assertEquals ("Isosceles", result);
	}


    public static void main(String[] args) {
		jh61b.junit.textui.runClasses(TriangleTest.class);
=======
      //TODO: FILL IN
    }

    //TODO: CREATE MORE TESTS

    public static void main(String[] args) {
      //TODO: RUN TESTS (Look in ArithmeticTest.java main method for help!)
>>>>>>> 906cc9fa6152927956e257ed894b73a741173383
    }
}
