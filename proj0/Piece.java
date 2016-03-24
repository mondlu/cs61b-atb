/*
********************
** Mondee Lu - atb**
********************
*/

public class Piece {
	private boolean isFire;
	private Board b;
	private int x;
	private int y;
	private String type;
	private boolean hasCaptured = false;
	private boolean isKing = false;
	
	public Piece(boolean isFire, Board b, int x, int y, String type) {
		this.isFire = isFire;
		this.b = b;
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	public boolean isFire() {
		return isFire;
	}
	
	public int side() {
		if (isFire()) {
			return 0;
		}
		else {
			return 1;
		}
	}
	
	public boolean isKing() {
		return isKing;
	}
	
	public boolean isShield() {
		if (type == "shield") {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isBomb() {
		if (type == "bomb") {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean hasCaptured() {
		return hasCaptured;
	}
	
	public void doneCapturing() {
		this.hasCaptured = false;
	}
	
	public void move(int x, int y) {
		//checks if the move results in a king
		if (isKing == false) {
			if (this.side() == 0) {
				if (y == 7 && b.canSelect(x, y)) {
					isKing = true;
				}
			}
			else {
				if (y == 0 && b.canSelect(x, y)) {
					isKing = true;
				}
			}
		}
		//check if capturing
		if ((Math.abs(this.x-x) == 2) && (Math.abs(this.y-y) ==2)) {
			//check if bomb
			if (type == "bomb") {
				b.remove(this.x, this.y);
				b.remove(Math.abs((this.x+x)/2), Math.abs((this.y+y)/2)); //remove the piece that was jumped over
				this.x = x;
				this.y = y;
				
				//remove any piece in the radius
				if (x+1 < 8 && y+1 < 8) {
					if (b.pieceAt(x+1, y+1) != null && b.pieceAt(x+1, y+1).type != "shield") {
						b.remove(x+1, y+1);
					}
				}
				if (x-1 < 8 && y+1 < 8 && x-1 >= 0 && y+1 >= 0) {	
					if (b.pieceAt(x-1, y+1) != null && b.pieceAt(x-1, y+1).type != "shield") {
						b.remove(x-1, y+1); //top left corner
					}
				}
				if (x-1 < 8 && y-1 < 8 && x-1 >= 0 && y-1 >= 0) {	
					if (b.pieceAt(x-1, y-1) != null && b.pieceAt(x-1, y-1).type != "shield") {
						b.remove(x-1, y-1); //bottom left corner
					}
				}
				if (x+1 < 8 && y-1 < 8 && x+1 >= 0 && y-1 >= 0) {	
					if (b.pieceAt(x+1, y-1) != null && b.pieceAt(x+1, y-1).type != "shield") {	
						b.remove(x+1, y-1); //bottom right corner
					}
				}		
			}
			else {
				b.remove(this.x, this.y);
				b.remove(Math.abs((this.x + x)/2), Math.abs((this.y + y)/2)); //remove the piece that was jumped over
				this.x = x;
				this.y = y;
				hasCaptured = true;
				b.place(this, this.x, this.y);
			}
		}
		//if not capturing
		else {
			b.remove(this.x, this.y);
			this.x = x;
			this.y = y;
			b.place(this, this.x, this.y);
		}	
	}
}