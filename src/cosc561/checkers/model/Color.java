package cosc561.checkers.model;

import static cosc561.checkers.model.Direction.NE;
import static cosc561.checkers.model.Direction.NW;
import static cosc561.checkers.model.Direction.SE;
import static cosc561.checkers.model.Direction.SW;


public enum Color {
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

	public Color opponent() {
		return (this == RED)? BLACK : RED;
	}
}