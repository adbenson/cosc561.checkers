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
		
		initializeBoard();
		
	}
	
	public void initializeBoard() {
		spaces = new HashMap<>(TOTAL_SPACES);
		
		rows = new ArrayList<>(SPACES_PER_SIDE);
		columns = new ArrayList<>(SPACES_PER_SIDE);
		
		int spaceId = 1;
		
		for (int row = 0; row < SPACES_PER_SIDE; row++) {
			for (int col = 0; col < SPACES_PER_SIDE; col++) {
				
				Space space = new Space(row, col);
				spaces.put(spaceId, space);
				rows.add(row, space);
				columns.add(col, space);
				
				if (spaceId <= LAST_BLACK) {
					space.piece = new Piece(BLACK);
				}
				if (spaceId >= FIRST_RED) {
					space.piece = new Piece(RED);
				}
				
				spaceId++;
			}
		}
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
