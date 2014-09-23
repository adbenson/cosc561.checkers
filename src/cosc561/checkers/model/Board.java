package cosc561.checkers.model;

import java.util.HashMap;

import static cosc561.checkers.Checkers.COLOR.*;

public class Board {
	
	public static final int TOTAL_SPACES = 32;
	
	public static final int LAST_BLACK = 12;
	
	public static final int FIRST_RED = 21;
	
	private HashMap<Integer, Space> spaces;
	
	public Board() {
		
		spaces = initializeBoard();
		
	}
	
	public HashMap<Integer, Space> initializeBoard() {
		HashMap<Integer, Space> board = new HashMap<>();
		
		for (int i = 1; i <= TOTAL_SPACES; i++) {
			Space s = new Space();
			board.put(i, s);
			
			if (i <= LAST_BLACK) {
				s.piece = new Piece(BLACK);
			}
			if (i >= FIRST_RED) {
				s.piece = new Piece(RED);
			}
		}
		
		return board;
	}
	
	public class Space {
		
		Piece piece;
		
		public Space() {
			
		}
	}

}
