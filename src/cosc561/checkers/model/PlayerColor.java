package cosc561.checkers.model;

import static cosc561.checkers.model.Direction.NE;
import static cosc561.checkers.model.Direction.NW;
import static cosc561.checkers.model.Direction.SE;
import static cosc561.checkers.model.Direction.SW;


public enum PlayerColor {
	RED (new Direction[] {NE, NW}, "X"), 
	BLACK (new Direction[] {SE, SW}, "O"); 
	
	public static final int LAST_BLACK = 12;
	public static final int FIRST_RED = 21;
	
	public static final PlayerColor startingPlayer = RED;
	
	public final Direction[] directions;
	public final String shortName;
	
	private PlayerColor(Direction[] directions, String shortName) {
		this.directions = directions;
		this.shortName = shortName;
	}
	
	public static PlayerColor getColorForStartingSpace(int id) {
		if (id <= LAST_BLACK) {
			return BLACK;
		}
		if (id >= FIRST_RED) {
			return RED;
		}
		
		return null;
	}

	public PlayerColor opponent() {
		return (this == RED)? BLACK : RED;
	}
	
	
}