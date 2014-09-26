package cosc561.checkers.model;

import static cosc561.checkers.model.Direction.NE;
import static cosc561.checkers.model.Direction.NW;
import static cosc561.checkers.model.Direction.SE;
import static cosc561.checkers.model.Direction.SW;

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
	
	public static enum Color {
		RED (true, new Direction[] {NE, NW}), 
		BLACK (false, new Direction[] {SE, SW}); 
		
		public static final int LAST_BLACK = 12;
		public static final int FIRST_RED = 21;
		
		public final boolean first;
		public final Direction[] directions;
		
		private Color(boolean first, Direction[] directions) {
			this.first = first;
			this.directions = directions;
		}
		
		public static Color getColorForSpace(int id) {
			if (id <= LAST_BLACK) {
				return BLACK;
			}
			if (id >= FIRST_RED) {
				return RED;
			}
			
			return null;
		}
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
