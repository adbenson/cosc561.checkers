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
		return getLegalMoves(space, null, null);
	}
		

	private List<Space> getLegalMoves(Space space, Piece piece, Space jumpedSpace) {
		boolean hasJumped = (jumpedSpace != null);
		piece = (piece == null) ? getPiece(space) : piece;
	
		List<Space> moves = new ArrayList<Space>();
		List<Space> jumps = new ArrayList<Space>();

		if (piece != null) {
			for (Direction direction : piece.getDirections()) {
				Space adjacent = grid.getAdjacent(space, direction);
				if (adjacent != jumpedSpace) {
					// Edge spaces won't have some adjacent spaces
					if (adjacent != null) {
						if (!isEmpty(adjacent)) {
							if (piece.isOpponent(getPiece(adjacent))) {
								Space landingSpace = grid.getAdjacent(adjacent, direction);
								if (landingSpace != null && isEmpty(landingSpace)) {
									// Start the recursive jump search
									jumps.addAll(getLegalMoves(landingSpace, piece, adjacent));
								}
								// Opponent piece is against a side, can't jump
							}
							// Friendly piece, do nothing
						} else {
							moves.add(adjacent);
						}
					}
					// No adjacent piece so we can't do a jump
				}
				// Don't allow a King to jump back to his original spot.
			}
		}
		
		
		if (hasJumped) { 
			if (jumps.isEmpty()) {
				// A jump has been made. No more are available.
				jumps.add(space);
			}
		} else {
			if (jumps.isEmpty()) {
				// If no jump was ever available, then move normally
				jumps = moves;
			}
		}
		return jumps;
	}

	public String toString() {
		return grid.toString(this);
	}

}
