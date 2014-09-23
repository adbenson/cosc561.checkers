package cosc561.checkers.model;

import static cosc561.checkers.Checkers.COLOR.BLACK;
import static cosc561.checkers.Checkers.COLOR.RED;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Board {
	
	public static final int SPACES_PER_SIDE = 8;
	public static final int TOTAL_SPACES = SPACES_PER_SIDE * SPACES_PER_SIDE;
	
	public static final int LAST_BLACK = 12;
	
	public static final int FIRST_RED = 21;
	
	private HashMap<Integer, Space> spaces;
	
	private ArrayList<Space> rows;
	private ArrayList<Space> columns;
	
	public Board() {
		
		spaces = initializeBoard();
		
	}
	
	public HashMap<Integer, Space> initializeBoard() {
		HashMap<Integer, Space> board = new HashMap<>(TOTAL_SPACES);
		
		rows = new ArrayList<>(SPACES_PER_SIDE);
		columns = new ArrayList<>(SPACES_PER_SIDE);
		
		int spaceId = 1;
		
		for (int row = 0; row < SPACES_PER_SIDE; row++) {
			for (int col = 0; col < SPACES_PER_SIDE; col++) {
				
				Space s = new Space(row, col);
				board.put(spaceId, s);
				rows.add(row, s);
				columns.add(col, s);
				
				if (spaceId <= LAST_BLACK) {
					s.piece = new Piece(BLACK);
				}
				if (spaceId >= FIRST_RED) {
					s.piece = new Piece(RED);
				}
				
				spaceId++;
			}
		}
		
		return board;
	}
	
	public Space getSpace(int i) {
		return spaces.get(i);
	}
	
	public class Space {
		
		Piece piece;
		
		public final int row;
		public final int column;
		
		public Space(int row, int column) {
			this.row = row;
			this.column = column;
		}
		
			
		}
	}

}
