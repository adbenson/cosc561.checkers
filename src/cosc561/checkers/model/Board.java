package cosc561.checkers.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
	
	public static final boolean FIRST_SPACE_WHITE = true;
	public static final int SPACES_PER_SIDE = 8;
	public static final int TOTAL_SPACES = SPACES_PER_SIDE * SPACES_PER_SIDE;
	
	public static final int LAST_BLACK = 12;
	
	public static final int FIRST_RED = 21;
	
	private HashMap<Integer, Space> spaces;
	
	private Space[][] grid;
	
	public Board() {
		
		initializeBoard();
		
	}
	
	public void initializeBoard() {
		spaces = new HashMap<>(TOTAL_SPACES);
		
		grid = new Space[SPACES_PER_SIDE][SPACES_PER_SIDE];
		
		int spaceId = 1;
		
		for (int row = 0; row < SPACES_PER_SIDE; row++) {
			for (int col = 0; col < SPACES_PER_SIDE; col++) {
				
				//If both coordinates have the same parity, 
				// the space should be blank IF the first space should be white
				if (FIRST_SPACE_WHITE == (row % 2 != col % 2)) {
					Space space = new Space(spaceId, row, col);
					spaces.put(spaceId, space);
					grid[row][col] = space;
					
					if (spaceId <= LAST_BLACK) {
						space.piece = new Piece(Piece.Color.BLACK);
					}
					if (spaceId >= FIRST_RED) {
						space.piece = new Piece(Piece.Color.RED);
					}
					
					spaceId++;
				}
			}
		}
	}
	
	public Space getSpace(int i) {
		return spaces.get(i);
	}
	
	public class Space {
		
		Piece piece;
		
		public final int id;
		
		public final int row;
		public final int column;
		
		private Map<Direction, Space> adjacent;
		
		public Space(int id, int row, int column) {
			this.id = id;
			this.row = row;
			this.column = column;
		}
		
		public Map<Direction, Space> getAdjacent() {
			if (adjacent == null) {
				adjacent = findAdjacent();
			}

			return adjacent;
		}
		
		private Map<Direction, Space> findAdjacent() {
			Map<Direction, Space> adjacent = new HashMap<>();
			Space that;
			
			if (row > 0 && column > 0) {
				that = grid[row - 1][column - 1];
				adjacent.put(Direction.NW, that);
			}
			if (row > 0 && column < SPACES_PER_SIDE - 1) {
				that = grid[row - 1][column + 1];
				adjacent.put(Direction.NE, that);
			}
			if (row < SPACES_PER_SIDE - 1 && column < SPACES_PER_SIDE - 1) {
				that = grid[row + 1][column + 1];
				adjacent.put(Direction.SE, that);
			}
			if (row < SPACES_PER_SIDE - 1 && column > 0) {
				that = grid[row + 1][column - 1];
				adjacent.put(Direction.SW, that);
			}
			
			return adjacent;
		}
		
		public String toString() {
			return "Space#"+id+"@("+row+","+column+")";
		}
	}

}
