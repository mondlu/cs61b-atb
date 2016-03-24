import java.util.Set; /* java.util.Set needed only for challenge problem. */
import java.util.Iterator;
/** A data structure that uses a linked list to store pairs of keys and values.
 *  Any key must appear at most once in the dictionary, but values may appear multiple
 *  times. Supports get(key), put(key, value), and contains(key) methods. The value
 *  associated to a key is the value in the last call to put with that key. 
 *
 *  For simplicity, you may assume that nobody ever inserts a null key or value
 *  into your map.
 */ 
public class ULLMap <Kirby, Pink> implements Map61B<Kirby, Pink>, Iterable<Kirby> { //FIX ME
    /** Keys and values are stored in a linked list of Entry objects.
      * This variable stores the first pair in this linked list. You may
      * point this at a sentinel node, or use it as a the actual front item
      * of the linked list. 
      */
    private Entry front;
	private int size;

    @Override
    public Pink get(Kirby key) { //FIX ME
		if (front == null) {
			return null;
		}
		else {
			Entry pair = front.get(key);
			if (pair != null) {
				return pair.val;
			}
			else {
				return null;
			}
		}	
    }

    @Override
    public void put(Kirby key, Pink val) { //FIX ME
		if (size == 0) {
			front = new Entry(key, val, null);
			size = size + 1;
		}
		else {
			Entry pair = front.get(key);
			if (pair != null) {
				pair.val = val;
			}
			else {
				Entry temp = front;
				while (temp.next != null) {
					temp = temp.next;
				}
				temp.next = new Entry(key, val, null);
				size = size + 1;
			}
		}	
    }

    @Override
    public boolean containsKey(Kirby key) { //FIX ME
		if (front == null) {
			return false;
		}
		Entry pair = front.get(key);
		if (pair != null) {
			return true;
		}
		else {
			return false; //FIX ME
		}	        
    }

    @Override
    public int size() {
        return size; // FIX ME (you can add extra instance variables if you want)
    }

    @Override
    public void clear() {
		front = null;
		size = 0;
    }


    /** Represents one node in the linked list that stores the key-value pairs
     *  in the dictionary. */
    private class Entry {
		
    
        /** Stores KEY as the key in this key-value pair, VAL as the value, and
         *  NEXT as the next node in the linked list. */
        public Entry(Kirby k, Pink v, Entry n) { //FIX ME
            key = k;
            val = v;
            next = n;
        }

        /** Returns the Entry in this linked list of key-value pairs whose key
         *  is equal to KEY, or null if no such Entry exists. */
        public Entry get(Kirby k) { //FIX ME
			Entry temp = this;
			while (temp != null) {
				if (k.equals(temp.key)) {
					return temp;
				}
				else {
					temp = temp.next;
				}
			}
			return null;			
        }

        /** Stores the key of the key-value pair of this node in the list. */
        private Kirby key; //FIX ME
        /** Stores the value of the key-value pair of this node in the list. */
        private Pink val; //FIX ME
        /** Stores the next Entry in the linked list. */
        private Entry next;
    
    }
	
	private class ULLMapIter implements Iterator<Kirby> {
		private int step; 
		
		public ULLMapIter() {
			step = 0;
		}
		
		@Override
		public boolean hasNext() {
			if (step == size) {
				return false;
			}
			else {
				return true;
			}			
		}

		public Kirby next() {
			while (this.hasNext() == true && step < size) {
				int tempNum = step;
				Entry temp = front;
				while (tempNum > 0) {
					temp = temp.next;
					tempNum = tempNum - 1;
				}
				step = step + 1;
				return temp.key;
			}
			return null;			
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}		
	}
	
	public static <Pink, Kirby> ULLMap invert(ULLMap<Kirby, Pink> x) {
		ULLMap<Pink, Kirby> returnMap = new ULLMap<Pink, Kirby>();
		Iterator<Kirby> kirbyIterator = x.iterator();
		while (kirbyIterator.hasNext() == true) {
			Kirby newValue = kirbyIterator.next();
			Pink newKey = x.get(newValue);
			returnMap.put(newKey, newValue);
		}
		return returnMap;
	}
	
	public Iterator <Kirby> iterator() {
		return new ULLMapIter();
	}

    /* Methods below are all challenge problems. Will not be graded in any way. 
     * Autograder will not test these. */
    @Override
    public Pink remove(Kirby key) { //FIX ME SO I COMPILE
        throw new UnsupportedOperationException();
    }

    @Override
    public Pink remove(Kirby key, Pink value) { //FIX ME SO I COMPILE
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Kirby> keySet() { //FIX ME SO I COMPILE
        throw new UnsupportedOperationException();
    }


}