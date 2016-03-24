/** Adapted from Algorithms Text book, trie Chapter
* @author Mondee
*/

public class TSTSet {
    
	private TernaryNode root;
    
    public TSTSet() {}
    
    public TernaryNode root() {
        return root;
    }
	public Double get(String word) {
		TernaryNode x = get(root, word, 0);
		if (x == null) {
			return 0.0;
		} else {
			return x.val();
		}
	}
	public TernaryNode get(TernaryNode x, String key, int d) {
		if (key == null || x == null) {
			return null;
		} else if (key.equals("")) {
			return root;
		} else {
			char character = key.charAt(d);
			if (character < x.value()) {
				return get(x.left(), key, d);
			}
			if (character > x.value()) {
				return get(x.right(), key, d);
			}
			if (d < key.length() - 1) {
				return get(x.middle(), key, d + 1);
			} else {
				return x;
		    }		
		}
	}
	public void put(String key, Double val) {
		root = put(root, key, val, 0);
	}
	
	public TernaryNode put(TernaryNode x, String key, Double val, int d) {
		char c = key.charAt(d);
		if (x == null) {
			x = new TernaryNode();
			x.setCharValue(c);
			x.setMax(val);
		}
		if (val > x.max()) {
			x.setMax(val);
		}
		if (c < x.value()) {
			x.setLeft(put(x.left(), key, val, d));
		} else if (c > x.value()) {
			x.setRight(put(x.right(), key, val, d));
		} else if (d < key.length() - 1) {
			x.setMiddle(put(x.middle(), key, val, d+1));
		} else {
			x.setVal(val);
			x.setFullString(key);
		}
		return x;
	}
}