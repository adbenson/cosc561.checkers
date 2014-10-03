package cosc561.checkers.model;

import static cosc561.checkers.model.Direction.NE;
import static cosc561.checkers.model.Direction.NW;
import static cosc561.checkers.model.Direction.SE;
import static cosc561.checkers.model.Direction.SW;


public enum PlayerColor {
	RED (new Direction[] {NE, NW}), 
	BLACK (new Direction[] {SE, SW}); 
	
	public static final int LAST_BLACK = 12;
	public static final int FIRST_RED = 21;
	
	public static final PlayerColor startingPlayer = RED;
	
	public final Direction[] directions;
	
	private PlayerColor(Direction[] directions) {
		this.directions = directions;
	}
	
	public static PlayerColor getColorForSpace(int id) {
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