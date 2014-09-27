package cosc561.checkers.model;

import java.util.ArrayList;
import java.util.List;

public class Board {

	private static Grid grid = Grid.getInstance();

	private Piece[] pieces;
	
	public Board() {
		pieces = new Piece[Grid.USED_SPACES + 1];
	}
	
	public void addStartingPieces() {
		for(Space space : grid.getSpaces()) {
			Piece.Color color = Piece.Color.getColorForSpace(space.id);
			if (color != null) {
				addPiece(space, new Piece(color));
			}
		}
	}
	
	public void addPiece(int id, Piece piece) {
		pieces[id] = piece;
	}
	
	public void addPiece(Space space, Piece piece) {
		addPiece(space.id, piece);
	}
	
	public Piece getPiece(Space space) {
		return pieces[space.id];
	}
	
	public void removePiece(int id) {
		pieces[id] = null;
	}
	
	public void movePiece(int from, int to) {
		pieces[to] = pieces[from];
		pieces[from] = null;
	}
	
	public void kingPiece(int id) {
		pieces[id].setKing();
	}
	
	public Space getSpace(int id) {
		return grid.getSpaceById(id);
	}
	
	public boolean isEmpty(Space space) {
		return pieces[space.id] == null;
	}
	
	public List<Space> getLegalMoves(int id) {
		return getLegalMoves(getSpace(id));
	}
	
	public List<Space> getLegalMoves(Space space) {
		Piece piece = getPiece(space);

		List<Space> moves = new ArrayList<Space>();
		List<Space> jumps = new ArrayList<Space>();
		if (piece != null) {
			
			for (Direction direction : piece.getDirections()) {
				
				Space adjacent = grid.getAdjacent(space, direction);
				
				//Edge spaces won't have some adjacent spaces
				if (adjacent != null) {
					
					if (!isEmpty(adjacent)) {
						if (piece.isOpponent(getPiece(adjacent))) {
							Space landingSpace = grid.getAdjacent(adjacent, direction);
							if (landingSpace != null && isEmpty(landingSpace)) {
								//Start the recursive jump search
								ArrayList<Space> multiJumpOptions = findMultiJumpOptions(landingSpace, piece, adjacent);
								
								if (multiJumpOptions.isEmpty()) { 
									jumps.add(landingSpace);
								} else {
									jumps.addAll(multiJumpOptions);
								}
							}
							//Opponent piece is against a side, can't jump
						}						
						//Friendly piece, do nothing
					}
					else {
						moves.add(adjacent);
					}
				}
			}
		}
		
		if (!jumps.isEmpty()) { 
			return jumps;
		} else {
			return moves;
		}
	}

	private ArrayList<Space> findMultiJumpOptions(Space space, Piece piece, Space origin) {
		ArrayList<Space> jumpOptions = new ArrayList<Space>();

		for (Direction direction : piece.getDirections()) {
			Space adjacent = grid.getAdjacent(space, direction);
			if (adjacent != origin) {

				// Edge spaces won't have some adjacent spaces
				if (adjacent != null) {

					if (!isEmpty(adjacent)) {
						if (piece.isOpponent(getPiece(adjacent))) {
							Space landingSpace = grid.getAdjacent(adjacent, direction);
							if (landingSpace != null && isEmpty(landingSpace)) {
								// Start the recursive jump search
								jumpOptions.addAll(findMultiJumpOptions(landingSpace, piece, adjacent));
							}
							// Opponent piece is against a side, can't jump
						}
						// Friendly piece, do nothing
					}
					//No adjacent piece so we can't do a jump
				}
				// Don't jump back to the space we just jumped OVER
			}
		}
		
		if (jumpOptions.isEmpty()) {
			jumpOptions.add(space);
		}
		return jumpOptions;
	}

	public String toString() {
		return grid.toString(this);
	}

}
