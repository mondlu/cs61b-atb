import java.util.HashMap;

public class Node {
    private boolean fullWord;
    private  HashMap<Character, Node> links;
    
    public Node() {
        fullWord = false;
        links = new HashMap<Character, Node>();
    }
	
	public boolean fullWord() {
		return fullWord;
	}
	
	public void setFullWord(boolean b) {
		fullWord = b;
	}
	
	public HashMap<Character, Node> links() {
		return links;
	}

}