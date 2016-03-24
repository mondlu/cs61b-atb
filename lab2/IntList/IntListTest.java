import static org.junit.Assert.*;
import org.junit.Test;

public class IntListTest {

    /** Example test that verifies correctness of the IntList.list static 
     *  method. The main point of this is to convince you that 
     *  assertEquals knows how to handle IntLists just fine.
     */

    @Test 
    public void testList() {
        IntList one = new IntList(1, null);
        IntList twoOne = new IntList(2, one);
        IntList threeTwoOne = new IntList(3, twoOne);

        IntList x = IntList.list(3, 2, 1);
        assertEquals(threeTwoOne, x);
    }

    @Test
    public void testdSquareList() {
      IntList L = IntList.list(1, 2, 3);
      IntList.dSquareList(L);
      assertEquals(IntList.list(1, 4, 9), L);
    }

    /** Do not use the new keyword in your tests. You can create
     *  lists using the handy IntList.list method.  
     * 
     *  Make sure to include test cases involving lists of various sizes
     *  on both sides of the operation. That includes the empty list, which
     *  can be instantiated, for example, with 
     *  IntList empty = IntList.list(). 
     *
     *  Keep in mind that dcatenate(A, B) is NOT required to leave A untouched.
     *  Anything can happen to A. 
     */

<<<<<<< HEAD
    //TODO:  Create testSquareListRecursive(); also test that it's not destructive
	@Test
	public void testSquareListRecursive() {
		IntList L = IntList.list(1, 2, 3, 4, 5, 6);
		IntList M = IntList.list();
		IntList newL = IntList.squareListRecursive(L);
		assertEquals(IntList.list(1, 4, 9, 16, 25, 36), newL);
		assertEquals(IntList.list(1, 2, 3, 4, 5, 6), L);
	}
    //TODO:  Create testDcatenate and testCatenate
	@Test
	public void testDcatenate() {
		//test 1
		IntList a = IntList.list(2, 4, 6, 8, 10, 12);
		IntList b = IntList.list(14, 16);
		IntList.dcatenate(a, b);
		assertEquals(IntList.list(2, 4, 6, 8, 10, 12, 14, 16), a);
		//test 2
		IntList a2 = IntList.list();
		IntList b2 = IntList.list(1, 2, 3, 4);
		IntList.dcatenate(a2, b2);
		assertEquals(IntList.list(1, 2, 3, 4), IntList.dcatenate(a2, b2));
		//test 3
		IntList a3 = IntList.list(10, 11);
		IntList b3 = IntList.list();
		IntList c3 = IntList.dcatenate(a3, b3);
		assertEquals(IntList.list(10, 11), c3);
	}
	
	@Test
	public void testCatenate() {
		IntList a = IntList.list(1, 3);
		IntList b = IntList.list(5, 7, 9);
		IntList c = IntList.catenate(a, b);
		assertEquals(IntList.list(1, 3, 5, 7, 9), c);
		assertEquals(IntList.list(1,3), a);
		//test 2
		IntList a2 = IntList.list(1, 3);
		IntList b2 = IntList.list();
		assertEquals(IntList.list(1, 3), IntList.catenate(a2, b2));
		assertEquals(IntList.list(1, 3), a2);
		//test 3
		IntList a3 = IntList.list();
		IntList b3 = IntList.list();
		assertEquals(IntList.list(), IntList.catenate(a3, b3));
		assertEquals(IntList.list(), a3);
	}
=======
    //TODO:  Create testSquareListRecursive()
    //TODO:  Create testDcatenate and testCatenate
>>>>>>> 906cc9fa6152927956e257ed894b73a741173383

    /* Run the unit tests in this file. */
    public static void main(String... args) {
        jh61b.junit.textui.runClasses(IntListTest.class);
    }       
}   
