package cosc561.checkers.model;


public class Piece {
		
	public final Color color;
	
	private boolean king;
	
	public Piece(Color color) {
		this.color = color;
		this.king = false;
	}

	public boolean isKing() {
		return king;
	}

	public void setKing() {
		this.king = true;
	}

	public Direction[] getDirections() {
		if (isKing()) {
			return Direction.values();
		}
		else {
			return color.directions;
		}
	}

	public boolean isOpponent(Piece that) {
		return that.color != this.color;
	}
	
	public String toString() {
		return (color == Color.RED? "R" : "B") + (isKing()? "K" : " ");
	}
}
