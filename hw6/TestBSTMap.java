import edu.princeton.cs.introcs.StdAudio;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestBSTMap {
    @Test
    public void genericTests() {
        BSTMap test = new BSTMap(4, "a");
		test.put(10, "a");
		test.put(0, "a");
		test.put(2, "a");
		test.put(9, "a");
		test.put(11, "a");
		test.put(100, "b");
		test.put(7, "a");
		test.printInOrder();
		assertEquals(8, test.size());
		assertEquals("b", test.get(100));
    }
    @Test
    public void genericTests2() {
        BSTMap test = new BSTMap("hello", 1);
		test.put("apple", 1);
		test.put("lynn", 1);
		test.put("butcher", 1);
		test.put("zypher", 1);
		test.put("tomorrow", 1);
		test.put("oski", 1);
		test.put("brew", 1);
		test.printInOrder();
    }

    /** Calls tests for GuitarString. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestBSTMap.class);
    }
} 