public class TernaryNode {
	
    private Character charValue;
    private TernaryNode leftNode;
    private TernaryNode rightNode;
    private TernaryNode middleNode;
    private Double val;
    private Double max;
	private String fullString;
    
    public void setLeft(TernaryNode n) {
        leftNode = n;
    }
    public void setRight(TernaryNode n) {
        rightNode = n;
    }
    public void setMiddle(TernaryNode n) {
        middleNode = n;
    }
    public void setVal(double d) {
        val = d;
    }
    public void setMax(Double d) {
        max = d;
    }
    public Double val() {
        return val;
    }
    public Double max() {
        return max;
    }
    public void setCharValue(char c) {
        charValue = c;
    }
    public Character value() {
        return charValue;
    }
    public TernaryNode left() {
        return leftNode;
    }
    public TernaryNode right() {
        return rightNode;
    }
	public void setFullString(String s) {
		fullString = s;
	}
	public String fullString() {
		return fullString;
	}
    public TernaryNode middle() {
        return middleNode;
    }
    public TernaryNode() {
        
    }
    public TernaryNode(char val) {
        charValue = val;
    }
    public boolean hasValue() {
        if (charValue == null) {
            return false;
        }
        return true;
    }
}