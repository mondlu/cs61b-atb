package radix;
/* Radix.java */
import java.util.TreeMap;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;



/**
 * Sorts is a class that contains an implementation of radix sort.
 * @author Mondee 
 */
public class Sorts {


    /**
     *  Sorts an array of int keys according to the values of <b>one</b>
     *  of the base-16 digits of each key. Returns a <b>NEW</b> array and
     *  does not modify the input array.
     *  
     *  @param key is an array of ints.  Assume no key is negative.
     *  @param whichDigit is a number in 0...7 specifying which base-16 digit
     *    is the sort key. 0 indicates the least significant digit which
     *    7 indicates the most significant digit
     *  @return an array of type int, having the same length as "keys"
     *    and containing the same keys sorted according to the chosen digit.
     **/
    public static int[] countingSort(int[] keys, int whichDigit) {
        //YOUR CODE HERE
		TreeMap<Integer, ArrayList<Integer>> comparisonMap = new TreeMap<Integer, ArrayList<Integer>>();
		int[] returnArray = new int[keys.length];
		
		for (int i : keys) {
			if (whichDigit == 0) {
				int compareInt = i & 15;
				if (comparisonMap.containsKey(compareInt)) {
					ArrayList<Integer> assocArray = comparisonMap.get(compareInt);
					assocArray.add(i);
					comparisonMap.put(compareInt, assocArray);
				} else {
					ArrayList<Integer> assocArray = new ArrayList<Integer>();
					assocArray.add(i);
					comparisonMap.put(compareInt, assocArray);
				}
			} else if (whichDigit == 1) {
				int intermediary = i >>> 4;
				int compareInt = intermediary & 15;
				if (comparisonMap.containsKey(compareInt)) {
					ArrayList<Integer> assocArray = comparisonMap.get(compareInt);
					assocArray.add(i);
					comparisonMap.put(compareInt, assocArray);
				} else {
					ArrayList<Integer> assocArray = new ArrayList<Integer>();
					assocArray.add(i);
					comparisonMap.put(compareInt, assocArray);
				}
			} else if (whichDigit == 2) {
				int intermediary = i >>> 8;
				int compareInt = intermediary & 15;
				if (comparisonMap.containsKey(compareInt)) {
					ArrayList<Integer> assocArray = comparisonMap.get(compareInt);
					assocArray.add(i);
					comparisonMap.put(compareInt, assocArray);
				} else {
					ArrayList<Integer> assocArray = new ArrayList<Integer>();
					assocArray.add(i);
					comparisonMap.put(compareInt, assocArray);
				}
			} else if (whichDigit == 3) {
				int intermediary = i >>> 12;
				int compareInt = intermediary & 15;
				if (comparisonMap.containsKey(compareInt)) {
					ArrayList<Integer> assocArray = comparisonMap.get(compareInt);
					assocArray.add(i);
					comparisonMap.put(compareInt, assocArray);
				} else {
					ArrayList<Integer> assocArray = new ArrayList<Integer>();
					assocArray.add(i);
					comparisonMap.put(compareInt, assocArray);
				}
			} else if (whichDigit == 4) {
				int intermediary = i >>> 16;
				int compareInt = intermediary & 15;
				if (comparisonMap.containsKey(compareInt)) {
					ArrayList<Integer> assocArray = comparisonMap.get(compareInt);
					assocArray.add(i);
					comparisonMap.put(compareInt, assocArray);
				} else {
					ArrayList<Integer> assocArray = new ArrayList<Integer>();
					assocArray.add(i);
					comparisonMap.put(compareInt, assocArray);
				}
			} else if (whichDigit == 5) {
				int intermediary = i >>> 20;
				int compareInt = intermediary & 15;
				if (comparisonMap.containsKey(compareInt)) {
					ArrayList<Integer> assocArray = comparisonMap.get(compareInt);
					assocArray.add(i);
					comparisonMap.put(compareInt, assocArray);
				} else {
					ArrayList<Integer> assocArray = new ArrayList<Integer>();
					assocArray.add(i);
					comparisonMap.put(compareInt, assocArray);
				}
			} else if (whichDigit == 6) {
				int intermediary = i >>> 24;
				int compareInt = intermediary & 15;
				if (comparisonMap.containsKey(compareInt)) {
					ArrayList<Integer> assocArray = comparisonMap.get(compareInt);
					assocArray.add(i);
					comparisonMap.put(compareInt, assocArray);
				} else {
					ArrayList<Integer> assocArray = new ArrayList<Integer>();
					assocArray.add(i);
					comparisonMap.put(compareInt, assocArray);
				}
			} else {
				int intermediary = i >>> 28;
				int compareInt = intermediary & 15;
				if (comparisonMap.containsKey(compareInt)) {
					ArrayList<Integer> assocArray = comparisonMap.get(compareInt);
					assocArray.add(i);
					comparisonMap.put(compareInt, assocArray);
				} else {
					ArrayList<Integer> assocArray = new ArrayList<Integer>();
					assocArray.add(i);
					comparisonMap.put(compareInt, assocArray);
				}
			}			
		}
		
		Set<Integer> keySet = comparisonMap.keySet();
		int index = 0;
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			ArrayList<Integer> array = comparisonMap.get(iterator.next());
			for (int num : array) {
				returnArray[index] = num;
				index = index + 1;
			}
		}
		return returnArray;		
    }

    /**
     *  radixSort() sorts an array of int keys (using all 32 bits
     *  of each key to determine the ordering). Returns a <b>NEW</b> array
     *  and does not modify the input array
     *  @param key is an array of ints.  Assume no key is negative.
     *  @return an array of type int, having the same length as "keys"
     *    and containing the same keys in sorted order.
     **/
    public static int[] radixSort(int[] keys) {
        int[] oldArray = keys;
		int[] newArray = new int[keys.length];
		for (int i = 0; i < 8; i ++) {
			newArray = countingSort(oldArray, i);
			oldArray = newArray;
		}
		return newArray;
    }
}
