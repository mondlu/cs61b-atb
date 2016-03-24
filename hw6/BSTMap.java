//author: Mondee Lu
import java.util.Set;
import java.util.TreeSet;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private BSTMap<K, V> left;
    private BSTMap<K, V> right;
    private K key;
    private V value;
    private TreeSet<K> keySet = new TreeSet<K>();
    
    //constructor that sets the root key:value pair
    public BSTMap(K k, V v) {
        key = k;
        value = v;  
        keySet.add(k);  
    }
    
    public BSTMap() {
    }
    
    @Override
    /** Removes all of the mappings from this map. */
    public void clear() {
        left = null;
        right = null;
        key = null;
        value = null;
        keySet = new TreeSet<K>();
    }
    
    @Override
    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        return keySet.contains(key);    
    }
    
    @Override
    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key. 
     */
    public V get(K key) {
        if (!containsKey(key)) {
            return null;
        } else if (this.key.compareTo(key) == 0) {
            return this.value;
        } else if (this.key.compareTo(key) > 0) {
            return left.get(key);
        } else {
            return right.get(key);
        }       
    }
    
    @Override
    /* Returns the number of key-value mappings in this map. */
    public int size() {
        return keySet.size();   
    }
    
    @Override
    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
		
        if (keySet.size() == 0 || this.key.equals(key)) {
            this.key = key;
			this.value = value;
		} else if (this.key.compareTo(key) < 0 && this.right == null) {          
			this.right = new BSTMap<K, V>(key, value);
		} else if (this.key.compareTo(key) > 0 && this.left == null) {
			this.left = new BSTMap<K, V>(key, value);
		} else {
			if (this.key.compareTo(key) > 0) {
				left.put(key, value);	
			} else {
				right.put(key, value);
			}
		}
		keySet.add(key);
	}		
	   
    
    public void printInOrder(){
        for (K key : keySet) {
            System.out.println(key);
        } 
    }
    
    //exceptions taken from ULLMap class
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }       
} 
