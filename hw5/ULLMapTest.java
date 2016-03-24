import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Iterator;

/** ULLMapTest. You should write additional tests.
 *  @author Josh Hug
 */

public class ULLMapTest {
    @Test
    public void testBasic() {
        ULLMap<String, String> um = new ULLMap<String, String>();
        um.put("Gracias", "Dios Basado");
        assertEquals(um.get("Gracias"), "Dios Basado");
		um.put("Gracias", "por todos");
		assertEquals(1, um.size());
		assertEquals(um.get("Gracias"), "por todos");
		um.put("I love", "Kirby!");
		assertEquals(2, um.size());
    }
    @Test
    public void testBasic2() {
        ULLMap<Number, String> um = new ULLMap<Number, String>();
        um.put(1, "Mooshiiee");
        assertEquals(um.get(1), "Mooshiiee");
		um.put(2, "snoops");
		um.put(3, "mr.dexter");
		assertEquals(3, um.size());
		um.clear();
		assertEquals(0, um.size());
		assertEquals(null, um.get(3));
    }
	

    
    @Test
    public void testIterator() {
        ULLMap<Integer, String> um = new ULLMap<Integer, String>();
        um.put(0, "zero");
        um.put(1, "one");
        um.put(2, "two");
		um.put(3, "three");
        Iterator<Integer> umi = um.iterator();
        System.out.println(umi.next());
		System.out.println(umi.next());
		System.out.println(umi.next());
		System.out.println(umi.next());
    }
	
    @Test
    public void testInvert() {
        ULLMap<String, Integer> um = new ULLMap<String, Integer>();
        um.put("a", 1);
        um.put("b", 2);
        um.put("c", 3);
        ULLMap<Integer, String> um2 = um.invert(um);
        assertEquals(3, um2.size());
		assertEquals("a", um2.get(1));
		assertEquals("b", um2.get(2));
		assertEquals("c", um2.get(3));
    
	}
		
    /** Runs tests. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(ULLMapTest.class);
    }
} 