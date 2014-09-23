package cosc561.checkers.model;

import cosc561.checkers.Checkers.COLOR;

public class Piece {
		
	public final COLOR color;
	
	private boolean king;
	
	public Piece(COLOR color) {
		this.color = color;
	}

	public boolean isKing() {
		return king;
	}

	public void setKing(boolean king) {
		this.king = king;
	}

}
