/**
 * Prefix-Trie. Supports linear time find() and insert(). 
 * Should support determining whether a word is a full word in the 
 * Trie or a prefix.
 * @author Mondee
 */
public class Trie {
    
    private Node root = new Node();
    
    
    /** 
    * Finds the String in the Trie
    * @param s The word to be found
    * @param isFullWord If false, a prefix is ok
    * @return if the string is stored in tree		
    */
    public boolean find(String s, boolean isFullWord) {
        Node tempNode = root;
        for (int i = 0; i < s.length(); i += 1) {
            char nextChar = s.charAt(i);
            if (!tempNode.links().containsKey(nextChar)) {
                return false;
            }
            tempNode = tempNode.links().get(nextChar);
        }
        if (!isFullWord) {
            return true;
        }
        if (tempNode.fullWord()) {
            return true;
        }
        return false;
    }
    
    /** 
    * Puts in the String in the Trie
    * @param s The word to be inserted
    */
    public void insert(String s) {
        if (s == null || s.equals("")) {
            throw new IllegalArgumentException("Cannot insert null or empty string.");
        }
        Node tempNode = root;
        for (int i = 0; i < s.length(); i += 1) {
            char nextChar = s.charAt(i);
            Node newNode = new Node();
            if (!tempNode.links().containsKey(nextChar)) { 
                tempNode.links().put(nextChar, newNode);
                tempNode = tempNode.links().get(nextChar);
            } else {
                tempNode = tempNode.links().get(nextChar);
            }       
        }
        tempNode.setFullWord(true);       
    }
	
	/** 
    * returns the root
    *@param nothing
    *@return the root
    */		
    public Node root() {
        return root;
    }
}
