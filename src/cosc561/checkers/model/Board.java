package cosc561.checkers.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

	private static Grid grid = Grid.getInstance();

	
	
	
	
	
	private HashMap<Integer, Space> spaces;
	
	public Board() {
		initializeSpaces();
	}
	
	public void addStartingPieces() {
		for(Space space : spaces.values()) {
			if (space.id <= LAST_BLACK) {
				space.piece = new Piece(Piece.Color.BLACK);
			}
			if (space.id >= FIRST_RED) {
				space.piece = new Piece(Piece.Color.RED);
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
		
		public List<Space> getLegalMoves() {
			List<Space> moves = new ArrayList<Space>();
			
			if (piece != null) {
				
				for (Direction direction : piece.getDirections()) {
					
					Space candidate = getAdjacent().get(direction);
					
					//Edge spaces won't have some adjacent spaces
					if (candidate != null) {
						
						if (!candidate.isEmpty()) {
							if (piece.isOpponent(candidate.piece)) {
								Space target = candidate.getAdjacent().get(direction);
								if (target != null && target.isEmpty()) {
									//Make the jump!
									moves.add(target);
								}
								//Opponent piece is against a side, can't jump
							}						
							//Friendly piece, do nothing
						}
						else {
							moves.add(candidate);
						}
					}
				}
			}
			
			return moves;
		}
		
		private boolean isEmpty() {
			return piece == null;
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
