/**
 * @author melaniecebula
 */
public class Triangle {

  int s1;
  int s2;
  int s3;

  public Triangle(int side1, int side2, int side3) {
    s1 = side1;
    s2 = side2;
    s3 = side3;
  }

  /** Returns a String describing the type of triangle. */
  public String triangleType() {

    // Chceck that nothing is negative
    if (s1 <= 0 || s2 <= 0 || s3 <= 0) {
      return "At least one length is less than 0!";
    }

    // Check for side length
    if ((s1 + s2 <= s3) || (s1 + s3 <= s2) || (s2 + s3 <= s1)) {
      return "The lengths of the triangles do not form a valid triangle!";
    }  

    // Return triangle type
<<<<<<< HEAD
	String triangleType = "";
    if ((s1 == s2) && (s2 == s3)) {
    	triangleType = "Equilateral";
    }
	else if ((s1 == s2) || (s1 == s3) || (s2 == s3)) {
		triangleType = "Isosceles";
	}
	else {
		triangleType = "Scalene";
	}
    return triangleType; 
=======
    //TODO:  return either "Equilateral", "Isosceles", or "Scalene"
    return null; //REPLACE THIS LINE
>>>>>>> 906cc9fa6152927956e257ed894b73a741173383
  }
}
